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
import shafi.sbf.com.doctorsappointment.pojo.HospitalBooking;

public class PatientHospitalBookingAdapter extends RecyclerView.Adapter<PatientHospitalBookingAdapter.PatientHospitalBookingAdapterViewHolder> {

    private Context context;
    private List<HospitalBooking> list;

    public PatientHospitalBookingAdapter(Context context, List<HospitalBooking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PatientHospitalBookingAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PatientHospitalBookingAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.patient_hospital_booking_list_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientHospitalBookingAdapterViewHolder holder, int i) {

        final HospitalBooking hb = list.get(i);

        Picasso.get().load(Uri.parse(hb.getDoctorImage())).into(holder.doctorImage);
        holder.hNameTV.setText(hb.getHospitalName());
        holder.dNameTV.setText("Dr. "+hb.getDoctorName());
        holder.dDescriptionTV.setText(hb.getDoctorDegree()+", "+hb.getDoctorSpeciality());
        holder.timeTV.setText("Time: "+hb.getTime());
        holder.dateTV.setText(hb.getBookingDate());
        holder.addressTV.setText("Room: "+hb.getRoom()+", Floor: "+hb.getFloor()+", "+hb.getArea()+", "+hb.getCity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PatientHospitalBookingAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView doctorImage;
        TextView hNameTV, dNameTV, dDescriptionTV, timeTV, dateTV, addressTV;
        public PatientHospitalBookingAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.hospital_booking_DoctorIV);
            hNameTV = itemView.findViewById(R.id.hospital_booking_hospital_name_TV);
            dNameTV = itemView.findViewById(R.id.hospital_booking_DoctorNameTV);
            dDescriptionTV = itemView.findViewById(R.id.hospital_booking_DoctorDescriptionTV);
            timeTV = itemView.findViewById(R.id.hospital_booking_TimeTV);
            dateTV = itemView.findViewById(R.id.hospital_booking_date);
            addressTV = itemView.findViewById(R.id.hospital_booking_AddressTV);
        }
    }
}
