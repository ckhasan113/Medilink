package shafi.sbf.com.doctorsappointment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.ForeignBooking;

public class PatientRecylaerViewAdapter extends RecyclerView.Adapter<PatientRecylaerViewAdapter.MyViewHolder>{

    private Context context;
    private List<ForeignBooking> foreignBookings;

    public PatientRecylaerViewAdapter(Context context, List<ForeignBooking> foreignBookings) {
        this.context = context;
        this.foreignBookings = foreignBookings;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView PatientName,date,REfDoc,Addre;
        public ImageView patientImage;

        LinearLayout getDetailsLayout;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.ReqDetails);

            PatientName = (TextView) view.findViewById(R.id.PatientName);
            REfDoc = (TextView) view.findViewById(R.id.refDoc);
            date = (TextView) view.findViewById(R.id.DateTV);
            Addre = (TextView) view.findViewById(R.id.address);
            patientImage = (ImageView) view.findViewById(R.id.PatientImage);

        }
    }

    @NonNull
    @Override
    public PatientRecylaerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.req_item, parent, false);

        return new PatientRecylaerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientRecylaerViewAdapter.MyViewHolder holder, int position) {

        final ForeignBooking foreignBooking = foreignBookings.get(position);


        holder.PatientName.setText(foreignBooking.getPatientDetails().getFirstName()+" "+foreignBooking.getPatientDetails().getLastName());
        holder.REfDoc.setText(foreignBooking.getForeignHospitalDetails().getName());
        holder.date.setText(foreignBooking.getBookingDate());
        holder.Addre.setText("block-"+foreignBooking.getForeignDoctorDetails().getBlock()+",f-"+foreignBooking.getForeignDoctorDetails().getBlock()+",r-"+foreignBooking.getForeignDoctorDetails().getRoom());
        Picasso.get().load(foreignBooking.getForeignDoctorDetails().getImageURI()).into(holder.patientImage);



    }

    @Override
    public int getItemCount() {
        return foreignBookings.size();
    }


}
