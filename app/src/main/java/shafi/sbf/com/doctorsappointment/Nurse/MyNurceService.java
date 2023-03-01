package shafi.sbf.com.doctorsappointment.Nurse;

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
import shafi.sbf.com.doctorsappointment.adapter.RequestRecylaerViewAdapter;
import shafi.sbf.com.doctorsappointment.pojo.NurseAddtoCart;

public class MyNurceService extends AppCompatActivity implements RequestRecylaerViewAdapter.ReqDetailsListener{

    private FirebaseAuth auth;

    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;

    private RecyclerView packageRecycler;
    private List<NurseAddtoCart> reqList = new ArrayList<NurseAddtoCart>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_nurce_service);

        rootRef = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        packageRecycler = findViewById(R.id.my_service_base_recycler);

        user = auth.getCurrentUser();

        userRef = rootRef.child("Patient").child(user.getUid()).child("Nurse Taken");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reqList.clear();
                //historyTV.setText(String.valueOf(dataSnapshot.getValue()));
                for (DataSnapshot hd: dataSnapshot.getChildren()){
                    NurseAddtoCart doc = hd.child("Details").getValue(NurseAddtoCart.class);
                    reqList.add(doc);
                }

                RequestRecylaerViewAdapter recylaerViewAdapter = new RequestRecylaerViewAdapter(MyNurceService.this, reqList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                packageRecycler.setLayoutManager(mLayoutManager);
                packageRecycler.setItemAnimator(new DefaultItemAnimator());
                packageRecycler.setAdapter(recylaerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onReqDetails(NurseAddtoCart requestDetails) {

    }
}
