package kkm.mjc.kistagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity3 extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDB;

    private ImageView back_img;
    private EditText phone_edt, auth_edt;
    private Button send_btn, auth_btn, complete_btn;
    private TextInputLayout phone_text_layout, auth_text_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        inflating();

        back_img.setOnClickListener(new View.OnClickListener() {    // 뒤로가기 이미지를 눌렀을 때
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);

            }
        });




        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    fAuth.createUserWithEmailAndPassword(RegisterActivity.account.getEmail(), RegisterActivity.account.getPassword()).addOnCompleteListener(RegisterActivity3.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        FirebaseUser user = fAuth.getCurrentUser();
                                        RegisterActivity.account.setIdToken(user.getUid());
                                        RegisterActivity.account.setPhone(phone_edt.getText().toString());
                                        RegisterActivity.account.setName("");
                                        RegisterActivity.account.setNickname("");
                                        RegisterActivity.account.setPost(0);
                                        ;
                                        RegisterActivity.account.setFollower(0);
                                        RegisterActivity.account.setFollowing(0);

                                        fDB.child("UserAccount").child(user.getUid()).setValue(RegisterActivity.account);
                                        Toast.makeText(RegisterActivity3.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity3.this, LoginActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
                                    }

                                }
                            });

                } catch (Exception e) {

                    Toast.makeText(RegisterActivity3.this, "회원가입 과정에서 오류가 생겼습니다.", Toast.LENGTH_SHORT).show();

                }


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
        complete_btn = findViewById(R.id.complete_btn);
        phone_text_layout = findViewById(R.id.phone_text_layout);
        auth_text_layout = findViewById(R.id.auth_text_layout);

    }

/*    private void btnAble(TextInputLayout til) {

        continue_btn.setEnabled(true);
        continue_btn.setTextColor(getResources().getColor(R.color.white));
        continue_btn.setBackgroundResource(R.drawable.btn_shape_enable);
        til.setHintTextColor(getResources().getColorStateList(R.color.colorSignupNumber));

    }

    private void btnDisable(TextInputLayout til) {

        continue_btn.setEnabled(false);
        continue_btn.setTextColor(getResources().getColor(R.color.colorDivision));
        continue_btn.setBackgroundResource(R.drawable.btn_shape);
        til.setHintTextColor(getResources().getColorStateList(R.color.colorGoogleSignInPressed));

    }*/
}