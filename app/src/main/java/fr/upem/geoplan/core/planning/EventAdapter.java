package fr.upem.geoplan.core.planning;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import upem.fr.geoplan.R;

/**
 * Created by Huynh on 03/03/2016.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_event, parent, false);
        }

        EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new EventViewHolder();
            viewHolder.nameEvent = (TextView) convertView.findViewById(R.id.eventName);
            viewHolder.startingTime = (TextView) convertView.findViewById(R.id.startingTime);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.endTime);
            viewHolder.localization = (TextView) convertView.findViewById(R.id.localization);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va recupérer l'item [position] de la List<Event> events
        Event event = getItem(position);

        //on remplit la vue
        viewHolder.nameEvent.setText(event.getName());
        viewHolder.startingTime.setText(event.getStart_date_time().toString());
        viewHolder.endTime.setText(event.getEnd_date_time().toString());
        viewHolder.localization.setText(event.getLocalization());
        viewHolder.icon.setImageDrawable(new ColorDrawable(event.getColor()));

        return convertView;
    }
}
