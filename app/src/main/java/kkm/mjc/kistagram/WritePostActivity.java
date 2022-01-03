package kkm.mjc.kistagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WritePostActivity extends AppCompatActivity {

    private Button addPhoto_btn_upload;
    private ImageView addPhoto_img;

    private FirebaseAuth fAuth;
    private FirebaseStorage fStorage;
    private Uri photoUri;
    private DatabaseReference fDB;

    private final int PICK_IMAGE_FROM_ALBUM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        inflating();

        // 앨범을 열어준다.
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM);

        // 사진추가 업로드 이벤트
        addPhoto_btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                contentUpload();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_FROM_ALBUM) {

            if (resultCode == RESULT_OK) {  // 이미지를 선택했을 때

                photoUri = data.getData();
                addPhoto_img.setImageURI(photoUri);

            } else {    // 취소버튼을 눌렀을 때

                finish();

            }

        }
    }

    private void inflating() {  // 인플레이팅 작업

        fAuth = FirebaseAuth.getInstance();
        fStorage = FirebaseStorage.getInstance();
        fDB = FirebaseDatabase.getInstance().getReference();

        addPhoto_btn_upload = findViewById(R.id.addPhoto_btn_upload);
        addPhoto_img = findViewById(R.id.addPhoto_img);

    }

    private void contentUpload() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String imageFileName = "IMAGE_" + sdf.format(timestamp) + "_.png";

        StorageReference storageRef = fStorage.getReference().child("Post").child(fAuth.getCurrentUser().getEmail()).child(imageFileName);

        // 파일 업로드
        storageRef.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(WritePostActivity.this, getString(R.string.upload_success), Toast.LENGTH_LONG).show();

                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        fDB.child("UserAccount").child(fAuth.getCurrentUser().getUid()).child("postImage").push().setValue(uri.toString());

                        fDB.child("UserAccount").child(fAuth.getCurrentUser().getUid()).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int postCount = Integer.parseInt(snapshot.getValue().toString());

                                Map<String, Object> updateMap = new HashMap<>();
                                updateMap.put("post", postCount + 1);
                                String key = fAuth.getCurrentUser().getUid();
                                fDB.child("UserAccount").child(key).updateChildren(updateMap);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {



                            }
                        });

                    }
                });

            }
        });

    }
}