package pes.gogreenapp.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.junit.runner.Request;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import pes.gogreenapp.R;

/**
 * Created by jalvarez on 4/7/17.
 */

public class FormUserActivity extends AppCompatActivity {
    private static Integer mYear, mMonth, mDay;
    public static final String TAG = "submitUserTag";
    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final ImageButton pickDate = (ImageButton) findViewById(R.id.datePickerButton);
        final TextView textView = (TextView) findViewById(R.id.editFechaNacimiento);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                textView.setText(sdf.format(myCalendar.getTime()));
            }


        };

        pickDate.setOnClickListener((View v) -> {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year > mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (monthOfYear > mMonth && year == mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (dayOfMonth > mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear, mMonth, mDay);

                                textView.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();

        });
    }
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_register, menu);
            return super.onCreateOptionsMenu(menu);
        }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_favorite:
                String password = ((TextView) findViewById(R.id.editContraseña)).getText().toString();
                if(!password.isEmpty() && password.equals(((TextView) findViewById(R.id.editContraseñaConfirmar)).getText().toString()))
                    submitData();
                else
                    Toast.makeText(this,"Error contraseña no valida", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void submitData(){
        String username = ((TextView) findViewById(R.id.editUsername)).getText().toString();
        String email = ((TextView) findViewById(R.id.editEmail)).getText().toString();
        String password = ((TextView) findViewById(R.id.editContraseña)).getText().toString();
        String birthdayDate = ((TextView) findViewById(R.id.editContraseña)).getText().toString();
        mRequestQueue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";
        if(!(username.isEmpty() || email.isEmpty() || birthdayDate.isEmpty())) {
            // Request a string response from the provided URL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Toast.makeText(getApplicationContext(),"Creado correctamente", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"No Creado correctamente", Toast.LENGTH_LONG).show();
                            mTextView.setText("That didn't work!");
                        }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    getParams().put("username",username);
                    getParams().put("email",email);
                    getParams().put("password",password);
                    getParams().put("birthdayDate",birthdayDate);
                    return getParams();
                }
            };
// Add the request to the RequestQueue.
            stringRequest.setTag(TAG);
            mRequestQueue.add(stringRequest);
        }
    }
}