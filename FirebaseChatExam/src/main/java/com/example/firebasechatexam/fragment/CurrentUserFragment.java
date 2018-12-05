package com.example.ggj04.sejongtalk.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ggj04.sejongtalk.R;
import com.example.ggj04.sejongtalk.model.CurrentUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.crashlytics.android.core.CrashlyticsCore.TAG;

public class CurrentUserFragment extends Fragment {

    private String mUserEmail;

    private DatabaseReference mFirebaseDatabaseReference;

    @Nullable
    @Override
    // fragment View 생성
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_account, container, false);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        mUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Firebase의 데이터베이스를 참조해서 값 읽어오기
        mFirebaseDatabaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CurrentUserModel currentUserModel = dataSnapshot.getValue(CurrentUserModel.class);
                try {
                    ImageView imageView = (ImageView) view.findViewById(R.id.account_imageView);
                    TextView userName = (TextView) view.findViewById(R.id.userNameinDatabase);
                    TextView userEmail = (TextView) view.findViewById(R.id.userEmailinDatabase);

                    userEmail.setText(mUserEmail);
                    userName.setText(currentUserModel.userName);

                    // 이미지 불러와서 이미지뷰에 띄우기
                    Glide
                            .with(getActivity())
                            .load(currentUserModel.profileImageUrl)
                            .apply(new RequestOptions().circleCrop())
                            .into(imageView);

                }catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
        return view;
    }
}
