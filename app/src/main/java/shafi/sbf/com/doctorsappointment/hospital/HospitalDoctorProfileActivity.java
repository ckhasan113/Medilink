package shafi.sbf.com.doctorsappointment.hospital;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import shafi.sbf.com.doctorsappointment.Activity.Hospital;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.dialog.Description_Dialog;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.HospitalBooking;
import shafi.sbf.com.doctorsappointment.pojo.HospitalDetails;
import shafi.sbf.com.doctorsappointment.pojo.HospitalDoctorDetails;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;

public class HospitalDoctorProfileActivity extends AppCompatActivity implements Description_Dialog.DescriptionDialogListener {

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

    private HospitalDoctorDetails doctor;

    private DatabaseReference hospitalRef;
    private DatabaseReference hBookingRef;
    private DatabaseReference userRef;
    private DatabaseReference rootRef;

    private String patientFirstName;
    private String patientLastName;
    private String patientImage;

    private ImageView hProImage;

    private TextView hProNameTV, hProDegreeTV, hProSpecialityTV, hProHistoryTV, hProMonthTV;

    private CompactCalendarView compactCalendar;

    private List<String> dates = new ArrayList<String>();

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_doctor_profile);

        dialog = new LoadingDialog(HospitalDoctorProfileActivity.this, "Loading...");
        dialog.show();

        hospitalID = getIntent().getStringExtra("HospitalID");
        doctor = (HospitalDoctorDetails) getIntent().getSerializableExtra("HospitalDoctorDetails");

        dates = doctor.getDateList();

        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patient").child(user.getUid());
        hospitalRef = rootRef.child("Hospital").child(hospitalID).child("Details");
        hBookingRef = rootRef.child("Hospital").child(hospitalID).child("DoctorList").child(doctor.getDoctorID());

        hProImage = findViewById(R.id.hospitalDoctorImage);
        hProNameTV = findViewById(R.id.hospitalDoctorName);
        hProDegreeTV = findViewById(R.id.hospitalDoctorDegree);
        hProSpecialityTV = findViewById(R.id.hospitalDoctorSpeciality);
        hProHistoryTV = findViewById(R.id.hospitalDoctorHistory);
        hProMonthTV = findViewById(R.id.tvMonthHospitalDoctor);

        compactCalendar = findViewById(R.id.compactcalendar_view_hospital);

        Picasso.get().load(Uri.parse(doctor.getImage())).into(hProImage);

        hProNameTV.setText("Dr. " + doctor.getName());
        hProDegreeTV.setText(doctor.getDegree());
        hProSpecialityTV.setText(doctor.getSpeciality());
        hProHistoryTV.setText("Start time: " + doctor.getStartTime() + "\n" + "Phone: " + doctor.getPhone());

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
                        Description_Dialog descriptionDialog = new Description_Dialog();
                        descriptionDialog.show(getSupportFragmentManager(), "Description");
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalDoctorProfileActivity.this, Hospital.class);
        intent.putExtra("HospitalID", hospitalID);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSubmit(final String description) {
        dialog.show();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String key = hBookingRef.push().getKey();
                PatientDetails patient = dataSnapshot.child("Details").getValue(PatientDetails.class);

                patientFirstName = patient.getFirstName();
                patientLastName = patient.getLastName();
                patientImage = patient.getImageURI();
                String patientID = user.getUid();
                final String time = doctor.getStartTime()+" - "+doctor.getEndTime();
                final String id = hospitalID;

                hospitalRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        HospitalDetails hd = dataSnapshot.getValue(HospitalDetails.class);
                        HospitalBooking booking = new HospitalBooking(key, hospitalID, doctor.getDoctorID(), doctor.getName(), user.getUid(), appointmentDate, patientFirstName, patientLastName, description, patientImage,hd.getName(),doctor.getImage(),doctor.getDegree(), doctor.getSpeciality(),time, hd.getArea(), hd.getCity(), doctor.getRoom(), doctor.getFloor());
                        hBookingRef.child("Booking").child("Pending").child(key).child("Value").setValue(booking);
                        userRef.child("HospitalBookingList").child(key).child("Value").setValue(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("sms", "New booking request arrive");
                                tokenMap.put("from", String.valueOf(user.getUid()));
                                tokenMap.put("title", "Patient Booking");
                                tokenMap.put("to", id);
                                rootRef.child("Notifications").child(id).child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(HospitalDoctorProfileActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Intent intent = new Intent(HospitalDoctorProfileActivity.this, Hospital.class);
                                        intent.putExtra("HospitalID", hospitalID);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
