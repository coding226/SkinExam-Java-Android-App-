package com.skinexam.myapplication.splah;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skinexam.myapplication.R;
import com.skinexam.myapplication.adapter.BodyAdapter;
import com.skinexam.myapplication.adapter.CustomAdapter;
import com.skinexam.myapplication.adapter.HealthAdapter;
import com.skinexam.myapplication.adapter.PrevAdapter;
import com.skinexam.myapplication.app.MySingleton;
import com.skinexam.myapplication.model.AddCaseModel;
import com.skinexam.myapplication.model.AgeResponseModel;
import com.skinexam.myapplication.model.BodyResponseModel;
import com.skinexam.myapplication.model.HealthResponseModel;
import com.skinexam.myapplication.model.MyCheckBoxModel;
import com.skinexam.myapplication.model.PatientBody;
import com.skinexam.myapplication.model.PatientHealth;
import com.skinexam.myapplication.model.PatientPrev;
import com.skinexam.myapplication.model.PatientState;
import com.skinexam.myapplication.model.PatientStateModel;
import com.skinexam.myapplication.model.PrevResponseModel;
import com.skinexam.myapplication.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostProblemActivity_1 extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_TAKE_PHOTO2 = 2;
    static final int REQUEST_TAKE_PHOTO3 = 3;

    private ArrayList<MyCheckBoxModel> modelArrayList;
    private TextView next_btn, toproblem;
    private CustomAdapter customAdapter;

    private EditText caseTitle, editDesc, editDesc2, editDesc3, edtTicketDesc;
    private String title, editDes1_s, editDes2_s, editDes3_s, editTicketDesc, spinAge_t, spinHealth_t, spinBody_t, spinUsed_t;
    private Spinner spinAge, spinHealth, spinBody, spinUsed;
    private ListView lstList;
    String spinBody_id, spinHealth_id, spinUsed_id;
    public File filen = null;

    AddCaseModel addCaseModel = new AddCaseModel();



    List<PatientState> patientState;
    List<PatientState> selectedState = new ArrayList<PatientState>();

    PatientHealth selectedHealth;

    //    for select image
    private ImageButton btnAdd1, btnAdd2, btnAdd3;
    String btn1="", btn2="", btn3="";
    private ImageView imagPhoto1, imagPhoto2, imagPhoto3;
    String imgPhotoUrl1, imgPhotoUrl2, imgPhotoUrl3,imgExt, imgExt2, imgExt3;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 0;
    int mtype;
    ArrayList<String> imageString = new ArrayList<String>();

    String [] statelist = {"Itchy", "Changing color over time", "I can fill it as a bump"};

    Uri selectedImage;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_problem_1);

        String sHealth;

        Methods.showProgress(PostProblemActivity_1.this);

        requestMultiplePermissions();
        loadAgeData();
        loadHealthData();
        loadStatusData();
        loadBodyPartData();
        loadPrevtData();


        btnAdd1 = (ImageButton) findViewById(R.id.btnAdd1);
        imagPhoto1 = (ImageView) findViewById(R.id.imgPhoto1);

        btnAdd2 = (ImageButton) findViewById(R.id.btnAdd2);
        imagPhoto2 = (ImageView) findViewById(R.id.imgPhoto2);

        btnAdd3 = (ImageButton) findViewById(R.id.btnAdd3);
        imagPhoto3 = (ImageView) findViewById(R.id.imgPhoto3);

        next_btn = (TextView) findViewById(R.id.btnNext_1);
        toproblem = (TextView) findViewById(R.id.toproblem);

        btnAdd1.setOnClickListener(this);
        btnAdd2.setOnClickListener(this);
        btnAdd3.setOnClickListener(this);
        toproblem.setOnClickListener(this);

        caseTitle = (EditText) findViewById(R.id.case_title);
        editDesc = (EditText) findViewById(R.id.edtDesc);
        editDesc2 = (EditText) findViewById(R.id.edtDesc2);
        editDesc3 = (EditText) findViewById(R.id.edtDesc3);
        edtTicketDesc = (EditText) findViewById(R.id.edtTicketDesc);
        lstList = (ListView) findViewById(R.id.lstList);

