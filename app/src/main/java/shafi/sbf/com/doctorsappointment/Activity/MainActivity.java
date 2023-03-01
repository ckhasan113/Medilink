package shafi.sbf.com.doctorsappointment.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import shafi.sbf.com.doctorsappointment.EPharma.EVendor;
import shafi.sbf.com.doctorsappointment.Foreign.ForeignHospitalList;
import shafi.sbf.com.doctorsappointment.Nurse.VendorActivity;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.REgistration.LogIn;
import shafi.sbf.com.doctorsappointment.RecylaerViewAdapter;
import shafi.sbf.com.doctorsappointment.ViewPagerAdepter;
import shafi.sbf.com.doctorsappointment.physiotherapist.PhysiotherapistActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;

    private CardView doctorApoim,hospitalApoi,BloodDonor,Health,Accossires,
            Foreign,Ambulance,HealthTips,petAndAnimal,emergencyDoctor,Epharma,physiotherapist,NurseActi;

    private CardView Logout;
    private ViewPager ViewPAGEm;
    private RecylaerViewAdapter recylaerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patient").child(user.getUid());

        ViewPAGEm = findViewById(R.id.ViewPage);
        ViewPagerAdepter viewPAgerAdepter = new ViewPagerAdepter(this);
        ViewPAGEm.setAdapter(viewPAgerAdepter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000 , 3000);


        Logout = findViewById(R.id.log_out);

        doctorApoim = findViewById(R.id.doctor_appoi);
        hospitalApoi = findViewById(R.id.hospital_appoi);
        BloodDonor = findViewById(R.id.blood_donor);
        BloodDonor = findViewById(R.id.blood_donor);
        Health = findViewById(R.id.health_report);
        Foreign = findViewById(R.id.foregin_appointment);
        Ambulance = findViewById(R.id.ambulance);
        HealthTips = findViewById(R.id.health_tips);
        petAndAnimal = findViewById(R.id.pet_and_animal);
        emergencyDoctor = findViewById(R.id.emergency_doctor);
        Epharma = findViewById(R.id.epharma);
        Accossires = findViewById(R.id.accoss);
        physiotherapist = findViewById(R.id.physiotherapist);
        NurseActi = findViewById(R.id.nurse);


        doctorApoim.setOnClickListener(this);
        hospitalApoi.setOnClickListener(this);
        BloodDonor.setOnClickListener(this);
        Health.setOnClickListener(this);
        Foreign.setOnClickListener(this);
        Ambulance.setOnClickListener(this);
        HealthTips.setOnClickListener(this);
        petAndAnimal.setOnClickListener(this);
        emergencyDoctor.setOnClickListener(this);
        Epharma.setOnClickListener(this);
        Accossires.setOnClickListener(this);
        physiotherapist.setOnClickListener(this);
        NurseActi.setOnClickListener(this);


        Logout.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify1", "notification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("client");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.doctor_appoi){
            Intent intent = new Intent(MainActivity.this,DoctorAppointmentList.class);
            startActivity(intent);
        }else if (v.getId() == R.id.hospital_appoi){
            Intent intent = new Intent(MainActivity.this,HospitalList.class);
            startActivity(intent);
        }else if (v.getId() == R.id.blood_donor){
            Intent intent = new Intent(MainActivity.this,BloodBank.class);
            startActivity(intent);
        }else if (v.getId() == R.id.health_report){
            Intent intent = new Intent(MainActivity.this, HealthReportActivity.class);
            startActivity(intent);
            finish();
        }else if (v.getId() == R.id.foregin_appointment){
            Intent intent = new Intent(MainActivity.this, ForeignHospitalList.class);
            startActivity(intent);
        }else if (v.getId() == R.id.ambulance){
            Intent intent = new Intent(MainActivity.this,Ambulance.class);
            startActivity(intent);
        }else if (v.getId() == R.id.health_tips){
            Intent intent = new Intent(MainActivity.this,HealthTipsHome.class);
            startActivity(intent);
            finish();
        }else if (v.getId() == R.id.pet_and_animal){
            Intent intent = new Intent(MainActivity.this,PetHome.class);
            startActivity(intent);
        }else if (v.getId() == R.id.emergency_doctor){
            Intent intent = new Intent(MainActivity.this,EmergencyDoctor.class);
            startActivity(intent);
        }else if (v.getId() == R.id.epharma){
            Intent intent = new Intent(MainActivity.this, EVendor.class);
            startActivity(intent);
        }else if (v.getId() == R.id.accoss){
            Intent intent = new Intent(MainActivity.this,AccessoricsList.class);
            startActivity(intent);
        }else if (v.getId() == R.id.physiotherapist){
            Intent intent = new Intent(MainActivity.this, PhysiotherapistActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.nurse){
            Intent intent = new Intent(MainActivity.this, VendorActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.log_out){

            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("device_token", "");
            rootRef.child("Users").child(user.getUid()).child("Token").updateChildren(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    auth.signOut();
                    Intent intent = new Intent(MainActivity.this, LogIn.class);
                    startActivity(intent);
                    finish();
                }
            });

            /*SharedPrefManager.getInstance(this).clear();
            Intent intent = new Intent(MainActivity.this, LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();*/
        }
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(ViewPAGEm.getCurrentItem() == 0){
                        ViewPAGEm.setCurrentItem(1);
                    }else if(ViewPAGEm.getCurrentItem() == 1){
                        ViewPAGEm.setCurrentItem(2);
                    }else {
                        ViewPAGEm.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
