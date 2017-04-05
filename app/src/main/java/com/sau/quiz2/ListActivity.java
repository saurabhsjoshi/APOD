package com.sau.quiz2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.sau.quiz2.adapter.ListAdapter;
import com.sau.quiz2.model.Picture;
import com.sau.quiz2.rest.ApiClient;
import com.sau.quiz2.rest.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by saurabh on 2017-04-01.
 */

public class ListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.listview) RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        final ArrayList<Picture> pictures = new ArrayList<>();
        final ListAdapter listAdapter = new ListAdapter(ListActivity.this, pictures);
        recyclerView.setAdapter(listAdapter);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
        List<String> dates = getLastTenDays();
        for(String date: dates) {
            apiService.getPictureForDateObservable(Secret.API_KEY, date)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Picture>() {
                        @Override
                        public void onCompleted() {
                            Collections.sort(pictures);
                            listAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Picture picture) {
                            pictures.add(picture);
                        }
                    });
        }
    }

    private List<String> getLastTenDays() {
        ArrayList<String> ten_days = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i < 15; i++){
            ten_days.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, -1);
        }

        return ten_days;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, ListActivity.this, 2015, 1, 1);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_date:
                showDatePicker();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        dateFormat.format(calendar.getTime());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Picture> call = apiService.getPictureForDate(Secret.API_KEY, dateFormat.format(calendar.getTime()));
        call.enqueue(new Callback<Picture>() {
            @Override
            public void onResponse(Call<Picture> call, Response<Picture> response) {
                Intent i = new Intent(ListActivity.this, MainActivity.class);
                i.putExtra("isOpen", true);
                i.putExtra("apod", response.body());
                startActivity(i);
            }

            @Override
            public void onFailure(Call<Picture> call, Throwable t) {
                Toast.makeText(ListActivity.this, "Error contacting NASA server!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
