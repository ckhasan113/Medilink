package shafi.sbf.com.doctorsappointment.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.HealthReport;

public class HealthReportUpload extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 848;
    private static final int CAMERA_REQUEST_CODE = 849;
    private static final int PERMISSION_CODE = 8972;

    private boolean isPermissionGranted = false;

    private StorageReference storageReference;

    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;

    private LinearLayout uploadImageLO;
    private ImageView reportImage;
    private EditText fileNameEdt, doctorNameEdt;
    private Button submitBtn;

    private LoadingDialog dialog;

    private Uri ImageUrl_main;

    private String id, image, fileName, doctorName, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_report_upload);

        dialog = new LoadingDialog(HealthReportUpload.this, "Loading...");

        user = FirebaseAuth.getInstance().getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patient").child(user.getUid());

        uploadImageLO = findViewById(R.id.upload_health_report_image);
        reportImage = findViewById(R.id.upload_health_report_image_view);
        fileNameEdt = findViewById(R.id.health_report_file_name);
        doctorNameEdt = findViewById(R.id.health_report_doctor_name);
        submitBtn = findViewById(R.id.health_report_submit);

        uploadImageLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        reportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageUrl_main == null){
                    Toast.makeText(HealthReportUpload.this, "Select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                fileName = fileNameEdt.getText().toString().trim();
                if (fileName.isEmpty()){
                    fileNameEdt.setError(getString(R.string.required_field));
                    fileNameEdt.requestFocus();
                    return;
                }

                doctorName = doctorNameEdt.getText().toString().trim();
                if (doctorName.isEmpty()){
                    doctorNameEdt.setError(getString(R.string.required_field));
                    doctorNameEdt.requestFocus();
                    return;
                }

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                date = df.format(c);

                id = userRef.push().getKey();

                dialog.show();

                addHealthReport();
            }
        });
    }

    private void addHealthReport() {
        storageReference = FirebaseStorage.getInstance().getReference();
        final Uri imageUri = ImageUrl_main;
        final StorageReference imageRef = storageReference.child("HealthReportImage").child(imageUri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(imageUri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();
                image = downloadUri.toString();
                HealthReport hr = new HealthReport(id, image, fileName, doctorName, date);
                userRef.child("HealthReport").child(id).child("Details").setValue(hr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(HealthReportUpload.this, "Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HealthReportUpload.this, HealthReportActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            dialog.dismiss();
                            Toast.makeText(HealthReportUpload.this, "Failed to registration", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void choosePhotoFromGallery() {
        checkPermission();
        if(isPermissionGranted){
            openGallery();
        }else {
            Toast.makeText(HealthReportUpload.this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uploadImageLO.setVisibility(View.GONE);
        reportImage.setVisibility(View.VISIBLE);
        ImageUrl_main = data.getData();
        if(resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE){
            reportImage.setImageURI(ImageUrl_main);
        }else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE){
            reportImage.setImageURI(ImageUrl_main);
        }
    }


    private void checkPermission() {
        if ((ActivityCompat
                .checkSelfPermission(HealthReportUpload.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) &&
                (ActivityCompat
                        .checkSelfPermission(HealthReportUpload.this
                                ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(HealthReportUpload.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },PERMISSION_CODE);

        }else {
            isPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==PERMISSION_CODE){

            if((grantResults[0] ==PackageManager.PERMISSION_GRANTED
                    && grantResults[1] ==PackageManager.PERMISSION_GRANTED
            )){
                isPermissionGranted = true;
            }else {
                checkPermission();
            }
        }
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(HealthReportUpload.this, HealthReportActivity.class));
        finish();
    }
}
