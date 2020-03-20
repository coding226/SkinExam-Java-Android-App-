package com.skinexam.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skinexam.myapplication.activityeditprofile.EditProfileActivity;
import com.skinexam.myapplication.activityeditprofile.Step1;
import com.skinexam.myapplication.activityeditprofile.Step2;
import com.skinexam.myapplication.splah.Dash_all;
import com.skinexam.myapplication.splah.Dash_pend;
import com.skinexam.myapplication.splah.Dash_recent;
import com.skinexam.myapplication.splah.Methods;
import com.skinexam.myapplication.splah.PostProblemActivity_1;
import com.skinexam.myapplication.splah.SignInActivity;
import com.skinexam.myapplication.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class DashboardActivity extends AppCompatActivity implements Step1.OnFragmentInteractionListener, Step2.OnFragmentInteractionListener, Dash_recent.OnFragmentInteractionListener, Dash_pend.OnFragmentInteractionListener, Dash_all.OnFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;

    private ImageButton btnAdd_1;
    private ImageView imagPhoto_1;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    FloatingActionButton fab;
    String h;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, PostProblemActivity_1.class);
                startActivity(intent);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nave_dashboard, R.id.nav_myPlan, R.id.nav_subScription, R.id.nav_mycase, R.id.nav_newcase, R.id.nav_history, R.id.nav_changePwd, R.id.nav_editProfile, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_newcase ){
                    onBackPressed();
                    Intent intent = new Intent(getApplicationContext(), PostProblemActivity_1.class);
                    startActivity(intent);
                    fab.setVisibility(View.GONE);
                }else {
                    fab.setVisibility(View.VISIBLE);
                }
                if (destination.getId() == R.id.nav_editProfile ){
                    fab.setVisibility(View.GONE);
                }
                if (destination.getId() == R.id.nav_logout){
                    ChkLogOut();
                }
                if (destination.getId() == R.id.nav_editProfile){
                    onBackPressed();
                    Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void ChkLogOut() {

        if (Methods.isOnline(DashboardActivity.this)) {
            Logout();
//            onBackPressed();
        } else {
            Toast.makeText(this, R.string.error_network_check, Toast.LENGTH_SHORT).show();
        }
    }

    private void Logout() {
//        StringRequest sr = new StringRequest(Request.Method.POST, Constants.STUDENT_LOGIN, new Response.Listener<String>() {
//            public void onResponse(String response) {
//                try {
//                    JSONObject obj = new JSONObject(response);
//
//                    if (obj.getString("status").equalsIgnoreCase("true")){
//                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//                        startActivity(intent);
//                    }else {
//                        Methods.showAlertDialog(getString(R.string.error_network_check), DashboardActivity.this);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Methods.showAlertDialog(getString(R.string.error_network_check), DashboardActivity.this);
//                if (error.networkResponse == null) {
//                    if (error.getClass().equals(TimeoutError.class)) {
//                        Toast.makeText(getApplicationContext(), "Oops. Timeout error!", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        }) {
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put(Constants.EMAIL, getString(Constants.EMAIL));
////                params.put(Constants.PARAM_PASSWORD, getString(Constants.PARAM_PASSWORD));
////                params.put(Constants.USER_ID, getString("user_id"));
////                params.put(Constants.SESSION_ID, getString("session_id"));
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return addHeader();
//            }
//        };
//        sr.setRetryPolicy(new DefaultRetryPolicy(50000, 1, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
//        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        insertBoolean("SaveLogin", false);

        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public synchronized void clear() {
//                getPreferences().edit().clear().apply();
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public synchronized void insertString(String key, String value) {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }

    private Map<String, String> addHeader() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s",Constants.Auth_UserName, Constants.Auth_Password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        Log.e("Header", auth);
        return params;
    }

    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Picture Option");
        String [] pictureDialogItems = { "CAMERA", "GALLERY" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case 0: takePhotoFromCamera(); break;
                            case 1: choosePhotoFromGallery(); break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallery() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void takePhotoFromCamera() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    imagPhoto_1.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(DashboardActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imagPhoto_1.setImageBitmap(thumbnail);
            saveImage(thumbnail);
        }
    }

    private String saveImage(Bitmap myBitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
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
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                        }
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public synchronized void insertBoolean(String key, boolean value) {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }
}
