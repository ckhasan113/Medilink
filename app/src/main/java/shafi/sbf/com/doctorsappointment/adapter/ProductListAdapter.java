package shafi.sbf.com.doctorsappointment.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.PharmacyProductDetails;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListAdapterViewHolder> {

    private Context context;
    private List<PharmacyProductDetails> list;

    private ProductListAdapterListener listener;

    public ProductListAdapter(Context context, List<PharmacyProductDetails> list) {
        this.context = context;
        this.list = list;
        listener = (ProductListAdapterListener) context;
    }

    @NonNull
    @Override
    public ProductListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.product_list_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapterViewHolder holder, int position) {
        final PharmacyProductDetails product = list.get(position);

        Picasso.get().load(Uri.parse(product.getImage())).into(holder.image);
        holder.name.setText(product.getName());
        holder.company.setText(product.getCompany());
        holder.price.setText(product.getPrice());
        holder.expired.setText(product.getExpiredDate());

        holder.Curt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getProductDetails(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductListAdapterViewHolder extends RecyclerView.ViewHolder{

        CardView getProductDetails;
        ImageView image;
        TextView name, company, price, expired,Curt;

        public ProductListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            getProductDetails = itemView.findViewById(R.id.getProductDetails);
            image = itemView.findViewById(R.id.imageOFproduct);
            name = itemView.findViewById(R.id.name);
            company = itemView.findViewById(R.id.company);
            price = itemView.findViewById(R.id.price);
            expired = itemView.findViewById(R.id.expired_date);
            Curt = itemView.findViewById(R.id.medicin_curt);
        }
    }

    public interface ProductListAdapterListener{
        void getProductDetails(PharmacyProductDetails product);
    }
}
