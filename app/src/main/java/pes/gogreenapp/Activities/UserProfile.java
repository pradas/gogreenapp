package pes.gogreenapp.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pes.gogreenapp.Adapters.RewardsAdapter;
import pes.gogreenapp.Adapters.UserAdapter;
import pes.gogreenapp.Handlers.HttpHandler;
import pes.gogreenapp.Objects.Reward;
import pes.gogreenapp.Objects.User;
import pes.gogreenapp.R;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static pes.gogreenapp.R.id.user_creation_date;
import static pes.gogreenapp.R.id.user_email;
import static pes.gogreenapp.R.id.user_image;
import static pes.gogreenapp.R.id.user_name;
import static pes.gogreenapp.R.id.user_nickname;
import static pes.gogreenapp.R.id.user_points;

public class UserProfile extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //no tiene que ser user adapter, sino dealadapter
    //UserAdapter adapter;
    User testUser;

    Boolean SoyUsuarioPropio = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        TextView userName = (TextView) findViewById(user_name);
        TextView userNickName = (TextView) findViewById(user_nickname);
        TextView userPoints = (TextView) findViewById(user_points);
        TextView userEmail = (TextView) findViewById(user_email);
        TextView userCreationDate = (TextView) findViewById(user_creation_date);
        ImageView userImage = (ImageView) findViewById(user_image);

        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
        initializeUser();
        new GetInfoUser().execute();
        //a la espera de tener la petición de la API hecha
        //new GetUserImage().execute("http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

        userName.setText(testUser.getName());
        userNickName.setText(testUser.getUsername());
        userPoints.setText(String.valueOf(testUser.getTotalPoints()));
        userCreationDate.setText((String) sourceFormat.format(testUser.getCreationDate()));
        //userCreationDate.setText(date);
        userEmail.setText(testUser.getEmail());
        //userImage.setImageResource();



        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void initializeUser(){
        testUser = new User ("realPepeViyuela", "Pepe Viyuela", "viyuela@gmail.com", "12-10-1983", "http://ep01.epimg.net/verne/imagenes/2015/09/28/articulo/1443439253_452315_1443439404_sumario_normal.jpg");

    }


    private class GetInfoUser extends AsyncTask<String, Void, Void> {
        Bitmap b_image_user;

        private Bitmap getRemoteImage(final URL aURL) {
            try {
                final URLConnection conn = aURL.openConnection();
                conn.connect();
                final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                final Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            } catch (IOException e) {}
            return null;
        }


        @Override
        protected Void doInBackground(String... urls) {
            //HttpHandler httpHandler = new HttpHandler();
            //String response = httpHandler.makeServiceCall(urls[0]);
            //Log.i(TAG, "Response from url: " + response);

            URL imageUrl = null;
            try {
                imageUrl = new URL(testUser.getUserUrlImage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            b_image_user = this.getRemoteImage(imageUrl);
            //testUser.setUserImage();

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            ImageView userImage = (ImageView) findViewById(user_image);
            userImage.setImageBitmap(b_image_user);
        }

    }

    /*
    private class GetUserInfo extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(urls[0]);
            Log.i(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    JSONObject aux = new JSONObject(response);
                    JSONArray jsonArray = aux.getJSONArray("rewards");
                    System.out.println(jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Date d = df.parse((String) jsonObject.get("end_date"));
                        rewards.add(new Reward((Integer) jsonObject.get("id"),
                                (String) jsonObject.get("title"), (Integer) jsonObject.get("points"),
                                d, (String) jsonObject.get("category")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /*
        @Override
        protected void onPostExecute(Void result) {
            adapter = new RewardsAdapter(rewards);
            recyclerView.setAdapter(adapter);
        }

    }*/


    /*
    private void onItemsLoadComplete(){
        adapter.setUserInfo(testUser);
        adapter.notifyDataSetChanged();
    }*/
}
