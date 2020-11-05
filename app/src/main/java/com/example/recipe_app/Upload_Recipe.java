package com.example.recipe_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class Upload_Recipe extends AppCompatActivity {

    ImageView recipeImage;
    Uri uri;
    EditText edtName, edtDescription, edtPrice;
    String imageUrl;
    private final static int STATIC_STATIC_REQuest_cODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__recipe);

        recipeImage = (ImageView) findViewById(R.id.iv_food_image);
        edtName = (EditText) findViewById(R.id.edt_recipe_name);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        edtPrice = (EditText) findViewById(R.id.edt_price);
    }

    public void btnSelectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,STATIC_STATIC_REQuest_cODE);
//        Intent photoPicker = new Intent(Intent.ACTION_PICK);
//        photoPicker.setType("Image/*");
//        startActivityForResult(photoPicker, STATIC_STATIC_REQuest_cODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            uri = data.getData();
            recipeImage.setImageURI(uri);
        }
        else Toast.makeText(this, "You have not picked an image", Toast.LENGTH_SHORT).show();

    }

    public void UploadImage() {

        StorageReference  storageReference = FirebaseStorage.getInstance()
                .getReference().child("RecipeImage").child(uri.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uploading...!!");
        progressDialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri>   uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadRecipe();
                progressDialog.dismiss();

//                Toast.makeText(Upload_Recipe.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
            }
        });
    }

    public void btnUploadRecipe(View view) {

        UploadImage();
    }

    public void uploadRecipe(){

        FoodData foodData = new FoodData(
                edtName.getText().toString(),
                edtDescription.getText().toString(),
                edtPrice.getText().toString(),
                imageUrl
        );

        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("recipe-app")
                .child(myCurrentDateTime).setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(Upload_Recipe.this, "Recipe Uploaded", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Upload_Recipe.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }
        });
    }
}