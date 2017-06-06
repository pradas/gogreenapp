package pes.gogreenapp.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pes.gogreenapp.Adapters.EmployeeListAdapter;
import pes.gogreenapp.R;
import pes.gogreenapp.Utils.AsyncHttpHandler;
import pes.gogreenapp.Utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeManagerFragment extends Fragment {


    public EmployeeManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.employee_manager_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        SessionManager instance = SessionManager.getInstance();

        List<String> employees = new ArrayList<>();
        AsyncHttpHandler.get("shops/" + instance.getShopId() + "/employees", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Handle resulting parsed JSON response here
                try {
                    JSONArray jsonArray = response.getJSONArray("employees");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String employee = jsonObject.getString("username");
                        employees.add(employee);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                // called when response HTTP status is "4XX"
                Log.e("API_ERROR", String.valueOf(statusCode) + " " + throwable.getMessage());
            }
        });

        // create the adapter and instance the content of the list view
        EmployeeListAdapter adapter = new EmployeeListAdapter(employees, getActivity().getApplicationContext());
        ListView listView = (ListView) getView().findViewById(R.id.list_employees);
        listView.setAdapter(adapter);
    }


}
