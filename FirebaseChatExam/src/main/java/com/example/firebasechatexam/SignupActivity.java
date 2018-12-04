package com.example.ggj04.sejongtalk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ggj04.sejongtalk.model.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private TextView textView;
    private ImageView profile;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        profile = (ImageView)findViewById(R.id.signupActivity_imageview_profile);
        // 사진 등록 아이콘을 클릭하면 기기의 앨범에서 사진을 고르는 이벤트 생성
        profile.setOnClickListener(new View.OnClickListener() {         
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });

        email = (EditText) findViewById(R.id.signupActivity_edittext_email);
        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
        signup = (Button) findViewById(R.id.signupActivity_button_signup);

        // 회원가입 버튼을 눌렀을 때 데이터베이스에 정보를 기입하는 이벤트 생성
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력창에 null 값이 있는지 확인
                if (email.getText().toString() == null || name.getText().toString() == null || password.getText().toString() == null || imageUri == null) {
                    return;
                }

                FirebaseAuth.getInstance()
                        // 이메일과 비밀번호 기반의 계정 생성
                        .createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())       
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            // 계정이 성공적으로 생성되었을 때 데이터베이스에 사용자 정보를 입력하고 프로필 사진을 저장소에 저장하는 이벤트 생성
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference().child("userImages").child(uid);

                                // 저장소에 프로필 사진을 저장
                                profileImageRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return profileImageRef.getDownloadUrl();
                                    }
                                // 데이터베이스에 사용자 정보(이름, 프로필사진 경로) 저장    
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if(task.isSuccessful()) {
                                            Uri downUri = task.getResult();
                                            String imageUri = downUri.toString();

                                            UserModel userModel = new UserModel();
                                            userModel.userName = name.getText().toString();
                                            userModel.profileImageUrl = imageUri;

                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                                    startActivity(intent);
                                                    SignupActivity.this.finish();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode ==RESULT_OK){
            profile.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData();// 이미지 경로 원본
        }
    }
}
