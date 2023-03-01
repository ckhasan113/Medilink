package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import shafi.sbf.com.doctorsappointment.R;

public class AccessoricsSingle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessorics_single);
    }

    public void paymentProcess(View view) {

        startActivity(new Intent(AccessoricsSingle.this,EpharmaPaymentMethod.class));
    }
}