//        this is need for checkbox listview
        modelArrayList = getModel(false);
        customAdapter = new CustomAdapter(PostProblemActivity_1.this, modelArrayList);
        lstList.setAdapter(customAdapter);

        spinAge = (Spinner) findViewById(R.id.spnrAge);
        spinHealth = (Spinner) findViewById(R.id.spnrHealth);
        spinBody = (Spinner) findViewById(R.id.spnrbodypart);
        spinUsed = (Spinner) findViewById(R.id.spnrused);
        next_btn.setOnClickListener(this);
    }

    public void loadAgeData() {
        StringRequest srAge = new StringRequest(Request.Method.POST, Constants.AGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Methods.closeProgress();
                        Gson gson = new Gson();
                        AgeResponseModel objAge = gson.fromJson(response, AgeResponseModel.class);
                        if(objAge!=null){
                            ArrayList<String> listAge = new ArrayList<String>();
                            listAge.add("Select Age");
                            for (Integer ageStr : objAge.getAge()) {
                                listAge.add(ageStr + "");
                            }
                            spinAge.setAdapter(new ArrayAdapter<String>(PostProblemActivity_1.this, R.layout.simple_spinner_item, listAge));
                            spinAge.setOnItemSelectedListener(PostProblemActivity_1.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), PostProblemActivity_1.this);
                    }
                }) {

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srAge);
    }

    public void loadHealthData() {
        final StringRequest srHealth = new StringRequest(Request.Method.POST, Constants.HEALTH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Methods.closeProgress();
                        Gson gson = new Gson();

                        List<PatientHealth> patientHealth = new ArrayList<PatientHealth>();

                        if(patientHealth!=null){
                            PatientHealth first = new PatientHealth();
                            first.setId(0);
                            first.setStatus("Health");
                            patientHealth.add(first);
                            HealthResponseModel objHealth = gson.fromJson(response, HealthResponseModel.class);
                            patientHealth.addAll(objHealth.getPatientHealth());

                            spinHealth.setAdapter(new HealthAdapter(PostProblemActivity_1.this, patientHealth));
                            spinHealth.setOnItemSelectedListener(PostProblemActivity_1.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        if(getActivity()!=null && isAdded()){
                        Methods.showAlertDialog(getString(R.string.error_network_check), PostProblemActivity_1.this);
//                        }
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srHealth);
    }

    public void loadBodyPartData() {
        final StringRequest srBody = new StringRequest(Request.Method.POST, Constants.BODYPART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Methods.closeProgress();
                        Gson gson = new Gson();

                        List<PatientBody> patientBody = new ArrayList<PatientBody>();

                        if(patientBody!=null){
                            PatientBody first = new PatientBody();
                            first.setId(0);
                            first.setStatus("Select Body Part");
                            patientBody.add(first);
                            BodyResponseModel objBody = gson.fromJson(response, BodyResponseModel.class);
                            patientBody.addAll(objBody.getPatientHealth());

                            spinBody.setAdapter(new BodyAdapter(PostProblemActivity_1.this, patientBody));
                            spinBody.setOnItemSelectedListener(PostProblemActivity_1.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        if(getActivity()!=null && isAdded()){
                        Methods.showAlertDialog(getString(R.string.error_network_check), PostProblemActivity_1.this);
//                        }
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srBody);
    }

    public void loadPrevtData() {
        final StringRequest srPrev = new StringRequest(Request.Method.POST, Constants.PATIENTPREV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Methods.closeProgress();
                        Gson gson = new Gson();

                        List<PatientPrev> patientPrev = new ArrayList<PatientPrev>();

                        if(patientPrev!=null){
                            PatientPrev first = new PatientPrev();
                            first.setId(0);
                            first.setStatus("Select Previous Used");
                            patientPrev.add(first);
                            PrevResponseModel objPrev = gson.fromJson(response, PrevResponseModel.class);
                            patientPrev.addAll(objPrev.getPatientPrev());

                            spinUsed.setAdapter(new PrevAdapter(PostProblemActivity_1.this, patientPrev));
                            spinUsed.setOnItemSelectedListener(PostProblemActivity_1.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        if(getActivity()!=null && isAdded()){
                        Methods.showAlertDialog(getString(R.string.error_network_check), PostProblemActivity_1.this);
//                        }
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srPrev);
    }

    public void loadStatusData() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, Constants.STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
//                        if(getActivity()!=null && isAdded()){
                        patientState = new ArrayList<PatientState>();
                        PatientStateModel objPatientState = gson.fromJson(response, PatientStateModel.class);
                        if(objPatientState!=null){
                            patientState.addAll(objPatientState.getPatientState());
                        }
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), PostProblemActivity_1.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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

    private void showPictureDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Picture Option");
        alertDialog.setIcon(getResources().getDrawable(R.drawable.icon_image));
        alertDialog.setPositiveButton("GALLARY",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choosePhotoFromGallery();
            }
        });
        alertDialog.setNegativeButton("CAMERA",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhotoFromCamera();
            }
        });
        alertDialog.show();
    }

    public void choosePhotoFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == Activity.RESULT_OK){
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    if (mtype == REQUEST_TAKE_PHOTO){
                        imagPhoto1.setImageBitmap(thumbnail);
                        imagPhoto1.setVisibility(View.VISIBLE);
                    }else if (mtype == REQUEST_TAKE_PHOTO2){
                        imagPhoto2.setImageBitmap(thumbnail);
                        imagPhoto2.setVisibility(View.VISIBLE);
                    }else if (mtype == REQUEST_TAKE_PHOTO3){
                        imagPhoto3.setImageBitmap(thumbnail);
                        imagPhoto3.setVisibility(View.VISIBLE);
                    }
                    saveImage(thumbnail);
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK){

                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                            String path = saveImage(bitmap);
                            if (mtype == REQUEST_TAKE_PHOTO){
                                imagPhoto1.setImageBitmap(bitmap);
                                imagPhoto1.setVisibility(View.VISIBLE);
                            }else if (mtype == REQUEST_TAKE_PHOTO2){
                                imagPhoto2.setImageBitmap(bitmap);
                                imagPhoto2.setVisibility(View.VISIBLE);
                            }else if (mtype == REQUEST_TAKE_PHOTO3){
                                imagPhoto3.setImageBitmap(bitmap);
                                imagPhoto3.setVisibility(View.VISIBLE);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(PostProblemActivity_1.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            if (mtype == REQUEST_TAKE_PHOTO){
                btn1 = f.getAbsolutePath();
            }else if (mtype == REQUEST_TAKE_PHOTO2){
                btn2 = f.getAbsolutePath();
            }else if (mtype == REQUEST_TAKE_PHOTO3){
                btn3 = f.getAbsolutePath();
            }


            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private ArrayList<MyCheckBoxModel> getModel(boolean isSelect) {
        ArrayList<MyCheckBoxModel> list = new ArrayList<>();
        for (int i = 0; i < statelist.length; i++){

            MyCheckBoxModel model = new MyCheckBoxModel();
            model.setSelected(isSelect);
            model.setState(statelist[i]);
            list.add(model);
        }
        return list;
    }


    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd1:
                mtype=REQUEST_TAKE_PHOTO;
                showPictureDialog();
                break;
            case R.id.btnAdd2:
                mtype = REQUEST_TAKE_PHOTO2;
                showPictureDialog();
                break;
            case R.id.btnAdd3:
                mtype = REQUEST_TAKE_PHOTO3;
                showPictureDialog();
                break;
            case R.id.toproblem:
                onBackPressed();
                break;
            case R.id.btnNext_1:
                Intent next_intent = new Intent(com.skinexam.myapplication.splah.PostProblemActivity_1.this, com.skinexam.myapplication.splah.PostProblemActivity_2.class);

                title = caseTitle.getText().toString();
                editDes1_s = editDesc.getText().toString();
                editDes2_s = editDesc2.getText().toString();
                editDes3_s = editDesc3.getText().toString();
                editTicketDesc = edtTicketDesc.getText().toString();

                next_intent.putExtra("TITLE", title);
                next_intent.putExtra("EDITDEST1", editDes1_s);
                next_intent.putExtra("EDITDEST2", editDes2_s);
                next_intent.putExtra("EDITDEST3", editDes3_s);
                next_intent.putExtra("CONCERN", editTicketDesc);

                next_intent.putExtra("SPINAGE", spinAge_t);
                next_intent.putExtra("SPINHEALTH", spinHealth_t);
                next_intent.putExtra("SPINHEALTH_ID", spinHealth_id);
                next_intent.putExtra("SPINBODY", spinBody_t);
                next_intent.putExtra("SPINBODY_ID", spinBody_id);
                next_intent.putExtra("SPINUSED", spinUsed_t);
                next_intent.putExtra("SPINUSED_ID",spinUsed_id);
                next_intent.putExtra("image_1", btn1);
                next_intent.putExtra("image_2", btn2);
                next_intent.putExtra("image_3", btn3);

                if(checkValidation()) {
//                    if (!TextUtils.isEmpty(btn1)) {
//                        imageString.add(imgPhotoUrl1);
//                    }
//                    if (!TextUtils.isEmpty(imgPhotoUrl2)) {
//                        imageString.add(imgPhotoUrl2);
//                    }
//                    if (!TextUtils.isEmpty(imgPhotoUrl3)) {
//                        imageString.add(imgPhotoUrl3);
//                    }
                    selectedState.clear();
                    if(patientState!=null){
                        for (PatientState pas : patientState) {
                            if (pas.isSelected()) {
                                selectedState.add(pas);
                            }
                        }
                    }
//                    loadData();
                    startActivity(next_intent);
                }

                break;
        }

    }

//    private void loadData() {
//
//        addCaseModel.setImgArrayList(imageString);
//
//        addCaseModel.setTitle(caseTitle.getText().toString());
//        // ((CaseDetailActivity) getActivity()).getAddCaseModel().setSummery(edtSummary.getText().toString());
//        addCaseModel.setImage1(imgPhotoUrl1);
//        addCaseModel.setDescription1(editDesc.getText().toString());
//        addCaseModel.setImage2(imgPhotoUrl2);
//        addCaseModel.setDescription2(editDesc2.getText().toString());
//        addCaseModel.setImage3(imgPhotoUrl3);
//        addCaseModel.setDescription3(editDesc3.getText().toString());
//        addCaseModel.setImg_extension1(imgExt);
//        addCaseModel.setImg_extension2(imgExt2);
//        addCaseModel.setImg_extension3(imgExt3);
//        addCaseModel.setAge(spinAge_t);
//        addCaseModel.setPatientHealth(selectedHealth);
//        addCaseModel.setSummery(edtTicketDesc.getText().toString());
//        addCaseModel.setPatientStates(selectedState);
//
//    }

    private boolean checkValidation() {
        if (!Validation.hasText(caseTitle,"Please provide Case title")){
            caseTitle.requestFocus();
            return false;
        }else{
            if(caseTitle.getText().length()<8){
                Toast.makeText(PostProblemActivity_1.this,"Please Enter character size for Title greater than 8 ",Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (!TextUtils.isEmpty(btn1)) {
            if (editDesc.getText().length() < 8) {
                editDesc.requestFocus();
                Toast.makeText(PostProblemActivity_1.this,"Please Enter character size for Image Description greater than 8",Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(PostProblemActivity_1.this,"Please provide atleast one image",Toast.LENGTH_LONG).show();
            return false;
        }

//        if (!TextUtils.isEmpty(imgPhotoUrl2)) {
//            if (editDesc2.getText().length() < 8) {
//                editDesc2.requestFocus();
//                Toast.makeText(PostProblemActivity_1.this,"Please Enter character size for Image Description greater than 8",Toast.LENGTH_LONG).show();
//                return false;
//            }
//        }
//
//        if (!TextUtils.isEmpty(imgPhotoUrl3)) {
//            if (editDesc3.getText().length() < 8) {
//                editDesc2.requestFocus();
//                Toast.makeText(PostProblemActivity_1.this,"Please Enter character size for Image Description greater than 8",Toast.LENGTH_LONG).show();
//                return false;
//            }
//        }
//
//        if(!TextUtils.isEmpty(edtTicketDesc.getText().toString())) {
//            if (edtTicketDesc.getText().length() < 8) {
//                edtTicketDesc.requestFocus();
//                Toast.makeText(PostProblemActivity_1.this, "Please Enter character size for Image Ticket Descritption greater than 8",Toast.LENGTH_LONG).show();
//                return false;
//            }
//        }
        return true;
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.equals(spinAge)){
            if (position > 0) {
                spinAge_t = parent.getItemAtPosition(position).toString();
            }else{
                spinAge_t="";
            }
        }else if(parent.equals(spinHealth)){
            if (position > 0) {
                spinHealth_t = ((PatientHealth) parent.getItemAtPosition(position)).getStatus();
                spinHealth_id = Integer.toString(((PatientHealth) parent.getItemAtPosition(position)).getId());
//                sHealth = parent.getItemAtPosition(position).toString();
            }else{
                spinHealth_t="";
                spinHealth_id="";
            }
        }else if(parent.equals(spinBody)){
            if(position > 0){
                spinBody_t = ((PatientBody) parent.getItemAtPosition(position)).getStatus();
                spinBody_id = Integer.toString(((PatientBody) parent.getItemAtPosition(position)).getId());
            }else {
                spinBody_t = "";
                spinBody_id = "";
            }
        }else if(parent.equals((spinUsed))){
            if (position > 0) {
                spinUsed_t = ((PatientPrev) parent.getItemAtPosition(position)).getStatus();
                spinUsed_id = Integer.toString(((PatientPrev) parent.getItemAtPosition(position)).getId());
            } else {
                spinUsed_t = "";
                spinUsed_id = "";
            }
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
