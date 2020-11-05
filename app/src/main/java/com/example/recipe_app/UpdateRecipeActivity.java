package com.example.recipe_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class UpdateRecipeActivity extends AppCompatActivity {
    ImageView recipeImage;
    Uri uri;
    EditText edtName, edtDescription, edtPrice;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);
        recipeImage = (ImageView) findViewById(R.id.iv_food_image);
        edtName = (EditText) findViewById(R.id.edt_recipe_name);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        edtPrice = (EditText) findViewById(R.id.edt_price);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            Glide.with(UpdateRecipeActivity.this)
                    .load(bundle.getString("oldimageUrl"))
                    .into(recipeImage);
            edtName.setText(bundle.getString("recipeNameKey"));
            edtDescription.setText(bundle.getString("descriptionKey"));
            edtPrice.setText(bundle.getString("priceKey"));
        }



    }

    public void btnSelectImage(View view) {
    }

    public void mBtnUpdateRecipe(View view) {
    }
}