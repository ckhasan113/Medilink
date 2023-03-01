package shafi.sbf.com.doctorsappointment.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.DoctorListAdapter;
import shafi.sbf.com.doctorsappointment.appointment.AppointmentListActivity;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.DoctorDetails;

public class DoctorAppointmentList extends AppCompatActivity implements DoctorListAdapter.GetBookingAppointment {

    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference doctorRef;

    private Toolbar searchBar;

    private Spinner spinner;
    private String spinnerName;

    private List<DoctorDetails> doctorList = new ArrayList<DoctorDetails>();

    private LoadingDialog dialog;

    private DoctorListAdapter adapter;

    private LinearLayoutManager llm;

    private RecyclerView doctorRecycler;

    private LinearLayout doctorCard;

    private TextView yourAppointmentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment_list);

        dialog = new LoadingDialog(DoctorAppointmentList.this,"Loading...");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        doctorRef = rootRef.child("Doctor");

        spinner= (Spinner) findViewById(R.id.spinner_doctor_speciality);
        doctorRecycler = findViewById(R.id.doctor_appointment_recycler);

        searchBar = findViewById(R.id.search_bar_base);
        setSupportActionBar(searchBar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\" style=\"text-align: center\">" + "Doctors appointment" + "</font>"));

        ArrayAdapter spinnerSpecialityAdapter = ArrayAdapter.createFromResource(DoctorAppointmentList.this, R.array.speciality, R.layout.spinner_item_select_model);

        spinnerSpecialityAdapter.setDropDownViewResource(R.layout.spinner_item_drop_down_model);

        spinner.setAdapter(spinnerSpecialityAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    spinnerName = spinner.getItemAtPosition(i).toString();
                    doctorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            doctorList.clear();
                            for (DataSnapshot d: dataSnapshot.getChildren()){
                                DoctorDetails doc = d.child("Details").getValue(DoctorDetails.class);
                                if (doc.getDepartment().equals(spinnerName)){
                                    doctorList.add(doc);
                                }
                            }
                            adapter = new DoctorListAdapter(DoctorAppointmentList.this, doctorList);
                            llm = new LinearLayoutManager(DoctorAppointmentList.this);
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
                    doctorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            doctorList.clear();
                            for (DataSnapshot d: dataSnapshot.getChildren()){
                                DoctorDetails doc = d.child("Details").getValue(DoctorDetails.class);
                                doctorList.add(doc);
                            }
                            adapter = new DoctorListAdapter(DoctorAppointmentList.this, doctorList);
                            llm = new LinearLayoutManager(DoctorAppointmentList.this);
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

        doctorCard = findViewById(R.id.card);

        dialog.show();
        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    //patientcount = String.valueOf(d.getValue());
                    DoctorDetails doc = d.child("Details").getValue(DoctorDetails.class);
                    doctorList.add(doc);
                }
                DoctorListAdapter adapterBase = new DoctorListAdapter(DoctorAppointmentList.this, doctorList);
                llm = new LinearLayoutManager(DoctorAppointmentList.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                doctorRecycler.setLayoutManager(llm);
                doctorRecycler.setAdapter(adapterBase);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        yourAppointmentTV = findViewById(R.id.patient_doctor_appointment);

        yourAppointmentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorAppointmentList.this, AppointmentListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*doctorCard.setOnClickListener(this);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        androidx.appcompat.widget.SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();

        if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            searchView.setPadding(3,0,0,0);
            searchView.setQueryHint("Search by first name...");

        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final String firstName = s.toLowerCase();
                doctorRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        doctorList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            DoctorDetails doc = d.child("Details").getValue(DoctorDetails.class);
                            String name = doc.getFirstName().toLowerCase();
                            if (name.equals(firstName)){
                                doctorList.add(doc);
                            }
                        }
                        adapter = new DoctorListAdapter(DoctorAppointmentList.this, doctorList);
                        llm = new LinearLayoutManager(DoctorAppointmentList.this);
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

    /*@Override
    public void onClick(View v) {
        if (v.getId()==R.id.card){
            Intent intent = new Intent(DoctorAppointmentList.this,Doctor.class);
            startActivity(intent);
        }
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoctorAppointmentList.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void bookingAppointment(DoctorDetails doctorDetails) {
        Intent intent = new Intent(DoctorAppointmentList.this, Doctor.class);
        intent.putExtra("DoctorDetails", doctorDetails);
        startActivity(intent);
        finish();
    }
}
