package io.contactsapp.app;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import io.contactsapp.app.model.Address;
import io.contactsapp.app.model.HomeContactModel;
import io.contactsapp.app.model.ProfileContactModel;

/**
 * Created by alexthornburg on 4/5/14.
 */
public class ContactProfile extends ActionBarActivity {
    HomeContactModel person;
    TextView name;
    TextView phoneHome;
    TextView phoneWork;
    TextView phoneMobile;
    TextView birthday;
    TextView company;
    TextView email;
    TextView street;
    TextView cityState;
    ImageView favorite;
    private ProgressDialog pd;
    private RequestQueue requestQueue;
    ProfileContactModel prof;
    private ImageLoader mImageLoader;
    NetworkImageView largeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_profile);

        name = (TextView)findViewById(R.id.name_large);
        street = (TextView)findViewById(R.id.street_big);
        cityState = (TextView)findViewById(R.id.city_state_big);
        phoneHome = (TextView)findViewById(R.id.phone_home_big);
        phoneWork = (TextView)findViewById(R.id.phone_work_big);
        phoneMobile = (TextView)findViewById(R.id.phone_mobile_big);
        birthday = (TextView)findViewById(R.id.birthday);
        company = (TextView)findViewById(R.id.company);
        email = (TextView)findViewById(R.id.email);
        favorite = (ImageView)findViewById(R.id.favorite);
        largeImage = (NetworkImageView)findViewById(R.id.contact_avatar_large);

        person = (HomeContactModel) this.getIntent().getSerializableExtra("home_model");

        name.setText(person.getName());
        phoneHome.setText(person.getPhone().getHome());
        phoneWork.setText(person.getPhone().getWork());
        phoneMobile.setText(person.getPhone().getWork());
        birthday.setText(person.getBirthdate());
        company.setText(person.getCompany());

        requestQueue =  Volley.newRequestQueue(this);
        String url = person.getDetailsURL();
        pd = ProgressDialog.show(this, "Loading", "Getting additional info...");
        JsonObjectRequest jArray = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        prof = convertToProfile(response);
                        largeImage.setImageUrl(prof.getLargeImageURL(),mImageLoader);
                        street.setText(prof.getAddress().getStreet());
                        cityState.setText(prof.getAddress().getCity()+", "+prof.getAddress().getState());
                        email.setText(prof.getEmail());
                        if(prof.isFavorite()){
                            favorite.setVisibility(View.VISIBLE);
                        }else{
                            favorite.setVisibility(View.INVISIBLE);
                        }
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        requestQueue.add(jArray);
        mImageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

    }

    private final ProfileContactModel convertToProfile(JSONObject obj) {
        try {
            ProfileContactModel cm = new ProfileContactModel();
            cm.setEmail(obj.getString("email"));
            cm.setEmployeeId(obj.getInt("employeeId"));
            if(cm.getEmployeeId()==12){
                if(obj.getInt("favorite")==0){
                    cm.setFavorite(false);
                }else{
                    cm.setFavorite(true);
                }
            }else{
                cm.setFavorite(obj.getBoolean("favorite"));
            }
            cm.setLargeImageURL(obj.getString("largeImageURL"));
            cm.setWebsite(obj.getString("website"));
            JSONObject addressJSON = obj.getJSONObject("address");
            Address address = new Address();
            address.setCity(addressJSON.getString("city"));
            address.setCountry(addressJSON.getString("country"));
            address.setLatitude(addressJSON.getDouble("latitude"));
            address.setLongitude(addressJSON.getDouble("latitude"));
            address.setZip(addressJSON.getString("zip"));
            address.setStreet(addressJSON.getString("street"));
            address.setState(addressJSON.getString("state"));
            cm.setAddress(address);
            return cm;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
