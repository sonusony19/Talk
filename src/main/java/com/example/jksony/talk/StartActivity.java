package com.example.jksony.talk;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);

        delaying();



        /*DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
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
        });*/

    }

    private void delaying() {
        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(StartActivity.this,LoginActivity.class));
                finish();
            }
        },500);
    }

    private boolean checkConnectionStatus()
    {
        return false;
    }

}
