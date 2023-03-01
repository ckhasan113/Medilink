package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import shafi.sbf.com.doctorsappointment.R;

public class Ambulance extends AppCompatActivity {

    EditText From,To;

    String Sfrom,Sto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        From = findViewById(R.id.from);
        To = findViewById(R.id.to);


    }

    public void select_ride(View view) {
        Sfrom = From.getText().toString();
        Sto = To.getText().toString();

        if (Sfrom.isEmpty()){
            From.setError("Field is required");
            From.requestFocus();
            return;
        }
        if (Sto.isEmpty()){
            To.setError("Field is required");
            To.requestFocus();
            return;
        }

        else {
            Intent intent = new Intent(Ambulance.this, AmbulanceSelect.class);
            intent.putExtra("from",Sfrom);
            intent.putExtra("to",Sto);
            startActivity(intent);
        }

    }
}
