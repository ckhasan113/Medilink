package shafi.sbf.com.doctorsappointment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.OrderChart;

public class PharmacyOrderListAdapter extends RecyclerView.Adapter<PharmacyOrderListAdapter.PharmacyOrderListAdapterViewHolder> {

    private Context context;
    private List<OrderChart> chartList;

    public PharmacyOrderListAdapter(Context context, List<OrderChart> chartList) {
        this.context = context;
        this.chartList = chartList;
    }

    @NonNull
    @Override
    public PharmacyOrderListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PharmacyOrderListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.pharmacy_order_list_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyOrderListAdapterViewHolder holder, int position) {

        final OrderChart oc = chartList.get(position);

        holder.productNameTV.setText("Product: "+oc.getProductDetails().getName());
        holder.productCompanyTV.setText("Company: "+oc.getProductDetails().getCompany());
        holder.productQuantityTV.setText("Quantity: "+oc.getQuentity());
        holder.productPriceTV.setText("Price: "+oc.getPrice());

    }

    @Override
    public int getItemCount() {
        return chartList.size();
    }

    class PharmacyOrderListAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView productNameTV, productCompanyTV, productQuantityTV, productPriceTV;
        public PharmacyOrderListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTV = itemView.findViewById(R.id.product_name_tv);
            productCompanyTV = itemView.findViewById(R.id.product_company_name_tv);
            productQuantityTV = itemView.findViewById(R.id.product_quantity_tv);
            productPriceTV = itemView.findViewById(R.id.product_price_tv);
        }
    }
}
