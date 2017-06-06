package pes.gogreenapp.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

    EmployeeListAdapter adapter;
    ListView listView;

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

                    // create the adapter and instance the content of the list view
                    adapter = new EmployeeListAdapter(employees, getActivity().getApplicationContext());
                    listView = (ListView) getView().findViewById(R.id.list_employees);
                    listView.setAdapter(adapter);

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

        // set the listener of add employee button
        Button buttonAddEmployee = (Button) getView().findViewById(R.id.button_add_employee);
        buttonAddEmployee.setOnClickListener(v -> {
            boolean inputCorrect = true;
            EditText editTextUsername = (EditText) getView().findViewById(R.id.edit_text_employee_username);
            String username = editTextUsername.getText().toString();
            EditText editTextEmail = (EditText) getView().findViewById(R.id.edit_text_employee_email);
            String email = editTextEmail.getText().toString();
            EditText editTextPassword = (EditText) getView().findViewById(R.id.edit_text_employee_password);
            String password = editTextPassword.getText().toString();

            // see if username input isn't empty
            if (username.isEmpty()) {
                inputCorrect = false;
                editTextUsername.setError("username necesario");
            }
            if (email.isEmpty()) {
                inputCorrect = false;
                editTextEmail.setError("email necesario");
            }
            if (password.isEmpty()) {
                inputCorrect = false;
                editTextPassword.setError("contraeña necesaria");
            }

            // if all correct send the POST petition to the API
            if (inputCorrect) {
                // create the request params for the petition
                RequestParams requestParams = new RequestParams();
                requestParams.add("username", username);
                requestParams.add("email", email);
                requestParams.add("password", password);

                AsyncHttpHandler.post("shops/" + instance.getShopId() + "/employees", requestParams,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                // Handle resulting parsed JSON response here
                                if (statusCode == 201) {
                                    Toast.makeText(getActivity().getApplicationContext(), "empleado añadido",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String errorRaw,
                                                  Throwable throwable) {
                                // called when response HTTP status is "4XX"
                                Log.e("API_ERROR", String.valueOf(statusCode) + " " + throwable.getMessage());
                            }
                        });
            }
        });
    }
}
