package shafi.sbf.com.doctorsappointment.Foreign;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import java.util.List;


import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.PatientRecylaerViewAdapter;
import shafi.sbf.com.doctorsappointment.pojo.ForeignBooking;

public class MyAppointment extends AppCompatActivity {

    RecyclerView doctorRecycler;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private List<ForeignBooking> reqList = new ArrayList<ForeignBooking>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);

        rootRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        doctorRecycler = findViewById(R.id.appoiment_base_recycler);

        user = auth.getCurrentUser();

        userRef = rootRef.child("Patient").child(user.getUid()).child("ForeignHospitalBookingList");

        getAppointmentdata();
    }

    private void getAppointmentdata() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reqList.clear();
                //historyTV.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd: dataSnapshot.getChildren()){
                    ForeignBooking doc = hd.child("Value").getValue(ForeignBooking.class);
                    reqList.add(doc);
                }

                PatientRecylaerViewAdapter recylaerViewAdapter = new PatientRecylaerViewAdapter(MyAppointment.this,reqList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                doctorRecycler.setLayoutManager(mLayoutManager);
                doctorRecycler.setItemAnimator(new DefaultItemAnimator());
                doctorRecycler.setAdapter(recylaerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyAppointment.this, ForeignHospitalList.class));
        finish();
    }
}
