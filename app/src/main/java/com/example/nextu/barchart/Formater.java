package com.example.nextu.barchart;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

/**
 * Created by jorge Caro
 */
public class Formater implements AxisValueFormatter {

    private BarLineChartBase<?> chart;
    @Override
    public String getFormattedValue(float value, AxisBase axis) {

            if(value==1){

                return "Lunes";
            }else if(value==2) {
                return  "Martes";

            }else if(value==3) {

                return "Miercoles";
            }else if(value==4) {

                return  "Jueves";
            }
            else if(value==5) {

                return  "Viernes";
            }
            else if(value==6) {

                return  "Sabado";
            }
            else {
                return  "";
            }



    }

    public Formater(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
