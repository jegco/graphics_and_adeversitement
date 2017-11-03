package com.example.nextu.barchart;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private static final long MILISECONDS = 1000;
    protected BarChart mChart;
    private InterstitialAd ad;
    private CountDownTimer countDownTimer;
    private boolean adIsInProgress;
    private long timer;
    private TextInputLayout inputLayout;
    private ArrayList<BarEntry> yVals1;
    private String value;
    private String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yVals1 = new ArrayList<BarEntry>();
        values = new String[2];
        inputLayout = (TextInputLayout) findViewById(R.id.input);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(6);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);


        AxisValueFormatter xAxisFormatter = new Formater(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(5f);
        xAxis.setTypeface(Typeface.SERIF);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(10f);

        MobileAds.initialize(this, getResources().getString(R.string.idApp));
        ad = new InterstitialAd(this);
        ad.setAdUnitId(getResources().getString(R.string.idUniqueAd));
        ad.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = inputLayout.getEditText().getText().toString();
                values = value.split(",");
                try {
                    yVals1.add(new BarEntry(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
                    setData(yVals1.size(), yVals1);
                    Toast.makeText(MainActivity.this, "Datos agregados exitosamente", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    inputLayout.setError("Deben ser solo datos numericos enteros");
                }

            }
        });
    }

    private void createTimer(final long miliseconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(miliseconds, 50) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                showAd();
                adIsInProgress = false;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adIsInProgress) {
            resume(timer);
        }
    }

    @Override
    protected void onPause() {
        countDownTimer.cancel();
        super.onPause();

    }

    private void showAd() {
        Log.i("entro al add", "entro al add");
        if (ad != null && ad.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            ad.show();
        } else startAd();
    }

    private void startAd() {
        if (!ad.isLoading() && !ad.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().
            addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("6C30ADE7627B6C365D038A3F4BF77F55")
                    .build();
            ad.loadAd(adRequest);
        }
        resume(MILISECONDS);
    }

    private void resume(long miliseconds) {
        adIsInProgress = true;
        timer = miliseconds;
        createTimer(timer);
        countDownTimer.start();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.advertisement) {
            showAd();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setData(int count, ArrayList<BarEntry> yVals1) {
        float start = 0f;
        mChart.getXAxis().setAxisMinValue(start);
        mChart.getXAxis().setAxisMaxValue(start + count + 1);
        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "Dias de la semana");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(Typeface.SERIF);
        data.setBarWidth(0.9f);
        mChart.setData(data);
        mChart.notifyDataSetChanged();
    }

}
