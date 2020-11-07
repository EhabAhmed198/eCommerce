package com.ehabahmed.ecommerce.sallers;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.prevalent.Prevalent;

public class SallerProducrCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView tShirts, sportsTshirts, femaleDresses, sweather;
    private ImageView glasses, pursesBags, hatsCaps, shoess;
    private ImageView headPhoness, laptops, watches, mobilesPhones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saller_category);


        Paper.init(this);
        tShirts = (ImageView) findViewById(R.id.t_shirts);
        sportsTshirts = (ImageView) findViewById(R.id.sports_t_shirts);
        femaleDresses = (ImageView) findViewById(R.id.female_dresses);
        sweather = (ImageView) findViewById(R.id.sweather);

        tShirts.setOnClickListener(this);
        sportsTshirts.setOnClickListener(this);
        femaleDresses.setOnClickListener(this);
        sweather.setOnClickListener(this);

        glasses = (ImageView) findViewById(R.id.glasses);
        pursesBags = (ImageView) findViewById(R.id.purses_bags);
        hatsCaps = (ImageView) findViewById(R.id.hats_caps);
        shoess = (ImageView) findViewById(R.id.shoess);

        glasses.setOnClickListener(this);
        pursesBags.setOnClickListener(this);
        hatsCaps.setOnClickListener(this);
        shoess.setOnClickListener(this);

        headPhoness = (ImageView) findViewById(R.id.headphoness);
        laptops = (ImageView) findViewById(R.id.laptops);
        watches = (ImageView) findViewById(R.id.watches);
        mobilesPhones = (ImageView) findViewById(R.id.mobiles_phones);

        headPhoness.setOnClickListener(this);
        laptops.setOnClickListener(this);
        watches.setOnClickListener(this);
        mobilesPhones.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
//--------------------cetgory1--------------------------------------
            case R.id.t_shirts:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "tShirts");
                startActivity(intent);
                break;
            case R.id.sports_t_shirts:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "sportsTshirts");
                startActivity(intent);
                break;

            case R.id.female_dresses:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "femaleDresses");
                startActivity(intent);
                break;
            case R.id.sweather:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "sweather");
                startActivity(intent);
                break;
            //--------------cetgory2-----------------------------
            case R.id.glasses:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "glasses");
                startActivity(intent);
                break;
            case R.id.purses_bags:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "pursesBags");
                startActivity(intent);
                break;

            case R.id.hats_caps:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "hatsCaps");
                startActivity(intent);
                break;
            case R.id.shoess:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "shoess");
                startActivity(intent);
                break;

            //--------------cetgory3-----------------------------
            case R.id.headphoness:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "headPhoness");
                startActivity(intent);
                break;
            case R.id.laptops:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "laptops");
                startActivity(intent);
                break;

            case R.id.watches:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "watches");
                startActivity(intent);
                break;
            case R.id.mobiles_phones:
                intent = new Intent(SallerProducrCategoryActivity.this, SallerAddNewProductActivity.class);
                intent.putExtra(Prevalent.CATEGORY, "mobilesPhones");
                startActivity(intent);
                break;



        }
    }
}