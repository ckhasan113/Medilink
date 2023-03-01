package shafi.sbf.com.doctorsappointment.REgistration;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import shafi.sbf.com.doctorsappointment.Activity.MainActivity;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference patientRef;

    private CardView LogIn;
    private EditText editTextEmail;

    private EditText editTextPassword;

    private LoadingDialog loadingDialog;

    private static final String TAG = "token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email_log_in);
        editTextPassword = findViewById(R.id.password_log_in);

        loadingDialog = new LoadingDialog(this, "Loading");

        LogIn = findViewById(R.id.login_master);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                }

                /*if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Enter a valid email");
                    editTextEmail.requestFocus();
                    return;
                }*/

                if (password.isEmpty()) {
                    editTextPassword.setError("Password required");
                    editTextPassword.requestFocus();
                    return;
                }

                /*if (password.length() < 3) {
                    editTextPassword.setError("Password should be atleast 6 character long");
                    editTextPassword.requestFocus();
                    return;
                }*/

                loadingDialog.show();

                auth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loadingDialog.dismiss();
                                if (task.isSuccessful()) {
                                    user = auth.getCurrentUser();
                                    if (user!=null){
                                        rootRef = FirebaseDatabase.getInstance().getReference();

                                        userRef = rootRef.child("Patient").child(user.getUid());

                                        patientRef = userRef.child("Details");

                                        patientRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                PatientDetails p = dataSnapshot.getValue(PatientDetails.class);
                                                try {
                                                    if (p.getUserType().equals("2")){
                                                        Intent intent = new Intent(LogIn.this, MainActivity.class);
                                                        String token = FirebaseInstanceId.getInstance().getToken();
                                                        Map<String, Object> tokenMap = new HashMap<>();
                                                        tokenMap.put("device_token", token);

                                                        rootRef.child("Users").child(user.getUid()).child("Token").setValue(tokenMap);
                                                        loadingDialog.dismiss();
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }catch (NullPointerException e){
                                                    loadingDialog.dismiss();
                                                    auth.signOut();
                                                    Toast.makeText(LogIn.this, "User not valid", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }else {
                                        //goToEditProfile();
                                    }

                                } else {
                                    loadingDialog.dismiss();
                                    Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    public void goregister(View view) {
        Intent intent = new Intent(LogIn.this, Register.class);
        startActivity(intent);
        finish();
    }
}
