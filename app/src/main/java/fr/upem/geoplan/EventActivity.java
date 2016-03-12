package fr.upem.geoplan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.planning.EventAdapter;
import fr.upem.geoplan.core.planning.GuestAdapter;
import fr.upem.geoplan.core.planning.Planning;
import fr.upem.geoplan.core.server.gcm.RequestToServer;
import fr.upem.geoplan.core.session.User;

public class EventActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    int mHour = c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);

    EditText editEmail;
    ImageView imageViewEmail;

    EditText editNameEvent;
    ImageView image_name_event;

    EditText editDescription;
    ImageView imageViewDescription;

    EditText editPosition;
    ImageView image_position;

    ImageView owner_img;

    EditText txtDateStart;
    EditText txtDateEnd;
    EditText txtTimeStart;
    EditText txtTimeEnd;

    Button btnCalendarStart;
    Button btnTimePickerStart;
    Button btnCalendarEnd;
    Button btnTimePickerEnd;

    EditText editColor;
    EditText editCost;

    ListView listGuests;

    Button validateButton;
    Button cancelButton;

    private User current_user;
    private Planning planning;
    private Event event;

    private List<User> guests;
    private GuestAdapter adapter;

    private enum Protocol {CREATE, EDIT;}

    private Protocol protocol;

    ListPopupWindow listPopupWindow;

    private void setProtocol(Protocol protocol, long id) {
        this.protocol = protocol;
        switch (protocol) {
            case CREATE:
                event = new Event(id);
                requestEvent();
                init();
                prepareToCreate();
                break;
            case EDIT:
                event = planning.getEventByID(id);
                init();
                prepareToEdit();
                break;
            default:
                break;
        }
    }

    private void requestEvent() {
        // TODO: sync the event on the server
        // get the server generated id and set it to the event
        long event_id = -1;
        event.setId(event_id);
    }

    private void syncEvent() {
        // TODO: update the event on the server
        // the event has already been created when requestEvent() was called
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        current_user = MainActivity.currentUser;
        planning = MainActivity.planning;

        /*final String[] PROJECTION =
                {
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };*/

        Intent i = getIntent();
        if (i.hasExtra("create") && i.getBooleanExtra("create", false)) {
            setProtocol(Protocol.CREATE, -2);
        } else if (i.hasExtra("edit") && i.getBooleanExtra("edit", false) && i.hasExtra("event") && i.getLongExtra("event", -1) != -1) {
            setProtocol(Protocol.EDIT, i.getLongExtra("event", -1));
        }
    }

    private void init() {
        setContentView(R.layout.activity_new_event);

        editEmail = (EditText) findViewById(R.id.editEmail);
        imageViewEmail = (ImageView) findViewById(R.id.imageViewEmail);
        imageViewEmail.setImageResource(R.drawable.king_event);

        editNameEvent = (EditText) findViewById(R.id.editNameEvent);
        image_name_event = (ImageView) findViewById(R.id.imageViewEventName);
        image_name_event.setImageResource(R.drawable.event_name);

        editDescription = (EditText) findViewById(R.id.editDescription);
        imageViewDescription = (ImageView) findViewById(R.id.imageViewDescription);
        //imageViewDescription.setImageResource(R.drawable.description);

        editPosition = (EditText) findViewById(R.id.editPosition);
        image_position = (ImageView) findViewById(R.id.imageViewPosition);
        image_position.setImageResource(R.drawable.position);

        txtDateStart = (EditText) findViewById(R.id.txtStartDate);
        txtTimeStart = (EditText) findViewById(R.id.txtStartTime);
        txtDateEnd = (EditText) findViewById(R.id.txtEndDate);
        txtTimeEnd = (EditText) findViewById(R.id.txtEndTime);

        btnCalendarStart = (Button) findViewById(R.id.startingDate);
        btnTimePickerStart = (Button) findViewById(R.id.startingTime);
        btnCalendarEnd = (Button) findViewById(R.id.endingDate);
        btnTimePickerEnd = (Button) findViewById(R.id.endingTime);
        btnCalendarStart.setOnClickListener(this);
        btnTimePickerStart.setOnClickListener(this);
        btnCalendarEnd.setOnClickListener(this);
        btnTimePickerEnd.setOnClickListener(this);

        editColor = (EditText) findViewById(R.id.editColor);
        editCost = (EditText) findViewById(R.id.editCost);

        validateButton = (Button) findViewById(R.id.validateButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        validateButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        listGuests = (ListView) findViewById(R.id.listGuests);
/*      TODO
        listPopupWindow = new ListPopupWindow(
                EventActivity.this);
        listPopupWindow.setAdapter(new ArrayAdapter(
                EventActivity.this,
                R.layout.list_item, getContactsEmails()));
        listPopupWindow.setOnItemClickListener(
                EventActivity.this);
*/
        View header = getLayoutInflater().inflate(R.layout.header_guest, null);
        Button addGuest = (Button) header.findViewById(R.id.addGuest);
        addGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guests.add(new User());
                adapter.notifyDataSetChanged();
            }
        });
        listGuests.addHeaderView(header);
/*      TODO
        editEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        editEmail.setText(getContactsEmails().indexOf(position));
        listPopupWindow.dismiss();
    }

    private void prepareToCreate() {
        setTitle("New event");

        guests = new ArrayList<User>();
        adapter = new GuestAdapter(EventActivity.this, guests);
        listGuests.setAdapter(adapter);
    }

    private void prepareToEdit() {
        setTitle(event.getName());

        //editEmail.setText(event.getOwners().get(0).getDisplayName());
        editEmail.setText("BIDET");
        editNameEvent.setText(event.getName());
        editDescription.setText(event.getDescription());
        editPosition.setText(event.getLocalization());

        Calendar c = Calendar.getInstance();

        c.setTime(event.getStart_date_time());
        String start_date = c.get(Calendar.DATE) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        txtDateStart.setText(start_date);
        String start_time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        txtTimeStart.setText(start_time);

        c.setTime(event.getEnd_date_time());
        String end_date = c.get(Calendar.DATE) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        txtDateEnd.setText(end_date);
        String end_time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        txtTimeEnd.setText(end_time);

        //editColor.setText(event.getColor());
        editColor.setText("blue");
        editCost.setText(Float.toString(event.getCost()));

        guests = event.getGuests();
        adapter = new GuestAdapter(EventActivity.this, guests);
        listGuests.setAdapter(adapter);
    }

    private void updateEvent() {
        event.setName(editNameEvent.getText().toString());
        event.setDescription(editDescription.getText().toString());
        event.setLocalization(editPosition.getText().toString());
        // TODO: don't forget to parse start and end date-time
        event.setEnd_date_time(null);
        event.setStart_date_time(null);
        // TODO: don't forget to add guests and set owner to current user
        event.setColor(Integer.parseInt(editColor.getText().toString()));
        event.setCost(Float.parseFloat(editCost.getText().toString()));
        if (protocol == Protocol.CREATE) {
            planning.addEvent(event);
        }
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
        if (v == validateButton) {
            updateEvent();
            syncEvent();
        }
        if (v == cancelButton) {
            this.finish();
        }
    }

    public List<String> getContactsEmails() {
        ArrayList<String> mails = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);

        while (emailCur.moveToNext()) {
            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            mails.add(email);
        }
        emailCur.close();
        return mails;
    }
}
