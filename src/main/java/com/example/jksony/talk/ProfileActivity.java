package com.example.jksony.talk;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jksony.talk.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    boolean flag=false;
    CircleImageView circleImageView;
    Toolbar toolbar;
    TextView username;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseUser fUser;
    private Uri imageUri,tempUri;
    private StorageTask uploadTask;
    FloatingActionButton imagechoosing;
   private static final int IMAGE_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        circleImageView=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        toolbar =findViewById(R.id.toolbar);
        imagechoosing = findViewById(R.id.imagechoosing);
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

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fUser = FirebaseAuth.getInstance().getCurrentUser();
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
        imagechoosing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }




    private void chooseImage() {
       Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(intent,IMAGE_REQUEST);
    }

  private String getFileExtension(Uri uri)
  {
      ContentResolver contentResolver = ProfileActivity.this.getContentResolver();
      MimeTypeMap mimeTypeMap =  MimeTypeMap.getSingleton();
      return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
  }

  private void uploadImage()
  {
      final ProgressDialog progressDialog= new ProgressDialog(ProfileActivity.this);
      progressDialog.setMessage("Uploading");
      progressDialog.show();

      if(imageUri!=null)
      {
          final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
          uploadTask = fileReference.putFile(imageUri);
          uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
              @Override
              public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                  if(!task.isSuccessful())
                  {
                      throw  task.getException();
                  }
                  return fileReference.getDownloadUrl();
              }
          }).addOnCompleteListener(new OnCompleteListener<Uri>() {
              @Override
              public void onComplete(@NonNull Task<Uri> task) {
                  if(task.isSuccessful())
                  {
                      Uri downloadUri = task.getResult();
                      String mUri = downloadUri.toString();
                      reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
                      HashMap<String, Object>map = new HashMap<>();
                      map.put("imagelink",mUri);
                      reference.updateChildren(map);
                      progressDialog.dismiss();
                  }
                  else
                  {
                      Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                      progressDialog.dismiss();
                  }
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                  progressDialog.dismiss();
              }
          });
      }
      else
      {
          Toast.makeText(getApplicationContext(),"No image selected",Toast.LENGTH_SHORT).show();
      }
  }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==IMAGE_REQUEST && resultCode== RESULT_OK &&data!=null&& data.getData()!=null) {
            imageUri = data.getData();

            if(uploadTask!=null && uploadTask.isInProgress())
            {
                Toast.makeText(ProfileActivity.this,"Upload in Progress",Toast.LENGTH_LONG).show();

            }
            else
            {
                uploadImage();
            }
        }

    }


}
