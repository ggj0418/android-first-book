package com.example.ggj04.sejongtalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ggj04.sejongtalk.fragment.CurrentUserFragment;
import com.example.ggj04.sejongtalk.fragment.DepartmentFragment;
import com.example.ggj04.sejongtalk.fragment.PeopleFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Daum 지도를 쓰려고 했을 때 해시 키를 얻기 위해서 추가했다가 삭제함
        try{
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key:", "!!!!!!!"+key+"!!!!!!");
            }
        } catch (Exception e){
            Log.e("name not found", e.toString());
        }
        */

        Button button = (Button)findViewById(R.id.NaverMapButton);      // 지도버튼 클릭 시 MapActivity로 넘어가는 이벤트 생성
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainactivity_bottomnavigationview);

        PeopleFragment defaultPeopleFragment = new PeopleFragment();    // 디폴트 화면으로 각 학과의 단톡방 목록 화면 지정
        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,defaultPeopleFragment).commit();

        // BottomNavigation drawer의 아이템을 선택하면 해당 fragment가 실행되도록 
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_department:
                        PeopleFragment peopleFragment = new PeopleFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,peopleFragment).commit();
                        return true;
                    case R.id.action_school:
                        DepartmentFragment departmentFragment = new DepartmentFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,departmentFragment).commit();
                        return true;
                    case R.id.action_account:
                        CurrentUserFragment currentUserFragment = new CurrentUserFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,currentUserFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
    public void test(){
        //this is test;
    }
}
