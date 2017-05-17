package pes.gogreenapp.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.R;

/**
 * Created by Adrian on 11/05/2017.
 */

public class GivePointsByEventsAdapter extends BaseAdapter {

    Context context;
    private List<String> users;
    private List<String> userNames;
    private List<String> events;

    class ListViewHolder {
        TextView userNumberByEvents;
        EditText userNameTextByEvents;
        Spinner spinnerEvents;

        ListViewHolder (View v) {
            userNumberByEvents = (TextView) v.findViewById(R.id.userNumberByEvents);
            userNameTextByEvents = (EditText) v.findViewById(R.id.userNameToGiveByEvents);
            spinnerEvents = (Spinner) v.findViewById(R.id.spinnerEvents);
        }
    }


    public GivePointsByEventsAdapter(Context context, List<String> users) {
        this.users = users;
        this.context = context;
        this.userNames = new ArrayList<String>();
        this.events = new ArrayList<String>();
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

        return convertView;
    }

    public List <String> getUserNames () {
        return userNames;
    }
}
