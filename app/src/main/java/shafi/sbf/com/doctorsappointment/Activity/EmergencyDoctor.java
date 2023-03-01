package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

import shafi.sbf.com.doctorsappointment.R;

public class EmergencyDoctor extends AppCompatActivity implements View.OnClickListener {

    CardView chat,talk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_doctor);

        chat = findViewById(R.id.chat_doctor);
        talk = findViewById(R.id.talk_doctor);

        chat.setOnClickListener(this);
        talk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.chat_doctor){
            Intent intent = new Intent(EmergencyDoctor.this,EmergencyDoctorChat.class);
            startActivity(intent);
        }else if (v.getId() == R.id.talk_doctor){
            Intent intent = new Intent(EmergencyDoctor.this,EmergencyDoctorTalk.class);
            startActivity(intent);
        }
    }
}
