package com.example.ggj04.sejongtalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ggj04.sejongtalk.fragment.PeopleFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new PeopleFragment()).commit();
    }
}
