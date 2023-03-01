package shafi.sbf.com.doctorsappointment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPagerAdepter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.doctor_white,R.drawable.hospital_white,R.drawable.blood};
    private String [] headline = {"Doctor's appointment","Hospital appointment","Blood Donor"};
    private String [] Subline = {"Just chose and Connect","Best service you can get","Just chose and Connect"};

    public ViewPagerAdepter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.castom_layout,null);
        ImageView imageView= view.findViewById(R.id.imageView);
        TextView mainText=view.findViewById(R.id.mainText);
        TextView subText=view.findViewById(R.id.subText);
        imageView.setImageResource(images[position]);
        mainText.setText(headline[position]);
        subText.setText(Subline[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
