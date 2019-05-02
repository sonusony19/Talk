package com.example.jksony.talk;

import android.Manifest;
import android.app.Notification;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jksony.talk.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Intent intent;
    boolean flag=false;
    CircleImageView circleImageView;
    Toolbar toolbar;
    TextView username;
    DatabaseReference reference;
    private Uri imageUri,tempUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        circleImageView=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent =getIntent();
        String UID=intent.getStringExtra("user");

        reference = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImagelink().equals("default"))
                {
                    circleImageView.setImageResource(R.drawable.user_icon);
                }else
                {
                    Glide.with(ProfileActivity.this).load(user.getImagelink()).into(circleImageView);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

    }


    public void editMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        getMenuInflater().inflate(R.menu.profile_pic_menu_layout,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.cameramenu:
                        //openCamera();
                        Toast.makeText(ProfileActivity.this,"under development",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.galarymenu:
                        checkRunTimePermissions();
                        if(flag)
                        chooseImage();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }



    private void openCamera() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),"file"+String.valueOf(System.currentTimeMillis())+".jpg");
        imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,0);
    }


    private void chooseImage() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      //  intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select a pic"),1);
    }

    private void cropImage() {
       try
       {
           intent = new Intent("com.android.camera.action.CROP");
           intent.setDataAndType(tempUri,"image/*");
           intent.putExtra("crop",true);
           intent.putExtra("outputX",150);
           intent.putExtra("outputY",150);
           intent.putExtra("aspectX",1);
           intent.putExtra("aspectY",1);
           intent.putExtra("scaleUpIfNeeded",true);
           intent.putExtra("return-data",true);

           startActivityForResult(intent,2);
       }
       catch (ActivityNotFoundException e)
       {}

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==0 && resultCode== RESULT_OK &&data!=null)
        {
            Toast.makeText(ProfileActivity.this,data.getData().toString(),Toast.LENGTH_LONG).show();
        }
        else if(requestCode==1)
        {
            Toast.makeText(ProfileActivity.this,data.getData().toString(),Toast.LENGTH_LONG).show();
        }

    }


    private boolean checkRunTimePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            else flag=true;
        }else flag=true;
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                flag=true;
            }
            else flag=false;
        }
    }
}
