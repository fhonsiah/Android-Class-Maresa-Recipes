package com.example.recipe_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView foodDescription , mTvName, mTvPrice ;
    ImageView foodImage;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTvName = (TextView) findViewById(R.id.tv_recipe_name);
        mTvPrice = (TextView) findViewById(R.id.tvPrice);
        foodDescription = (TextView) findViewById(R.id.txtDescription);
        foodImage = (ImageView) findViewById(R.id.ivImage_two);

        Bundle mBundle = getIntent().getExtras();

        if (mBundle!=null){

            foodDescription.setText(mBundle.getString("Description"));
            key = mBundle.getString("keyValue");
            imageUrl = mBundle.getString("Image");
            mTvName.setText(mBundle.getString("RecipeName"));

//            foodImage.setImageResource(mBundle.getInt("Image"));


            Glide.with(this)
                    .load(mBundle.getString("Image"))
                    .into(foodImage);

        }

    }

    public void btnDeleteRecipe(View view) {

        final DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("recipe-app");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                reference.child(key).removeValue();
                Toast.makeText(DetailActivity.this, "Recipe Deleted", Toast.LENGTH_SHORT).show();
                startActivity( new Intent(getApplicationContext(),SecondActivity.class));
                finish();
            }
        });



    }

    public void btnUpdateRecipe(View view) {

        startActivity( new Intent(getApplicationContext(), UpdateRecipeActivity.class)
        .putExtra("recipeNameKey",mTvName.getText().toString())
        .putExtra("descriptionKey", foodDescription.getText().toString())
       .putExtra("priceKey", mTvPrice.getText().toString())
        .putExtra("oldimageUrl", imageUrl)
        .putExtra("key", key)
        );


    }
}