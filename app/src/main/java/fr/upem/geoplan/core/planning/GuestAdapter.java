package fr.upem.geoplan.core.planning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import fr.upem.geoplan.R;
import fr.upem.geoplan.core.session.User;

/**
 * Created by Jeremy on 03/03/2016.
 */
public class GuestAdapter extends ArrayAdapter<User> {

    private static final StartDateFormat startDateFormat = new StartDateFormat();

    public GuestAdapter(Context context, List<User> guests) {
        super(context, 0, guests);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_guest, parent, false);
        }

        GuestViewHolder viewHolder = (GuestViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new GuestViewHolder();
            viewHolder.guest = (EditText) convertView.findViewById(R.id.editTextGuest);
            viewHolder.imageViewGuest = (ImageView) convertView.findViewById(R.id.imageViewGuest);
            viewHolder.imageViewGuest.setImageResource(R.drawable.guest);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va recupérer l'item [position] de la List<Event> events
        final User guest = getItem(position);

        //on remplit la vue
        viewHolder.guest.setText(guest.getEmail());
        viewHolder.removeGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(guest);
            }
        });

        return convertView;
    }

}
