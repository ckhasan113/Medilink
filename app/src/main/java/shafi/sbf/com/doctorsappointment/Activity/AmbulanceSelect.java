package shafi.sbf.com.doctorsappointment.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.appointment.DoctorChamberListActivity;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.AmbulanceTaken;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;

public class AmbulanceSelect extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference useRefsetValu;
    private DatabaseReference ambRefsetValu;


    private String id;
    private String price;
    private String ambulanceType;
    private String patient_id;
    private String PatientName;
    private String phone;
    private String email;
    private String status;

    PatientDetails patientDetails;

    CardView lite,air,premier;
    Dialog mydialog;

    TextView From,To;

    String Sfrom,Sto;

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_select);

        dialog = new LoadingDialog(AmbulanceSelect.this, "Loading...");

        mydialog = new Dialog(this);

        Sfrom = getIntent().getStringExtra("from");
        Sto = getIntent().getStringExtra("to");

        From = findViewById(R.id.Tfrom);
        To = findViewById(R.id.Tto);

        From.setText(Sfrom);
        To.setText(Sto);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //Init FireBase Storage
        rootRef = FirebaseDatabase.getInstance().getReference();

        patient_id = user.getUid();
        userRef = rootRef.child("Patient").child(patient_id);
        useRefsetValu=userRef.child("Ambulance Taken");
        id = useRefsetValu.push().getKey();

        getPatientDetails();

        lite = findViewById(R.id.ambulance_lite);
        premier = findViewById(R.id.ambulance_premier);
        air = findViewById(R.id.ambulance_air);

        lite.setOnClickListener(this);
        premier.setOnClickListener(this);
        air.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ambulance_lite){

            ambulanceType = "Lite";

            mydialog.setContentView(R.layout.custom_ambulance_confirm);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            TextView txtf =(TextView) mydialog.findViewById(R.id.TVfrom);
            TextView txtt =(TextView) mydialog.findViewById(R.id.TVto);

            txtf.setText(Sfrom);
            txtt.setText(Sto);

            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBooking);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmAmbulance();
                    mydialog.dismiss();


                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.ambulance_premier){
            ambulanceType = "Premier";
            mydialog.setContentView(R.layout.custom_ambulance_confirm);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            TextView txtf =(TextView) mydialog.findViewById(R.id.TVfrom);
            TextView txtt =(TextView) mydialog.findViewById(R.id.TVto);

            txtf.setText(Sfrom);
            txtt.setText(Sto);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBooking);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmAmbulance();
                    mydialog.dismiss();


                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.ambulance_air){
            ambulanceType = "Air";
            mydialog.setContentView(R.layout.custom_ambulance_confirm);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            TextView txtf =(TextView) mydialog.findViewById(R.id.TVfrom);
            TextView txtt =(TextView) mydialog.findViewById(R.id.TVto);

            txtf.setText(Sfrom);
            txtt.setText(Sto);

            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBooking);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ConfirmAmbulance();
                    mydialog.dismiss();


                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }
    }

    private void ConfirmAmbulance() {

        dialog.show();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String Date = df.format(c);

        ambRefsetValu = rootRef.child("Ambulance").child("Ambulance Type").child(ambulanceType).child("Request").child(id);

        final AmbulanceTaken ambulanceTaken= new AmbulanceTaken(id,Sfrom,Sto,"00.00",ambulanceType,patientDetails,"null",Date);

        ambRefsetValu.child("Details")
                .setValue(ambulanceTaken)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            useRefsetValu.child(id).child("Details").setValue(ambulanceTaken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    rootRef.child("Ambulance").child("All Request").child(id).child("Details").setValue(ambulanceTaken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Map<String, Object> tokenMap = new HashMap<>();
                                            tokenMap.put("sms", "New ambulance request arrive");
                                            tokenMap.put("from", String.valueOf(user.getUid()));
                                            tokenMap.put("title", "Client Request");
                                            tokenMap.put("to", "adminID");
                                            rootRef.child("Notifications").child("adminID").child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    dialog.dismiss();
                                                    Toast.makeText(AmbulanceSelect.this, "Successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(AmbulanceSelect.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                }
                            });


                        } else {
                            Toast.makeText(AmbulanceSelect.this, "Failed..! Try again....!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void getPatientDetails() {

        userRef.child("Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                patientDetails = dataSnapshot.getValue(PatientDetails.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
