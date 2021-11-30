package kkm.mjc.kistagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDB;

    private ImageView back_img;
    private EditText email_edt;
    private Button continue_btn;
    private TextInputLayout email_text_layout;

    public static UserAccount account;

    private String email;
    private String emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inflating();    // 인플레이팅 함수호출

        back_img.setOnClickListener(new View.OnClickListener() {    // 뒤로가기 이미지를 눌렀을 때
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);     // 위에서 아래로 떨어지는 화면전환 애니메이션

            }
        });

        email_edt.addTextChangedListener(new TextWatcher() {    // 이메일 EditText 실시간 처리 리스너
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {    // 입력하기 전에 조치

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {   // 입력난에 변화가 있을 시 조치

            }

            @Override
            public void afterTextChanged(Editable s) {  // 입력이 끝났을 때 조치

                email = s.toString().trim(); //공백제거
                if (email.matches(emailValidation) && s.length() > 0) {     //이메일 형태가 정상일 경우

                    email_text_layout.setHint("올바른 이메일 주소입니다!");
                    btnAble();

                } else if(s.length() == 0) {

                    email_text_layout.setHint("이메일 주소를 입력해 주세요");
                    btnDisable();

                } else {

                    email_text_layout.setHint("이메일 형식이 올바르지 않습니다!");
                    btnDisable();

                }

            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);

                account = new UserAccount();
                account.setEmail(email);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);

    }

    private void inflating() {  // 인플레이팅 작업

        fAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance().getReference();

        back_img = findViewById(R.id.back_img);
        email_edt = findViewById(R.id.email_edt);
        continue_btn = findViewById(R.id.continue_btn);
        email_text_layout = findViewById(R.id.email_text_layout);

    }

    private void btnAble() {

        continue_btn.setEnabled(true);
        continue_btn.setTextColor(getResources().getColor(R.color.white));
        continue_btn.setBackgroundResource(R.drawable.btn_shape_enable);
        email_text_layout.setHintTextColor(getResources().getColorStateList(R.color.colorSignupNumber));

    }

    private void btnDisable() {

        continue_btn.setEnabled(false);
        continue_btn.setTextColor(getResources().getColor(R.color.colorDivision));
        continue_btn.setBackgroundResource(R.drawable.btn_shape);
        email_text_layout.setHintTextColor(getResources().getColorStateList(R.color.colorGoogleSignInPressed));

    }
}