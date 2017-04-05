package com.sau.quiz2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sau.quiz2.model.Picture;
import com.sau.quiz2.rest.ApiClient;
import com.sau.quiz2.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.img_apod) ImageView imgApod;
    @BindView(R.id.txt_title) TextView txtTitle;
    @BindView(R.id.txt_explanation) TextView txtExplanation;
    @BindView(R.id.txt_date) TextView txtDate;

    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ButterKnife.bind(this);

        if(getIntent().getBooleanExtra("isOpen", false)) {
            isOpen = true;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Picture p = getIntent().getParcelableExtra("apod");
            showDataFromParcelable(p);
        } else {
            loadPictureOfTheDay();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_list:
                startActivity(new Intent(this, ListActivity.class));
                break;

            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void showDataFromParcelable(Picture picture) {
        Picasso.with(MainActivity.this).load(picture.getUrl()).fit().centerCrop().into(imgApod);
        setTitle(picture.getTitle());
        txtTitle.setText(picture.getTitle());
        txtDate.setText(picture.getDate());
        txtExplanation.setText(picture.getExplanation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isOpen) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void loadPictureOfTheDay() {

        if(Secret.API_KEY.isEmpty())
            return;

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Picture> call = apiService.getPictureOfTheDay(Secret.API_KEY);
        call.enqueue(new Callback<Picture>() {
            @Override
            public void onResponse(Call<Picture> call, Response<Picture> response) {
                String url = response.body().getUrl();
                Picasso.with(MainActivity.this).load(url).fit().centerCrop().into(imgApod);

                txtTitle.setText(response.body().getTitle());
                txtExplanation.setText(response.body().getExplanation());
                txtDate.setText(response.body().getDate());
            }

            @Override
            public void onFailure(Call<Picture> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error contacting NASA server!", Toast.LENGTH_LONG).show();
            }
        });
    }


}