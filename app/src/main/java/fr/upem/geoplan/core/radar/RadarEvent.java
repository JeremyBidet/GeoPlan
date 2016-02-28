package fr.upem.geoplan.core.radar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import co.geeksters.radar.Radar;

class RadarEvent implements View.OnTouchListener {
    private final Radar radar;
    private final Context context;

    RadarEvent(Context context, Radar radar) {
        this.context = context;
        this.radar = radar;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String pinIdentifier = radar.getTouchedPin(event);
        if (pinIdentifier != null) {
            Toast.makeText(context, pinIdentifier, Toast.LENGTH_SHORT).show();
        }
        // TODO Let user choose some actions
        return true;
    }
}
