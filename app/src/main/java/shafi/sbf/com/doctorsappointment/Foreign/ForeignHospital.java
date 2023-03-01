package shafi.sbf.com.doctorsappointment.Foreign;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.RecylaerViewAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.ForeignDoctorDetails;
import shafi.sbf.com.doctorsappointment.pojo.ForeignHospitalDetails;


public class ForeignHospital extends AppCompatActivity implements RecylaerViewAdapter.FDoctorDetailsListener {

    LinearLayout doctor;

    String country;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference userCRef;
    private DatabaseReference doctorRef;

    private LoadingDialog dialog;

    private ImageView profileImageIV;

    private TextView nameTV, areaTV, CountryS, historyTV,EstablishTV;

    private ForeignHospitalDetails FhosDetails;

    private List<ForeignDoctorDetails> doctorList = new ArrayList<ForeignDoctorDetails>();
    private RecyclerView doctorRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_hospital);

        profileImageIV = findViewById(R.id.Shospital_image);
        nameTV = findViewById(R.id.Shospital_name);
        areaTV = findViewById(R.id.Shospital_area);
        CountryS = findViewById(R.id.country);
        historyTV = findViewById(R.id.Shospital_history);
        EstablishTV = findViewById(R.id.Shospital_establish);

        country = getIntent().getStringExtra("country");
        FhosDetails = (ForeignHospitalDetails) getIntent().getSerializableExtra("DoctorDetails");

       nameTV.setText(FhosDetails.getName());
       areaTV.setText(FhosDetails.getArea());
       historyTV.setText(FhosDetails.getHistory());
       EstablishTV.setText(FhosDetails.getEsYear());
       CountryS.setText(country);
       Picasso.get().load(FhosDetails.getImage()).into(profileImageIV);


        doctorRecycler = findViewById(R.id.doctor_base_recycler);
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Foreign Doctor").child("Country").child(country).child("Hospital").child(FhosDetails.getHospitalID());
        doctorRef = userRef.child("DoctorList");


        showDoctorList();


    }

    private void showDoctorList() {

        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();

                for (DataSnapshot hd: dataSnapshot.getChildren()){
                    ForeignDoctorDetails doc = hd.child("details").getValue(ForeignDoctorDetails.class);
                    doctorList.add(doc);
                }

                RecylaerViewAdapter recylaerViewAdapter = new RecylaerViewAdapter(ForeignHospital.this,doctorList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                doctorRecycler.setLayoutManager(mLayoutManager);
                doctorRecycler.setItemAnimator(new DefaultItemAnimator());
                doctorRecycler.setAdapter(recylaerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForeignHospital.this, ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDoctorDetails(ForeignDoctorDetails doctorDetails) {
        Intent intent = new Intent(ForeignHospital.this, ForegnDoctor.class);
        intent.putExtra("country",country);
        intent.putExtra("DoctorDetails", doctorDetails);
        intent.putExtra("HospitalDetails", FhosDetails);
        startActivity(intent);
    }
}
