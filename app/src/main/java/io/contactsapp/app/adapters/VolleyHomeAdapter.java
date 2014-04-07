package io.contactsapp.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import io.contactsapp.app.R;
import io.contactsapp.app.model.HomeContactModel;

/**
 * Created by alexthornburg on 4/5/14.
 */
public class VolleyHomeAdapter extends ArrayAdapter {

    private ArrayList<HomeContactModel> contactList;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private int resource;
    private Context context;
    ArrayList<HomeContactModel> storedContacts;

    public VolleyHomeAdapter(Context context, int resource, ArrayList<HomeContactModel> contactList) {
        super(context, resource, contactList);
        this.context = context;
        this.contactList = contactList;
        this.resource = resource;
        this.storedContacts = contactList;
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public HomeContactModel getItem(int i) {
        return contactList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return contactList.get(i).getEmployeeId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ContactHolder contactHolder;
        if (view == null) {
            LayoutInflater lf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contactHolder = new ContactHolder();
            view = lf.inflate(resource, null);
            contactHolder.avatarURL = (NetworkImageView) view.findViewById(R.id.contact_avatar);
            contactHolder.phoneNumber = (TextView) view.findViewById(R.id.phone_number);
            contactHolder.name = (TextView) view.findViewById((R.id.name));
            view.setTag(contactHolder);
        } else {
            contactHolder = (ContactHolder) view.getTag();
        }
        HomeContactModel hm = contactList.get(i);
        String url = hm.getSmallImageURL();
        contactHolder.avatarURL.setImageUrl(url, mImageLoader);
        contactHolder.phoneNumber.setText(hm.getPhone().getMobile());
        contactHolder.name.setText(hm.getName());
        return view;
    }


    private class ContactHolder {
        public NetworkImageView avatarURL;
        public TextView phoneNumber;
        public TextView name;
    }

    @Override
    public Filter getFilter() {
                return new Filter() {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void publishResults(CharSequence constraint,
                                                  FilterResults results) {

                        if (results.count == 0)
                            notifyDataSetInvalidated();
                        else {
                            contactList = (ArrayList<HomeContactModel>) results.values;
                            VolleyHomeAdapter.this.notifyDataSetChanged();
                        }

                    }

                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {

                        FilterResults results = new FilterResults();
                        results.values = storedContacts;
                        results.count = storedContacts.size();
                        String filterText = constraint.toString().toUpperCase();
                        if(filterText == null || filterText.length() == 0){
                            results.values = storedContacts;
                            results.count = storedContacts.size();
                        }else {
                            ArrayList<HomeContactModel> listFiltered = new ArrayList<HomeContactModel>();
                            for (HomeContactModel model : contactList) {
                                if (model.getName()
                                        .toUpperCase()
                                        .startsWith(constraint.toString().toUpperCase())) {
                                    listFiltered.add(model);
                                }
                            }
                            results.count = listFiltered.size();
                            results.values = listFiltered;
                        }
                        return results;
                    }
                };
            }
        }
