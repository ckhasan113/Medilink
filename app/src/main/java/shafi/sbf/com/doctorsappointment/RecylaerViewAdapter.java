package shafi.sbf.com.doctorsappointment;

import android.content.ClipData;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecylaerViewAdapter extends RecyclerView.Adapter<RecylaerViewAdapter.MyViewHolder> {

    private List<ClipData.Item> items;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView DoctorName, DoctorTitel,DoctorSpecility;
        public ImageView DoctorImage;



        public MyViewHolder(View view) {
            super(view);
            DoctorName = (TextView) view.findViewById(R.id.DoctorName);
            DoctorTitel = (TextView) view.findViewById(R.id.DoctorTitel);
            DoctorSpecility = (TextView) view.findViewById(R.id.DoctorSpeciality);
            DoctorImage = (ImageView) view.findViewById(R.id.DoctorImage);

        }
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}