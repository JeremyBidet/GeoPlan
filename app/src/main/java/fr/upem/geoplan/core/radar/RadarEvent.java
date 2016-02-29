package fr.upem.geoplan.core.radar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import co.geeksters.radar.Radar;

class RadarEvent implements View.OnTouchListener {
    private final Radar radar;
    private final RadarActivity activity;
    private final ConcurrentHashMap<String, Bundle> bundles = new ConcurrentHashMap<>();

    RadarEvent(RadarActivity activity, Radar radar, HashMap<String, Bundle> bundles) {
        this.activity = activity;
        this.radar = radar;

        this.bundles.putAll(bundles);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String pinIdentifier = radar.getTouchedPin(event);

        if (pinIdentifier != null && bundles.containsKey(pinIdentifier)) {
            RadarDialogUserAction dialog = new RadarDialogUserAction();
            Bundle bundle = new Bundle();
            bundle.putAll(bundles.get(pinIdentifier));
            dialog.setArguments(bundle);
            dialog.show(activity.getFragmentManager(), "UserAction");

            return true;
        }

        return false;
    }
}
