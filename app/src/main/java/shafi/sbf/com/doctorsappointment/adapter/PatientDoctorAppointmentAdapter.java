package shafi.sbf.com.doctorsappointment.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.Appointments;

public class PatientDoctorAppointmentAdapter extends RecyclerView.Adapter<PatientDoctorAppointmentAdapter.PatientDoctorAppointmentAdapterViewHolder> {

    private Context context;
    private List<Appointments> list;

    public PatientDoctorAppointmentAdapter(Context context, List<Appointments> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PatientDoctorAppointmentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PatientDoctorAppointmentAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.patient_doctor_appoint_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientDoctorAppointmentAdapterViewHolder holder, int i) {
        Appointments ap = list.get(i);
        Picasso.get().load(Uri.parse(ap.getDoctorImage())).into(holder.doctorImage);
        holder.nameTV.setText("Dr. "+ap.getDoctorName());
        holder.descriptionTV.setText(ap.getDoctorDegree()+", "+ap.getDoctorSpeciality());
        holder.timeTV.setText("Time: "+ap.getDoctorTime());
        holder.dateTV.setText(ap.getAppointmentDate());
        holder.addressTV.setText("Address: "+ap.getChamberArea()+", "+ap.getChamberCity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PatientDoctorAppointmentAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView doctorImage;
        TextView nameTV, descriptionTV, timeTV, addressTV, dateTV;

        public PatientDoctorAppointmentAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.appointmentDoctorIV);
            nameTV = itemView.findViewById(R.id.appointmentDoctorNameTV);
            descriptionTV = itemView.findViewById(R.id.appointmentDoctorDescriptionTV);
            timeTV = itemView.findViewById(R.id.appointmentDateTV);
            addressTV = itemView.findViewById(R.id.appointmentAddressTV);
            dateTV = itemView.findViewById(R.id.doctor_appointment_date);
        }
    }
}
