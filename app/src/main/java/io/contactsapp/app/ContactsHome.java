package io.contactsapp.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.contactsapp.app.adapters.VolleyHomeAdapter;
import io.contactsapp.app.model.HomeContactModel;
import io.contactsapp.app.model.Phone;


public class ContactsHome extends ActionBarActivity {

    private ListView lstView;
    private RequestQueue requestQueue;
    private VolleyHomeAdapter va;
    private ArrayList<HomeContactModel> contactList;
    private ProgressDialog pd;
    private EditText inputSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_home);
        requestQueue =  Volley.newRequestQueue(this);
        lstView = (ListView) findViewById(R.id.contactsList);
        inputSearch = (EditText)findViewById(R.id.inputSearch);
        contactList = new ArrayList<HomeContactModel>();
        va = new VolleyHomeAdapter(this,R.layout.contact_row,contactList);
        lstView.setAdapter(va);
        lstView.setTextFilterEnabled(true);
        String url = "https://solstice.applauncher.com/external/contacts.json";
        pd = ProgressDialog.show(this,"Loading","Gathering contacts...");
        JsonArrayRequest jArray = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        convertToContact(response);
                        va.notifyDataSetChanged();
                        pd.dismiss();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),getString(R.string.JSONError),Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(jArray);

        lstView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                HomeContactModel model = va.getItem(position);
                Intent intent = new Intent(getApplicationContext(),ContactProfile.class);
                intent.putExtra("home_model",model);
                startActivity(intent);

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
               ContactsHome.this.va.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private final void convertToContact(JSONArray items){
      try {
          for (int i = 0; i < items.length(); i++) {
              JSONObject obj = items.getJSONObject(i);
              HomeContactModel cm = new HomeContactModel();
              cm.setName(obj.getString("name"));
              cm.setEmployeeId(obj.getInt("employeeId"));
              cm.setBirthdate(obj.getString("birthdate"));
              cm.setCompany(obj.getString("company"));
              cm.setSmallImageURL(obj.getString("smallImageURL"));
              cm.setDetailsURL(obj.getString("detailsURL"));
              JSONObject phoneJSON = obj.getJSONObject("phone");
              Phone phone = new Phone();
              phone.setHome(phoneJSON.getString("home"));
              phone.setWork(phoneJSON.getString("work"));
              phone.setMobile(phoneJSON.getString("mobile"));
              cm.setPhone(phone);
              contactList.add(cm);
          }
      }catch(JSONException e){
         e.printStackTrace();
      }

    }



}
