package com.example.ggj04.sejongtalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;

    private Button login;
    private Button signup;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();                     // 앱 실행 시마다 새로 로그아웃을 해서 항상 로그인 화면이 나오도록 설정

        id = (EditText) findViewById(R.id.loginActivity_edittext_id);
        password = (EditText) findViewById(R.id.loginActivity_edittext_password);

        login = (Button)findViewById(R.id.loginActivity_button_login);
        signup = (Button)findViewById(R.id.loginActivity_button_signin);

        login.setOnClickListener(new View.OnClickListener() {   // 로그인 버튼 클릭 시 loginEvent 함수를 실행시키는 이벤트 설정
            @Override
            public void onClick(View view) {
                loginEvent();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {  // 회원가입 버튼 클릭시 SignupActivity로 넘어가는 이벤트 설정
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // 현재 로그인 신청한 사용자가 데이터베이스에 등록되어있는 사용자라면 MainActivity로 넘어감
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    // 로그인 신청한 사용자의 정보가 없으면 firebaseAuth.signOut(); 으로 돌아가서 다시 로그인화면을 띄움
                }

            }
        };

    }

    void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(id.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            //로그인에 실패하면 실해했다고 메시지를 띄워줌
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);   
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
