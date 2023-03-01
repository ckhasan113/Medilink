package shafi.sbf.com.doctorsappointment.physiotherapist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistBooking;
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistPackageDetails;
import shafi.sbf.com.doctorsappointment.pojo.PhysiotherapistVendorDetails;

public class ConfirmPhysioActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 848;
    private static final int PERMISSION_CODE = 8972;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference vendorRef;

    private Uri ImageUrl_main;

    private ImageView addImageBtn;

    private boolean isPermissionGranted = false;

    private EditText doctorRefEdt;

    private TextView packageNameTV;

    private CardView confirmCard;

    private LoadingDialog dialog;

    private PhysiotherapistVendorDetails vendorDetails;
    private PhysiotherapistPackageDetails packageDetails;
    private PatientDetails patientDetails;

    private String bookingID, prescriptionImage, doctorName, bookingDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_physio);

        dialog = new LoadingDialog(ConfirmPhysioActivity.this, "Loading...");

        vendorDetails = (PhysiotherapistVendorDetails) getIntent().getSerializableExtra("Physiotherapist");
        packageDetails = (PhysiotherapistPackageDetails) getIntent().getSerializableExtra("PackageDetails");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patient").child(user.getUid());
        vendorRef = rootRef.child("Physiotherapist").child(vendorDetails.getVendorID()).child("ClientRequest");

        dialog.show();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PatientDetails pd = dataSnapshot.child("Details").getValue(PatientDetails.class);
                patientDetails = pd;
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addImageBtn = findViewById(R.id.physioAddPrescriptionImageBtn);
        doctorRefEdt = findViewById(R.id.physio_doctor_reference);
        packageNameTV = findViewById(R.id.physio_package_name);
        confirmCard = findViewById(R.id.confirmPhysiotherapist);

        packageNameTV.setText(packageDetails.getPackageName()+" Package");

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                if(isPermissionGranted){
                    openGallery();
                }else {
                    Toast.makeText(ConfirmPhysioActivity.this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ImageUrl_main == null){
                    Toast.makeText(ConfirmPhysioActivity.this, "Prescription image is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                doctorName = doctorRefEdt.getText().toString().trim();
                if (doctorName.isEmpty()){
                    doctorRefEdt.setError("DoctorName is required");
                    doctorRefEdt.requestFocus();
                    return;
                }
                bookingID = vendorRef.push().getKey();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                bookingDate = df.format(c);

                dialog.show();
                bookingAddToDatabase();
            }
        });
    }

    private void bookingAddToDatabase() {
        storageReference = FirebaseStorage.getInstance().getReference();
        final Uri imageUri = ImageUrl_main;
        final StorageReference imageRef = storageReference.child("PhysioPrescriptionImage").child(imageUri.getLastPathSegment());
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
                final String vendorID = vendorDetails.getVendorID();
                prescriptionImage = downloadUri.toString();
                final PhysiotherapistBooking details = new PhysiotherapistBooking(bookingID, vendorDetails, packageDetails, prescriptionImage, doctorName, bookingDate, patientDetails);
                vendorRef.child(bookingID).child("Details").setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userRef.child("PhysiotherapistBooking").child(bookingID).child("Details").setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){

                                       Map<String, Object> tokenMap = new HashMap<>();
                                       tokenMap.put("sms", "You have new request");
                                       tokenMap.put("from", String.valueOf(user.getUid()));
                                       tokenMap.put("title", "Client request");
                                       tokenMap.put("to", vendorID);

                                       rootRef.child("Notifications").child(vendorID).child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               dialog.dismiss();
                                               Toast.makeText(ConfirmPhysioActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                               Intent intent = new Intent(ConfirmPhysioActivity.this, GetPhysiotherapistActivity.class);
                                               intent.putExtra("Physiotherapist", vendorDetails);
                                               startActivity(intent);
                                               finish();
                                           }
                                       });

                                   }else {
                                       dialog.dismiss();
                                       Toast.makeText(ConfirmPhysioActivity.this, "Failed....", Toast.LENGTH_SHORT).show();
                                   }
                                }
                            });

                        } else {
                            dialog.dismiss();
                            Toast.makeText(ConfirmPhysioActivity.this, "Failed....", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE){
            ImageUrl_main = data.getData();
            addImageBtn.setImageURI(ImageUrl_main);
        }
    }

    private void checkPermission() {
        if ((ActivityCompat
                .checkSelfPermission(ConfirmPhysioActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) &&
                (ActivityCompat
                        .checkSelfPermission(ConfirmPhysioActivity.this
                                ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(ConfirmPhysioActivity.this,
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
        Intent intent = new Intent(ConfirmPhysioActivity.this, GetPhysiotherapistActivity.class);
        intent.putExtra("Physiotherapist", vendorDetails);
        startActivity(intent);
        finish();
    }
}
