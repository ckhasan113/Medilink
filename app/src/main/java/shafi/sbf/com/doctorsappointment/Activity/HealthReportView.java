package shafi.sbf.com.doctorsappointment.Activity;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.HealthReport;

public class HealthReportView extends AppCompatActivity {

    private HealthReport hr;

    private ImageView reportImage;

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_report_view);

        hr = (HealthReport) getIntent().getSerializableExtra("HealthReport");
        dialog = new LoadingDialog(HealthReportView.this, "Loading...");
        dialog.show();

        reportImage = findViewById(R.id.reportDetailsImage);

        Picasso.get().load(Uri.parse(hr.getImage())).into(reportImage);
        /*reportImage.setImageURI(Uri.parse(hr.getImage()));*/
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HealthReportView.this, HealthReportActivity.class);
        startActivity(intent);
        finish();
    }
}
