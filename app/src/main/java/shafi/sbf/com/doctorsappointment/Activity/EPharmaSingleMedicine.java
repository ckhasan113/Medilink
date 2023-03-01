package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
