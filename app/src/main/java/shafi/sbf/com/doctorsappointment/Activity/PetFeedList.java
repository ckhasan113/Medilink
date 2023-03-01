package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

import shafi.sbf.com.doctorsappointment.R;

public class PetFeedList extends AppCompatActivity implements View.OnClickListener {

    CardView food1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_feed_list);

        food1 = findViewById(R.id.feed1);

        food1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.feed1){
            startActivity(new Intent(PetFeedList.this,PetFeedDetails.class));
        }
    }
}
