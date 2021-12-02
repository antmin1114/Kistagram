package kkm.mjc.kistagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RegisterActivity3 extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDB;

    private ImageView back_img;
    private EditText phone_edt, auth_edt;
    private Button send_btn, auth_btn;
    private TextInputLayout phone_text_layout, auth_text_layout;

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        inflating();

        phone_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                fDB.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (s.toString().equals(dataSnapshot.child("phone").getValue().toString())) {

                                phone_text_layout.setHint("이미 가입된 휴대폰 번호입니다!");
                                btnDisable();
                                break;

                            } else {

                                phone_text_layout.setHint("휴대폰 번호를 입력하세요");
                                btnAble();

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                Toast.makeText(RegisterActivity3.this, "인증코드가 전송되었습니다. 3분 이내에 입력해주세요 :)", Toast.LENGTH_SHORT).show();
                //signInWithPhoneAuthCredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(RegisterActivity3.this, "인증 실패..", Toast.LENGTH_SHORT).show();
                Log.e("인증실패", e.toString());

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        super.onCodeSent(s, forceResendingToken);

                //Toast.makeText(RegisterActivity3.this, "인증코드가 전송되었습니다. 90초 이내에 입력해주세요 :)", Toast.LENGTH_SHORT).show();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {

                        auth_btn.setEnabled(true);
                        auth_btn.setBackgroundResource(R.drawable.btn_shape_enable);

                    }
                }, 2000);

                mVerificationId = s;
                mResendToken = forceResendingToken;

                Log.d("아이디토큰", mVerificationId + "\n" + mResendToken);
            }
        };


        back_img.setOnClickListener(new View.OnClickListener() {    // 뒤로가기 이미지를 눌렀을 때
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);

            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fDB.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (phone_edt.getText().toString().equals(dataSnapshot.child("phone").getValue().toString())) {

                                Toast.makeText(RegisterActivity3.this, "이미 가입된 휴대폰 번호입니다!", Toast.LENGTH_SHORT).show();
                                return;

                            }

                        }

                        auth_text_layout.setVisibility(View.VISIBLE);
                        auth_btn.setVisibility(View.VISIBLE);

                        startPhoneNumberVerification("+82" + phone_edt.getText().toString().substring(1));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (auth_edt.getText().toString().equals("")) {

                    Toast.makeText(RegisterActivity3.this, "인증번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;

                }

                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                        mVerificationId,
                        auth_edt.getText().toString()
                );
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);

    }

    private void inflating() {  // 인플레이팅 작업

        fAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance().getReference();

        back_img = findViewById(R.id.back_img);
        phone_edt = findViewById(R.id.phone_edt);
        auth_edt = findViewById(R.id.auth_edt);
        send_btn = findViewById(R.id.send_btn);
        auth_btn = findViewById(R.id.auth_btn);
        phone_text_layout = findViewById(R.id.phone_text_layout);
        auth_text_layout = findViewById(R.id.auth_text_layout);

    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(fAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(90L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("성공", "signInWithCredential:success");
                            Toast.makeText(RegisterActivity3.this, "인증 성공!", Toast.LENGTH_SHORT).show();

                            RegisterActivity.account.setPhone(phone_edt.getText().toString());

                            Intent intent = new Intent(RegisterActivity3.this, RegisterActivity4.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);

                            //FirebaseUser user = task.getResult().getUser();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("실패", "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterActivity3.this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void btnAble() {

        send_btn.setEnabled(true);
        send_btn.setTextColor(getResources().getColor(R.color.white));
        send_btn.setBackgroundResource(R.drawable.btn_shape_enable);
        phone_text_layout.setHintTextColor(getResources().getColorStateList(R.color.colorSignupNumber));

    }

    private void btnDisable() {

        send_btn.setEnabled(false);
        send_btn.setTextColor(getResources().getColor(R.color.colorDivision));
        send_btn.setBackgroundResource(R.drawable.btn_shape);
        phone_text_layout.setHintTextColor(getResources().getColorStateList(R.color.colorGoogleSignInPressed));

    }


}