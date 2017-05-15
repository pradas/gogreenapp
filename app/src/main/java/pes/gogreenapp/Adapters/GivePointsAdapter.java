package pes.gogreenapp.Adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.R;

/**
 * Created by Adrian on 11/05/2017.
 */

public class GivePointsAdapter extends BaseAdapter {

    Context context;
    private List<String> users;
    private List<String> userNames;
    private List<String> events;
    private Integer position;

    class ListViewHolder {
        RelativeLayout itemEvents;
        RelativeLayout itemPoints;
        TextView userNumber;
        EditText userNameText;
        RadioGroup radioButtons;

        ListViewHolder (View v) {
            itemEvents = (RelativeLayout) v.findViewById(R.id.givePointsByEvents);
            itemPoints = (RelativeLayout) v.findViewById(R.id.givePointsByPoints);
            userNumber = (TextView) v.findViewById(R.id.userNumber);
            userNameText = (EditText) v.findViewById(R.id.userNameToGive);
            radioButtons = (RadioGroup) v.findViewById(R.id.radioGroupGivePoints);
        }
    }


    public GivePointsAdapter (Context context, List<String> users) {
        this.users = users;
        this.context = context;
        this.userNames = new ArrayList<String>();
        this.position = 0;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int[] buttonMode = {0};

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_points_item_events, parent, false);
            viewHolder = new ListViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }

        viewHolder.userNumber.setText(users.get(position));
        viewHolder.userNameText.addTextChangedListener(new TextWatcher() {
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
        viewHolder.radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radioButtonEvents) {
                    selectedItem(position);
                }
                else if (checkedId == R.id.radioButtonPoints) {
                    selectedItem(position);
                }
            }
        });

        if (this.position == position) {
            View view2 = convertView;
            view2 = inflater.inflate(R.layout.list_points_item_points, parent, false);
            TextView title = (TextView) view2.findViewById(R.id.userNumber);
            title.setText(users.get(position));
            return view2;
        }
        return convertView;
    }

    public void selectedItem (int position) {
        this.position = position;
    }

    public List <String> getUserNames () {
        return userNames;
    }
}
