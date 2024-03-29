package com.android.pyp.property;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.pyp.R;
import com.android.pyp.utils.Utils;

import java.util.List;

/**
 * Created by devel-73 on 21/8/17.
 */

public class DetailsInnerFragment extends Fragment {

    private View mView;
    private Context mContext;

    private LinearLayout mapLinear, quickCallLinear, shareLinear;
    private List<PropertyData> propertyList;
    private PropertyData propertyData;
    private String contactNum = "";
    private LinearLayout bhkLinear, genderLinear, sqftLinear;
    private TextView title, description, priceCurrency, bhk, nationality, city, state, country, landmark, sqft, gender;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_property_details, null, false);
        initVariables();
        mContext = getActivity();
        getActivity().setTitle("");
        if (getArguments() != null) {
            propertyList = getArguments().getParcelableArrayList("property_list");
            propertyData = propertyList.get(0);
        }

        shareLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, propertyData.getShareLink() + "");
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

        mapLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsMapActivity.class);
                intent.putExtra("latitude", propertyData.getLatitude() + "");
                intent.putExtra("longitude", propertyData.getLongitude() + "");
                intent.putExtra("address", propertyData.getAddress() + "");
                startActivity(intent);
            }
        });

        quickCallLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Contact No", propertyData.getContactNum() + "es");
                if (!propertyData.getContactNum().equalsIgnoreCase("null") && !propertyData.getContactNum().equalsIgnoreCase(null)) {
                    contactNum = propertyData.getContactNum();
                    int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        callPhone();
                    }
                } else {
                    Utils.presentToast(mContext, "This user doesn't provide contact number", 1);
                }
            }
        });


        updateUI();
        return mView;
    }

    private void updateUI() {
        title.setText("Rent for " + propertyData.getTitle());
        getActivity().setTitle("Rent for " + propertyData.getTitle());
        if (propertyData.getTitle().trim().equalsIgnoreCase("bedspace")) {
            bhkLinear.setVisibility(View.GONE);
            genderLinear.setVisibility(View.GONE);
            sqftLinear.setVisibility(View.GONE);
        } else {
            bhkLinear.setVisibility(View.VISIBLE);
            genderLinear.setVisibility(View.VISIBLE);
            sqftLinear.setVisibility(View.VISIBLE);
        }
        bhk.setText(propertyData.getBhk().equalsIgnoreCase("null") ? "0" : propertyData.getBhk());
        gender.setText(propertyData.getGender().equalsIgnoreCase("null") ? "0" : propertyData.getGender());
        sqft.setText(propertyData.getSqft().equalsIgnoreCase("null") ? "0" : propertyData.getSqft());
        priceCurrency.setText(propertyData.getPrice() + " " + propertyData.getCurrency());
        nationality.setText(propertyData.getNationality().equalsIgnoreCase("null") ? "Not Specified" : propertyData.getNationality());
        city.setText(propertyData.getCity().equalsIgnoreCase("null") ? "Not Specified" : propertyData.getCity());
        state.setText(propertyData.getState().equalsIgnoreCase("null") ? "Not Specified" : propertyData.getState());
        country.setText(propertyData.getCountry().equalsIgnoreCase("null") ? "Not Specified" : propertyData.getCountry());
        landmark.setText(propertyData.getLandmark().equalsIgnoreCase("null") ? "Not Specified" : propertyData.getLandmark());
        description.setText(propertyData.getDescription().equalsIgnoreCase("null") ? "No Description Found" : propertyData.getDescription());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
            }
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + propertyData.getContactNum()));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }

    private void initVariables() {
        mapLinear = (LinearLayout) mView.findViewById(R.id.mapLinear);
        quickCallLinear = (LinearLayout) mView.findViewById(R.id.quickCallLinear);
        shareLinear = (LinearLayout) mView.findViewById(R.id.shareLinear);
        title = (TextView) mView.findViewById(R.id.title);
        description = (TextView) mView.findViewById(R.id.description);
        priceCurrency = (TextView) mView.findViewById(R.id.priceCurrency);
        bhk = (TextView) mView.findViewById(R.id.bhk);
        nationality = (TextView) mView.findViewById(R.id.nationality);
        city = (TextView) mView.findViewById(R.id.city);
        state = (TextView) mView.findViewById(R.id.state);
        country = (TextView) mView.findViewById(R.id.country);
        landmark = (TextView) mView.findViewById(R.id.landmark);
        sqft = (TextView) mView.findViewById(R.id.sqft);
        gender = (TextView) mView.findViewById(R.id.gender);
        sqft = (TextView) mView.findViewById(R.id.sqft);
        bhkLinear = (LinearLayout) mView.findViewById(R.id.bhkLinear);
        genderLinear = (LinearLayout) mView.findViewById(R.id.genderLinear);
        sqftLinear = (LinearLayout) mView.findViewById(R.id.sqftLinear);

    }

}
