package shafi.sbf.com.doctorsappointment.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistPackageDetails;

public class PhysiotherapistPackageListAdapter extends RecyclerView.Adapter<PhysiotherapistPackageListAdapter.PhysiotherapistPackageListAdapterViewHolder> {

    private Context context;
    private List<PhysiotherapistPackageDetails> packageDetails;

    private PhysiotherapistPackageListAdapterListener listener;

    public PhysiotherapistPackageListAdapter(Context context, List<PhysiotherapistPackageDetails> packageDetails) {
        this.context = context;
        this.packageDetails = packageDetails;
        listener = (PhysiotherapistPackageListAdapterListener) context;
    }

    @NonNull
    @Override
    public PhysiotherapistPackageListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhysiotherapistPackageListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.physio_package_list_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhysiotherapistPackageListAdapterViewHolder holder, int position) {

        final PhysiotherapistPackageDetails details = packageDetails.get(position);


        holder.packageName.setText(details.getPackageName());
        holder.price.setText(details.getPackagePrice());
        holder.startTime.setText(details.getStartTime());
        holder.endTime.setText(details.getEndTime());

        if (details.getStartTime().equals("Select")){
            holder.timeShow.setVisibility(View.INVISIBLE);
        }else {
            holder.timeShow.setVisibility(View.VISIBLE);
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

    class PhysiotherapistPackageListAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView packageName, price, startTime, endTime;

        private LinearLayout getDetailsLayout, timeShow;

        public PhysiotherapistPackageListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getDetailsLayout = itemView.findViewById(R.id.getPackageDetails);

            packageName = itemView.findViewById(R.id.packageName);
            price = itemView.findViewById(R.id.price);
            startTime = itemView.findViewById(R.id.start);
            endTime = itemView.findViewById(R.id.end);
            timeShow = itemView.findViewById(R.id.time);
        }
    }

    public interface PhysiotherapistPackageListAdapterListener {
        void onPackageDetails(PhysiotherapistPackageDetails packageDetails);
    }
}
