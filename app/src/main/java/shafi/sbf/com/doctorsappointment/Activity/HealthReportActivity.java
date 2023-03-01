package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.HealthReportAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.HealthReport;

public class HealthReportActivity extends AppCompatActivity implements HealthReportAdapter.HealthReportAdapterListener {

    private RecyclerView reportRecycler;

    private List<HealthReport> reportList = new ArrayList<HealthReport>();

    private DatabaseReference reportRef;

    private LoadingDialog dialog;

    private HealthReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_report);

        dialog = new LoadingDialog(HealthReportActivity.this, "Loading...");
        dialog.show();

        reportRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HealthReport");

        reportRecycler = findViewById(R.id.health_report_recycler);

        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HealthReport hr = d.child("Details").getValue(HealthReport.class);
                    reportList.add(hr);
                }
                Collections.reverse(reportList);
                adapter = new HealthReportAdapter(HealthReportActivity.this, reportList);
                LinearLayoutManager llm = new LinearLayoutManager(HealthReportActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                reportRecycler.setLayoutManager(llm);
                reportRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addReport(View view) {

        startActivity(new Intent(HealthReportActivity.this,HealthReportUpload.class));
        finish();
    }

    /*@Override
    public void onClick(View v) {
        if (v.getId()==R.id.re1){
            startActivity(new Intent(HealthReportActivity.this,HealthReportView.class));
        }
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HealthReportActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onReportDetails(HealthReport hr) {
        Intent intent = new Intent(HealthReportActivity.this, HealthReportView.class);
        intent.putExtra("HealthReport", hr);
        startActivity(intent);
        finish();
    }
}
