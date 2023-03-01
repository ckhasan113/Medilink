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

import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.HospitalDetails;

public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.HospitalListAdapterViewHolder> {

    private Context context;
    private List<HospitalDetails> detailsList;
    private HospitalListAdapterListener listener;
    private int count = 0;

    private DatabaseReference doctorRef;

    public HospitalListAdapter(Context context, List<HospitalDetails> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        listener = (HospitalListAdapterListener) context;
    }

    @NonNull
    @Override
    public HospitalListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HospitalListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.hospital_list_recycler_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final HospitalListAdapterViewHolder holder, int i) {
        final HospitalDetails hd = detailsList.get(i);

        doctorRef = FirebaseDatabase.getInstance().getReference().child("Hospital").child(hd.getHospitalID()).child("DoctorList");
        Uri photoUri = Uri.parse(hd.getImage());
        Picasso.get().load(photoUri).into(holder.hospitalImageIV);

        holder.hospitalNameTV.setText(hd.getName());
        holder.hospitalAddressTV.setText(hd.getArea()+", "+hd.getCity());
        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    count++;
                }
                holder.hospitalDoctorTV.setText(String.valueOf(count)+" Doctor available");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.hospitalDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickBooking(hd.getHospitalID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    class HospitalListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout hospitalDetailsLayout;
        ImageView hospitalImageIV;
        TextView hospitalNameTV, hospitalAddressTV, hospitalDoctorTV;
        public HospitalListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            hospitalDetailsLayout = itemView.findViewById(R.id.hospitalDetails);
            hospitalImageIV = itemView.findViewById(R.id.hospitalImageIV);
            hospitalNameTV = itemView.findViewById(R.id.hospitalNameTV);
            hospitalAddressTV = itemView.findViewById(R.id.hospitalAddressTV);
            hospitalDoctorTV = itemView.findViewById(R.id.hospitalDoctorNumberTV);
        }
    }

    public interface HospitalListAdapterListener{
        void onClickBooking(String hospitalID);
    }
}
