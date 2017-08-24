package com.android.pyp.property;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pyp.R;
import com.android.pyp.utils.DataCallback;
import com.android.pyp.utils.PYPApplication;
import com.android.pyp.utils.SessionManager;
import com.android.pyp.utils.URLConstants;
import com.android.pyp.utils.Utils;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by devel-73 on 24/8/17.
 */

public class MyFavoriteProperty extends Fragment {

    private View mView;
    private Context mContext;
    private MyFavoritePropertyAdapter myFavoritePropertyAdapter;
    private List<PropertyData> propertyDataList;
    private RecyclerView mypropertyRecycler;
    private PYPApplication pypApplication;
    private String site_user_id = "";
    private SessionManager manager;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.activity_myproperty, null, false);
        initVariables();
        myFavoriteProperties();
        return mView;
    }

    private void initVariables() {
        propertyDataList = new ArrayList<>();
        manager = Utils.manager;
        preferences = Utils.preferences;
        site_user_id = preferences.getString(SessionManager.KEY_USERID, "1");
        pypApplication = new PYPApplication(mContext);
        mypropertyRecycler = (RecyclerView) mView.findViewById(R.id.mypropertyRecycler);
        mypropertyRecycler.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void myFavoriteProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("site_user_id", site_user_id);
        Log.e("Map is", map.toString());
        pypApplication.customStringRequest(URLConstants.urlMyFav, map, new DataCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.e("Result is", result.toString());
                propertyDataList = new ArrayList<>();

                try {
                    JSONArray array = new JSONArray(result.toString());
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            PropertyData data = new PropertyData();
                            data.setPropertyId(jsonObject.getString("p_id"));
                            data.setPrice(jsonObject.getString("price"));
                            data.setCurrency(jsonObject.getString("currency"));
                            data.setImageName(jsonObject.getString("image_name"));
                            data.setCity(jsonObject.getString("city"));
                            data.setState(jsonObject.getString("state"));
                            data.setCountry(jsonObject.getString("country"));
                            propertyDataList.add(data);
                        }
                        updateUI(propertyDataList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {
                Log.e("Error is", error.toString());
            }
        });
    }


    private void updateUI(final List<PropertyData> propertyDataList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myFavoritePropertyAdapter = new MyFavoritePropertyAdapter(mContext, propertyDataList, 1);
                mypropertyRecycler.setAdapter(myFavoritePropertyAdapter);
            }
        });
    }
}