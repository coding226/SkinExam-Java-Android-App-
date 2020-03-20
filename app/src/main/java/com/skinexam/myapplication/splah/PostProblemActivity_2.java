package com.skinexam.myapplication.splah;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.skinexam.myapplication.DashboardActivity;
import com.skinexam.myapplication.R;
import com.skinexam.myapplication.adapter.CustomAdapter;
import com.skinexam.myapplication.adapter.ViewPagerAdapter;
import com.skinexam.myapplication.app.BaseActivity;
import com.skinexam.myapplication.app.MySingleton;
import com.skinexam.myapplication.model.CreateCaseResultModel;
import com.skinexam.myapplication.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PostProblemActivity_2 extends AppCompatActivity implements View.OnClickListener {

    String title, des1, des2, des3, concern, spinage, spinhealth, spinbody, spinused, spinhealth_id, spinbody_id, spinused_id, itchy_id = "0", changeColor_id= "0", bumper_id= "0";
    TextView pre_title_con, age_prev, helth_prev, state_prev_content, bodypart_prev_content, prior_prev_content, pre_concern_con, newcase_pref ;
    String image_1="", image_2="", image_3="";
    TextView submit_btn;
    private List<String> arrayImagesList;

    BaseActivity baseActivity;

    ArrayList<String> images = new ArrayList<String>();

    CreateCaseResultModel createCaseResultModel;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_problem_2);

        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                title = null;
            }else {
                title = extras.getString("TITLE");
                des1 = extras.getString("EDITDEST1");
                des2 = extras.getString("EDITDEST2");
                des3 = extras.getString("EDITDEST3");
                concern = extras.getString("CONCERN");

                spinage = extras.getString("SPINAGE");
                spinhealth = extras.getString("SPINHEALTH");
                spinbody = extras.getString("SPINBODY");
                spinused = extras.getString("SPINUSED");

                spinhealth_id = extras.getString("SPINHEALTH_ID");
                spinbody_id = extras.getString("SPINBODY_ID");
                spinused_id = extras.getString("SPINHEALTH_ID");

                image_1 = extras.getString("image_1");
                image_2 = extras.getString("image_2");
                image_3 = extras.getString("image_3");

            }
        }else {
            title = (String) savedInstanceState.getSerializable("TITLE");
        }

        if(image_1.length() > 0 ){
            images.add(image_1);
        }
        if (image_2.length() > 0){
            images.add(image_2);
        }
        if ((image_3.length() > 0)){
            images.add(image_3);
        }



        newcase_pref = (TextView) findViewById(R.id.newcase_pref);
        newcase_pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        arrayImagesList = new ArrayList<String>();
        arrayImagesList.add(image_1);
        arrayImagesList.add(image_2);
        arrayImagesList.add(image_3);

        baseActivity = new BaseActivity();

        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, (ArrayList<String>) arrayImagesList);
        viewPager.setAdapter(adapter);


        pre_title_con = (TextView) findViewById(R.id.pre_title_con);
        pre_concern_con = (TextView) findViewById(R.id.pre_concern_con);
        age_prev = (TextView) findViewById(R.id.age_prev);
        helth_prev = (TextView) findViewById(R.id.health_prev);

        bodypart_prev_content = (TextView) findViewById(R.id.bodypart_prev_content) ;
        prior_prev_content = (TextView) findViewById(R.id.prior_prev_content);

        state_prev_content = (TextView) findViewById(R.id.state_prev_content);

        submit_btn = (TextView) findViewById(R.id.caseSubmit);
        submit_btn.setOnClickListener(this);

        pre_title_con.setText(title);
        pre_concern_con.setText(concern);
        age_prev.setText("Age : " + spinage);
        helth_prev.setText("Health : " + spinhealth);;
        bodypart_prev_content.setText(spinbody);
        prior_prev_content.setText(spinused);

//        for (int i = 0; i < CustomAdapter.modelArrayList.size(); i++){
//            if (CustomAdapter.modelArrayList.get(i).getSelected()){
//                state_prev_content.setText(state_prev_content.getText() + "\t" + CustomAdapter.modelArrayList.get(i).getState());
//                Log.e("statelist_count", String.valueOf(CustomAdapter.modelArrayList.size()));
//                Log.e("statelist",  CustomAdapter.modelArrayList.get(i).getState());
//            }
//        }

        for (int i = 0; i < CustomAdapter.modelArrayList.size(); i++){
            if (CustomAdapter.modelArrayList.get(i).getSelected()){
                state_prev_content.setText(state_prev_content.getText() + "\n" + CustomAdapter.modelArrayList.get(i).getState());
//                Log.e("itchy", CustomAdapter.modelArrayList.get(0).getState());
//                Log.e("change_ color", CustomAdapter.modelArrayList.get(1).getState());
//                Log.e("Bump", CustomAdapter.modelArrayList.get(2).getState());
                String state =   CustomAdapter.modelArrayList.get(i).getState();
                if (state.compareTo("Itchy") == 0){
                    itchy_id = "1";
                }
                if (state.compareTo("I can fill it as a bump") == 0){
                    bumper_id = "1";
                }
                if (state.compareTo("Changing color over time") == 0){
                    changeColor_id = "1";
                }
            }
        }
        Log.e("itchy", itchy_id);
        Log.e("bumper", bumper_id);
        Log.e("changeColor", changeColor_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.caseSubmit:
                Methods.showProgress(PostProblemActivity_2.this);
                Create_case();
         }
    }

    public void Create_case() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, Constants.CREATECASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        createCaseResultModel = gson.fromJson(response, CreateCaseResultModel.class);
//                        Log.e("image_re", response.toString());

                        Toast.makeText(PostProblemActivity_2.this, createCaseResultModel.getImage_msg(), Toast.LENGTH_SHORT).show();

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(PostProblemActivity_2.this, "Joker", Toast.LENGTH_SHORT).show();
//                        onBackPressed();
                        Intent toDash = new Intent(PostProblemActivity_2.this, DashboardActivity.class);
                        startActivity(toDash);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), PostProblemActivity_2.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.TOKEN, getString(Constants.TOKEN));
                params.put(Constants.TITLE, title);
                params.put(Constants.SUMMERY, "");
                params.put(Constants.AGE_DATA, spinage);
                params.put(Constants.HEALTH_STATUS_ID, spinhealth_id);
                params.put(Constants.USERBODYPART, spinbody_id);
                params.put(Constants.PRIVTREAT, spinused_id);
                params.put(Constants.DES_1, des1);
                params.put(Constants.DES_2, des2);
                params.put(Constants.DES_3, des3);
                params.put(Constants.ITCHY, itchy_id);
                params.put(Constants.CHANGECOLOR, changeColor_id);
                params.put(Constants.FEELBUMP, bumper_id);
                if (!TextUtils.isEmpty((image_1.substring(image_1.lastIndexOf(".")+1, image_1.length())))){
                    params.put(Constants.IMAGE1, baseActivity.encodeImgTOBase64(image_1, (image_1.substring(image_1.lastIndexOf(".")+1))));

                }
                if (!TextUtils.isEmpty((image_2.substring(image_2.lastIndexOf(".")+1, image_2.length())))){
                    params.put(Constants.IMAGE2, baseActivity.encodeImgTOBase64(image_2, (image_2.substring(image_2.lastIndexOf(".")+1))));
                }
                if (!TextUtils.isEmpty((image_3.substring(image_3.lastIndexOf(".")+1, image_3.length())))){
                    params.put(Constants.IMAGE3, baseActivity.encodeImgTOBase64(image_3, (image_3.substring(image_3.lastIndexOf(".")+1))));
                }


                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srStatus);
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
}
