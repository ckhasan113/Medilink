package shafi.sbf.com.doctorsappointment.Nurse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.PackageRecylaerViewAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.PackageDetails;
import shafi.sbf.com.doctorsappointment.pojo.VendorDetails;

public class NursePackage extends AppCompatActivity implements PackageRecylaerViewAdapter.PackageDetailsListener {


    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference pacRef;
    private DatabaseReference doctorRef;

    private LoadingDialog dialog;


    private ImageView profileImageIV;

    private TextView nameTV, areaTV, CountryS, historyTV,EstablishTV;

    private String vendorID, image, name, area,  history;
    private List<PackageDetails> packageList = new ArrayList<PackageDetails>();
    private RecyclerView packageRecycler;

    private VendorDetails vendorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_package);

        dialog = new LoadingDialog(NursePackage.this,"Data Loading...");
        dialog.show();

        vendorDetails = (VendorDetails) getIntent().getSerializableExtra("VendorDetails");

        rootRef = FirebaseDatabase.getInstance().getReference();

        profileImageIV = findViewById(R.id.Shospital_image);
        nameTV = findViewById(R.id.Shospital_name);
        areaTV = findViewById(R.id.Shospital_area);
        historyTV = findViewById(R.id.Shospital_history);



        packageRecycler = findViewById(R.id.package_base_recycler);



        userRef = rootRef.child("Nurse Vendor").child(vendorDetails.getVendorID());
        setValue();
        pacRef = userRef.child("Package");
        seePackage();
    }

    private void seePackage() {

        pacRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                packageList.clear();
                //historyTV.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd: dataSnapshot.getChildren()){
                    PackageDetails doc = hd.child("details").getValue(PackageDetails.class);
                    packageList.add(doc);
                }

                PackageRecylaerViewAdapter recylaerViewAdapter = new PackageRecylaerViewAdapter(NursePackage.this, packageList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                packageRecycler.setLayoutManager(mLayoutManager);
                packageRecycler.setItemAnimator(new DefaultItemAnimator());
                packageRecycler.setAdapter(recylaerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setValue() {

                Uri photoUri = Uri.parse(vendorDetails.getImage());
                Picasso.get().load(photoUri).into(profileImageIV);

                nameTV.setText(vendorDetails.getName());
                areaTV.setText(vendorDetails.getArea());
                historyTV.setText(vendorDetails.getHistory());

                dialog.dismiss();
            }


    @Override
    public void onPackageDetails(PackageDetails packageDetails) {
        Intent intent = new Intent(NursePackage.this, ConfirmNurse.class);
        intent.putExtra("VendorDetails",vendorDetails);
        intent.putExtra("NurseDetails", packageDetails);
        startActivity(intent);
    }
}
