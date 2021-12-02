package kkm.mjc.kistagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity4 extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDB;
    private FirebaseStorage fStorage;

    private ImageView back_img, profile_img;
    private EditText name_edt, nickname_edt;
    private Button complete_btn;
    private TextInputLayout name_text_layout, nickname_text_layout;

    private static final int REQUEST_CODE = 0;
    private StorageReference storageRef, riversRef;
    private UploadTask uploadTask;
    private Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);

        inflating();

        back_img.setOnClickListener(new View.OnClickListener() {    // 뒤로가기 이미지를 눌렀을 때
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);

            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //String cutEmail = RegisterActivity.account.getEmail().substring(0, RegisterActivity.account.getEmail().indexOf("."));

                storageRef = fStorage.getReference();
                riversRef = storageRef.child("UserProfile/" + RegisterActivity.account.getEmail() + "/" +"profile.png");

                if (file == null) {

                }
                uploadTask = riversRef.putFile(file);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(RegisterActivity4.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(RegisterActivity4.this, "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                fDB.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        int i = 0;
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (i == snapshot.getChildrenCount() - 1) {

                                                Map<String, Object> updateMap = new HashMap<>();
                                                updateMap.put("profile", uri.toString());
                                                String key = dataSnapshot.getKey();
                                                fDB.child("UserAccount").child(key).updateChildren(updateMap);
                                                i++;
                                                break;

                                            } else {
                                                i++;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                    }
                });


                complete();


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
        fStorage = FirebaseStorage.getInstance();

        back_img = findViewById(R.id.back_img);
        profile_img = findViewById(R.id.profile_img);
        name_edt = findViewById(R.id.name_edt);
        nickname_edt = findViewById(R.id.nickname_edt);
        complete_btn = findViewById(R.id.complete_btn);
        name_text_layout = findViewById(R.id.name_text_layout);
        nickname_text_layout = findViewById(R.id.nickname_text_layout);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    file = data.getData();
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    profile_img.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void complete() {

        try {

            fAuth.createUserWithEmailAndPassword(RegisterActivity.account.getEmail(), RegisterActivity.account.getPassword()).addOnCompleteListener(RegisterActivity4.this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                FirebaseUser user = fAuth.getCurrentUser();
                                RegisterActivity.account.setIdToken(user.getUid());
                                RegisterActivity.account.setName(name_edt.getText().toString());
                                RegisterActivity.account.setNickname(nickname_edt.getText().toString());
                                RegisterActivity.account.setProfile(file.toString());
                                RegisterActivity.account.setPost(0);
                                RegisterActivity.account.setFollower(0);
                                RegisterActivity.account.setFollowing(0);


                                fDB.child("UserAccount").child(user.getUid()).setValue(RegisterActivity.account);
                                Toast.makeText(RegisterActivity4.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity4.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
                            }

                        }
                    });

        } catch (Exception e) {

            Toast.makeText(RegisterActivity4.this, "회원가입 과정에서 오류가 생겼습니다.", Toast.LENGTH_SHORT).show();

        }

    }
}