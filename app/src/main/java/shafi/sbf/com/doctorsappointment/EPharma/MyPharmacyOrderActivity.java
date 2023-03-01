package shafi.sbf.com.doctorsappointment.EPharma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.MyEPharmacyOrderListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.ConfirmOrder;

public class MyPharmacyOrderActivity extends AppCompatActivity implements MyEPharmacyOrderListAdapter.MyEPharmacyOrderListAdapterListener {

    private LoadingDialog dialog;
    private RecyclerView recyclerView;

    private DatabaseReference myOrderRef;

    private FirebaseUser user;

    private MyEPharmacyOrderListAdapter adapter;

    private List<ConfirmOrder> orderList = new ArrayList<ConfirmOrder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pharmacy_order);

        dialog = new LoadingDialog(MyPharmacyOrderActivity.this, "Loading...");
        dialog.show();

        user = FirebaseAuth.getInstance().getCurrentUser();

        myOrderRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(user.getUid()).child("Pharmacy").child("Order");
        recyclerView = findViewById(R.id.e_pharmacy_my_order_base_recycler);

        myOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ConfirmOrder cd = d.child("Details").getValue(ConfirmOrder.class);
                    orderList.add(cd);
                }
                Collections.reverse(orderList);
                adapter = new MyEPharmacyOrderListAdapter(MyPharmacyOrderActivity.this, orderList);
                LinearLayoutManager llm = new LinearLayoutManager(MyPharmacyOrderActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyPharmacyOrderActivity.this, EVendor.class));
        finish();
    }

    @Override
    public void onGetOrderDetails(ConfirmOrder confirmOrder) {
        Intent intent = new Intent(MyPharmacyOrderActivity.this, MyPharmacyOrderDetailsActivity.class);
        intent.putExtra("OrderDetails", confirmOrder);
        startActivity(intent);
        finish();
    }
}
