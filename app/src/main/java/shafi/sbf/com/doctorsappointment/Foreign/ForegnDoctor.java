package shafi.sbf.com.doctorsappointment.Foreign;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import shafi.sbf.com.doctorsappointment.Activity.Doctor;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.appointment.DoctorChamberListActivity;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.ForeignBooking;
import shafi.sbf.com.doctorsappointment.pojo.ForeignDoctorDetails;
import shafi.sbf.com.doctorsappointment.pojo.ForeignHospitalDetails;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;

public class ForegnDoctor extends AppCompatActivity {

    private ForeignHospitalDetails FhosDetails;
    private ForeignDoctorDetails FdocDetails;
    private FirebaseUser user;

    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd-MMMM, yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatMonthForAvailable = new SimpleDateFormat("dd-MMMM", Locale.getDefault());
    private SimpleDateFormat dateFormatForDateOfMonth = new SimpleDateFormat("dd", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonthOfMonth = new SimpleDateFormat("MM", Locale.getDefault());
    private SimpleDateFormat dateFormatForYearOfMonth = new SimpleDateFormat("yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForAppointment = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private String monthString;
    private String yearString;
    private String dateString;
    private String fullDateString;
    private String appointmentDate;
    private String checkDate;

    private String hospitalID;



    private DatabaseReference hospitalDocRef;
    private DatabaseReference hBookingRef;
    private DatabaseReference userRef;
    private DatabaseReference rootRef;

    private String patientName;
    private String patientLastName;
    private String patientImage;
    private String hospitalName;
    private String hospitalArea;
    private String hospitalCity;

    String patientID,time;

    private ImageView hProImage;

    private TextView hProNameTV, hProDegreeTV, hProSpecialityTV, hProMonthTV;

    Dialog mydialog;

    private CompactCalendarView compactCalendar;

    private List<String> dates = new ArrayList<String>();

    private LoadingDialog dialog;

    String country;


    private PatientDetails patientDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foregn_doctor);


        country = getIntent().getStringExtra("country");
        FhosDetails = (ForeignHospitalDetails) getIntent().getSerializableExtra("HospitalDetails");
        FdocDetails = (ForeignDoctorDetails) getIntent().getSerializableExtra("DoctorDetails");

        dialog = new LoadingDialog(ForegnDoctor.this, "Loading...");
        dialog.show();

        mydialog = new Dialog(this);

        dates = FdocDetails.getDateList();

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patient").child(user.getUid());
        hBookingRef = rootRef.child("Foreign Doctor").child("Country").child(country).child("Hospital").child(FhosDetails.getHospitalID()).child("BookingList");
        hospitalDocRef = rootRef.child("Foreign Doctor").child("Country").child(country).child("Hospital").child(FhosDetails.getHospitalID()).child("DoctorList").child(FdocDetails.getDoctorID()).child("Pending");

        hProImage = findViewById(R.id.FDoctorImage);
        hProNameTV = findViewById(R.id.FDoctorName);
        hProDegreeTV = findViewById(R.id.FDoctorDegree);
        hProSpecialityTV = findViewById(R.id.FDoctorSpeciality);
        hProMonthTV = findViewById(R.id.FtvMonth);


        compactCalendar = findViewById(R.id.Fcompactcalendar_view);

        Picasso.get().load(Uri.parse(FdocDetails.getImageURI())).into(hProImage);

        hProNameTV.setText(FdocDetails.getName());
        hProDegreeTV.setText(FdocDetails.getDegree());
        hProSpecialityTV.setText(FdocDetails.getSpeciality());

        time = FdocDetails.getStartTime()+" ~ "+FdocDetails.getEndTime();

        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setFirstDayOfWeek(Calendar.SATURDAY);

        hProMonthTV.setText(dateFormatMonth.format(Calendar.getInstance().getTime()));

        dateString = dateFormatForDateOfMonth.format(Calendar.getInstance().getTime());
        monthString = dateFormatForMonthOfMonth.format(Calendar.getInstance().getTime());
        yearString = dateFormatForYearOfMonth.format(Calendar.getInstance().getTime());
        fullDateString = dateString + "/" + monthString + "/" + yearString;

        calendarInitializing();

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date date) {
                final Context context = getApplicationContext();
                hProMonthTV.setText(dateFormatMonth.format(date));

                checkDate = dateFormatForDateOfMonth.format(date);

                for (String l : dates) {
                    if (l.equals(checkDate)) {
                        appointmentDate = dateFormatForAppointment.format(date);
                        ConfirmPopUp();
                        return;
                    }
                }
                Toast.makeText(context, "Doctor not available on " + dateFormatMonthForAvailable.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthScroll(Date date) {
                hProMonthTV.setText(dateFormatMonth.format(date));
                monthString = dateFormatForMonthOfMonth.format(date);
                yearString = dateFormatForYearOfMonth.format(date);
                calendarInitializing();
            }
        });


    }

    private void ConfirmPopUp() {
        mydialog.setContentView(R.layout.castom_appointment2);
        TextView txtclose =(TextView) mydialog.findViewById(R.id.Ftxtclose);
        TextView HName =(TextView) mydialog.findViewById(R.id.FHospital_name);
        TextView Dname =(TextView) mydialog.findViewById(R.id.FDoctor_name);
        TextView Date =(TextView) mydialog.findViewById(R.id.Fdate);
        TextView Time =(TextView) mydialog.findViewById(R.id.Ftime);

        HName.setText(FhosDetails.getName());
        Dname.setText(FdocDetails.getName());
        Date.setText(appointmentDate);
        Time.setText(time);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

        final Button confirm = mydialog.findViewById(R.id.confirmBooking222);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetUserDAta();
                mydialog.dismiss();

            }
        });

        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
    }

    private void GetUserDAta() {

        dialog.show();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                patientDetails = dataSnapshot.child("Details").getValue(PatientDetails.class);

                confirmBooking();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void confirmBooking() {

        final String key = hBookingRef.push().getKey();
        final ForeignBooking booking = new ForeignBooking(key,appointmentDate, "apatoto nai",FdocDetails,FhosDetails,patientDetails);
        hBookingRef.child(key).child("Value").setValue(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                hospitalDocRef.child(key).child("Value").setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userRef.child("ForeignHospitalBookingList").child(key).child("Value").setValue(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("sms", "You have new appointment");
                                tokenMap.put("from", String.valueOf(user.getUid()));
                                tokenMap.put("title", "Patient appointment");
                                tokenMap.put("to", booking.getForeignHospitalDetails().getHospitalID());
                                rootRef.child("Notifications").child(booking.getForeignHospitalDetails().getHospitalID()).child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ForegnDoctor.this, "Successful", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Intent intent = new Intent(ForegnDoctor.this, ForegnDoctor.class);
                                        intent.putExtra("country",country);
                                        intent.putExtra("DoctorDetails", FdocDetails);
                                        intent.putExtra("HospitalDetails", FhosDetails);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });



            }
        });


    }

    private void calendarInitializing() {
        dialog.show();

        for (String d : dates) {

            long startDate = 0L;
            try {

                fullDateString = d + "/" + monthString + "/" + yearString;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(fullDateString);
                startDate = date.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Event ev = new Event(Color.GREEN, startDate, "Doctor Available");
            compactCalendar.addEvent(ev);
        }

        dialog.dismiss();
    }

}
