package pes.gogreenapp.Activities;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.R;

/**
 * Created by jalvarez on 4/7/17.
 */

public class FormUserActivity extends AppCompatActivity {
    private static Integer mYear, mMonth, mDay;
    private static final String TAG = "submitUserTag";
    private static final String URLPetition = "http://raichu.fib.upc.edu/api/users";
    RequestQueue mRequestQueue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.editFechaNacimiento).setOnKeyListener(null);
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
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                textView.setText(sdf.format(myCalendar.getTime()));
            }


        };

        pickDate.setOnClickListener((View v) -> {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR)-18;
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
                                String sDayOfMonth = String.format("%02d",dayOfMonth);
                                String sMonthOfYear = String.format("%02d",monthOfYear + 1);
                                textView.setText(sDayOfMonth + "-"
                                        + sMonthOfYear + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear,mMonth,mDay);
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
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
    /**
     * Override default onStop to apply a interruption of Petitions
     */
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
                String username = ((TextView) findViewById(R.id.editUsername)).getText().toString();
                String name = ((TextView) findViewById(R.id.editName)).getText().toString();
                String email = ((EditText) findViewById(R.id.editEmail)).getText().toString();
                String birthdayDate = ((TextView) findViewById(R.id.editFechaNacimiento)).getText().toString();
                if(username.isEmpty() || name.isEmpty() || email.isEmpty() || birthdayDate.isEmpty())
                    Toast.makeText(this, "Faltan datos por rellenar", Toast.LENGTH_LONG).show();
                else {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Toast.makeText(this, "Email no valido", Toast.LENGTH_LONG).show();
                    } else if (!password.isEmpty() && password.equals(((TextView) findViewById(R.id.editContraseñaConfirmar)).getText().toString())) {
                        new PostMethod().execute(URLPetition, username, name, email, password, birthdayDate);
                    } else
                        Toast.makeText(this, "Error contraseña no valida", Toast.LENGTH_LONG).show();
                    }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Asynchronous Task for the petition POST to send a petition of register an User
     */
    private class PostMethod extends AsyncTask<String, Void, Void> {
        @Override
        /**
         * Execute Asynchronous Task calling the url passed by parameter 0.
         *
         * @param params params[0] is the petition url,
         *               params[1] is the username,
         *               params[2] is the name of the user
         *               params[3] is the email
         *               params[4] is the password
         *               params[5] is the birthday date
         * @return void when finished
         */
        protected Void doInBackground(String... params) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            HashMap<String,String> impl = new HashMap<>();
            impl.put("username",params[1]);
            impl.put("name",params[2]);
            impl.put("email",params[3]);
            impl.put("password",params[4]);
            impl.put("birth_date",params[5]);
            String result = new HttpHandler().makeServiceCall(params[0],"POST" ,impl,"");
            Log.i(TAG, "Response from url: " + result);
            if(result.isEmpty()){
                Toast.makeText(getApplicationContext(),"No se ha conseguido enviar los datos",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Usuario creado",Toast.LENGTH_LONG).show();
                //TODO Change Location
            }
            return null;
        }
    }
}