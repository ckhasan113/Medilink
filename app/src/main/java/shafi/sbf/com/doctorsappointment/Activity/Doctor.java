package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
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
import java.util.List;
import java.util.Locale;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.appointment.DoctorChamberListActivity;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.DoctorDates;
import shafi.sbf.com.doctorsappointment.pojo.DoctorDetails;

public class Doctor extends AppCompatActivity {

    private CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd-MMMM, yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatMonthForAvailable = new SimpleDateFormat("dd-MMMM", Locale.getDefault());
    private SimpleDateFormat dateFormatForDateOfMonth = new SimpleDateFormat("dd", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonthOfMonth = new SimpleDateFormat("MM", Locale.getDefault());
    private SimpleDateFormat dateFormatForYearOfMonth = new SimpleDateFormat("yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForAppointment = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private int satArray[] = new int[8];
    private int sunArray[] = new int[8];
    private int monArray[] = new int[8];
    private int tueArray[] = new int[8];
    private int wedArray[] = new int[8];
    private int thuArray[] = new int[8];
    private int friArray[] = new int[8];

    private String firstDayOfMonth;

    private TextView TvMonth;

    private String monthString;
    private String yearString;
    private String dateString;
    private String fullDateString;
    private String appointmentDate;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference doctorRef;
    private DatabaseReference doctorDateRef;

    private LoadingDialog dialog;

    private DoctorDetails doctorDetails;

    private ImageView doctorImageIV;

    private TextView doctorNameTV, doctorDegreeTV, doctorSpecialityTV, doctorHistoryTV;

    private List<String> dates = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        doctorDetails = (DoctorDetails) getIntent().getSerializableExtra("DoctorDetails");

        rootRef = FirebaseDatabase.getInstance().getReference();
        doctorRef = rootRef.child("Doctor").child(doctorDetails.getDoctorID());
        doctorDateRef = doctorRef.child("DoctorDates");


        dialog = new LoadingDialog(Doctor.this,"Loading...");
        dialog.show();

        TvMonth = findViewById(R.id.tvMonth);

        doctorImageIV = findViewById(R.id.doctorImage);
        doctorNameTV = findViewById(R.id.doctorName);
        doctorDegreeTV = findViewById(R.id.doctorDegree);
        doctorSpecialityTV = findViewById(R.id.doctorSpeciality);
        doctorHistoryTV = findViewById(R.id.doctorHistory);

        Uri photoUri = Uri.parse(doctorDetails.getImageURI());
        Picasso.get().load(photoUri).into(doctorImageIV);
        doctorNameTV.setText("Dr. "+doctorDetails.getFirstName()+" "+doctorDetails.getLastName());
        doctorDegreeTV.setText(doctorDetails.getDegree());
        doctorSpecialityTV.setText(doctorDetails.getDepartment());
        doctorHistoryTV.setText("Experience: "+doctorDetails.getExperience()+"\nID: "+doctorDetails.getDoctorID());
        dialog.dismiss();
//        DoctorAppointment = findViewById(R.id.doctor_appoi_byTime);
//
//        mydialog = new Dialog(this);
//
//        DoctorAppointment.setOnClickListener(new CardView.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mydialog.setContentView(R.layout.castom_appointment);
//                TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
//                txtclose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mydialog.dismiss();
//                    }
//                });
//
//                Button confirm = mydialog.findViewById(R.id.confirmBooking);
//                confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mydialog.dismiss();
//
//                    }
//                });
//
//                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                mydialog.show();
//            }
//        });
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setFirstDayOfWeek(Calendar.SATURDAY);

        TvMonth.setText(dateFormatMonth.format(Calendar.getInstance().getTime()));

        dateString = dateFormatForDateOfMonth.format(Calendar.getInstance().getTime());
        monthString = dateFormatForMonthOfMonth.format(Calendar.getInstance().getTime());
        yearString = dateFormatForYearOfMonth.format(Calendar.getInstance().getTime());
        fullDateString = dateString+"/"+monthString+"/"+yearString;

        /*doctorNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int a: friArray){
                    Toast.makeText(Doctor.this, String.valueOf(a), Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        calendarInitializing();

        //Set an event for Teachers' Professional Day 2016 which is 21st of October

        /*Event ev1 = new Event(Color.GREEN, 1559126559000L, "Doctor Available");
        Event ev2 = new Event(Color.GREEN, 1559299359000L, "Doctor Available" +
                "");
        compactCalendar.addEvent(ev1);
        compactCalendar.addEvent(ev2);*/



        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(final Date dateClicked) {
                final Context context = getApplicationContext();
                TvMonth.setText(dateFormatMonth.format(dateClicked));
                //calendarInitializing();

                //TvMonth.setText(dateFormatForFirstDayOfMonth.format(dateClicked));

//                String date = dateClicked.toString();
//                Toast.makeText(context, date, Toast.LENGTH_SHORT).show();
//                Log.d("date",date);
                appointmentDate = dateFormatForDateOfMonth.format(dateClicked);

                for (String l: dates){
                    if (l.equals(appointmentDate)){
                        Intent intent = new Intent(Doctor.this, DoctorChamberListActivity.class);
                        intent.putExtra("DoctorDetailsSingle", doctorDetails);
                        intent.putExtra("Date", l);
                        intent.putExtra("BookingDate", dateFormatForAppointment.format(dateClicked));
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                Toast.makeText(context, "Doctor not available on "+dateFormatMonthForAvailable.format(dateClicked), Toast.LENGTH_SHORT).show();
               /* doctorDateRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            DoctorDates dd = d.getValue(DoctorDates.class);
                            if (dd.getDate().equals(appointmentDate)){
                                Intent intent = new Intent(Doctor.this, DoctorChamberListActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                available[0] = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
               // actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));

                TvMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
                monthString = dateFormatForMonthOfMonth.format(firstDayOfNewMonth);
                yearString = dateFormatForYearOfMonth.format(firstDayOfNewMonth);
                calendarInitializing();
            }
        });
    }

    public void calendarInitializing(){

        doctorDateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dates.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    DoctorDates dd = d.child("Dates").getValue(DoctorDates.class);
                    long startDate = 0L;
                    try {
                        dateString = dd.getDate();
                        fullDateString = dateString+"/"+monthString+"/"+yearString;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = sdf.parse(fullDateString);
                        startDate = date.getTime();
                        dates.add(dateString);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Event ev = new Event(Color.GREEN, startDate, "Doctor Available");
                    compactCalendar.addEvent(ev);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*doctorDateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    DoctorDates dd = d.getValue(DoctorDates.class);
                    long startDate = 0L;
                    try {
                        dateString = dd.getDate();
                        fullDateString = dateString+"/"+monthString+"/"+yearString;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = sdf.parse(fullDateString);
                        startDate = date.getTime();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Event ev = new Event(Color.GREEN, startDate, "Doctor Available");
                    compactCalendar.addEvent(ev);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Doctor.this, DoctorAppointmentList.class);
        startActivity(intent);
        finish();
    }
}