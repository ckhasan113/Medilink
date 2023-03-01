package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.HealthTipsAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.HealthTips;

public class HealthTipsHome extends AppCompatActivity implements HealthTipsAdapter.HealthTipsAdapterListener {

    private RecyclerView tipRecycler;

    private LoadingDialog dialog;

    private DatabaseReference rootRef;
    private DatabaseReference tipRef;

    private List<HealthTips> tipsList = new ArrayList<HealthTips>();

    private HealthTipsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips_home);

        dialog = new LoadingDialog(HealthTipsHome.this, "Loading...");
        dialog.show();

        rootRef = FirebaseDatabase.getInstance().getReference();
        tipRef = rootRef.child("Admin").child("Health Tips");

        tipRecycler = findViewById(R.id.health_tips_recycler);

        tipRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tipsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HealthTips health = d.child("Value").getValue(HealthTips.class);
                    tipsList.add(health);
                }
                Collections.reverse(tipsList);
                adapter = new HealthTipsAdapter(HealthTipsHome.this, tipsList);
                LinearLayoutManager llm = new LinearLayoutManager(HealthTipsHome.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                tipRecycler.setLayoutManager(llm);
                tipRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HealthTipsHome.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTipDetails(HealthTips ht) {
        Intent intent = new Intent(HealthTipsHome.this, HealthTipsDetails.class);
        intent.putExtra("HealthTipDetails", ht);
        startActivity(intent);
        finish();
    }
}
