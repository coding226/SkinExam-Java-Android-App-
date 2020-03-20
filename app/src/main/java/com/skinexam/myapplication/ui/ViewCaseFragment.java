package com.skinexam.myapplication.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.skinexam.myapplication.R;
import com.skinexam.myapplication.adapter.CustomCasePagerAdapter;
import com.skinexam.myapplication.app.MySingleton;
import com.skinexam.myapplication.model.TicketResponseModel;
import com.skinexam.myapplication.splah.Methods;
import com.skinexam.myapplication.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;


public class ViewCaseFragment extends Fragment {

    private TextView txtTitle, txtSummery, txtAge, txtHealth, txtState, txtDocReply;
    private ViewPager recentViewpager;
    private List<String> arrayImagesList;
    private static int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_case, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtSummery = (TextView) view.findViewById(R.id.txtSummery);
        recentViewpager = (ViewPager) view.findViewById(R.id.recentViewpager);
        txtAge = (TextView) view.findViewById(R.id.txtAge);
        txtHealth = (TextView) view.findViewById(R.id.txtHealth);
        txtState = (TextView) view.findViewById(R.id.txtState);
        txtDocReply = (TextView) view.findViewById(R.id.txtDocReply);
        ticketResponse(view);
        Methods.showProgress(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void ticketResponse(final View view) {
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.TICKET_JSON,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Methods.closeProgress();
                        if(getActivity()!=null && isAdded()){
                            TicketResponseModel ticketResponseModel = gson.fromJson(response, TicketResponseModel.class);
                            if (ticketResponseModel.getStatus()) {
                                txtTitle.setText("Title : " + ticketResponseModel.getTicket().getTitle());
                                if(TextUtils.isEmpty(ticketResponseModel.getTicket().getTreatment())){
                                    txtSummery.setText("Summary : Not Available");
                                }else{
                                    txtSummery.setText("Summary : " + Html.fromHtml(ticketResponseModel.getTicket().getTreatment()));
                                }

                                if(TextUtils.isEmpty(ticketResponseModel.getTicket().getAge())){
                                    txtAge.setText("Not Available");
                                }else{
                                    txtAge.setText("Age: " + ticketResponseModel.getTicket().getAge());
                                }

                                if(TextUtils.isEmpty(ticketResponseModel.getTicket().getHealthStatus())){
                                    txtHealth.setText("Health Status : Not Available");
                                }else{
                                    txtHealth.setText("Health Status: " + ticketResponseModel.getTicket().getHealthStatus());
                                }

//                                if(ticketResponseModel.getPatientState().get(0)==null){
//                                    txtState.setText("State : Not Available");
//                                }else{
//                                    String strState="";
//                                    for(String str:ticketResponseModel.getPatientState()){
//                                        strState+=str +" , ";
//                                    }
//                                    txtState.setText("State : " + strState.substring(0,strState.lastIndexOf(",")));
//                                }

                                String strState="";
                                for (String str: ticketResponseModel.getPatientState()){
                                    if (str != null){
                                        strState += str + " , ";
                                    }
                                }
                                txtState.setText("State : " + strState);

//                                if (TextUtils.isEmpty(ticketResponseModel.getTicket().getDoctorReply())) {
//                                    txtDocReply.setText("Doctor's Reply Pending");
//                                } else {
//                                    txtDocReply.setText("Doctor's Reply: " + Html.fromHtml(ticketResponseModel.getTicket().getDoctorReply()));
//                                }

//                                if (ticketResponseModel.getTicket().getImage().get(0) == null) {
//                                    Log.e("image counter", (ticketResponseModel.getTicket().getImage()).toString());
//                                }
//                                if ((ticketResponseModel.getTicket().getImage().get(0).indexOf("gif") >= 0)){
//                                    Log.e("has gif", "--it means no image");
//
//                                }
                                arrayImagesList = ticketResponseModel.getTicket().getImage();
                                recentViewpager.setAdapter(new CustomCasePagerAdapter(getActivity(), arrayImagesList));

                                CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
                                indicator.setViewPager(recentViewpager);
                                recentViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        currentPage = position;
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {
//                                        Toast.makeText(getActivity(), "context changed", Toast.LENGTH_SHORT).show();

                                        if (state == ViewPager.SCROLL_STATE_IDLE) {
                                            int pageCount = arrayImagesList.size();
                                            if (currentPage == 0) {
//                                                recentViewpager.setCurrentItem(pageCount - 1, false);
                                            } else if (currentPage == pageCount - 1) {
//                                                recentViewpager.setCurrentItem(0, false);
                                            }
                                        }
                                    }
                                });

//                                final Handler handler = new Handler();
//                                final Runnable update = new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (currentPage == NUM_PAGES) {
//                                            currentPage = 0;
//                                        }
//                                        recentViewpager.setCurrentItem(currentPage++, true);
//                                    }
//                                };
//                                Timer swipeTimer = new Timer();
//                                swipeTimer.schedule(new TimerTask() {
//
//                                    @Override
//                                    public void run() {
//                                        handler.post(update);
//                                    }
//                                }, 1000, 1000);
//                                circlePageIndicator.setViewPager(recentViewpager);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(getActivity()!=null && isAdded()){
                    Toast.makeText(getActivity(), "Some Problem Occured", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.TICKET_ID, getArguments().getString("id"));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                return addHeader();
            }
        };
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
}