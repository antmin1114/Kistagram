package kkm.mjc.kistagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity2 extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDB;

    private ImageView back_img;
    private EditText pw_edt, pwok_edt;
    private Button continue_btn;
    private TextInputLayout pw_text_layout, pwok_text_layout;
    private boolean pwSame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        inflating();


        back_img.setOnClickListener(new View.OnClickListener() {    // 뒤로가기 이미지를 눌렀을 때
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);

            }
        });

        pw_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() < 6) {

                    pw_text_layout.setHint("비밀번호는 6자 이상이어야 합니다!");
                    btnDisable(pw_text_layout);

                } else if (s.length() >= 6) {

                    pw_text_layout.setHint("올바른 비밀번호 입니다!");
                    pw_text_layout.setHintTextColor(getResources().getColorStateList(R.color.colorSignupNumber));
                    continue_btn.setEnabled(false);
                    continue_btn.setTextColor(getResources().getColor(R.color.colorDivision));
                    continue_btn.setBackgroundResource(R.drawable.btn_shape);

                } else {

                    pw_text_layout.setHint("비밀번호를 다시 확인해주세요!");
                    btnDisable(pw_text_layout);

                }

            }
        });

        pwok_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (pw_edt.getText().toString().equals(s.toString()) && pw_edt.length() >= 6) {

                    pwok_text_layout.setHint("비밀번호가 일치합니다!");
                    btnAble(pwok_text_layout);
                    pw_text_layout.setHint("비밀번호가 일치합니다!");
                    pw_text_layout.setHintTextColor(getResources().getColorStateList(R.color.colorSignupNumber));
                    pwSame = true;

                } else {

                    pwok_text_layout.setHint("비밀번호가 일치하지 않습니다!");
                    btnDisable(pwok_text_layout);
                    pwSame = false;

                }

            }
        });


        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);

                RegisterActivity.account.setPassword(pw_edt.getText().toString());

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
        pw_edt = findViewById(R.id.pw_edt);
        pwok_edt = findViewById(R.id.pwok_edt);
        continue_btn = findViewById(R.id.continue_btn);
        pw_text_layout = findViewById(R.id.pw_text_layout);
        pwok_text_layout = findViewById(R.id.pwok_text_layout);

    }

    private void btnAble(TextInputLayout til) {

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

    }
}