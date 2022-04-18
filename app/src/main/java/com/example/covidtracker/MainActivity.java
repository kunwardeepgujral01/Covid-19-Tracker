package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView totalConfirmed, totalActive, totalDeath, totalTest, totalRecovered;
    private TextView todayDeath, today_Recovered, todayConfirmed,dateTV;
    private PieChart pieChart;
    String country="India";
    List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        if(getIntent().getStringExtra("country")!=null)
            country=getIntent().getStringExtra("country");

        init();
        TextView cname=findViewById(R.id.cname);
        cname.setText(country);
        cname.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,CountryActivity.class)));


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                for (int i=0;i<list.size(); i++)
                {
                    if(list.get(i).getCountry().equals(country))
                    {
                        int confirmed=Integer.parseInt(list.get(i).getCases());
                        int active=Integer.parseInt(list.get(i).getActive());
                        int recovered=Integer.parseInt(list.get(i).getRecovered());
                        int death=Integer.parseInt(list.get(i).getDeaths());
                        int tests=Integer.parseInt(list.get(i).getTests());

                        int todayRecovered=Integer.parseInt(list.get(i).getTodayRecovered());
                        int todayDeaths=Integer.parseInt(list.get(i).getTodayDeaths());
                        int todayCases=Integer.parseInt(list.get(i).getTodayCases());


                        totalActive.setText(NumberFormat.getInstance().format(active));
                        totalDeath.setText(NumberFormat.getInstance().format(death));
                        totalTest.setText(NumberFormat.getInstance().format(tests));
                        totalConfirmed.setText(NumberFormat.getInstance().format(confirmed));
                        totalRecovered.setText(NumberFormat.getInstance().format(recovered));

                        pieChart.addPieSlice(new PieModel("Confirm",confirmed,getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel("Deaths",death,getResources().getColor(R.color.red_pie)));
                        pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue_pie)));
                        pieChart.addPieSlice(new PieModel("Recovered",recovered,getResources().getColor(R.color.green_pie)));

                        pieChart.startAnimation();


                        setText(list.get(i).getUpdated());

                        todayConfirmed.setText(NumberFormat.getInstance().format(todayCases));
                        todayDeath.setText(NumberFormat.getInstance().format(todayDeaths));
                        today_Recovered.setText(NumberFormat.getInstance().format(todayRecovered));



                    }
                }

            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }

    private void setText(String updated) {
        DateFormat format=new SimpleDateFormat("MMM dd,yyyy");
        Long milliseconds=Long.parseLong(updated);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        dateTV.setText("Updated at: " +format.format(calendar.getTime()));
    }

    private void init() {
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeath = findViewById(R.id.totalDeaths);
        totalConfirmed = findViewById(R.id.totalConfirm);
        totalTest = findViewById(R.id.totalTests);
        todayConfirmed = findViewById(R.id.todayConfirm);
        todayDeath = findViewById(R.id.todayDeaths);
        today_Recovered = findViewById(R.id.todayRecovered);
        pieChart=findViewById(R.id.piechart);
        dateTV=findViewById(R.id.date);

    }
}