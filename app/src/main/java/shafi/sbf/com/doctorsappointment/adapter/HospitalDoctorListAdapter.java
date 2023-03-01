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

import com.squareup.picasso.Picasso;

import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.HospitalDoctorDetails;

public class HospitalDoctorListAdapter extends RecyclerView.Adapter<HospitalDoctorListAdapter.HospitalDoctorListAdapterViewHolder> {

    private Context context;
    private List<HospitalDoctorDetails> detailsList;

    private HospitalDoctorListAdapterListener listener;

    public HospitalDoctorListAdapter(Context context, List<HospitalDoctorDetails> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        listener = (HospitalDoctorListAdapterListener) context;
    }

    @NonNull
    @Override
    public HospitalDoctorListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HospitalDoctorListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.doctor_appointment_recycler_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalDoctorListAdapterViewHolder holder, int i) {
        final HospitalDoctorDetails doctor = detailsList.get(i);
        Uri photoUri = Uri.parse(doctor.getImage());
        Picasso.get().load(photoUri).into(holder.proImage);

        holder.nameTV.setText("Dr. "+doctor.getName());
        holder.degreeTV.setText(doctor.getDegree());
        holder.specialityTV.setText(doctor.getSpeciality());
        holder.appointmentTV.setText(doctor.getStartTime()+" - "+doctor.getEndTime());

        holder.getAppointmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGetHospitalDetails(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    class HospitalDoctorListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getAppointmentLayout;
        ImageView proImage;
        TextView nameTV, degreeTV, specialityTV, appointmentTV;

        public HospitalDoctorListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getAppointmentLayout = itemView.findViewById(R.id.getAppointment);
            proImage = itemView.findViewById(R.id.doctor_image);
            nameTV = itemView.findViewById(R.id.doctor_name);
            degreeTV = itemView.findViewById(R.id.doctor_degree);
            specialityTV = itemView.findViewById(R.id.doctor_speciality);
            appointmentTV = itemView.findViewById(R.id.doctor_appointment_number);
        }
    }

    public interface HospitalDoctorListAdapterListener{
        void onGetHospitalDetails(HospitalDoctorDetails doctor);
    }
}
