package shafi.sbf.com.doctorsappointment.Nurse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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


import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.VendorRecycleVAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.VendorDetails;

public class VendorActivity extends AppCompatActivity implements VendorRecycleVAdapter.VendorListener{

    private DatabaseReference rootRef;
    private DatabaseReference venRef;

    private LoadingDialog dialog;

    private List<VendorDetails> vendorList = new ArrayList<VendorDetails>();
    private RecyclerView VendorRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        dialog = new LoadingDialog(VendorActivity.this, "Loading...");

        rootRef = FirebaseDatabase.getInstance().getReference();

        venRef = rootRef.child("Nurse Vendor");

        VendorRecycler = findViewById(R.id.nurse_vendor_base_recycler);


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
                    VendorDetails doc = hd.child("Details").getValue(VendorDetails.class);
                    vendorList.add(doc);
                }

                VendorRecycleVAdapter recylaerViewAdapter = new VendorRecycleVAdapter(VendorActivity.this, vendorList);
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
    public void onVenDetails(VendorDetails vendorDetails) {
        Intent intent = new Intent(VendorActivity.this, NursePackage.class);
        intent.putExtra("VendorDetails", vendorDetails);
        startActivity(intent);
    }

    public void myServices(View view) {

        Intent intent = new Intent(VendorActivity.this, MyNurceService.class);

        startActivity(intent);
    }
}
