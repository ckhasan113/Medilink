package shafi.sbf.com.doctorsappointment.DoctorApp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import shafi.sbf.com.doctorsappointment.R;

public class DoctorProfile extends AppCompatActivity implements View.OnClickListener {

    LinearLayout AddSedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        AddSedule = findViewById(R.id.add_scduale);
        AddSedule.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(DoctorProfile.this,AddSedule.class));
    }
}
