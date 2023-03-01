package shafi.sbf.com.doctorsappointment.appointment;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import shafi.sbf.com.doctorsappointment.Activity.DoctorAppointmentList;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.PatientDoctorAppointmentAdapter;
import shafi.sbf.com.doctorsappointment.pojo.Appointments;

public class AppointmentListActivity extends AppCompatActivity {

    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;

    private RecyclerView appointmentRecycler;

    private List<Appointments> list = new ArrayList<Appointments>();

    private PatientDoctorAppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        appointmentRecycler = findViewById(R.id.your_doctor_appointment_recycler);

        user = FirebaseAuth.getInstance().getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patient").child(user.getUid()).child("AppointmentList");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Appointments a = d.child("Value").getValue(Appointments.class);
                    list.add(a);
                }
                Collections.reverse(list);
                adapter = new PatientDoctorAppointmentAdapter(AppointmentListActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(AppointmentListActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                appointmentRecycler.setLayoutManager(llm);
                appointmentRecycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AppointmentListActivity.this, DoctorAppointmentList.class);
        startActivity(intent);
        finish();
    }
}
