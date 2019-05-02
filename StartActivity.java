package com.example.jksony.talk;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);

        delaying();



        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Toast.makeText(StartActivity.this,"Connected",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(StartActivity.this,LoginActivity.class));
                } else {
                    Toast.makeText(StartActivity.this,"Not Connected",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(StartActivity.this,StartActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StartActivity.this,"Listener was canceled",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void delaying() {
        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        },500);
    }

    private boolean checkConnectionStatus()
    {
        return false;
    }

}
