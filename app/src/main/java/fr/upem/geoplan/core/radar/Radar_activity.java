package fr.upem.geoplan.core.radar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import co.geeksters.radar.Radar;
import co.geeksters.radar.RadarPoint;
import upem.fr.geoplan.R;

public class Radar_activity extends AppCompatActivity {

    private Radar radar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_activity);
        radar = (co.geeksters.radar.Radar) findViewById(R.id.radar);

        //And here set the reference Point (or for exemple your GPS location)
        radar.setReferencePoint(new RadarPoint("myLocation", 10.00000f,22.0000f));

        // the other points in the Radar
        ArrayList<RadarPoint> points = new ArrayList<>();

        points.add(new RadarPoint("identifier1", 10.00200f,22.0000f));
        points.add(new RadarPoint("identifier2", 10.00220f,22.0000f));
        points.add(new RadarPoint("identifier3", 10.00420f, 22.0010f));

        radar.setPoints(points);
        radar.refresh();
    }
}
