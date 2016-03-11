package fr.upem.geoplan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.planning.GuestAdapter;
import fr.upem.geoplan.core.planning.Planning;
import fr.upem.geoplan.core.session.User;

public class NewEventActivity extends AppCompatActivity implements View.OnClickListener {

    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    int mHour = c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);
    Button btnCalendarStart, btnTimePickerStart, btnCalendarEnd, btnTimePickerEnd, validateButton, cancelButton;
    EditText txtDateStart, txtDateEnd, txtTimeStart, txtTimeEnd;
    ListView listGuests;

    private Planning planning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        planning = (Planning) i.getParcelableExtra("planning");

        setContentView(R.layout.activity_new_event);
        ImageView image_organisateur = (ImageView)findViewById(R.id.imageViewEmail);
        image_organisateur.setImageResource(R.drawable.king_event);
        ImageView image_name_event = (ImageView)findViewById(R.id.imageViewEventName);
        image_name_event.setImageResource(R.drawable.event_name);
        ImageView image_position = (ImageView)findViewById(R.id.imageViewPosition);
        image_position.setImageResource(R.drawable.position);
        setTitle("New event");

        btnCalendarStart = (Button) findViewById(R.id.startingDate);
        btnTimePickerStart = (Button) findViewById(R.id.startingTime);
        btnCalendarEnd = (Button) findViewById(R.id.endingDate);
        btnTimePickerEnd = (Button) findViewById(R.id.endingTime);
        validateButton = (Button) findViewById(R.id.validateButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        txtDateStart = (EditText) findViewById(R.id.txtStartDate);
        txtTimeStart = (EditText) findViewById(R.id.txtStartTime);
        txtDateEnd = (EditText) findViewById(R.id.txtEndDate);
        txtTimeEnd = (EditText) findViewById(R.id.txtEndTime);

        btnCalendarStart.setOnClickListener(this);
        btnTimePickerStart.setOnClickListener(this);
        btnCalendarEnd.setOnClickListener(this);
        btnTimePickerEnd.setOnClickListener(this);
        validateButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        listGuests = (ListView) findViewById(R.id.listGuests);

        List<User> guests = new ArrayList<User>();
        guests.add(new User());
        GuestAdapter adapter = new GuestAdapter(NewEventActivity.this, guests);
        listGuests.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == btnCalendarStart) {

            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            txtDateStart.setText(dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
        if (v == btnCalendarEnd) {

            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            txtDateEnd.setText(dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }

        if (v == btnTimePickerStart) {

            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // Display Selected time in textbox
                            txtTimeStart.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }
        if (v == btnTimePickerEnd) {

            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // Display Selected time in textbox
                            txtTimeEnd.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }
        if(v == validateButton) {
            // TODO:
            // retreive datas from fields
            // set a new Event() with datas
            // add this event to planning
            planning.addEvent(new Event(-999));
            // then exit this activity
            // come back to main activity
            // send new planning
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("planning", planning);
            startActivity(intent);
        }
        if(v == cancelButton) {
            // TODO:
            // exit this activity
        }
    }
}
