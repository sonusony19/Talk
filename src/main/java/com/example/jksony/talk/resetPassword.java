package com.example.jksony.talk;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetPassword extends AppCompatActivity {
    EditText email;
    FirebaseAuth auth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        email =findViewById(R.id.emailtag);
       toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setTitle("Reset Password");

       auth = FirebaseAuth.getInstance();

    }

    public void resetClicked(View view) {
        if(email.getText().toString().equals(""))
        {
            Toast.makeText(resetPassword.this,"All fileds are required",Toast.LENGTH_SHORT).show();

        }
        else {
            auth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(resetPassword.this,"Please check your email",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(resetPassword.this,LoginActivity.class));
                    }
                    else{

                        String error = task.getException().getMessage();
                        Toast.makeText(resetPassword.this,"error",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
