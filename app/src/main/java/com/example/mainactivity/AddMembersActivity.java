package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddMembersActivity extends AppCompatActivity {

    public Uri imgUri;
    DatabaseReference membersReference;

    EditText edtID,edtName,edtAddress,edtAmountOfMoney,edtNID;
    Button btnSubmit;
    Button btnChoose,btnUpload;
    ImageView imgUpload;
    MembersDataObject membersDataObject;
    StorageReference storageReference;
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        FirebaseApp.initializeApp(this);
        membersReference= FirebaseDatabase.getInstance().getReference("members");
        storageReference= FirebaseStorage.getInstance().getReference("Images");
        membersDataObject=new MembersDataObject();
        edtID=findViewById(R.id.edtID);
        edtName=findViewById(R.id.edtName);
        edtAddress=findViewById(R.id.edtAddress);
        edtAmountOfMoney=findViewById(R.id.edtAmountOfMoney);
        edtNID=findViewById(R.id.edtNID);

        btnChoose=findViewById(R.id.btnChoose);
        btnUpload=findViewById(R.id.btnUpload);
        imgUpload=findViewById(R.id.imgPicture);

        btnSubmit=findViewById(R.id.btnSubmit);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(AddMembersActivity.this,"Uploading Progress///",Toast.LENGTH_LONG).show();
                }
                else
                {
                    fileUploader();
                }

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

    }

    private  String getExtention(Uri Uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Uri));
    }
    private void fileUploader() {
        StorageReference mstorageReference=storageReference.child(System.currentTimeMillis()+"."+getExtention(imgUri));

       uploadTask= mstorageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Toast.makeText(AddMembersActivity.this,"Uploaded Successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void fileChooser() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imgUri=data.getData();
            imgUpload.setImageURI(imgUri);
        }
    }

    private void addData() {
        String strID,strName,strAddress,strAmountOfMoney,strNID;
        String amountOfMoney;
        strID=edtID.getText().toString().trim();
        strName=edtName.getText().toString().trim();
        strAddress=edtAddress.getText().toString().trim();
        strAmountOfMoney=edtAmountOfMoney.getText().toString().trim();
        strNID=edtNID.getText().toString().trim();

        if(!TextUtils.isEmpty(strID))
        {
            MembersDataObject dataObject=new MembersDataObject(strID,strName,strAddress,strAmountOfMoney,strNID);
            membersReference.child(strID).setValue(dataObject);
            Toast.makeText(this,"Data added Successfully...",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast toast=Toast.makeText(getApplicationContext(),"Country Name Empty...",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
