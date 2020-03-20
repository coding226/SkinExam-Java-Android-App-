package com.skinexam.myapplication.activityeditprofile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.skinexam.myapplication.R;
import com.skinexam.myapplication.adapter.PageAdapter;
import com.skinexam.myapplication.app.MySingleton;
import com.skinexam.myapplication.model.ProfileDataModel;
import com.skinexam.myapplication.model.RegistrationModel;
import com.skinexam.myapplication.splah.Methods;
import com.skinexam.myapplication.utils.Communicator;
import com.skinexam.myapplication.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements Communicator, Step1.OnFragmentInteractionListener, Step2.OnFragmentInteractionListener, View.OnClickListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem tabStep_1;
    TabItem tabStep_2;
    TextView textView_back;
    ProfileDataModel profileRespo;
    ArrayList<ProfileDataModel> arrayList;
    Context current_act;

    RegistrationModel model;
    ProfileDataModel profileData;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_editprofile_);

        Chk_online();
        Methods.showProgress(this);

        tabLayout = findViewById(R.id.tabLayout);
        tabStep_1 = findViewById(R.id.step_1tab);
        tabStep_2 = findViewById(R.id.step_2tab);
        viewPager = findViewById(R.id.viewPager);
        textView_back = findViewById(R.id.edt_back);
        textView_back.setOnClickListener(this);


        model = new RegistrationModel();

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem((tab.getPosition()));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    private void Chk_online() {

        if (Methods.isOnline(this)) {
            callProfileData();
        } else {
            Toast.makeText(this, R.string.error_network_check, Toast.LENGTH_SHORT).show();
        }
    }

    public RegistrationModel getRegistrationModel() {
        return model;
    }

    public ProfileDataModel getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileDataModel model) {
        Methods.closeProgress();
        profileData = model;
    }

    public void callProfileData() {
        StringRequest requestProfile = new StringRequest(Request.Method.POST, Constants.STUDENT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                profileRespo = gson.fromJson(response, ProfileDataModel.class);
                if(profileRespo!=null){
                    setProfileData(profileRespo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences mSharedPreferences = getSharedPreferences(Constants.LOGIN_PREF, Context.MODE_PRIVATE);
                String PARAM_TOKEN = mSharedPreferences.getString(Constants.TOKEN, "");

                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.TOKEN, PARAM_TOKEN);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(requestProfile);
    }

    private Map<String, String> addHeader() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s",Constants.Auth_UserName, Constants.Auth_Password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }

    public String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edt_back:
                finish();
        }
    }

    @Override
    public void respond() {

        viewPager.setCurrentItem(1);
    }
}
