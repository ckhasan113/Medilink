package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import shafi.sbf.com.doctorsappointment.R;

public class PetParchesList extends AppCompatActivity implements View.OnClickListener {

    LinearLayout Pet1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_parches_list);

        Pet1 = findViewById(R.id.pet1);

        Pet1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.pet1){
            startActivity(new Intent(PetParchesList.this,PetParchesDetails.class));
        }
    }
}
