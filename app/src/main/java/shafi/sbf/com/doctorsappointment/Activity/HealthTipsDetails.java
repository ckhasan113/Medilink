package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.pojo.HealthTips;

public class HealthTipsDetails extends AppCompatActivity {

    private ImageView image;

    private TextView titleTV, detailsTV, noImageTV;

    private HealthTips ht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips_details);

        ht = (HealthTips) getIntent().getSerializableExtra("HealthTipDetails");

        image = findViewById(R.id.health_tips_details_image);
        titleTV = findViewById(R.id.health_tips_details_title);
        detailsTV = findViewById(R.id.health_tips_details_tip);
        noImageTV = findViewById(R.id.health_tips_image_not_available);

        if (ht.getImage().equals("null")){
            image.setVisibility(View.GONE);
            noImageTV.setVisibility(View.VISIBLE);
        }else {
            Picasso.get().load(Uri.parse(ht.getImage())).into(image);
        }

        titleTV.setText(ht.getTitle());
        detailsTV.setText(ht.getTips());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HealthTipsDetails.this, HealthTipsHome.class);
        startActivity(intent);
        finish();
    }
}
