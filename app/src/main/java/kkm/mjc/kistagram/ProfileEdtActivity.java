package kkm.mjc.kistagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileEdtActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDB;

    private ImageView back_img, check_img, profile_img;
    private TextView profileChange_text;
    private EditText name_edt, userName_edt, website_edt, introduce_edt;

    private String strName, strUserName, strWebsite, strIntroduce, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edt);

        inflating();
        strEditText();

        userEmail = fAuth.getCurrentUser().getEmail();

        fDB.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("email").getValue().toString().equals(userEmail)) {

                        name_edt.setText(dataSnapshot.child("name").getValue().toString());
                        userName_edt.setText(dataSnapshot.child("nickname").getValue().toString());

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);

            }
        });

        check_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 저장

            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 프로필 사진 변경

            }
        });

        profileChange_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 프로필 사진 변경

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
        check_img = findViewById(R.id.check_img);
        profile_img = findViewById(R.id.profile_img);
        profileChange_text = findViewById(R.id.profileChange_text);
        name_edt = findViewById(R.id.name_edt);
        userName_edt = findViewById(R.id.userName_edt);
        website_edt = findViewById(R.id.website_edt);
        introduce_edt = findViewById(R.id.introduce_edt);

    }

    private void strEditText() {

        strName = name_edt.getText().toString();
        strUserName = userName_edt.getText().toString();
        strWebsite = website_edt.getText().toString();
        strIntroduce = introduce_edt.getText().toString();

    }
}