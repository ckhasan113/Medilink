package shafi.sbf.com.doctorsappointment.EPharma;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shafi.sbf.com.doctorsappointment.Activity.Doctor;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.appointment.DoctorChamberListActivity;
import shafi.sbf.com.doctorsappointment.pojo.ConfirmOrder;
import shafi.sbf.com.doctorsappointment.pojo.EPharmaDetails;
import shafi.sbf.com.doctorsappointment.pojo.OrderChart;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;

public class EpharmaOrderList extends AppCompatActivity implements TestAdapter.OrderListener {

    private FirebaseUser user;

    private RecyclerView test;
    private TestAdapter adapterT;

    private int mHour, mMinute;

    private TextView Time, Location,price,finalPrice,txtnote,sys;

    private ConfirmOrder confirmOrder;
    private PatientDetails patientDetails;
    private EPharmaDetails details;

    private List<OrderChart> orderCharts = new ArrayList<OrderChart>();

    double totalPrice,withvat;

    Dialog mydialog;

    String includeVatPrice,sTime;

    private DatabaseReference rootRef;
    private DatabaseReference patiRef;
    private DatabaseReference orderRef;
    String id,pid,epid;

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epharma_order_list);

        confirmOrder =(ConfirmOrder) getIntent().getSerializableExtra("curt");

        user = FirebaseAuth.getInstance().getCurrentUser();

        patientDetails = confirmOrder.getPatientDetails();
        details = confirmOrder.getePharmaDetails();
        pid = patientDetails.getPatient_id();
        epid = details.getId();

        test = findViewById(R.id.foodRecyTest);
        orderCharts = confirmOrder.getChartList();
        price = findViewById(R.id.showPrice);
        finalPrice = findViewById(R.id.showPriceF);

        cardView = findViewById(R.id.curt_master2F);
        rootRef = FirebaseDatabase.getInstance().getReference();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalPrice>0) {
                    ConfirmPArchas();
                }else {
                    Toast.makeText(EpharmaOrderList.this, "Add food first...!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        adapterT = new TestAdapter(EpharmaOrderList.this, orderCharts);
        LinearLayoutManager llm = new LinearLayoutManager(EpharmaOrderList.this);
        llm.setOrientation(RecyclerView.VERTICAL);
        test.setLayoutManager(llm);
        test.setAdapter(adapterT);

        totalPrice = 0;
        for (OrderChart c: orderCharts){
            double price = Double.parseDouble(c.getPrice());
            totalPrice += price;
        }
        price.setText(String.valueOf(totalPrice));

        withvat = (totalPrice+(totalPrice*15.0)/100.0);
        finalPrice.setText(String.valueOf(withvat));
    }


    private void ConfirmPArchas() {

        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Uploading...");
        mDialog.show();

        Date cd = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(cd);


        orderRef = rootRef.child("Pharmacy").child(epid).child("Order");
        patiRef = rootRef.child("Patient").child(pid).child("Pharmacy").child("Order");

        id = orderRef.push().getKey();


        final ConfirmOrder con = new ConfirmOrder(id,orderCharts,patientDetails,details,String.valueOf(totalPrice),String.valueOf(withvat),formattedDate,"Pending");
        //Set value to database
        orderRef.child(id).child("Details").setValue(con).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    patiRef.child(id).child("Details").setValue(con).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("sms", "You have new order");
                            tokenMap.put("from", String.valueOf(user.getUid()));
                            tokenMap.put("title", "Client order");
                            tokenMap.put("to", details.getId());
                            rootRef.child("Notifications").child(details.getId()).child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(EpharmaOrderList.this, shafi.sbf.com.doctorsappointment.Activity.EpharmaList.class);
                                    intent.putExtra("VendorDetails", details);
                                    intent.putExtra("chec","22");

                                    startActivity(intent);
                                    mDialog.dismiss();
                                    Toast.makeText(EpharmaOrderList.this, "Successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    });

                } else {
                    mDialog.dismiss();
                    Toast.makeText(EpharmaOrderList.this, "Failed to add Doctor", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mDialog.dismiss();
                Toast.makeText(EpharmaOrderList.this, "Failed : "+e, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onOrderDetails(OrderChart orderChart) {

        orderCharts.remove(orderChart);
        totalPrice = 0;
        for (OrderChart c: orderCharts){
            double price = Double.parseDouble(c.getPrice());
            totalPrice += price;
        }
        price.setText(String.valueOf(totalPrice));
        includeVatPrice = String.valueOf((totalPrice+(totalPrice*15.0)/100.0));
        finalPrice.setText(includeVatPrice);
    }

    @Override
    public void onBackPressed() {
        if(totalPrice>0) {
            ConfirmOrder confi = new ConfirmOrder(id,orderCharts,patientDetails,details,String.valueOf(totalPrice),String.valueOf(withvat),"nai","Request");
            Intent intent = new Intent(new Intent(EpharmaOrderList.this,EpharmaList.class));
            intent.putExtra("VendorDetails", details);
            intent.putExtra("curt",confi);
            intent.putExtra("chec","11");
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(EpharmaOrderList.this,EpharmaList.class);
            intent.putExtra("VendorDetails", details);
            intent.putExtra("chec","00");
            startActivity(intent);
            finish();
        }
    }
}
