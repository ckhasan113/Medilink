package shafi.sbf.com.doctorsappointment.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import shafi.sbf.com.doctorsappointment.R;

public class PetDoctor extends AppCompatActivity {

    Dialog mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_doctor);

        mydialog = new Dialog(this);
    }

    public void petDoctor(View view) {

        mydialog.setContentView(R.layout.custom_pet_doctor);
        TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

        Button confirm = mydialog.findViewById(R.id.confirmBooking);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();

            }
        });

        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();

    }
}
