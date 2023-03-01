package shafi.sbf.com.doctorsappointment.physiotherapist;

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

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.MyPhysiotherapistListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistBooking;

public class MyPhysiotherapistTakenServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseUser user;

    private LoadingDialog dialog;

    private DatabaseReference physioRef;

    private List<PhysiotherapistBooking> list = new ArrayList<PhysiotherapistBooking>();

    private MyPhysiotherapistListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_taken_services);

        dialog = new LoadingDialog(MyPhysiotherapistTakenServicesActivity.this, "Loading...");
        dialog.show();

        user = FirebaseAuth.getInstance().getCurrentUser();

        physioRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(user.getUid()).child("PhysiotherapistBooking");

        recyclerView = findViewById(R.id.myPhysiotherapistRecycler);

        physioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    PhysiotherapistBooking pb = d.child("Details").getValue(PhysiotherapistBooking.class);
                    list.add(pb);
                }
                Collections.reverse(list);
                adapter = new MyPhysiotherapistListAdapter(MyPhysiotherapistTakenServicesActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(MyPhysiotherapistTakenServicesActivity.this);
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
        Intent intent = new Intent(MyPhysiotherapistTakenServicesActivity.this, PhysiotherapistActivity.class);
        startActivity(intent);
        finish();
    }
}
