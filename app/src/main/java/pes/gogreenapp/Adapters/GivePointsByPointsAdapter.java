package pes.gogreenapp.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.R;

/**
 * Created by Adry on 17/05/2017.
 */

public class GivePointsByPointsAdapter extends BaseAdapter {
    Context context;
    private List<String> users;
    private List<String> userNames;
    private List<String> points;


    class ListViewHolder {

        TextView userNumberByPoints;
        EditText userNameTextByPoints;
        EditText pointsToGive;

        ListViewHolder (View v) {
            userNumberByPoints = (TextView) v.findViewById(R.id.userNumberByPoints);
            userNameTextByPoints = (EditText) v.findViewById(R.id.userNameToGiveByPoints);
            pointsToGive = (EditText) v.findViewById(R.id.pointsToGive);
        }
    }

    public GivePointsByPointsAdapter(Context context, List<String> users) {
        this.users = users;
        this.context = context;
        this.userNames = new ArrayList<String>();
        this.points = new ArrayList<String>();
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
        GivePointsByPointsAdapter.ListViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_points_item_points, parent, false);
            viewHolder = new GivePointsByPointsAdapter.ListViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (GivePointsByPointsAdapter.ListViewHolder) convertView.getTag();
        }

        viewHolder.userNumberByPoints.setText(users.get(position));
        viewHolder.userNameTextByPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (userNames.size() <= position) userNames.add(position, s.toString());
                else userNames.set(position, s.toString());
            }
        });
        viewHolder.pointsToGive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (points.size() <= position) points.add(position, s.toString());
                else points.set(position, s.toString());
            }
        });
        return convertView;
    }

    public List <String> getUserNames () {
        return userNames;
    }

    public List<String> getPoints() { return points;  }
}
