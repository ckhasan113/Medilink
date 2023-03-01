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
import shafi.sbf.com.doctorsappointment.pojo.EPharmaDetails;

public class EVendorRecycleVAdapter extends RecyclerView.Adapter<EVendorRecycleVAdapter.MyViewHolder>  {

    private Context context;
    private List<EPharmaDetails> vendorDetailsList;


    private EVendorListener listener;

    public EVendorRecycleVAdapter(Context context, List<EPharmaDetails> vendorDetails) {
        this.context = context;
        this.vendorDetailsList = vendorDetails;
        listener = (EVendorListener) context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView VName,VArea;
        ImageView VImage;

        LinearLayout getDetailsLayout;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.eGetVlayout);

            VName = (TextView) view.findViewById(R.id.evname);
            VArea = (TextView) view.findViewById(R.id.evarea);
            VImage = (ImageView) view.findViewById(R.id.evImage);

        }
    }


    @NonNull
    @Override
    public EVendorRecycleVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.evendor_item, viewGroup, false);

        return new EVendorRecycleVAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EVendorRecycleVAdapter.MyViewHolder myViewHolder, int i) {

        final EPharmaDetails ven = vendorDetailsList.get(i);

        Uri photoUri = Uri.parse(ven.getImage());
        myViewHolder.VName.setText(ven.getFarmName());
        myViewHolder.VArea.setText(ven.getArea());
        Picasso.get().load(photoUri).into(myViewHolder.VImage);


        myViewHolder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVenDetails(ven);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorDetailsList.size();
    }

    public interface EVendorListener{
        void onVenDetails(EPharmaDetails vendorDetails);
    }


}