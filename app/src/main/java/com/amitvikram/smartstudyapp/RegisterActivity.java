package com.amitvikram.smartstudyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "";
    private static final int RC_SIGN_IN = 101;
    private FirebaseAuth mAuth;
    private EditText editTextName, editTextEmail, editTextPhone, editTextPassword, editTextConfirmPassoword;
    private Button btnSignUp;
    private RadioGroup radioGroup;
    private TextView alreadyregistered;
    // Build a GoogleSignInClient with the options specified by gso.
    private GoogleSignInClient mGoogleSignInClient;
    //Todo: Change URL when integrate on server
    private static String URL_REGIST = "http://smartstudyapp.000webhostapp.com/register.php";
    private String userType = "";
    private RadioButton radioButtonStudent, radioButtonTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button signInButton = findViewById(R.id.googleSignUpBtn);
        mAuth = FirebaseAuth.getInstance();
        alreadyregistered = findViewById(R.id.already_user);
        editTextName = findViewById(R.id.Name);
        editTextEmail = findViewById(R.id.userEmailId);
        editTextPhone = findViewById(R.id.mobileNumber);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassoword = findViewById(R.id.confirmPassword);
        btnSignUp = findViewById(R.id.signUpBtn);
        radioGroup = findViewById(R.id.radioGrp);
        radioButtonStudent = findViewById(R.id.radioStudent);
        radioButtonTeacher = findViewById(R.id.radioTeacher);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regist();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == radioButtonStudent.getId()) {
                    userType = "student";
                } else if (i == radioButtonTeacher.getId()) {
                    userType = "teacher";
                }
            }
        });

        alreadyregistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

                Toast.makeText(this, "Google Sign in failed", Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                updateUI(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private boolean validateEmail() {
        String emailInput = editTextEmail.getText().toString();

        if (emailInput.isEmpty()) {
            editTextEmail.requestFocus();
            editTextEmail.setError("Field cannot be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editTextEmail.requestFocus();
            editTextEmail.setError("Please enter valid Email id");
            return false;

        } else {
            editTextEmail.requestFocus();
            editTextEmail.setError(null);
            return true;
        }
    }

    public boolean validatePhone() {
        String phone = editTextPhone.getText().toString();

        if (phone.isEmpty()) {
            editTextPhone.requestFocus();
            editTextPhone.setError("Field must be filled.");
            return false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            editTextPhone.requestFocus();
            editTextPhone.setError("Enter valid phone.");
            return false;
        } else if (phone.length() < 10) {
            editTextPhone.requestFocus();
            editTextPhone.setError("Phone must 10 digit long.");
            return false;
        } else {
            editTextPhone.requestFocus();
            return true;
        }
    }

    public boolean validatePassword() {
        String password = editTextPassword.getText().toString();
        if (password.isEmpty()) {
            editTextPassword.requestFocus();
            editTextPassword.setError("Field must be filled.");
            return false;
        } else {
            editTextPassword.requestFocus();
            return true;
        }
    }

    public boolean validateConfirmPassword() {
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassoword.getText().toString();
        if (confirmPassword.isEmpty()) {
            editTextConfirmPassoword.requestFocus();
            editTextConfirmPassoword.setError("Field must be filled.");
            return false;
        } else if (!confirmPassword.equals(password)) {
            editTextConfirmPassoword.requestFocus();
            editTextConfirmPassoword.setError("Password does not match.");
            return false;
        } else {
            editTextConfirmPassoword.requestFocus();
            return true;
        }
    }

    private boolean validateUname() {
        String unameInput = editTextName.getText().toString();
        if (unameInput.isEmpty()) {
            editTextName.requestFocus();
            editTextName.setError("Field cannot be empty!");
            return false;

        } else {
            editTextName.requestFocus();
            editTextName.setError(null);
            return true;
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            editTextName.setText(user.getDisplayName());
            editTextEmail.setText(user.getEmail());
            editTextPhone.setText(user.getPhoneNumber());
            editTextPhone.requestFocus();

            FirebaseAuth.getInstance().signOut();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void Regist() {
        //loading.setVisibility(View.VISIBLE);
        //btn_regist.setVisibility(View.GONE);

        final String name = this.editTextName.getText().toString().trim();
        final String email = this.editTextEmail.getText().toString().trim();
        final String password = this.editTextPassword.getText().toString().trim();
        final String mobile = this.editTextPhone.getText().toString().trim();
        final String type = userType;
        if (userType.equals("")) {
            Toast.makeText(this, "Please select student or teacher", Toast.LENGTH_LONG).show();
            return;
        }
        if (!validateUname() || !validatePhone() || !validateEmail() || !validatePassword() || !validateConfirmPassword()) {
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
                                    Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                } else if (success.equals("1")) {
                                    Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
//                                    Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(RegisterActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
//                                loading.setVisibility(View.GONE);
//                                btn_regist.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
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
                    params.put("user_password", password);
                    params.put("user_type", type);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}