package com.example.cookingapplication;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Cursor cursor = null;
    AccountHelper helper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new AccountHelper(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new FragmentHome());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                   // Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_SHORT).show();
                    fragment= new FragmentHome();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_week_top:
                  //  Toast.makeText(getApplicationContext(),"top week",Toast.LENGTH_SHORT).show();
                    fragment= new FragmentTop();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_search:
                  //  Toast.makeText(getApplicationContext(),"search",Toast.LENGTH_SHORT).show();
                    fragment=new FragmentSearch();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_menu:
//                    Account account = new Account("toanlk","deptrai");
//                    helper.addAccount(account);
                   Account account = helper.getAccountById(1);
                    int i = helper.getCount();
                    Toast.makeText(MainActivity.this, "du do "+Global.name+Global.url_image, Toast.LENGTH_SHORT).show();
                    if(account.getId()==0){
                       fragment = new FragmentMenu();
                       loadFragment(fragment);
                    }
                    else {
                        fragment = new FragmentUser();
                        loadFragment(fragment);
                    }
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
