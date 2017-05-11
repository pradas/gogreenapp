package pes.gogreenapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import pes.gogreenapp.R;

/**
 * Created by Adrian on 11/05/2017.
 */

public class GivePointsAdapter extends BaseAdapter {

    Context context;
    private List<String> users;
    private List<String> events;

    class ListViewHolder {
        TextView userNumber;
        EditText userNameText;
        RadioGroup radioButtons;

        ListViewHolder (View v) {
            userNumber = (TextView) v.findViewById(R.id.userNumber);
            userNameText = (EditText) v.findViewById(R.id.userNameToGive);
            radioButtons = (RadioGroup) v.findViewById(R.id.radioGroupGivePoints);
        }
    }


    public GivePointsAdapter (Context context, List<String> users) {
        this.users = users;
        this.context = context;
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
        View row = convertView;
        ListViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listpoints_item, parent, false);
            viewHolder = new ListViewHolder(row);
            row.setTag(viewHolder);
        }
        else {
            viewHolder = (ListViewHolder) row.getTag();
        }
        viewHolder.userNumber.setText(users.get(position));
        return row;
    }
}
