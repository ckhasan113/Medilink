package shafi.sbf.com.doctorsappointment.EPharma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import shafi.sbf.com.doctorsappointment.R;

public class EPharmaSingleMedicine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epharma_single_medicine);
    }

    public void paymentProcess(View view) {
        Intent intent = new Intent(EPharmaSingleMedicine.this,EpharmaPaymentMethod.class);
        startActivity(intent);
    }
}
