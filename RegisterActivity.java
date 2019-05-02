package com.example.jksony.talk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText name,email,password,cpassword;
    FirebaseAuth auth;
    DatabaseReference reference;
    Toolbar toolbar;
    DotProgressBar progressBar;
    Button registerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        name = findViewById(R.id.nametag);
        email = findViewById(R.id.emailtag);
        password = findViewById(R.id.passwordtag);
        cpassword = findViewById(R.id.cpasswordtag);
        progressBar=findViewById(R.id.progressbar);
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        registerbutton=findViewById(R.id.btn_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       progressBar.setVisibility(View.GONE);

    }

    public void methodRegister(View view) {
        if(validateForm())
        {
            registerbutton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            registerUser(name.getText().toString().trim(),email.getText().toString().trim(),password.getText().toString().trim());
        }
    }

    private void registerUser(final String sname, final String semail, final String spass) {
        auth.createUserWithEmailAndPassword(semail,spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = auth.getCurrentUser();
                    assert user != null;
                    String userID = user.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                    HashMap<String, String> values = new HashMap<>();
                    values.put("id",userID);
                    values.put("username",sname);
                    values.put("imagelink","default");
                    reference.setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent =  new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this,"You can't register with this email",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    registerbutton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean validateForm() {
        boolean state =true;
        String semail = email.getText().toString().trim();
        String sname = name.getText().toString().trim();
        String spass = password.getText().toString().trim();
        String scpass = cpassword.getText().toString().trim();
        if(TextUtils.isEmpty(semail)) { state=false; email.setError("Required");}
        if(TextUtils.isEmpty(sname)) { state=false; name.setError("Required");}
        if(TextUtils.isEmpty(spass)) { state=false; password.setError("Required");}
        if(TextUtils.isEmpty(scpass)) { state=false; cpassword.setError("Required");}
        if(!spass.equals(scpass))
        {
            Toast.makeText(RegisterActivity.this, "Passwords Doesn't Match", Toast.LENGTH_SHORT).show();
            cpassword.requestFocus();
            state=false;
        }
        if(spass.length()<6) {
            Toast.makeText(RegisterActivity.this, "Password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
        state=false;}

        return state;
    }
}
