package com.skinexam.myapplication.activityeditprofile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.skinexam.myapplication.R;
import com.skinexam.myapplication.app.MySingleton;
import com.skinexam.myapplication.model.ProfileDataModel;
import com.skinexam.myapplication.model.RegisterResponseModel;
import com.skinexam.myapplication.splah.Methods;
import com.skinexam.myapplication.utils.Constants;
import com.skinexam.myapplication.utils.Validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step2 extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText edtHomeTel, edtCellPhone, edtEmail;
    Button btnSave;



    ProfileDataModel profileRespo;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Step2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step2.
     */
    // TODO: Rename and change types and number of parameters
    public static Step2 newInstance(String param1, String param2) {
        Step2 fragment = new Step2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step2, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        editProfileActivity.getProfileData().getStudent();
        callProfileData();
        edtHomeTel = (EditText) view.findViewById(R.id.edtHomeTel);
        edtCellPhone = (EditText) view.findViewById(R.id.edtCellPhone);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    public void setProfileData(ProfileDataModel model){
        profileRespo = model;
        edtHomeTel.setText(((EditProfileActivity) getActivity()).getProfileData().getHomeTelNo());
        edtCellPhone.setText(((EditProfileActivity) getActivity()).getProfileData().getMobileNo());
        edtEmail.setText(((EditProfileActivity) getActivity()).getProfileData().getEmail());
    }


    public void callProfileData() {
        Methods.showProgress(getActivity());
        StringRequest requestProfile = new StringRequest(Request.Method.POST, Constants.STUDENT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                profileRespo = gson.fromJson(response, ProfileDataModel.class);
                if(profileRespo!=null){
                    Methods.closeProgress();
                    setProfileData(profileRespo);
//                    Log.e("-----Step2****",response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Methods.closeProgress();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.PARAM_USERNAME, getString(Constants.PARAM_USERNAME));
                params.put(Constants.PARAM_PASSWORD, getString(Constants.PARAM_PASSWORD));
                params.put(Constants.USER_ID, getString("user_id"));
                params.put(Constants.SESSION_ID, getString("session_id"));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return addHeader();
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(requestProfile);
    }

    private Map<String, String> addHeader() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s",Constants.Auth_UserName, Constants.Auth_Password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        Log.e("Header", auth);
        return params;
    }

    public String getString(String key) {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN_PREF, Context.MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isValidEmail(edtEmail.getText().toString())) ret = false;
        if (!Validation.isPhoneNumber(edtCellPhone, false)) ret = false;
        return ret;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
//                Toast.makeText(getActivity(), "Save btn click", Toast.LENGTH_SHORT).show();
                if (checkValidation()) {
                    Methods.showProgress(getActivity());
                    RegisterSecondPageEntry();
                    Methods.showProgress(getActivity());
//                    Log.e("country id 2", ((EditProfileActivity) getActivity()).getRegistrationModel().getCountryId());
                } else {
                    Toast.makeText(getActivity(), "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void RegisterSecondPageEntry() {
        ((EditProfileActivity) getActivity()).getRegistrationModel().setHomeTelNo(edtHomeTel.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setMobileNo(edtCellPhone.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setEmail(edtEmail.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setSubs("0");
        submitForm();
//        Toast.makeText(getActivity(), "wowowowowo", Toast.LENGTH_SHORT).show();
    }

    public void submitForm() {
        StringRequest srReg = new StringRequest(Request.Method.POST, Constants.PROFILE_EDIT,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Methods.closeProgress();
                Log.e("response edit", response);
                Log.e("country id 2", ((EditProfileActivity) getActivity()).getRegistrationModel().getCountryId());
                Gson gson = new Gson();
                RegisterResponseModel objRes = gson.fromJson(response, RegisterResponseModel.class);
                if(objRes!=null){
                    if(objRes.getStatus()) {
                        //    startActivity(new Intent(getActivity(), HomeActivity.class));
//                        getActivity().finish();
                        Toast.makeText(getActivity(), objRes.getMsg(), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), objRes.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.closeProgress();
                        Methods.showAlertDialog(getString(R.string.error_network_check), getActivity());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.TOKEN, getString(Constants.TOKEN));
                params.put(Constants.FIRST_NAME, ((EditProfileActivity) getActivity()).getRegistrationModel().getfName());
                params.put(Constants.LAST_NAME, ((EditProfileActivity) getActivity()).getRegistrationModel().getlName());
                params.put(Constants.ADDRESS, ((EditProfileActivity) getActivity()).getRegistrationModel().getAddress());
                params.put(Constants.CITY_NAME, ((EditProfileActivity) getActivity()).getRegistrationModel().getCity());
                params.put(Constants.STATE, ((EditProfileActivity) getActivity()).getRegistrationModel().getState());
                params.put(Constants.ZIP_CODE, ((EditProfileActivity) getActivity()).getRegistrationModel().getZipCode());
                params.put(Constants.COUNTRY_ID, ((EditProfileActivity) getActivity()).getRegistrationModel().getCountryId());

//                params.put(Constants.COUNTRY_ID, "20");
                params.put(Constants.MOB_NO, ((EditProfileActivity) getActivity()).getRegistrationModel().getMobileNo());
                params.put(Constants.TEL_NO, ((EditProfileActivity) getActivity()).getRegistrationModel().getHomeTelNo());
                params.put(Constants.EMAIL, ((EditProfileActivity) getActivity()).getRegistrationModel().getEmail());
                return checkParams(params);
            }
            @Override
            public Map<String, String> getHeaders() {
                return addHeader();
            }
            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs =  it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(srReg);
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
