package com.codebly.zibro.view.home.bottom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebly.zibro.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FriendBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private FriendAdapter adapter;
    private List<Friend> friendList = new ArrayList<>();
    private int selectedPosition = -1; // 선택된 친구의 위치

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_friends, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 친구 목록 데이터 설정 (예시)
        friendList.add(new Friend("홍길동"));
        friendList.add(new Friend("김철수"));
        friendList.add(new Friend("김철수"));
        friendList.add(new Friend("김철수"));
        friendList.add(new Friend("김철수"));
        friendList.add(new Friend("김철수"));
        adapter = new FriendAdapter(friendList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    // 친구 데이터 클래스
    public class Friend {
        String name;

        public Friend(String name) {
            this.name = name;
        }
    }

    // RecyclerView Adapter
    public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {
        private List<Friend> friends;

        public FriendAdapter(List<Friend> friends) {
            this.friends = friends;
        }

        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
            Friend friend = friends.get(holder.getAdapterPosition()); // Use getAdapterPosition() to get the current position

            holder.radioButton.setText(friend.name);
            holder.radioButton.setChecked(selectedPosition == holder.getAdapterPosition());

            // ... other code that might need the position
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = holder.getAdapterPosition(); // Get position inside click listener
                    // ... perform actions based on currentPosition
                }
            });
        }


        @Override
        public int getItemCount() {
            return friends.size();
        }
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}
