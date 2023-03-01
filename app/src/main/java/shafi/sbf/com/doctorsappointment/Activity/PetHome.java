package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

import shafi.sbf.com.doctorsappointment.R;

public class PetHome extends AppCompatActivity implements View.OnClickListener {

    CardView petPAr,petDoctor,petFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_home);

        petPAr = findViewById(R.id.pet_perch);
        petDoctor = findViewById(R.id.pet_doctor);
        petFood = findViewById(R.id.pet_feed);


        petPAr.setOnClickListener(this);
        petDoctor.setOnClickListener(this);
        petFood.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.pet_perch){
            startActivity(new Intent(PetHome.this,PetParchesList.class));
        }else if(v.getId()==R.id.pet_doctor){
            startActivity(new Intent(PetHome.this,PetDoctor.class));
        }else if(v.getId()==R.id.pet_feed){
            startActivity(new Intent(PetHome.this,PetFeedList.class));
        }
    }
}
