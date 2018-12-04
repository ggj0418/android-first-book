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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_account, container, false);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        mUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        
        /*
        mFirebaseDatabaseReference.child("users").child(uid).child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if(dataSnapshot.getValue() != null) {
                        try{
                            mUsername = dataSnapshot.getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("onCancelled"," cancelled");
            }
        });

        mFirebaseDatabaseReference.child("users").child(uid).child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if(dataSnapshot.getValue() != null) {
                        try{
                            mUserImage = dataSnapshot.getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("onCancelled"," cancelled");
            }
        });
        */
        return view;
    }
}