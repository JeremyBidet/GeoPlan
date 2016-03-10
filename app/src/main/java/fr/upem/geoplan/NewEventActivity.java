package fr.upem.geoplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class NewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ImageView image_organisateur = (ImageView)findViewById(R.id.imageViewEmail);
        image_organisateur.setImageResource(R.drawable.king_event);
        ImageView image_name_event = (ImageView)findViewById(R.id.imageViewEventName);
        image_name_event.setImageResource(R.drawable.event_name);
        ImageView image_position = (ImageView)findViewById(R.id.imageViewPosition);
        image_position.setImageResource(R.drawable.position);
        setTitle("New event");

    }
}
