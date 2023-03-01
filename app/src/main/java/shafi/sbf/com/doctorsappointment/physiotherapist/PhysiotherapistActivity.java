package shafi.sbf.com.doctorsappointment.physiotherapist;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.PhysiotherapistListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistVendorDetails;

public class PhysiotherapistActivity extends AppCompatActivity implements PhysiotherapistListAdapter.PhysiotherapistListAdapterListener {

    private DatabaseReference rootRef;
    private DatabaseReference physioRef;

    private LoadingDialog dialog;

    private List<PhysiotherapistVendorDetails> therapistList = new ArrayList<PhysiotherapistVendorDetails>();
    private RecyclerView therapistRecycler;

    private PhysiotherapistListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physiotherapist);

        dialog = new LoadingDialog(PhysiotherapistActivity.this, "Loading...");
        dialog.show();

        rootRef = FirebaseDatabase.getInstance().getReference();

        physioRef = rootRef.child("Physiotherapist");

        therapistRecycler = findViewById(R.id.physiotherapist_vendor_base_recycler);

        physioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    PhysiotherapistVendorDetails vd = d.child("Details").getValue(PhysiotherapistVendorDetails.class);
                    therapistList.add(vd);
                }
                adapter = new PhysiotherapistListAdapter(PhysiotherapistActivity.this, therapistList);
                LinearLayoutManager llm = new LinearLayoutManager(PhysiotherapistActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                therapistRecycler.setLayoutManager(llm);
                therapistRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void myTakenServices(View view) {
        Intent intent = new Intent(PhysiotherapistActivity.this, MyPhysiotherapistTakenServicesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getPhysiotherapist(PhysiotherapistVendorDetails vendorDetails) {
        Intent intent = new Intent(PhysiotherapistActivity.this, GetPhysiotherapistActivity.class);
        intent.putExtra("Physiotherapist", vendorDetails);
        startActivity(intent);
        finish();
    }
}
