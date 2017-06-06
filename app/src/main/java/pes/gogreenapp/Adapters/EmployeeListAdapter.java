package pes.gogreenapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import pes.gogreenapp.R;

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

        return view;
    }
}
