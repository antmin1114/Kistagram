package kkm.mjc.kistagram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private View view;
    private Context rootContext;

    private TextView nickname_text, name_text, post_text, follower_text, following_text;
    private ImageView profile_img;
    private Button profileEdt_btn;

    private FirebaseAuth fAuth;
    private DatabaseReference fDB;

    private String userEmail;
    private Uri uriProfile;

    private GridView gridView = null;
    private FeedAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_account, container, false);
        rootContext = view.getContext();

        inflating();

        gridView = view.findViewById(R.id.gridView);
        adapter = new FeedAdapter();

        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/UserProfile%2Fantmin1114%40naver.com%2Fprofile.png?alt=media&token=20d5c503-04c7-420d-a88d-4b91157d68b7"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160715_.png?alt=media&token=d891603c-78f3-445a-8ca1-93def7077f43"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));
        adapter.addItem(new FeedItem("https://firebasestorage.googleapis.com/v0/b/kistagram-d320f.appspot.com/o/Post%2Fantmin1114%40naver.com%2FIMAGE_20220103_160653_.png?alt=media&token=8df0f5ca-2b6c-4134-98ea-d013aa3047fd"));

        gridView.setAdapter(adapter);

        userEmail = fAuth.getCurrentUser().getEmail();

        fDB.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.child("email").getValue().toString().equals(userEmail)) {

                        nickname_text.setText(dataSnapshot.child("nickname").getValue().toString());
                        name_text.setText(dataSnapshot.child("name").getValue().toString());
                        post_text.setText(dataSnapshot.child("post").getValue().toString());
                        follower_text.setText(dataSnapshot.child("follower").getValue().toString());
                        following_text.setText(dataSnapshot.child("following").getValue().toString());
                        uriProfile = Uri.parse(dataSnapshot.child("profile").getValue().toString());

                        Glide.with(rootContext).load(uriProfile).apply(new RequestOptions().circleCrop()).into(profile_img);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });

        /*fDB.child(fAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });*/



        profileEdt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(rootContext, ProfileEdtActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);

            }
        });

        return view;
    }

    private void inflating() {  // 인플레이팅 작업

        fAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance().getReference();

        nickname_text = view.findViewById(R.id.nickname_text);
        name_text = view.findViewById(R.id.name_text);
        post_text = view.findViewById(R.id.post_text);
        follower_text = view.findViewById(R.id.follower_text);
        following_text = view.findViewById(R.id.following_text);
        profile_img = view.findViewById(R.id.profile_img);
        profileEdt_btn = view.findViewById(R.id.profileEdt_btn);

    }
}
