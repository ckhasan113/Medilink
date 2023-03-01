package shafi.sbf.com.doctorsappointment.appointment;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shafi.sbf.com.doctorsappointment.Activity.Doctor;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.ChamberListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.Description_Dialog;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.Appointments;
import shafi.sbf.com.doctorsappointment.pojo.Chamber;
import shafi.sbf.com.doctorsappointment.pojo.DoctorDates;
import shafi.sbf.com.doctorsappointment.pojo.DoctorDetails;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;

public class DoctorChamberListActivity extends AppCompatActivity implements ChamberListAdapter.onBookingService, Description_Dialog.DescriptionDialogListener {
    private String doctorID;
    private String patientDescription;
    private String patientFirstName;
    private String patientLastName;
    private String patientImage;
    private String bookedChamberKey;
    private String bookedChamberArea;
    private String bookedChamberCity;
    private String bookedChamberTime;
    private String doctorName;

    private ChamberListAdapter adapter;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference doctorRef;
    private DatabaseReference doctorchamberRef;
    private DatabaseReference chamberRef;
    private DatabaseReference appointmentRef;
    private DatabaseReference userRef;

    private RecyclerView recyclerView;

    private List<Chamber> chamberList = new ArrayList<Chamber>();

    private LoadingDialog dialog;

    private String dateKey;
    private String appointmentDateKey;

    private DoctorDates dates;

    private DoctorDetails doctorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_chamber_list);

        doctorDetails = (DoctorDetails) getIntent().getSerializableExtra("DoctorDetailsSingle");
        doctorID = doctorDetails.getDoctorID();
        doctorName = doctorDetails.getFirstName()+" "+doctorDetails.getLastName();
        dateKey = getIntent().getStringExtra("Date");
        appointmentDateKey = getIntent().getStringExtra("BookingDate");

        dialog = new LoadingDialog(DoctorChamberListActivity.this, "Loading...");
        dialog.show();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView = findViewById(R.id.chamberListOfDoctor);

        rootRef = FirebaseDatabase.getInstance().getReference();
        doctorRef = rootRef.child("Doctor").child(doctorID);
        userRef = rootRef.child("Patient").child(user.getUid());
        chamberRef = doctorRef.child("DoctorDates").child(dateKey).child("Chambers");

        chamberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    chamberList.clear();
                    Chamber chamber = d.child("Details").getValue(Chamber.class);
                    chamberList.add(chamber);
                    //Toast.makeText(DoctorChamberListActivity.this, String.valueOf(d.child("Details").getValue()), Toast.LENGTH_SHORT).show();
                }
                adapter = new ChamberListAdapter(DoctorChamberListActivity.this, chamberList);
                LinearLayoutManager llm = new LinearLayoutManager(DoctorChamberListActivity.this);
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
        Intent intent = new Intent(DoctorChamberListActivity.this, Doctor.class);
        intent.putExtra("DoctorDetails", doctorDetails);
        startActivity(intent);
        finish();
    }


    @Override
    public void onSubmit(String description) {
        dialog.show();
        patientDescription = description;

        appointmentRef = doctorRef.child("AppointmentList").child("Pending");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String appointmentKey = appointmentRef.push().getKey();
                PatientDetails patient = dataSnapshot.child("Details").getValue(PatientDetails.class);

                patientFirstName = patient.getFirstName();
                patientLastName = patient.getLastName();
                patientImage = patient.getImageURI();
                String patientID = user.getUid();

                final Appointments appointment = new Appointments(appointmentKey, doctorID, bookedChamberKey, patientID, patientFirstName, patientLastName, patientDescription, appointmentDateKey, patientImage, bookedChamberArea, bookedChamberCity, doctorName, doctorDetails.getImageURI(), doctorDetails.getDegree(), doctorDetails.getDepartment(), bookedChamberTime);
                appointmentRef.child(appointmentKey).child("Value").setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userRef.child("AppointmentList").child(appointmentKey).child("Value").setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("sms", "You have new appointment");
                                tokenMap.put("from", String.valueOf(user.getUid()));
                                tokenMap.put("title", "Patient appointment");
                                tokenMap.put("to", doctorID);
                                rootRef.child("Notifications").child(doctorID).child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(DoctorChamberListActivity.this, Doctor.class);
                                        intent.putExtra("DoctorDetails", doctorDetails);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBooked(String chamberKey, String chamberArea, String chamberCity, String time) {
        Description_Dialog descriptionDialog = new Description_Dialog();
        descriptionDialog.show(getSupportFragmentManager(), "Description");

        bookedChamberKey = chamberKey;
        bookedChamberArea = chamberArea;
        bookedChamberCity = chamberCity;
        bookedChamberTime = time;
    }
}
