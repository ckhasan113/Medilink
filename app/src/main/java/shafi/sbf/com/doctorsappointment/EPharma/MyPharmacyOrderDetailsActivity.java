package shafi.sbf.com.doctorsappointment.EPharma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.PharmacyOrderListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.ConfirmOrder;
import shafi.sbf.com.doctorsappointment.pojo.OrderChart;

public class MyPharmacyOrderDetailsActivity extends AppCompatActivity {

    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference pharmacyRef;

    private ConfirmOrder cd;

    private LoadingDialog dialog;

    private ImageView image;
    private TextView name, address, status, phone, email, price, vatPrice;
    private LinearLayout call, mail;
    private RecyclerView recycler;
    private Button received, again, remove;

    private List<OrderChart> chartList = new ArrayList<OrderChart>();

    private PharmacyOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pharmacy_order_details);

        cd = (ConfirmOrder) getIntent().getSerializableExtra("OrderDetails");
        chartList = cd.getChartList();

        dialog = new LoadingDialog(MyPharmacyOrderDetailsActivity.this, "Loading...");

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        userRef = rootRef.child("Patient").child(user.getUid()).child("Pharmacy").child("Order").child(cd.getOrderId());
        pharmacyRef = rootRef.child("Pharmacy").child(cd.getePharmaDetails().getId()).child("Order").child(cd.getOrderId());

        image = findViewById(R.id.my_pharmacy_order_details_vendor_image);
        name = findViewById(R.id.my_pharmacy_order_details_vendor_name);
        address = findViewById(R.id.my_pharmacy_order_details_vendor_address);
        status = findViewById(R.id.my_pharmacy_order_details_order_status);
        phone = findViewById(R.id.my_pharmacy_order_details_vendor_phone_tv);
        email = findViewById(R.id.my_pharmacy_order_details_vendor_email_tv);
        price = findViewById(R.id.my_pharmacy_order_details_showPrice);
        vatPrice = findViewById(R.id.my_pharmacy_order_details_showPriceF);
        call = findViewById(R.id.my_pharmacy_order_details_vendor_callLO);
        mail = findViewById(R.id.my_pharmacy_order_details_vendor_mailLO);
        recycler = findViewById(R.id.my_pharmacy_order_details_product_recycler);
        received = findViewById(R.id.my_pharmacy_order_received_btn);
        again = findViewById(R.id.my_pharmacy_order_again_btn);
        remove = findViewById(R.id.my_pharmacy_order_remove_btn);

        Picasso.get().load(Uri.parse(cd.getePharmaDetails().getImage())).into(image);
        name.setText(cd.getePharmaDetails().getFarmName());
        address.setText(cd.getePharmaDetails().getArea()+", "+cd.getePharmaDetails().getCity());
        status.setText("Status: "+cd.getStatus());
        phone.setText(cd.getePharmaDetails().getPhone());
        email.setText(cd.getePharmaDetails().getEmail());

        adapter = new PharmacyOrderListAdapter(MyPharmacyOrderDetailsActivity.this, chartList);
        LinearLayoutManager llm = new LinearLayoutManager(MyPharmacyOrderDetailsActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);

        price.setText(cd.getTotalPrice());
        vatPrice.setText(cd.getTotalPlusVat());

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + cd.getPatientDetails().getPhone().trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        return;
                    }
                }
                startActivity(intent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:"+cd.getPatientDetails().getEmail());
                intent.setData(data);
                startActivity(intent);
            }
        });

        if (cd.getStatus().equals("Pending")){
            received.setVisibility(View.GONE);
        }else if (cd.getStatus().equals("Delivering")){
            received.setVisibility(View.VISIBLE);
        }else if (cd.getStatus().equals("Received")){
            again.setVisibility(View.VISIBLE);
            remove.setVisibility(View.VISIBLE);
            received.setVisibility(View.GONE);
        }

        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                cd.setStatus("Received");
                userRef.child("Details").setValue(cd).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pharmacyRef.child("Details").setValue(cd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("sms", "Order is delivered");
                                tokenMap.put("from", String.valueOf(user.getUid()));
                                tokenMap.put("title", "Order Confirmation");
                                tokenMap.put("to", cd.getePharmaDetails().getId());
                                rootRef.child("Notifications").child(cd.getePharmaDetails().getId()).child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        Toast.makeText(MyPharmacyOrderDetailsActivity.this, "Order received", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MyPharmacyOrderDetailsActivity.this, MyPharmacyOrderActivity.class));
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                userRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pharmacyRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Toast.makeText(MyPharmacyOrderDetailsActivity.this, "Order removed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MyPharmacyOrderDetailsActivity.this, MyPharmacyOrderActivity.class));
                                finish();
                            }
                        });
                    }
                });
            }
        });

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                cd.setStatus("Pending");
                userRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pharmacyRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                userRef.child("Details").setValue(cd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pharmacyRef.child("Details").setValue(cd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Map<String, Object> tokenMap = new HashMap<>();
                                                tokenMap.put("sms", "You have new order");
                                                tokenMap.put("from", String.valueOf(user.getUid()));
                                                tokenMap.put("title", "Client order");
                                                tokenMap.put("to", cd.getePharmaDetails().getId());
                                                rootRef.child("Notifications").child(cd.getePharmaDetails().getId()).child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        dialog.dismiss();
                                                        Toast.makeText(MyPharmacyOrderDetailsActivity.this, "Order placed successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(MyPharmacyOrderDetailsActivity.this, MyPharmacyOrderActivity.class));
                                                        finish();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyPharmacyOrderDetailsActivity.this, MyPharmacyOrderActivity.class));
        finish();
    }
}
