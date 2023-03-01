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
import shafi.sbf.com.doctorsappointment.pojo.ConfirmOrder;

public class MyEPharmacyOrderListAdapter extends RecyclerView.Adapter<MyEPharmacyOrderListAdapter.MyEPharmacyOrderListAdapterViewHolder> {

    private Context context;
    private List<ConfirmOrder> orderList;
    private MyEPharmacyOrderListAdapterListener listener;

    public MyEPharmacyOrderListAdapter(Context context, List<ConfirmOrder> orderList) {
        this.context = context;
        this.orderList = orderList;
        listener = (MyEPharmacyOrderListAdapterListener) context;
    }

    @NonNull
    @Override
    public MyEPharmacyOrderListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyEPharmacyOrderListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.e_pharmacy_my_order_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyEPharmacyOrderListAdapterViewHolder holder, int position) {
        final ConfirmOrder co = orderList.get(position);

        Picasso.get().load(co.getePharmaDetails().getImage()).into(holder.image);
        holder.name.setText("Pharmacy Name\n"+co.getePharmaDetails().getFarmName());
        holder.status.setText("Status: "+co.getStatus());
        holder.getOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGetOrderDetails(co);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class MyEPharmacyOrderListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getOrderDetails;
        ImageView image;
        TextView name, status;
        public MyEPharmacyOrderListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getOrderDetails = itemView.findViewById(R.id.getMyPharmacyOrderDetails);
            image = itemView.findViewById(R.id.myOrderVendorImage);
            name = itemView.findViewById(R.id.myOrderVendorName);
            status = itemView.findViewById(R.id.myOrderStatus);
        }
    }

    public interface MyEPharmacyOrderListAdapterListener{
        void onGetOrderDetails(ConfirmOrder confirmOrder);
    }
}
