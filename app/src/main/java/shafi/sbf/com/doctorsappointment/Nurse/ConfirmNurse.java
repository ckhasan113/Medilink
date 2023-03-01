package shafi.sbf.com.doctorsappointment.Nurse;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import shafi.sbf.com.doctorsappointment.Activity.Doctor;
import shafi.sbf.com.doctorsappointment.Activity.MainActivity;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.appointment.DoctorChamberListActivity;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.NurseAddtoCart;
import shafi.sbf.com.doctorsappointment.pojo.PackageDetails;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;
import shafi.sbf.com.doctorsappointment.pojo.VendorDetails;

public class ConfirmNurse extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 848;
    private static final int PERMISSION_CODE = 8972;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference useRefsetValu;
    private DatabaseReference venRefsetValu;


    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    private Uri ImagUrl_main;

    EditText Doctorref;

    TextView Upload;

    ImageView addImage;

    CardView Confirm;

    private PackageDetails packageDetails;
    private VendorDetails vendorDetails;

    private PatientDetails patientDetails;

    private boolean isPermistionGranted = false;

    private String nurseTakenId;
    private String nurseTakenDate;
    private String nurseTakenPrectionImage;
    private String nurseTakenDoctorRef;
    private String vendorID;
    private String vendorName;
    private String vendorArea;
    private String vendorPhone;
    String packageId;
    String packageName;
    String packagePrice;
    private String patient_id;
    private String PatientName;
    private String phone;
    private String email;
    private String status;

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_nurse);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //Init FireBase Storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        rootRef = FirebaseDatabase.getInstance().getReference();

        patient_id = user.getUid();
        userRef = rootRef.child("Patient").child(patient_id);
        useRefsetValu=userRef.child("Nurse Taken");
        nurseTakenId = useRefsetValu.push().getKey();


        getPatientDetails();

        packageDetails = (PackageDetails) getIntent().getSerializableExtra("NurseDetails");
        vendorDetails = (VendorDetails) getIntent().getSerializableExtra("VendorDetails");
        packageId = packageDetails.getPackageId();
        packageName = packageDetails.getPackageName();
        packagePrice = packageDetails.getPackagePrice();

        vendorID = vendorDetails.getVendorID();
        vendorName = vendorDetails.getName();
        vendorArea = vendorDetails.getArea();
        vendorPhone = vendorDetails.getPhone();

        venRefsetValu=rootRef.child("Nurse Vendor").child(vendorID).child("Nurse Taken").child(nurseTakenId);

        dialog = new LoadingDialog(ConfirmNurse.this, "Loading...");




        Upload = findViewById(R.id.prescription);
        Doctorref = findViewById(R.id.doctor_reference);
        addImage = findViewById(R.id.addimagebtn);
        Confirm = findViewById(R.id.confirm);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermistion();
                if(isPermistionGranted){
                    openGallery();
                }else {
                    Toast.makeText(ConfirmNurse.this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        nurseTakenDate = df.format(c);


        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pacConfirm();
            }
        });

    }

    private void getPatientDetails() {

        userRef.child("Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                patientDetails = dataSnapshot.getValue(PatientDetails.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void pacConfirm() {
        nurseTakenDoctorRef = Doctorref.getText().toString();

        if (nurseTakenPrectionImage == null){
            Toast.makeText(ConfirmNurse.this, "Upload Your Prescription", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nurseTakenDoctorRef.isEmpty()){
            Doctorref.setError("Price is required");
            Doctorref.requestFocus();
            return;
        }
        else {
            confirm();
        }

    }

    private void confirm() {
        dialog.show();
        final NurseAddtoCart details = new NurseAddtoCart(nurseTakenId,nurseTakenDate,nurseTakenPrectionImage,nurseTakenDoctorRef,vendorDetails, packageDetails, patientDetails, "pending");
        useRefsetValu.child(nurseTakenId).child("Details")
                .setValue(details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            venRefsetValu.child("Details").setValue(details);

                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("sms", "New request arrive");
                            tokenMap.put("from", String.valueOf(user.getUid()));
                            tokenMap.put("title", "Client Request");
                            tokenMap.put("to", details.getVendorDetails().getVendorID());
                            rootRef.child("Notifications").child(details.getVendorDetails().getVendorID()).child(rootRef.push().getKey()).setValue(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ConfirmNurse.this, "Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ConfirmNurse.this, MainActivity.class);
                                    dialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } else {
                            dialog.dismiss();
                            Toast.makeText(ConfirmNurse.this, "Failed to registration", Toast.LENGTH_SHORT).show();
                        }
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
            ImagUrl_main = data.getData();
            if (ImagUrl_main != null) {
                final ProgressDialog mDialog = new ProgressDialog(this);
                mDialog.setMessage("Uploading...");
                mDialog.show();

                String imageName = UUID.randomUUID().toString();
                final StorageReference imageFolder = storageReference.child("image/" + imageName);
                imageFolder.putFile(ImagUrl_main)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mDialog.dismiss();

                                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Toast.makeText(ConfirmNurse.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                                        nurseTakenPrectionImage = uri.toString();
                                        Upload.setText("Uploaded");



                                    }
                                });


                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                mDialog.setMessage("Uploaded " + progress);
                            }
                        });
            }

            addImage.setImageURI(ImagUrl_main);
        }
    }

    private void checkPermistion() {
        if ((ActivityCompat
                .checkSelfPermission(ConfirmNurse.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) &&
                (ActivityCompat
                        .checkSelfPermission(ConfirmNurse.this
                                ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(ConfirmNurse.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },PERMISSION_CODE);

        }else {
            isPermistionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==PERMISSION_CODE){

            if((grantResults[0] ==PackageManager.PERMISSION_GRANTED
                    && grantResults[1] ==PackageManager.PERMISSION_GRANTED
            )){
                isPermistionGranted = true;
            }else {
                checkPermistion();
            }
        }
    }
}
