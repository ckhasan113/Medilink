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
import shafi.sbf.com.doctorsappointment.pojo.HealthReport;

public class HealthReportAdapter extends RecyclerView.Adapter<HealthReportAdapter.HealthReportAdapterViewHolder> {

    private Context context;
    private List<HealthReport> reportList;

    private HealthReportAdapterListener listener;

    public HealthReportAdapter(Context context, List<HealthReport> reportList) {
        this.context = context;
        this.reportList = reportList;
        listener = (HealthReportAdapterListener) context;
    }

    @NonNull
    @Override
    public HealthReportAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HealthReportAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.health_report_list_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HealthReportAdapterViewHolder holder, int i) {
        final HealthReport hr = reportList.get(i);

        Picasso.get().load(Uri.parse(hr.getImage())).into(holder.image);
        holder.file.setText(hr.getFileName());
        holder.doctor.setText(hr.getDoctorName());
        holder.date.setText(hr.getDate());

        holder.getDetailsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReportDetails(hr);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    class HealthReportAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView file, doctor, date;
        LinearLayout getDetailsCard;
        public HealthReportAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            getDetailsCard = itemView.findViewById(R.id.getHealthReportDetails);
            image = itemView.findViewById(R.id.reportImage);
            file = itemView.findViewById(R.id.reportName);
            doctor = itemView.findViewById(R.id.reportDoctor);
            date = itemView.findViewById(R.id.reportDate);
        }
    }

    public interface HealthReportAdapterListener{
        void onReportDetails(HealthReport hr);
    }
}
