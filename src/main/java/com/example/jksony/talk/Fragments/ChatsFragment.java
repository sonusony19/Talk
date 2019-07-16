package com.example.jksony.talk.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jksony.talk.Adapters.UserAdapter;
import com.example.jksony.talk.Model.Chat;
import com.example.jksony.talk.Model.User;
import com.example.jksony.talk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    private List<User> mUsers;
    private List<String> userlist;
    FirebaseUser firebaseUser;
    DatabaseReference reference;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsers = new ArrayList<>();
        userlist = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new UserAdapter(getContext(),mUsers,true);
        recyclerView.setAdapter(adapter);

        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(firebaseUser.getUid())) userlist.add(chat.getReceiver());
                    if(chat.getReceiver().equals(firebaseUser.getUid()))userlist.add(chat.getSender());
                }
                
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

      /* reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               userlist.clear();
               for(DataSnapshot snapshot:dataSnapshot.getChildren())
               {
                   Chatlist chatlist = snapshot.getValue(Chatlist.class);
                   userlist.add(chatlist);
               }

               chatList();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });*/

        return view;
    }

   /* private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    for(Chatlist chatlist:userlist)
                    {
                        if(user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }

                adapter= new UserAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/




    private void readChats() {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    for(String id:userlist)
                    {
                        if(id.equals(user.getId()))
                        {
                            if(!mUsers.contains(user)) mUsers.add(user);
                        }
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }




}
