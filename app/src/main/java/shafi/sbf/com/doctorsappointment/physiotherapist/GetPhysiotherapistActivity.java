package shafi.sbf.com.doctorsappointment.physiotherapist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.PhysiotherapistPackageListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistPackageDetails;
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistVendorDetails;

public class GetPhysiotherapistActivity extends AppCompatActivity implements PhysiotherapistPackageListAdapter.PhysiotherapistPackageListAdapterListener {

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference packageRef;
    private DatabaseReference physioRef;

    private LoadingDialog dialog;

    private PhysiotherapistPackageListAdapter adapter;

    private ImageView profileImageIV;

    private TextView nameTV, addressTV, historyTV;

    private List<PhysiotherapistPackageDetails> packageList = new ArrayList<PhysiotherapistPackageDetails>();
    private RecyclerView packageRecycler;

    private PhysiotherapistVendorDetails vendorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_physiotherapist);

        dialog = new LoadingDialog(GetPhysiotherapistActivity.this,"Data Loading...");
        dialog.show();

        vendorDetails = (PhysiotherapistVendorDetails) getIntent().getSerializableExtra("Physiotherapist");

        rootRef = FirebaseDatabase.getInstance().getReference();

        profileImageIV = findViewById(R.id.physio_therapist_Profile_Image_view);
        nameTV = findViewById(R.id.physio_therapist_Name);
        addressTV = findViewById(R.id.physio_therapist_address);
        historyTV = findViewById(R.id.physio_therapist_history);

        packageRecycler = findViewById(R.id.physio_package_recycler);

        Picasso.get().load(Uri.parse(vendorDetails.getImage())).into(profileImageIV);
        nameTV.setText(vendorDetails.getName());
        addressTV.setText(vendorDetails.getArea()+", "+vendorDetails.getCity());
        historyTV.setText(vendorDetails.getHistory());

        physioRef = rootRef.child("Physiotherapist").child(vendorDetails.getVendorID());
        packageRef = physioRef.child("Package");

        packageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    PhysiotherapistPackageDetails ppd = d.child("Details").getValue(PhysiotherapistPackageDetails.class);
                    packageList.add(ppd);
                }
                adapter = new PhysiotherapistPackageListAdapter(GetPhysiotherapistActivity.this, packageList);
                LinearLayoutManager llm = new LinearLayoutManager(GetPhysiotherapistActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                packageRecycler.setLayoutManager(llm);
                packageRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GetPhysiotherapistActivity.this, PhysiotherapistActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPackageDetails(PhysiotherapistPackageDetails packageDetails) {
        Intent intent = new Intent(GetPhysiotherapistActivity.this, ConfirmPhysioActivity.class);
        intent.putExtra("Physiotherapist", vendorDetails);
        intent.putExtra("PackageDetails", packageDetails);
        startActivity(intent);
        finish();
    }
}
