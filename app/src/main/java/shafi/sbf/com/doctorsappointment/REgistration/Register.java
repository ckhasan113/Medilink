package shafi.sbf.com.doctorsappointment.REgistration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import shafi.sbf.com.doctorsappointment.Activity.MainActivity;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;

public class Register extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 848;
    private static final int PERMISSION_CODE = 8972;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;

    private StorageReference storageReference;

    private CircleImageView addImage;

    private EditText firstNameEdt, lastNameEdt, areaEdt, cityEdt, phoneEdt, emailEdt, passwordEdt, rePassEdt;

    private String userID, image, firstName, lastName, area, city, phone, email, password, rePass;

    private LoadingDialog loadingDialog;

    private Uri ImageUrl_main;

    private boolean isPermissionGranted = false;

    private CardView DoctorRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingDialog = new LoadingDialog(this, "Loading");

        firebaseAuth = FirebaseAuth.getInstance();

        addImage = findViewById(R.id.addimagebtn);
        firstNameEdt = findViewById(R.id.first_name);
        lastNameEdt = findViewById(R.id.last_name);
        areaEdt = findViewById(R.id.area);
        cityEdt = findViewById(R.id.city);
        phoneEdt = findViewById(R.id.phone);
        emailEdt = findViewById(R.id.email);
        passwordEdt = findViewById(R.id.password);
        rePassEdt = findViewById(R.id.rePassword);

        DoctorRegister = findViewById(R.id.doctor_register);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                if(isPermissionGranted){
                    openGallery();
                }else {
                    Toast.makeText(Register.this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DoctorRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ImageUrl_main == null){
                    Toast.makeText(Register.this, "Select a profile picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                firstName = firstNameEdt.getText().toString().trim();
                if (firstName.isEmpty()){
                    firstNameEdt.setError(getString(R.string.required_field));
                    firstNameEdt.requestFocus();
                    return;
                }

                lastName = lastNameEdt.getText().toString().trim();
                if (lastName.isEmpty()){
                    lastNameEdt.setError(getString(R.string.required_field));
                    lastNameEdt.requestFocus();
                    return;
                }

                area = areaEdt.getText().toString();
                if (area.isEmpty()){
                    areaEdt.setError(getString(R.string.required_field));
                    areaEdt.requestFocus();
                    return;
                }

                city = cityEdt.getText().toString();
                if (city.isEmpty()){
                    cityEdt.setError(getString(R.string.required_field));
                    cityEdt.requestFocus();
                    return;
                }

                phone = phoneEdt.getText().toString();
                if (phone.isEmpty()){
                    phoneEdt.setError(getString(R.string.required_field));
                    phoneEdt.requestFocus();
                    return;
                }

                email = emailEdt.getText().toString();
                if (email.isEmpty()){
                    emailEdt.setError(getString(R.string.required_field));
                    emailEdt.requestFocus();
                    return;
                }

                password = passwordEdt.getText().toString();
                if (password.isEmpty()){
                    passwordEdt.setError(getString(R.string.required_field));
                    passwordEdt.requestFocus();
                    return;
                }

                rePass = rePassEdt.getText().toString();
                if (rePass.isEmpty()){
                    rePassEdt.setError(getString(R.string.required_field));
                    rePassEdt.requestFocus();
                    return;
                }

                if (!(rePass.equals(password))){
                    Toast.makeText(Register.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    rePassEdt.setError("Enter same password");
                    rePassEdt.requestFocus();
                    return;
                }

                loadingDialog.show();

                registerUser();

            }
        });
    }

    private void registerUser() {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                firebaseAuth = FirebaseAuth.getInstance();
                                rootRef = FirebaseDatabase.getInstance().getReference();
                                user = firebaseAuth.getCurrentUser();
                                userRef = rootRef.child("Patient").child(user.getUid());
                                userID = String.valueOf(user.getUid());
                                Toast.makeText(Register.this, "Successful", Toast.LENGTH_SHORT).show();

                                userRegistration();
                            }else {
                                loadingDialog.dismiss();
                                Toast.makeText(Register.this, "Failed to register...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void userRegistration() {
        storageReference = FirebaseStorage.getInstance().getReference();
        final Uri imageUri = ImageUrl_main;
        final StorageReference imageRef = storageReference.child("PatientImage").child(imageUri.getLastPathSegment());
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
                PatientDetails details = new PatientDetails(userID, image, firstName, lastName, area, city, phone, email, password, "2");
                userRef.child("Details").setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.dismiss();
                            Toast.makeText(Register.this, "Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            String token = FirebaseInstanceId.getInstance().getToken();
                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("device_token", token);

                            rootRef.child("Users").child(user.getUid()).child("Token").setValue(tokenMap);
                            loadingDialog.dismiss();
                            startActivity(intent);
                            finish();

                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(Register.this, "Failed to registration", Toast.LENGTH_SHORT).show();

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
            addImage.setImageURI(ImageUrl_main);
        }
    }

    private void checkPermission() {
        if ((ActivityCompat
                .checkSelfPermission(Register.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) &&
                (ActivityCompat
                        .checkSelfPermission(Register.this
                                ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(Register.this,
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
        Intent intent = new Intent(Register.this, LogIn.class);
        startActivity(intent);
        finish();
    }
}