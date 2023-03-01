package shafi.sbf.com.doctorsappointment.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import shafi.sbf.com.doctorsappointment.adapter.HospitalDoctorListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.hospital.HospitalDoctorProfileActivity;
import shafi.sbf.com.doctorsappointment.pojo.HospitalDetails;
import shafi.sbf.com.doctorsappointment.pojo.HospitalDoctorDetails;

public class Hospital extends AppCompatActivity implements HospitalDoctorListAdapter.HospitalDoctorListAdapterListener {

    private DatabaseReference hospitalRef;
    private DatabaseReference hospitalDoctorRef;

    private ImageView profileImageIV;

    private TextView proNameTV, proAddressTV, proDoctorAvailableTV, proHistoryTV;

    private String hospitalID;

    private Spinner spinner;
    private String spinnerName;

    private LoadingDialog dialog;

    private int count = 0;

    private Toolbar searchBar;

    private List<HospitalDoctorDetails> doctorList = new ArrayList<HospitalDoctorDetails>();

    private HospitalDoctorListAdapter adapter;

    private LinearLayoutManager llm;

    private RecyclerView doctorRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        dialog = new LoadingDialog(Hospital.this,"Loading...");
        dialog.show();

        hospitalID = getIntent().getStringExtra("HospitalID");

        hospitalRef = FirebaseDatabase.getInstance().getReference().child("Hospital").child(hospitalID);
        hospitalDoctorRef = hospitalRef.child("DoctorList");

        profileImageIV = findViewById(R.id.hospitalProfileImageIV);
        proNameTV = findViewById(R.id.hospitalProfileNameTV);
        proAddressTV = findViewById(R.id.hospitalProfileAddressTV);
        proDoctorAvailableTV = findViewById(R.id.hospitalProfileAvailableDoctorNumberTV);
        proHistoryTV = findViewById(R.id.hospitalProfileHistoryTV);

        spinner= (Spinner) findViewById(R.id.spinnerHospitalDoctor);
        doctorRecycler = findViewById(R.id.hospital_doctor_appointment_recycler);

        hospitalDoctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HospitalDoctorDetails doc = d.child("Details").getValue(HospitalDoctorDetails.class);
                    doctorList.add(doc);
                }
                adapter = new HospitalDoctorListAdapter(Hospital.this, doctorList);
                llm = new LinearLayoutManager(Hospital.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                doctorRecycler.setLayoutManager(llm);
                doctorRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter spinnerSpecialityAdapter = ArrayAdapter.createFromResource(Hospital.this, R.array.speciality, R.layout.spinner_item_select_model);

        spinnerSpecialityAdapter.setDropDownViewResource(R.layout.spinner_item_drop_down_model);

        spinner.setAdapter(spinnerSpecialityAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    spinnerName = spinner.getItemAtPosition(i).toString();
                    dialog.show();
                    hospitalDoctorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            doctorList.clear();
                            for (DataSnapshot d: dataSnapshot.getChildren()){
                                HospitalDoctorDetails doc = d.child("Details").getValue(HospitalDoctorDetails.class);
                                if (doc.getSpeciality().equals(spinnerName)){
                                    doctorList.add(doc);
                                }
                            }
                            adapter = new HospitalDoctorListAdapter(Hospital.this, doctorList);
                            llm = new LinearLayoutManager(Hospital.this);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);

                            doctorRecycler.setLayoutManager(llm);
                            doctorRecycler.setAdapter(adapter);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    spinnerName = "";
                    dialog.show();
                    hospitalDoctorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            doctorList.clear();
                            for (DataSnapshot d: dataSnapshot.getChildren()){
                                HospitalDoctorDetails doc = d.child("Details").getValue(HospitalDoctorDetails.class);
                                doctorList.add(doc);
                            }
                            adapter = new HospitalDoctorListAdapter(Hospital.this, doctorList);
                            llm = new LinearLayoutManager(Hospital.this);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);

                            doctorRecycler.setLayoutManager(llm);
                            doctorRecycler.setAdapter(adapter);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchBar = findViewById(R.id.search_bar_hospital_doctor);
        setSupportActionBar(searchBar);
        getSupportActionBar().setTitle(null);

        dialog.show();
        hospitalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HospitalDetails h = dataSnapshot.child("Details").getValue(HospitalDetails.class);
                Picasso.get().load(Uri.parse(h.getImage())).into(profileImageIV);
                proNameTV.setText(h.getName());
                proAddressTV.setText(h.getArea()+", "+h.getCity());
                proHistoryTV.setText(h.getHistory());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        hospitalDoctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    count++;
                }
                proDoctorAvailableTV.setText("Available doctor "+String.valueOf(count));
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*@Override
    public void onClick(View v) {
        if (v.getId()==R.id.hospital_doctor){

            Intent intent = new Intent(Hospital.this,Doctor.class);
            startActivity(intent);
        }
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Hospital.this, HospitalList.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search by full name...");

        /*if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setPadding(3,0,0,0);


        }*/
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final String doctorName = s.toLowerCase();
                dialog.show();
                hospitalDoctorRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        doctorList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            HospitalDoctorDetails doc = d.child("Details").getValue(HospitalDoctorDetails.class);
                            if (doc.getName().toLowerCase().equals(doctorName)){
                                doctorList.add(doc);
                            }
                        }
                        adapter = new HospitalDoctorListAdapter(Hospital.this, doctorList);
                        llm = new LinearLayoutManager(Hospital.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);

                        doctorRecycler.setLayoutManager(llm);
                        doctorRecycler.setAdapter(adapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }


    @Override
    public void onGetHospitalDetails(HospitalDoctorDetails doctor) {
        Intent intent = new Intent(Hospital.this, HospitalDoctorProfileActivity.class);
        intent.putExtra("HospitalID", hospitalID);
        intent.putExtra("HospitalDoctorDetails", doctor);
        startActivity(intent);
        finish();
    }
}
