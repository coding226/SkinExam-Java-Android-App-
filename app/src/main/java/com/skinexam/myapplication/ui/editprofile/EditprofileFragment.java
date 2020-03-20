package com.skinexam.myapplication.ui.editprofile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.skinexam.myapplication.R;
import com.skinexam.myapplication.app.MySingleton;
import com.skinexam.myapplication.model.ProfileDataModel;
import com.skinexam.myapplication.splah.Methods;
import com.skinexam.myapplication.activityeditprofile.Step1;
import com.skinexam.myapplication.activityeditprofile.Step2;
import com.skinexam.myapplication.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class EditprofileFragment extends Fragment implements Step1.OnFragmentInteractionListener, Step2.OnFragmentInteractionListener, View.OnClickListener {

    public EditprofileViewModel sendViewModel;
    public PagerAdapter pagerAdapter;
    ViewPager viewPager;
    private ProfileDataModel profileData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        sendViewModel = ViewModelProviders.of(this).get(EditprofileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_editprofile, container, false);
//        final TextView textView = root.findViewById(R.id.text_profile);
//        sendViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("STEP 1"));
        tabLayout.addTab(tabLayout.newTab().setText("STEP 2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = root.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);


        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//
        return root;
//        return inflater.inflate(R.layout.fragment_editprofile, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onResume(){
        super.onResume();
        Chk_online();
    }

    private void Chk_online() {

        if (Methods.isOnline(getActivity())) {
            callPendData();
        } else {
            Toast.makeText(getActivity(), R.string.error_network_check, Toast.LENGTH_SHORT).show();
        }
    }

    public ProfileDataModel getProfileData() {
        return profileData;
    }

    private void setProfileData(ProfileDataModel model){
        profileData = model;
    }

    private void callPendData() {
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.STUDENT_LOGIN, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            public void onResponse(String response) {
                Gson gson = new Gson();
                Methods.closeProgress();

                Log.e("Response_profile****",response);

                ProfileDataModel profileRespo = gson.fromJson(response, ProfileDataModel.class);
                if (profileRespo != null){
                    setProfileData(profileRespo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.showAlertDialog("Response_profile **error**"+getString(R.string.error_network_check), getActivity());
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        Toast.makeText(getActivity(), "Oops. Timeout error!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            protected Map<String, String> getParams() {
                SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN_PREF, Context.MODE_PRIVATE);
                String  PARAM_USERNAME_F =  mSharedPreferences.getString(Constants.PARAM_USERNAME, "");
                String PARAM_PASSWORD_F = mSharedPreferences.getString(Constants.PARAM_PASSWORD, "");

                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.PARAM_USERNAME, PARAM_USERNAME_F);
                params.put(Constants.PARAM_PASSWORD, PARAM_PASSWORD_F);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return addHeader();
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(50000, 1, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(getActivity()).addToRequestQueue(sr);
    }

    private Map<String, String> addHeader() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s",Constants.Auth_UserName, Constants.Auth_Password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        Log.e("Header", auth);
        return params;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {

    }
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        pagerAdapter = new PagerAdapter(getChildFragmentManager());
//        viewPager = view.findViewById(R.id.pager);
//        viewPager.setAdapter(pagerAdapter);
//    }
}
