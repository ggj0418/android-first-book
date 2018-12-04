import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ggj04.sejongtalk.LoginActivity;
import com.example.ggj04.sejongtalk.R;
import com.example.ggj04.sejongtalk.model.ChatModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // 메시지 틀을 가지고 있는 ChatModel과 MessageViewHolder를 가지고 Firebase과의 Adapter 생성
    private FirebaseRecyclerAdapter<ChatModel, MessageViewHolder> mFirebaseAdapter;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private EditText mMessageEditText;
    private String mUsername;
    private String mPhotoUrl;

    private RecyclerView mMessageRecyclerView;

    @NonNull
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // PeopleFragment에서 보낸 departmetName 값을 final 변수로 저장
        Intent intent = getIntent();
        final String departmentName = intent.getExtras().getString("departmentName");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mMessageEditText = findViewById(R.id.message_edit);

        mMessageRecyclerView = findViewById(R.id.message_recycler_view);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // 전송버튼 클릭 시 메시지 틀에 사용자가 보낸 메시지, 사용자 이름, 사용자 프로필사진을 담아서 데이터베이스에 저장하는 이벤트 생성
        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel(mMessageEditText.getText().toString(),mUsername,mPhotoUrl);
                mFirebaseDatabaseReference
                        .child("messages")
                        .child(departmentName)
                        .push()
                        .setValue(chatModel);
                mMessageEditText.setText("");
            }
        });

        // 사용자가 아닐 시 LoginActivity로 넘어감
        if(mFirebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            // 사용자일 경우 데이터베이스에서 사용자의 프로필사진에 대한 정보를 가져옴
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mFirebaseDatabaseReference.child("users").child(uid).child("profileImageUrl").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if(dataSnapshot.getValue() != null) {
                            try {
                                mPhotoUrl = dataSnapshot.getValue().toString();
                            }catch (Exception e) {
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


        }

        // 실시간으로 데이터베이스에 저장된 메시지를 ChatModel이라는 틀에 담아서 MessageViewHolder로 띄워줌
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatModel, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(MessageViewHolder holder, int position, ChatModel model) {
                holder.messageTextView.setText(model.getText());
                holder.nameTextView.setText(model.getName());
                if (model.getPhotoUrl() == null) {
                    holder.photoImageView.setImageDrawable(ContextCompat.getDrawable(MessageActivity.this,
                            R.drawable.ic_sejong));
                } else {
                    Glide.with(MessageActivity.this)
                            .load(model.getPhotoUrl())
                            .into(holder.photoImageView);
                }
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message, parent, false);
                return new MessageViewHolder(view);
            }
        };

        // 새로운 메시지를 가장 밑에서부터 띄워주는 기능 수행
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                LinearLayoutManager layoutManager = (LinearLayoutManager) mMessageRecyclerView.getLayoutManager();
                try {
                    int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (friendlyMessageCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        mMessageRecyclerView.scrollToPosition(positionStart);
                    }
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();
    }
}
