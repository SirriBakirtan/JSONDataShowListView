package com.bakirtansirri.jsontutorial;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {

    private String TAG =MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView listView;

    private static String url="example-json-format-link";

    ArrayList<HashMap<String,String>> personList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personList=new ArrayList<>();

        listView=findViewById(R.id.listPer);

        new GetContacts().execute();

    }

    private class GetContacts extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog=new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler hp = new HttpHandler();

            String jsonStr = hp.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray contacts = new JSONArray(jsonStr);

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        int userId = (int) c.get("userId");
                        String imageTn = c.getString("imageTn");
                        String title = c.getString("title");
                        String nameSurname = c.getString("nameSurname");
                        //String gsm = c.getString("gsm"); Check Gsm if it is null,returns "Unknown".
                        String email = c.getString("email");
                        String office = c.getString("office");


                        HashMap<String, String> contact = new HashMap<>();

                        String gsm;

                        if(c.isNull("gsm")){
                             gsm="Unknown";
                        }
                        else{
                            gsm=c.getString("gsm");
                        }


                        contact.put("userId", String.valueOf(userId));
                        contact.put("imageTn", imageTn);
                        contact.put("title", title);
                        contact.put("nameSurname", nameSurname);
                        contact.put("gsm",gsm);
                        contact.put("email",email);
                        contact.put("office",office);


                        personList.add(contact);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, personList,
                    R.layout.list_item, new String[]{"nameSurname", "email",
                    "gsm"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            listView.setAdapter(adapter);



        }

    }
}
