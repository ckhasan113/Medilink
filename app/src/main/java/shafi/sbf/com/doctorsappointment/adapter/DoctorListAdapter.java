package shafi.sbf.com.doctorsappointment.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.Chamber;
import shafi.sbf.com.doctorsappointment.pojo.DoctorDetails;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorListAdapterViewHolder> {

    private DatabaseReference rootRef;
    private DatabaseReference chamberRef;

    private Context context;
    private List<DoctorDetails> doctorDetailsList;
    private GetBookingAppointment bookingAppointment;
    private List<Chamber> chamberList = new ArrayList<Chamber>();

    public DoctorListAdapter(Context context, List<DoctorDetails> doctorDetailsList) {
        this.context = context;
        this.doctorDetailsList = doctorDetailsList;
        bookingAppointment = (GetBookingAppointment) context;
    }

    @NonNull
    @Override
    public DoctorListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DoctorListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.doctor_appointment_recycler_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorListAdapterViewHolder holder, int i) {

        final DoctorDetails doctor = doctorDetailsList.get(i);

        rootRef = FirebaseDatabase.getInstance().getReference();
        chamberRef = rootRef.child("Doctor").child(doctor.getDoctorID()).child("ChamberList").child("Approved");
        chamberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int countAppointment = 0;
                int count;
                chamberList.clear();
                for (DataSnapshot cbd: dataSnapshot.getChildren()){
                    Chamber cb = cbd.child("Details").getValue(Chamber.class);
                    chamberList.add(cb);
                    count = Integer.parseInt(cb.getPatient_count());
                    countAppointment+=count;
                }
                holder.appointmentNumberTV.setText(String.valueOf(countAppointment)+"  Appointments.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Uri photoUri = Uri.parse(doctor.getImageURI());
        Picasso.get().load(photoUri).into(holder.imageIV);

        holder.nameTV.setText("Dr. "+doctor.getFirstName()+" "+doctor.getLastName());
        holder.degreeTV.setText(doctor.getDegree());
        holder.specialityTV.setText(doctor.getDepartment());

        holder.getAppointmentL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingAppointment.bookingAppointment(doctor);
            }
        });


    }

    @Override
    public int getItemCount() {
        return doctorDetailsList.size();
    }

    class DoctorListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getAppointmentL;
        ImageView imageIV;
        TextView nameTV, degreeTV, specialityTV, appointmentNumberTV;

        public DoctorListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getAppointmentL = itemView.findViewById(R.id.getAppointment);
            imageIV = itemView.findViewById(R.id.doctor_image);
            nameTV = itemView.findViewById(R.id.doctor_name);
            degreeTV = itemView.findViewById(R.id.doctor_degree);
            specialityTV = itemView.findViewById(R.id.doctor_speciality);
            appointmentNumberTV = itemView.findViewById(R.id.doctor_appointment_number);
        }
    }

    public interface GetBookingAppointment{
        void bookingAppointment(DoctorDetails doctorDetails);
    }
}
