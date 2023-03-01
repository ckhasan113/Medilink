package shafi.sbf.com.doctorsappointment.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import shafi.sbf.com.doctorsappointment.R;

public class BloodBank extends AppCompatActivity implements View.OnClickListener {

    Button ap,bp,abp,op,am,bm,abm,om;

    Dialog mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        mydialog = new Dialog(this);

        ap=findViewById(R.id.buttonAp);
        bp=findViewById(R.id.buttonBp);
        abp=findViewById(R.id.buttonABp);
        op=findViewById(R.id.buttonOp);
        am=findViewById(R.id.buttonAm);
        bm=findViewById(R.id.buttonBm);
        abm=findViewById(R.id.buttonABm);
        om=findViewById(R.id.buttonOm);

        ap.setOnClickListener(this);
        bp.setOnClickListener(this);
        abp.setOnClickListener(this);
        op.setOnClickListener(this);
        am.setOnClickListener(this);
        bm.setOnClickListener(this);
        abm.setOnClickListener(this);
        om.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAp){
            mydialog.setContentView(R.layout.custom_blood_donor);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBlood);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_blood,(ViewGroup)
                            findViewById(R.id.custom_toast_layout_coming_soon));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.buttonAm){
            mydialog.setContentView(R.layout.custom_blood_donor);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();


                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBlood);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_blood,(ViewGroup)
                            findViewById(R.id.custom_toast_layout_coming_soon));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.buttonBp){
            mydialog.setContentView(R.layout.custom_blood_donor);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBlood);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_blood,(ViewGroup)
                            findViewById(R.id.custom_toast_layout_coming_soon));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.buttonBm){
            mydialog.setContentView(R.layout.custom_blood_donor);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBlood);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_blood,(ViewGroup)
                            findViewById(R.id.custom_toast_layout_coming_soon));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.buttonABp){
            mydialog.setContentView(R.layout.custom_blood_donor);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBlood);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_blood,(ViewGroup)
                            findViewById(R.id.custom_toast_layout_coming_soon));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.buttonABm){
            mydialog.setContentView(R.layout.custom_blood_donor);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();


                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBlood);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_blood,(ViewGroup)
                            findViewById(R.id.custom_toast_layout_coming_soon));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.buttonOp){
            mydialog.setContentView(R.layout.custom_blood_donor);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBlood);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_blood,(ViewGroup)
                            findViewById(R.id.custom_toast_layout_coming_soon));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }else if (v.getId() == R.id.buttonOm){
            mydialog.setContentView(R.layout.custom_blood_donor);
            TextView txtclose =(TextView) mydialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            Button confirm = mydialog.findViewById(R.id.confirmBlood);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast_blood,(ViewGroup)
                            findViewById(R.id.custom_toast_layout_coming_soon));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                }
            });

            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mydialog.show();

        }
    }
}
