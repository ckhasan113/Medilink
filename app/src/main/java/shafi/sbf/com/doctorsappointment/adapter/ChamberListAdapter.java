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
import shafi.sbf.com.doctorsappointment.pojo.Chamber;

public class ChamberListAdapter extends RecyclerView.Adapter<ChamberListAdapter.ChamberListAdapterViewHolder> {

    private Context context;
    private List<Chamber> chamberList;

    private onBookingService bookingService;

    public ChamberListAdapter(Context context, List<Chamber> chamberList) {
        this.context = context;
        this.chamberList = chamberList;
        bookingService = (onBookingService) context;
    }

    @NonNull
    @Override
    public ChamberListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChamberListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.chamber_list_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChamberListAdapterViewHolder holder, int i) {
        final Chamber chamber = chamberList.get(i);

        holder.addressTV.setText(chamber.getArea()+"\n"+chamber.getCity()+", House No. "+chamber.getHouse());
        holder.timeTV.setText(chamber.getStart()+" - "+chamber.getEnd());

        final String time = chamber.getStart()+" - "+chamber.getEnd();

        holder.booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingService.onBooked(chamber.getAdd_ch_id(), chamber.getArea(), chamber.getCity(), time);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chamberList.size();
    }

    class ChamberListAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView addressTV, timeTV;
        LinearLayout booking;

        public ChamberListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTV = itemView.findViewById(R.id.chamber_addressTV);
            timeTV = itemView.findViewById(R.id.chamber_timeTV);
            booking = itemView.findViewById(R.id.chamber_booking);
        }
    }

    public interface onBookingService{
        void onBooked(String chamberKey, String chamberArea, String chamberCity, String time);
    }
}
