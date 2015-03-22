package com.example.inwo.inwo_horasextra;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;


public class Calendario extends ActionBarActivity {

    private View calendarioVisual;
    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        calendarioVisual = findViewById(R.id.calendarView);

        calendarioVisual.setBackgroundResource(R.drawable.cell_shape);


//        calendarioVisual.setCustomizationRule(new Procedure<CalendarCell>() {
//            @Override
//            public void apply(CalendarCell calendarCell) {
//                calendar.setTimeInMillis(calendarCell.getDate());
//                if(calendar.get(Calendar.DAY_OF_MONTH) == 21 &&
//                        calendar.get(Calendar.MONTH) ==
//                                Calendar.getInstance().get(Calendar.MONTH)) {
//                    calendarCell.setBackgroundColor(Color.parseColor("#FF00A1"), Color.parseColor("#F988CF"));
//                } else {
//                    calendarCell.setBackgroundColor(Color.parseColor("#E5E5E5"), Color.parseColor("#EEEEEE"));
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
