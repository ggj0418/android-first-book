import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ggj04.sejongtalk.R;
import com.example.ggj04.sejongtalk.chat.MessageActivity;
import com.example.ggj04.sejongtalk.model.UserModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;

public class PeopleFragment extends Fragment {

    @Nullable
    @Override
    // 데이터베이스에 있는 학과들을 리스트뷰로 보여주는 View 설정
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());

        return view;
    }

    // 데이터베이스에서 불러온 학과정보를 View로 담아주는 Adapter 설정
    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<UserModel> userModels;

        // 리스트뷰의 요소인 item_friend를 읽어와서 View 생성
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);
            return new CustomViewHolder(view);
        }

        // ViewHolder에 학과이름과 사진을 설정
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            try {
                ((CustomViewHolder) holder).textView.setText(userModels.get(position).userName);
                Glide
                        .with(((CustomViewHolder) holder).itemView.getContext())
                        .load(userModels.get(position).profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(((CustomViewHolder) holder).imageView);

                // 리스트뷰에서 각각의 리스트를 클릭했을 때 MessageActivity로 넘어가는 이벤트 생성
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), MessageActivity.class);
                        intent.putExtra("departmentName", userModels.get(position).userName);                      
                        ActivityOptions activityOptions = null;
                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright,R.anim.toleft);
                        startActivity(intent,activityOptions.toBundle());
                    }
                });
            }catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        // 리스트뷰 요소와 xml 
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.frienditem_imageview);
                textView = (TextView) view.findViewById(R.id.frienditem_textview);
            }
        }
    }

}
