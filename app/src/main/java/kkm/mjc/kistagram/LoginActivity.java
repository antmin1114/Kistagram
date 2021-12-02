package kkm.mjc.kistagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDB;

    private Button login_btn, signup_btn;
    private EditText email_edt, pw_edt;

    private ImageView img;


    private long backKeyPressedTime = 0;    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private Toast toast;                    // 첫 번째 뒤로가기 버튼을 누를때 표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inflating();    // 인플레이팅 함수호출

        // 이메일 가입 버튼을 눌렀을 때
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);

            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    signup_btn.setEnabled(false);

                    // 로그인 요청
                    String strEmail = email_edt.getText().toString();
                    String strPwd = pw_edt.getText().toString();

                    fAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // 로그인 성공 !!
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this,  fAuth.getCurrentUser().getEmail() + "님 환영합니다!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(LoginActivity.this, "회원정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                            signup_btn.setEnabled(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(LoginActivity.this, "로그인 실패입니다!", Toast.LENGTH_SHORT).show();
                            signup_btn.setEnabled(true);

                        }
                    });

                } catch (Exception e) {

                    Toast.makeText(LoginActivity.this, "회원정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    signup_btn.setEnabled(true);

                }

            }
        });


        img =  findViewById(R.id.title_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity4.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();   // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }

    }

    private void inflating() {  // 인플레이팅 작업

        fAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance().getReference();

        signup_btn = findViewById(R.id.signup_btn);
        login_btn = findViewById(R.id.login_btn);
        email_edt = findViewById(R.id.email_edt);
        pw_edt = findViewById(R.id.pw_edt);

    }
}