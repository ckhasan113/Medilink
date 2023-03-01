package shafi.sbf.com.doctorsappointment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.PackageDetails;

public class PackageRecylaerViewAdapter extends RecyclerView.Adapter<PackageRecylaerViewAdapter.MyViewHolder> {


    private Context context;
    private List<PackageDetails> packageDetails;

    private PackageDetailsListener listener;

    public PackageRecylaerViewAdapter(Context context, List<PackageDetails> packageDetails) {
        this.context = context;
        this.packageDetails = packageDetails;
        listener = (PackageDetailsListener) context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView PackageName, Price, StartTime,EndTime;

        LinearLayout getDetailsLayout,TimeShow;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.getPacDetails);

            PackageName = (TextView) view.findViewById(R.id.packageName);
            Price = (TextView) view.findViewById(R.id.price);
            StartTime = (TextView) view.findViewById(R.id.start);
            EndTime = (TextView) view.findViewById(R.id.end);
            TimeShow = (LinearLayout) view.findViewById(R.id.time);


        }
    }


    @NonNull
    @Override
    public PackageRecylaerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pac_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageRecylaerViewAdapter.MyViewHolder holder, int position) {

        final PackageDetails details = packageDetails.get(position);


        holder.PackageName.setText(details.getPackageName());
        holder.Price.setText(details.getPackagePrice());
        holder.StartTime.setText(details.getStart());
        holder.EndTime.setText(details.getEnd());

        if (details.getStart().equals("null")){
            holder.TimeShow.setVisibility(View.INVISIBLE);
        }else {
            holder.TimeShow.setVisibility(View.VISIBLE);
        }

        holder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPackageDetails(details);
            }
        });

    }

    @Override
    public int getItemCount() {
        return packageDetails.size();
    }

    public interface PackageDetailsListener {
        void onPackageDetails(PackageDetails packageDetails);
    }
}
