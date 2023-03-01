package shafi.sbf.com.doctorsappointment.Foreign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import shafi.sbf.com.doctorsappointment.adapter.ForeignDoctorRecycleVAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.ForeignHospitalDetails;

public class ForeignHospitalList extends AppCompatActivity implements ForeignDoctorRecycleVAdapter.FHDetailsListener {

    LinearLayout hospital;

    private DatabaseReference rootRef;
    private DatabaseReference counRef;
    private DatabaseReference hosRef;
    private DatabaseReference doctorRef;

    private LoadingDialog dialog;

    private List<ForeignHospitalDetails> hospitalList = new ArrayList<ForeignHospitalDetails>();
    private RecyclerView FhospitalRecycler;

    String countryS="India";

    Spinner CountrySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_hospital_list);

        dialog = new LoadingDialog(ForeignHospitalList.this, "Loading...");

        rootRef = FirebaseDatabase.getInstance().getReference();

        final String country[] = {"India", "Singapore", "Malaysia", "England"};

        CountrySpinner = findViewById(R.id.spinner_Select_country);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_select_model2, country);

        CountrySpinner.setAdapter(adapter);


        CountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                countryS = CountrySpinner.getItemAtPosition(i).toString();
                counRef = rootRef.child("Foreign Doctor").child("Country").child(countryS);
                hosRef = counRef.child("Hospital");

                ShowHospitalList();
//                Toast.makeText(ForeignHospitalList.this, "" + countryS, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        FhospitalRecycler = findViewById(R.id.foreign_doctor_base_recycler);

    }


    private void ShowHospitalList() {


        dialog.show();

        hosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hospitalList.clear();
                //historyTV.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd : dataSnapshot.getChildren()) {
                    ForeignHospitalDetails doc = hd.child("Details").getValue(ForeignHospitalDetails.class);
                    hospitalList.add(doc);
                }

                ForeignDoctorRecycleVAdapter recylaerViewAdapter = new ForeignDoctorRecycleVAdapter(ForeignHospitalList.this, hospitalList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                FhospitalRecycler.setLayoutManager(mLayoutManager);
                FhospitalRecycler.setItemAnimator(new DefaultItemAnimator());
                FhospitalRecycler.setAdapter(recylaerViewAdapter);
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onFHDetails(ForeignHospitalDetails FhodpitalDetails) {

        Intent intent = new Intent(ForeignHospitalList.this, ForeignHospital.class);
        intent.putExtra("country",countryS);
        intent.putExtra("DoctorDetails", FhodpitalDetails);
        startActivity(intent);

    }


    public void myappoinment(View view) {

        startActivity(new Intent(ForeignHospitalList.this,MyAppointment.class));
        finish();
    }
}
