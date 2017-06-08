package pes.gogreenapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.Exceptions.NullParametersException;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.UserData;

public class AccountManagerAdapter extends BaseAdapter implements ListAdapter {


    private List<String> users = new ArrayList<String>();
    private Context context;

    public AccountManagerAdapter(List<String> users, Context context) {

        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {

        return users.size();
    }

    @Override
    public Object getItem(int pos) {

        return users.get(pos);
    }

    @Override
    public long getItemId(int pos) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.account_manager_listview, parent, false);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_account);
        listItemText.setText(users.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.list_item_delete_btn);
        deleteBtn.setOnClickListener(v -> {
            try {
                UserData.deleteUser(users.get(position), context);
            } catch (NullParametersException e) {
                e.printStackTrace();
            }
            users.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "cuenta desvinculada", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}