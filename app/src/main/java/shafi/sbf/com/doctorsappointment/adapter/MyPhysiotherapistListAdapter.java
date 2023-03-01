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
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistBooking;

public class MyPhysiotherapistListAdapter extends RecyclerView.Adapter<MyPhysiotherapistListAdapter.MyPhysiotherapistListAdapterViewHolder> {

    private Context context;
    private List<PhysiotherapistBooking> list;

    public MyPhysiotherapistListAdapter(Context context, List<PhysiotherapistBooking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyPhysiotherapistListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyPhysiotherapistListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.my_physiotherapist_booking_list_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyPhysiotherapistListAdapterViewHolder holder, int i) {

        final PhysiotherapistBooking pb = list.get(i);
        Picasso.get().load(Uri.parse(pb.getVendorDetails().getImage())).into(holder.image);

        holder.name.setText(pb.getVendorDetails().getName());
        holder.packageName.setText("Package: "+pb.getPackageDetails().getPackageName());
        holder.price.setText("Price: "+pb.getPackageDetails().getPackagePrice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyPhysiotherapistListAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name, packageName, price;
        public MyPhysiotherapistListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.physioImage);
            name = itemView.findViewById(R.id.physioVendorName);
            packageName = itemView.findViewById(R.id.physioPackageName);
            price = itemView.findViewById(R.id.physioPrice);
        }
    }
}
