package com.suriloo.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.suriloo.android.model.JwtResponse;
import com.suriloo.android.model.LoginRequest;
import com.suriloo.android.model.TokenRequest;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        etEmail = findViewById(R.id.login_email);
        etPassword = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        TextView tvToSignUp = findViewById(R.id.tv_to_sign_up);
        TextView tvForgotPassword = findViewById(R.id.forgotPassword);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("EMAIL")) {
            etEmail.setText(intent.getStringExtra("EMAIL"));
        }

        btnLogin.setOnClickListener(v -> performLogin());

        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        tvToSignUp.setOnClickListener(v -> {
            Intent newIntent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(newIntent);
            finish();
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot Password feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void performLogin() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required");
            return;
        }

        ApiService apiService = ApiClient.getInstance(this).create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiService.login(loginRequest).enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jwt = response.body().getJwt();

                    saveTokenAndNavigate(jwt);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    sendGoogleTokenToBackend(account.getIdToken());
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendGoogleTokenToBackend(String idToken) {
        ApiService apiService = ApiClient.getInstance(this).create(ApiService.class);
        TokenRequest tokenRequest = new TokenRequest(idToken);

        apiService.sendToken(tokenRequest).enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jwt = response.body().getJwt();
                    saveTokenAndNavigate(jwt);
                } else {
                    Toast.makeText(LoginActivity.this, "Google Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTokenAndNavigate(String jwt) {
        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        sharedPreferences.edit().putString("jwt", jwt).commit();

        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
