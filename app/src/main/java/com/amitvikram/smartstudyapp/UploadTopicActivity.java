package com.amitvikram.smartstudyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadTopicActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    private Button btnSelectModel, btnUpload;
    private TextView txtUploadModelName;
    private EditText editTextTopicName, editTextTopicDesc;
    List<String> resultList;
    String modelName, modelLink;
    private static String URL_RESULT = "http://smartstudyapp.000webhostapp.com/read_detail.php";
    private static String URL_UPLOAD = "http://smartstudyapp.000webhostapp.com/upload_topic.php";
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_topic);
        btnSelectModel = findViewById(R.id.btn_select_model);
        txtUploadModelName = findViewById(R.id.model_upload_name);
        editTextTopicName = findViewById(R.id.editText__topic_name);
        editTextTopicDesc = findViewById(R.id.editText__topic_desc);
        resultList = new ArrayList<>();
        spinner = findViewById(R.id.spinner);
        btnUpload = findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        btnSelectModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadTopicActivity.this, ModelActivity.class);
                startActivity(intent);
            }
        });
        loadResults();
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        modelName = intent.getStringExtra("model_name");
        modelLink = intent.getStringExtra("model_link");
        txtUploadModelName.setText(modelName);
    }

    private void loadResults() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        //swipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESULT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject result = array.getJSONObject(i);

                                //adding the product to product list
                                resultList.add(result.getString("category").trim());
                            }
                            spinner.setAdapter(new ArrayAdapter<String>(UploadTopicActivity.this, android.R.layout.simple_spinner_dropdown_item, resultList));
                            if (resultList.size() == 0) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("details_type", "category");
                return params;
            }
        };


        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(UploadTopicActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void upload() {
        //loading.setVisibility(View.VISIBLE);
        //btn_regist.setVisibility(View.GONE);

        String name = this.editTextTopicName.getText().toString().trim();
        String desc = this.editTextTopicDesc.getText().toString().trim();
        String category = spinner.getSelectedItem().toString();
        String modelname = modelName;
        String modellink = modelLink;

        if (name.equals("") || desc.equals("") || modelname.equals("") || modellink.equals("")) {
            Toast.makeText(this, "Please Enter All The Topic Details!!!", Toast.LENGTH_LONG);
//            loading.setVisibility(View.GONE);
//            btn_regist.setVisibility(View.VISIBLE);
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("2")) {
                                    Toast.makeText(UploadTopicActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                } else if (success.equals("1")) {
                                    Toast.makeText(UploadTopicActivity.this, "Upload Success!", Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(UploadTopicActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(UploadTopicActivity.this, "Upload Success!", Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(UploadTopicActivity.this, "Topic Already Exist. " + e.toString(), Toast.LENGTH_SHORT).show();
//                                loading.setVisibility(View.GONE);
//                                btn_regist.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UploadTopicActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
//                            loading.setVisibility(View.GONE);
//                            btn_regist.setVisibility(View.VISIBLE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("topic_name", name);
                    params.put("topic_desc", desc);
                    params.put("topic_category", category);
                    params.put("model_name", modelname);
                    params.put("model_link", modellink);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}