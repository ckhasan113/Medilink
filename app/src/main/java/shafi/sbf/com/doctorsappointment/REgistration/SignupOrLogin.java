package shafi.sbf.com.doctorsappointment.REgistration;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import shafi.sbf.com.doctorsappointment.Activity.MainActivity;
import shafi.sbf.com.doctorsappointment.R;

public class SignupOrLogin extends AppCompatActivity {

    private FirebaseUser user;
//    CardView LogIn,Register;
    public static int S_TIME_OUT =4000;
    TextView animateTextView;
    LinearLayout LogIn,Register;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_or_login);

        user = FirebaseAuth.getInstance().getCurrentUser();
//        LogIn = findViewById(R.id.login);
//        Register = findViewById(R.id.register);
//
//        Register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SignupOrLogin.this, Register.class));
//            }
//        });
//        LogIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SignupOrLogin.this, LogIn.class));
//            }
//        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (user == null) {
                    Intent intent = new Intent(SignupOrLogin.this, LogIn.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(SignupOrLogin.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },S_TIME_OUT);

        animateTextView = findViewById(R.id.welcome_text);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(2000f,0f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and then decrease
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                animateTextView.setTranslationY(progress);
                // no need to use invalidate() as it is already present in             //the text view.
            }
        });
        valueAnimator.start();
    }
}
