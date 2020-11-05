package com.example.recipe_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.recipe_app.R.id.logoutMenu;
import static java.util.Locale.filter;

public class SecondActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<FoodData>  myFoodList;
    FoodData mFoodData;
    MyAdapter myAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ValueEventListener  eventListener;
    ProgressDialog progressDialog;
    EditText mEdtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        firebaseAuth = FirebaseAuth.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(SecondActivity.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mEdtSearch = (EditText) findViewById(R.id.edt_searchtext);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Items .....");

        myFoodList = new ArrayList<>();

//        mFoodData = new FoodData("Chicken Pilau","How about you spoil yourself with some delicious chicken coconut pilau.This spicy and aromatic dish is just what your saturday lunch or dinner needs!Enjoy this delicious aromatic yumminess.","Tsh.7000",R.drawable.img_pilau);
//        myFoodList.add(mFoodData);
//
//        mFoodData = new FoodData("Vegetable Spaghetti Stir Fry","This veggie spaghetti stir-fry is a quick and healthy meal especially for busy days.Its delicious and easy to make try it.","Tsh.5000",R.drawable.img_food_one);
//        myFoodList.add(mFoodData);
//        mFoodData = new FoodData("Green Mango Rice","Freshly ground spices are more aromatic.So I like to buy whole spices and grind them when I need.For this recipe you should adjust the amount of green mango to your liking of sourness.The raisins are optional but I find that they balance the sourness with a little sweetness.","Tsh.6000",R.drawable.img_food_two);
//        myFoodList.add(mFoodData);
        
//        mFoodData = new FoodData("Sweet Banana Pancake","Always wait for the pretty bubbles before flipping to avoid making a mess.Serve with some fruits...drizzle some honey, kunywa na chai kama yotee!!!","Tsh.3000",R.drawable.img_food_three);
//        myFoodList.add(mFoodData);
        //Logout = (Button) findViewById(R.id.btn_Logout);

//        Logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              Logout();
//            }
//        })
       myAdapter = new MyAdapter(SecondActivity.this,myFoodList);
        mRecyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("recipe-app");

        progressDialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myFoodList.clear();

                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {

                    FoodData  foodData = itemSnapshot.getValue(FoodData.class);
                    foodData.setKey(itemSnapshot.getKey());    //using the setter key to get data from the object itemsnapshot
                    myFoodList.add(foodData);
                }

                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });


        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });


    }

    private void filter(String text) {

        ArrayList<FoodData>  filterList = new ArrayList<>();

        for (FoodData item: myFoodList){

            if (item.getItemName().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }

        myAdapter.filteredList(filterList);

    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity( new Intent(SecondActivity.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String id = item.getItemId()+"";
        String logoutID = R.id.logoutMenu+"";
        if (id.trim().equals(logoutID.trim())){
            Logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void btn_UploadActivity(View view) {

        startActivity( new Intent(SecondActivity.this, Upload_Recipe.class));
    }
}