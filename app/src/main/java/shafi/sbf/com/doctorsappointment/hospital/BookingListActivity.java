package shafi.sbf.com.doctorsappointment.hospital;

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

import shafi.sbf.com.doctorsappointment.Activity.HospitalList;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.PatientHospitalBookingAdapter;
import shafi.sbf.com.doctorsappointment.pojo.HospitalBooking;

public class BookingListActivity extends AppCompatActivity {

    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;

    private RecyclerView bookingRecycler;

    private List<HospitalBooking> list = new ArrayList<HospitalBooking>();

    private PatientHospitalBookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        bookingRecycler = findViewById(R.id.your_hospital_doctor_booking_recycler);

        user = FirebaseAuth.getInstance().getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patient").child(user.getUid()).child("HospitalBookingList");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HospitalBooking a = d.child("Value").getValue(HospitalBooking.class);
                    list.add(a);
                }
                Collections.reverse(list);
                adapter = new PatientHospitalBookingAdapter(BookingListActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(BookingListActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                bookingRecycler.setLayoutManager(llm);
                bookingRecycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BookingListActivity.this, HospitalList.class);
        startActivity(intent);
        finish();
    }
}
