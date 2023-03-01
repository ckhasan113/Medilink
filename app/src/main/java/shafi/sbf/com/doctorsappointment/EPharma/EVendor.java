package shafi.sbf.com.doctorsappointment.EPharma;

import android.content.Intent;


import android.os.Bundle;

import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import shafi.sbf.com.doctorsappointment.Activity.EpharmaList;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.EVendorRecycleVAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.EPharmaDetails;


public class EVendor extends AppCompatActivity implements EVendorRecycleVAdapter.EVendorListener {

    private DatabaseReference rootRef;
    private DatabaseReference venRef;

    private LoadingDialog dialog;

    private List<EPharmaDetails> vendorList = new ArrayList<EPharmaDetails>();
    private RecyclerView VendorRecycler;

    private Button myOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evendor);

        dialog = new LoadingDialog(EVendor.this, "Loading Data...");

        rootRef = FirebaseDatabase.getInstance().getReference();

        venRef = rootRef.child("Pharmacy");

        VendorRecycler = findViewById(R.id.e_vendor_base_recycler);

        myOrderBtn = findViewById(R.id.ePharmacyMyOrderBtn);

        myOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EVendor.this, MyPharmacyOrderActivity.class));
                finish();
            }
        });

        ShowVendorList();
    }

    private void ShowVendorList() {
        dialog.show();
        venRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vendorList.clear();
                //historyTV.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd : dataSnapshot.getChildren()) {
                    EPharmaDetails doc = hd.child("Details").getValue(EPharmaDetails.class);
                    vendorList.add(doc);
                }

                EVendorRecycleVAdapter recylaerViewAdapter = new EVendorRecycleVAdapter(EVendor.this, vendorList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                VendorRecycler.setLayoutManager(mLayoutManager);
                VendorRecycler.setItemAnimator(new DefaultItemAnimator());
                VendorRecycler.setAdapter(recylaerViewAdapter);
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onVenDetails(EPharmaDetails vendorDetails) {

        Intent intent = new Intent(EVendor.this, EpharmaList.class);
        intent.putExtra("VendorDetails", vendorDetails);
        intent.putExtra("chec","00");
        startActivity(intent);

//        Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
    }
}
