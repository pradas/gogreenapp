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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        this.userNames = new ArrayList<String>();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            row = inflater.inflate(R.layout.listpoints_item, parent, false);
            viewHolder = new ListViewHolder(row);
            row.setTag(viewHolder);
        }
        else {
            viewHolder = (ListViewHolder) row.getTag();
        }
        viewHolder.userNumber.setText(users.get(position));
        viewHolder.radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radioButtonEvents) {

                }
                else if (checkedId == R.id.radioButtonPoints) {

                }
            }
        });

        viewHolder.userNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return row;
    }

    public List <String> getUserNames () {
        return userNames;
    }
}
