package shafi.sbf.com.doctorsappointment.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
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
import shafi.sbf.com.doctorsappointment.adapter.HospitalListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.hospital.BookingListActivity;
import shafi.sbf.com.doctorsappointment.pojo.HospitalDetails;

public class HospitalList extends AppCompatActivity implements HospitalListAdapter.HospitalListAdapterListener {

    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference hospitalRef;

    private Toolbar searchBar;

    private LoadingDialog dialog;

    private LinearLayoutManager llm;

    private RecyclerView hospitalRecycler;

    private HospitalListAdapter adapter;

    private List<HospitalDetails> hospitalDetailsList = new ArrayList<HospitalDetails>();

    private TextView yourBookingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        dialog = new LoadingDialog(HospitalList.this,"Loading...");
        dialog.show();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        hospitalRef = rootRef.child("Hospital");

        hospitalRecycler = findViewById(R.id.hospital_list_recycler);
        yourBookingTV = findViewById(R.id.patient_hospital_doctor_booking);

        searchBar = findViewById(R.id.search_bar_base_hospital);
        setSupportActionBar(searchBar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\" style=\"text-align: center\">" + "Hospital appointment" + "</font>"));

        hospitalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hospitalDetailsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HospitalDetails hos = d.child("Details").getValue(HospitalDetails.class);
                    hospitalDetailsList.add(hos);
                }
                adapter = new HospitalListAdapter(HospitalList.this, hospitalDetailsList);
                llm = new LinearLayoutManager(HospitalList.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                hospitalRecycler.setLayoutManager(llm);
                hospitalRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        yourBookingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalList.this, BookingListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        androidx.appcompat.widget.SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();

        if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            searchView.setPadding(3,0,0,0);
            searchView.setQueryHint("Search by full name...");

        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final String hName = s.toLowerCase();
                dialog.show();
                //Toast.makeText(HospitalList.this, name, Toast.LENGTH_SHORT).show();
                hospitalRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hospitalDetailsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            HospitalDetails hos = d.child("Details").getValue(HospitalDetails.class);
                            String name = hos.getName().toLowerCase();
                            if (hName.equals(name)){
                                hospitalDetailsList.add(hos);
                            }
                        }
                        adapter = new HospitalListAdapter(HospitalList.this, hospitalDetailsList);
                        llm = new LinearLayoutManager(HospitalList.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);

                        hospitalRecycler.setLayoutManager(llm);
                        hospitalRecycler.setAdapter(adapter);
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

    @Override
    public void onClickBooking(String hospitalID) {
        Intent intent = new Intent(HospitalList.this, Hospital.class);
        intent.putExtra("HospitalID", hospitalID);
        startActivity(intent);
        finish();
    }

    /*@Override
    public void onClick(View v) {
        if (v.getId()==R.id.hospital){

            Intent intent = new Intent(HospitalList.this,ForeignHospital.class);
            startActivity(intent);
        }
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalList.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
