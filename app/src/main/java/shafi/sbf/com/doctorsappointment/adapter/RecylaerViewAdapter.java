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
import shafi.sbf.com.doctorsappointment.pojo.ForeignDoctorDetails;

public class RecylaerViewAdapter extends RecyclerView.Adapter<RecylaerViewAdapter.MyViewHolder> {


    private Context context;
    private List<ForeignDoctorDetails> doctorDetails;

    private FDoctorDetailsListener listener;

    public RecylaerViewAdapter(Context context, List<ForeignDoctorDetails> doctorDetails) {
        this.context = context;
        this.doctorDetails = doctorDetails;
        listener = (FDoctorDetailsListener) context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView DoctorName, DEgree,Specielity;
        public ImageView DoctorImage;

        LinearLayout getDetailsLayout;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.getDoctorDetails);

            DoctorName = (TextView) view.findViewById(R.id.fDoctorName22);
            DEgree = (TextView) view.findViewById(R.id.fDoctorTitel22);
            Specielity = (TextView) view.findViewById(R.id.fDoctorSpeciality22);
            DoctorImage = (ImageView) view.findViewById(R.id.fDoctorImage);

        }
    }


    @NonNull
    @Override
    public RecylaerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylaerViewAdapter.MyViewHolder holder, int position) {

        final ForeignDoctorDetails doctor = doctorDetails.get(position);


        holder.DoctorName.setText(doctor.getName());
        holder.DEgree.setText(doctor.getDegree());
        holder.Specielity.setText(doctor.getSpeciality());
        Picasso.get().load(doctor.getImageURI()).into(holder.DoctorImage);

        holder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDoctorDetails(doctor);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorDetails.size();
    }

    public interface FDoctorDetailsListener{
        void onDoctorDetails(ForeignDoctorDetails doctorDetails);
    }
}
