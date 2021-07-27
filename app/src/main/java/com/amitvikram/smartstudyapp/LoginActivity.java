package com.amitvikram.smartstudyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private TextView createAccount;
    private static final String TAG = "";
    private static final int RC_SIGN_IN = 101;
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private RadioButton radioButtonStudent, radioButtonTeacher;
    private RadioGroup rdGroup;
    private static String userType = "";
    private GoogleSignInButton signInButton;
    // Build a GoogleSignInClient with the options specified by gso.
    private GoogleSignInClient mGoogleSignInClient;
    private Button btn_login;
    //Todo: Change URL when integrate on server
    private static String URL_LOGIN = "http://smartstudyapp.000webhostapp.com/login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(LoginActivity.this);
        createAccount = findViewById(R.id.createAccount);
        etEmail = findViewById(R.id.login_emailid);
        etPassword = findViewById(R.id.login_password);
        radioButtonStudent = findViewById(R.id.rdbtnstudent);
        radioButtonTeacher = findViewById(R.id.rdbtnteacher);
        rdGroup = findViewById(R.id.rdGroup);
        signInButton = findViewById(R.id.googleLoginBtn);
        mAuth = FirebaseAuth.getInstance();
        btn_login = findViewById(R.id.loginBtn);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == radioButtonStudent.getId()) {
                    userType = "student";
                } else if (i == radioButtonTeacher.getId()) {
                    userType = "teacher";
                }
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please select student or teacher", Toast.LENGTH_LONG).show();
                    return;
                }
                signIn();
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = etEmail.getText().toString().trim();
                String mPass = etPassword.getText().toString().trim();
                if (userType.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please select student or teacher", Toast.LENGTH_LONG).show();
                    return;
                }
                if (validateEmail() && !mPass.isEmpty()) {
                    Login(mEmail, mPass, userType);
                } else {
                    etEmail.setError("Please insert valid email");
//                    loading.setVisibility(View.GONE);
//                    btn_login.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    private boolean validateEmail()
    {
        String emailInput = etEmail.getText().toString();

        if(emailInput.isEmpty()){
            etEmail.requestFocus();
            etEmail.setError("Field cannot be empty!");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.requestFocus();
            etEmail.setError("Please enter valid Email id");
            return false;

        }
        else{
            etEmail.requestFocus();
            etEmail.setError(null);
            return true;
        }
    }

    private void Login(final String email, final String password, final String userType) {
//        loading.setVisibility(View.VISIBLE);
//        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {
//                                loading.setVisibility(View.GONE);
//                                btn_login.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String mobile = object.getString("mobile").trim();
                                    sessionManager.createSession(name, email, mobile, userType);
                                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("mobile", mobile);
                                    intent.putExtra("usertype", userType);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Email or Password Invalid!", Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            loading.setVisibility(View.GONE);
//                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Invalid Username and Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        loading.setVisibility(View.GONE);
//                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Error " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", email);
                params.put("user_password", password);
                params.put("user_type", userType);
                params.put("auth_type", "sqlAuth");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            etEmail.setText(user.getEmail());

            FirebaseAuth.getInstance().signOut();
            googleLogin(etEmail.getText().toString().trim());
        }
    }

    private void googleLogin(final String email) {
        final String[] gotPassword = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {
//                                loading.setVisibility(View.GONE);
//                                btn_login.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    gotPassword[0] = object.getString("password").trim();
                                    break;
                                }
                                Login(email, gotPassword[0], userType);

                            } else {
                                Toast.makeText(LoginActivity.this, "User does not exist in system please register yourself.", Toast.LENGTH_SHORT).show();
//                                    loading.setVisibility(View.GONE);
//                                    btn_regist.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            loading.setVisibility(View.GONE);
//                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Invalid Username and Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        loading.setVisibility(View.GONE);
//                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Error " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", email);
                params.put("user_type", userType);
                params.put("auth_type", "googleAuth");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}