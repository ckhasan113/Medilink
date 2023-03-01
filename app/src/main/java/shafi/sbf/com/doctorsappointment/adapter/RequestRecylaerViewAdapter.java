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
import shafi.sbf.com.doctorsappointment.pojo.NurseAddtoCart;

public class RequestRecylaerViewAdapter extends RecyclerView.Adapter<RequestRecylaerViewAdapter.MyViewHolder> {


    private Context context;
    private List<NurseAddtoCart> nurseAddtoCarts;

    private ReqDetailsListener listener;

    public RequestRecylaerViewAdapter(Context context, List<NurseAddtoCart> nurseAddtoCarts) {
        this.context = context;
        this.nurseAddtoCarts = nurseAddtoCarts;
        listener = (ReqDetailsListener) context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView VendorName, Package, Date,price;

        LinearLayout getDetailsLayout;


        public MyViewHolder(View view) {
            super(view);

            getDetailsLayout = itemView.findViewById(R.id.reqDetails);

            VendorName = (TextView) view.findViewById(R.id.Vendor_name);
            Package = (TextView) view.findViewById(R.id.packageNameShow);
            price = (TextView) view.findViewById(R.id.priceShow);
            Date = (TextView) view.findViewById(R.id.show_date);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_services_item, parent, false);

        return new RequestRecylaerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final NurseAddtoCart nc = nurseAddtoCarts.get(position);

        holder.VendorName.setText(nc.getPatientDetails().getFirstName()+" "+nc.getPatientDetails().getLastName());
        holder.Package.setText(nc.getPackageDetails().getPackageName());
        holder.Date.setText(nc.getNurseTakenDate());
        holder.price.setText(nc.getPackageDetails().getPackagePrice());

        holder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReqDetails(nc);
            }
        });


    }

    @Override
    public int getItemCount() {
        return nurseAddtoCarts.size();
    }

    public interface ReqDetailsListener {
        void onReqDetails(NurseAddtoCart requestDetails);
    }

}
