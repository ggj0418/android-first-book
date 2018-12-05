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

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView messageTextView;
        ImageView messageImageView;
        CircleImageView photoImageView;

        public MessageViewHolder (@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        final String departmentName = intent.getExtras().getString("departmentName");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mMessageEditText = findViewById(R.id.message_edit);

        mMessageRecyclerView = findViewById(R.id.message_recycler_view);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

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

        if(mFirebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mFirebaseDatabaseReference.child("users").child(uid).child("userName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if(dataSnapshot.getValue() != null) {
                            try {
                                mUsername = dataSnapshot.getValue().toString();
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

        Query query = mFirebaseDatabaseReference.child("messages").child(departmentName);
        FirebaseRecyclerOptions<ChatModel> options = new FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel.class)
                .build();

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

        // 키보드 올라올 때 RecyclerView의 위치를 마지막 포지션으로 이동
        mMessageRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMessageRecyclerView.smoothScrollToPosition(mFirebaseAdapter.getItemCount());
                        }
                    }, 100);
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