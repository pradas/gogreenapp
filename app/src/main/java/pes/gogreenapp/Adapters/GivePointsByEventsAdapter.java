package pes.gogreenapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.Objects.Event;
import pes.gogreenapp.R;

/**
 * Created by Adrian on 11/05/2017.
 */

public class GivePointsByEventsAdapter extends BaseAdapter {

    Context context;
    private List<String> users;
    private List<String> userNames;
    private List<Event> events;
    private List<Integer> eventsSelected;



    class ListViewHolder {
        TextView userNumberByEvents;
        EditText userNameTextByEvents;
        Spinner spinnerEvents;

        /**
         * Constructor of the View Holder that sets all the items.
         *
         * @param v valid View where to construct.
         */
        ListViewHolder (View v) {
            userNumberByEvents = (TextView) v.findViewById(R.id.userNumberByEvents);
            userNameTextByEvents = (EditText) v.findViewById(R.id.userNameToGiveByEvents);
            spinnerEvents = (Spinner) v.findViewById(R.id.spinnerEvents);

            List <String> eventsToSpinner = new ArrayList<String>();
            for (int i = 0; i < events.size(); ++ i) {
                eventsToSpinner.add(events.get(i).getTitle());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.spinner_dropdown, eventsToSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEvents.setAdapter(adapter);
        }
    }


    /**
     * Constructor that set the List of Rewards.
     *
     * @param context non-null context of the application.
     * @param users non-null list of number of users to give points
     * @param events non-null list of events of the shop
     */
    public GivePointsByEventsAdapter(Context context, List<String> users, List <Event> events) {
        this.users = users;
        this.context = context;
        this.userNames = new ArrayList<String>();
        this.eventsSelected = new ArrayList<Integer>();
        this.events = events;
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public Object getItem(int position) {
        return this.users.get(position);
    }

    @Override
    public long getItemId(int position) { return 0; }

    /**
     * Obtains the LayoutInflater from the given context and usse it to create a new ViewHolder.
     *
     * @param position   The item of the view
     * @param convertView The new view
     * @param parent All the items of the list
     * @return A new view that holds all the methods of each item
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_points_item_events, parent, false);
            viewHolder = new ListViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }

        viewHolder.userNumberByEvents.setText(users.get(position));
        viewHolder.userNameTextByEvents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userNames.size() <= position) userNames.add(position, s.toString());
                else userNames.set(position, s.toString());
            }
        });
        viewHolder.spinnerEvents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (eventsSelected.size() <= position) eventsSelected.add(position, pos);
                else eventsSelected.set(position, pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }

    /**
     * @return the list of users to give points
     */
    public List <String> getUserNames () { return userNames; }

    /**
     * @return the list of events assisted by the users
     */
    public List <Event> getEvents() {
        List <Event> result = new ArrayList<Event>();
        for (int i = 0; i < eventsSelected.size(); ++ i) {
            result.add(events.get(eventsSelected.get(i)));
        }
        return result;
    }
}
