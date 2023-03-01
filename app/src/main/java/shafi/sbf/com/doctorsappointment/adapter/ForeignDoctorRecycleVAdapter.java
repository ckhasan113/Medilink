package shafi.sbf.com.doctorsappointment.adapter;

import android.content.Context;
import android.net.Uri;
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
import shafi.sbf.com.doctorsappointment.pojo.ForeignHospitalDetails;

public class ForeignDoctorRecycleVAdapter extends RecyclerView.Adapter<ForeignDoctorRecycleVAdapter.MyViewHolder> {


    private Context context;
    private List<ForeignHospitalDetails> foreignHospitalDetails;


    private FHDetailsListener listener;

    public ForeignDoctorRecycleVAdapter(Context context, List<ForeignHospitalDetails> foreigndoctorDetails) {
        this.context = context;
        this.foreignHospitalDetails = foreigndoctorDetails;
        listener = (FHDetailsListener) context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView FhName, FhCity,FhArea;
        ImageView FhImage;

        LinearLayout getDetailsLayout;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.GetFHLlayout);

            FhName = (TextView) view.findViewById(R.id.fhname);
            FhCity = (TextView) view.findViewById(R.id.fhcity);
            FhArea = (TextView) view.findViewById(R.id.fharea);
            FhImage = (ImageView) view.findViewById(R.id.fhImage);

        }
    }


    @NonNull
    @Override
    public ForeignDoctorRecycleVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.foreign_hospital_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForeignDoctorRecycleVAdapter.MyViewHolder myViewHolder, int i) {

        final ForeignHospitalDetails hospitalDetails = foreignHospitalDetails.get(i);

        Uri photoUri = Uri.parse(hospitalDetails.getImage());
        myViewHolder.FhName.setText(hospitalDetails.getName());
        myViewHolder.FhCity.setText(hospitalDetails.getCity());
        myViewHolder.FhArea.setText(hospitalDetails.getArea());
        Picasso.get().load(photoUri).into(myViewHolder.FhImage);


        myViewHolder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFHDetails(hospitalDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foreignHospitalDetails.size();
    }

    public interface FHDetailsListener{
        void onFHDetails(ForeignHospitalDetails FhodpitalDetails);
    }
}
