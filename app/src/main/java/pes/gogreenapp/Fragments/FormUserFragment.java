package pes.gogreenapp.Fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.R;

/**
 * Created by jalvarez on 4/7/17.
 */

public class FormUserFragment extends Fragment {
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
                EditText password = (EditText) findViewById(R.id.editContraseña);
                EditText password2 = (EditText) findViewById(R.id.editContraseñaConfirmar);
                EditText username = (EditText) findViewById(R.id.editUsername);
                EditText name = (EditText) findViewById(R.id.editName);
                EditText email = (EditText) findViewById(R.id.editEmail);
                EditText birthdayDate = (EditText) findViewById(R.id.editFechaNacimiento);
                boolean ok = true;
                int passwordok = 0;
                if(username.getText().toString().isEmpty()){
                    username.setError("Campo necesario");
                    ok = false;
                }

                if(name.getText().toString().isEmpty()){
                    name.setError("Campo necesario");
                    ok = false;
                }

                if(password.getText().toString().isEmpty()){
                    password.setError("Campo necesario");
                    ok = false;
                    passwordok++;
                }

                if(password2.getText().toString().isEmpty()){
                    password2.setError("Campo necesario");
                    ok = false;
                    passwordok++;
                }
                if(passwordok == 0 && !password.getText().toString().equals(password2.getText().toString())){
                    password.setError("La contraseña no es la misma");
                }

                if(email.getText().toString().isEmpty()){
                    email.setError("Campo necesario");
                    ok = false;
                }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Email no valido");
                    ok = false;
                }

                if(birthdayDate.getText().toString().isEmpty()){
                    birthdayDate.setError("Campo necesario");
                    ok = false;
                }

                if(ok) new PostMethod().execute(URLPetition, username.getText().toString(), name.getText().toString(), email.getText().toString(), password.getText().toString(), birthdayDate.getText().toString());

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Asynchronous Task for the petition POST to send a petition of register an User
     */
    private class PostMethod extends AsyncTask<String, Void, String> {


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
        protected String doInBackground(String... params) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            HashMap<String,String> impl = new HashMap<>();
            impl.put("username",params[1]);
            impl.put("name",params[2]);
            impl.put("email",params[3]);
            impl.put("password",params[4]);
            impl.put("birth_date",params[5]);

            String result = new HttpHandler().makeServiceCall(params[0],"POST" ,impl,"");
            Log.i(TAG, "Response from url: " + result);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("409")){
                Toast.makeText(getApplicationContext(),"Email o nombre de usuario repetido",Toast.LENGTH_LONG).show();
            }else if(s.equals("200")){
                Toast.makeText(getApplicationContext(),"Usuario creado",Toast.LENGTH_LONG).show();
                //TODO Change Location
            }else{
                Toast.makeText(getApplicationContext(),"Error, no se ha podido conectar, intentelo de nuevo más tarde",Toast.LENGTH_LONG).show();
            }
        }
    }
}