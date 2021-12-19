package kkm.mjc.kistagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.bottom_home:
                        setFrag(0);
                        break;

                    case R.id.bottom_search:
                        setFrag(1);
                        break;

                    case R.id.bottom_addPhoto:
                        setFrag(2);
                        break;

                    case R.id.bottom_favorite_alarm:
                        setFrag(3);
                        break;

                    case R.id.bottom_account:
                        setFrag(4);
                        break;


                }

                return true;
            }
        });

        setFrag(0);

    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_content, new HomeFragment());
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.main_content, new SearchFragment());
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.main_content, new AddPhotoFragment());
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.main_content, new FavoriteAlarmFragment());
                ft.commit();
                break;

            case 4:
                ft.replace(R.id.main_content, new AccountFragment());
                ft.commit();
                break;
        }
    }
}