package pes.gogreenapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.AsyncHttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * All right reserverd to GoBros Devevelopers team.
 * This code is free software; you can redistribute it and/or modify itunder the terms of
 * the GNU General Public License version 2 only, as published by the Free Software Foundation.
 */

public class EmployeeListAdapter extends BaseAdapter implements ListAdapter {

    private List<String> employees = new ArrayList<>();
    private Context context;

    public EmployeeListAdapter(List<String> employees, Context context) {

        this.employees = employees;
        this.context = context;
    }

    @Override
    public int getCount() {

        return employees.size();
    }

    @Override
    public Object getItem(int pos) {

        return employees.get(pos);
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
            view = inflater.inflate(R.layout.employee_manager_listview, parent, false);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.username_employee);
        listItemText.setText(employees.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.button_delete_employee);
        deleteBtn.setOnClickListener(v -> {
            SessionManager instance = SessionManager.getInstance();
            AsyncHttpHandler.delete("shops/" + instance.getShopId() + "/employees/" + employees.get(position), null,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            if (statusCode == 200) {
                                Toast.makeText(context, "empleado eliminado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String xml, Throwable throwable) {
                            // called when response HTTP status is "4XX"
                            Log.e("API_ERROR", String.valueOf(statusCode) + " " + throwable.getMessage());
                        }
                    });
            employees.remove(position);
            notifyDataSetChanged();
        });

        return view;
    }

    public void setEmployees(List<String> employees) {

        this.employees = employees;
    }
}
