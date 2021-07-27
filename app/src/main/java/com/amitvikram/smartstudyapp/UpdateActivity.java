package com.amitvikram.smartstudyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    private TextInputEditText txtName, txtPhone, txtEmail;
    private static String URL_REGIST = "http://smartstudyapp.000webhostapp.com/update_user_details.php";
//    private RadioButton radioButtonStudent, radioButtonTeacher;
    private String userType = "";
//    private RadioGroup radioGroup;
    private String old_email;
    private Button updateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        txtName = findViewById(R.id.editText_name);
        txtPhone = findViewById(R.id.editText_phone);
        txtEmail = findViewById(R.id.editText_email);
//        radioButtonStudent = findViewById(R.id.radioStudentUpdate);
//        radioButtonTeacher = findViewById(R.id.radioTeacherUpdate);
//        radioGroup = findViewById(R.id.radioGrpUpdate);
        updateBtn = findViewById(R.id.btn_update);
        Intent intent = getIntent();
        txtName.setText(intent.getStringExtra("name"));
        txtEmail.setText(intent.getStringExtra("email"));
        txtPhone.setText(intent.getStringExtra("phone"));
        old_email = txtEmail.getText().toString();
        //todo: get usertype by session
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == radioButtonStudent.getId()) {
//                    userType = "student";
//                } else if (i == radioButtonTeacher.getId()) {
//                    userType = "teacher";
//                }
//            }
//        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }
    private boolean validateEmail() {
        String emailInput = txtEmail.getText().toString();

        if (emailInput.isEmpty()) {
            txtEmail.requestFocus();
            txtEmail.setError("Field cannot be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            txtEmail.requestFocus();
            txtEmail.setError("Please enter valid Email id");
            return false;

        } else {
            txtEmail.requestFocus();
            txtEmail.setError(null);
            return true;
        }
    }

    public boolean validatePhone() {
        String phone = txtPhone.getText().toString();

        if (phone.isEmpty()) {
            txtPhone.requestFocus();
            txtPhone.setError("Field must be filled.");
            return false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            txtPhone.requestFocus();
            txtPhone.setError("Enter valid phone.");
            return false;
        } else if (phone.length() < 10) {
            txtPhone.requestFocus();
            txtPhone.setError("Phone must 10 digit long.");
            return false;
        } else {
            txtPhone.requestFocus();
            return true;
        }
    }
    private boolean validateUname() {
        String unameInput = txtName.getText().toString();
        if (unameInput.isEmpty()) {
            txtName.requestFocus();
            txtName.setError("Field cannot be empty!");
            return false;

        } else {
            txtName.requestFocus();
            txtName.setError(null);
            return true;
        }
    }
    private void update() {
        //loading.setVisibility(View.VISIBLE);
        //btn_regist.setVisibility(View.GONE);

        final String name = this.txtName.getText().toString().trim();
        final String email = this.txtEmail.getText().toString().trim();
        final String mobile = this.txtPhone.getText().toString().trim();
        final String type = userType;
        if (userType.equals("")) {
            Toast.makeText(this, "Please select student or teacher", Toast.LENGTH_LONG).show();
            return;
        }
        if (!validateUname() || !validatePhone() || !validateEmail()) {
            Toast.makeText(this, "PLease Enter All The Correct Details!!!", Toast.LENGTH_LONG);
//            loading.setVisibility(View.GONE);
//            btn_regist.setVisibility(View.VISIBLE);
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("2")) {
                                    Toast.makeText(UpdateActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                } else if (success.equals("1")) {
                                    Toast.makeText(UpdateActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(UpdateActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(UpdateActivity.this, "User Already Exist. " + e.toString(), Toast.LENGTH_SHORT).show();
//                                loading.setVisibility(View.GONE);
//                                btn_regist.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UpdateActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
//                            loading.setVisibility(View.GONE);
//                            btn_regist.setVisibility(View.VISIBLE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_name", name);
                    params.put("user_mobile", mobile);
                    params.put("user_email", email);
                    params.put("user_type", type);
                    params.put("old_user_email", old_email);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}