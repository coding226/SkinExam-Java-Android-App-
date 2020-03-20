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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.skinexam.myapplication.adapter.CountryAdapter;
import com.skinexam.myapplication.app.MySingleton;
import com.skinexam.myapplication.model.Country;
import com.skinexam.myapplication.model.CountryResponseModel;
import com.skinexam.myapplication.model.ProfileDataModel;
import com.skinexam.myapplication.splah.Methods;
import com.skinexam.myapplication.utils.Communicator;
import com.skinexam.myapplication.utils.Constants;
import com.skinexam.myapplication.utils.Validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step1 extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public EditText edtFirstName, edtLastName, edtAddress, edtCity, edtState, edtZipCode;
    TextView selsecte_country;
    public Spinner spCountry;
    public String countryResponse, CountryCode;
    Communicator comm;
    String spCountry_id;

    ProfileDataModel profileRespo;

    ProfileDataModel profileDataModel;

    private String mParam1;
    private String mParam2;
    String param;

    private OnFragmentInteractionListener mListener;

    public Step1() {
    }

    public static Step1 newInstance(String param1) {
        Step1 fragment = new Step1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Toast.makeText(getActivity(), mParam1, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step1, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        comm = (Communicator) getActivity();
        edtFirstName = (EditText) view.findViewById(R.id.edtFirstName);
        edtLastName = (EditText) view.findViewById(R.id.edtLastName);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        edtCity = (EditText) view.findViewById(R.id.edtCity);
        edtState = (EditText) view.findViewById(R.id.edtState);
        edtZipCode = (EditText) view.findViewById(R.id.edtZipCode);
        spCountry = (Spinner) view.findViewById(R.id.spCountry);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
//        selsecte_country = (TextView) view.findViewById(R.id.selsecte_country);
        spCountry.setOnItemSelectedListener(this);
        callCountryService();

        spCountry.setOnItemSelectedListener(this);
        Methods.showProgress(getActivity());
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if(checkValidation()){

            RegisterFirstPageEntry();
            comm.respond();
        }
    }

    private void RegisterFirstPageEntry() {
        ((EditProfileActivity) getActivity()).getRegistrationModel().setfName(edtFirstName.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setlName(edtLastName.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setAddress(edtAddress.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setCity(edtCity.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setState(edtState.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setZipCode(edtZipCode.getText().toString());
        ((EditProfileActivity) getActivity()).getRegistrationModel().setCountryId(spCountry_id);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setProfileData(){
        edtFirstName.setText(((EditProfileActivity) getActivity()).getProfileData().getfName());
        edtLastName.setText(((EditProfileActivity) getActivity()).getProfileData().getlName());
        edtAddress.setText(((EditProfileActivity) getActivity()).getProfileData().getAddress());
        edtCity.setText(((EditProfileActivity) getActivity()).getProfileData().getCity());
        edtState.setText(((EditProfileActivity) getActivity()).getProfileData().getState());
        edtZipCode.setText(((EditProfileActivity) getActivity()).getProfileData().getZipCode());
        Methods.closeProgress();
//        selsecte_country.setText(((EditProfileActivity) getActivity()).getProfileData().getCountryName());
    }
    private void callCountryService() {
        StringRequest requestCountry = new StringRequest(Request.Method.GET, Constants.CITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setProfileData();

                Gson gson = new Gson();

                List<Country> countries = new ArrayList<Country>();

                if(countries!=null){

                    CountryResponseModel objCountry = gson.fromJson(response, CountryResponseModel.class);
                    Log.e("objCountry", response.toString());
                    countries.addAll(objCountry.getCountry());

                    spCountry.setAdapter(new CountryAdapter(getActivity(), objCountry.getCountry()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.showAlertDialog(getString(R.string.error_network_check), getActivity());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.TOKEN, getString(Constants.TOKEN));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return addHeader();
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(requestCountry);
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
        if (!Validation.hasText(edtFirstName,"Please enter your First name")) ret = false;
        if (!Validation.hasText(edtLastName,"Please enter your Last name")) ret = false;
        if (!Validation.hasText(edtAddress,"Please provide your Address")) ret = false;
        if (!Validation.hasText(edtCity,"Please provide City")) ret = false;
        if (!Validation.hasText(edtState,"Please provide State")) ret = false;
        if (!Validation.hasText(edtZipCode,"Please enter Zipcode")) ret = false;
        return ret;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.equals(spCountry)){
            spCountry_id = ((Country) parent.getItemAtPosition(position)).getId();
            Log.e("Country_id", spCountry_id);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
