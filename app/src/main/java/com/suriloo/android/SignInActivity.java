package com.suriloo.android;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.suriloo.android.model.TokenRequest;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText etUsername, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        GoogleSignInClient googleSignInClient;
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        TextView btnLogin = findViewById(R.id.tvToLogin);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button btnSignup = findViewById(R.id.btnSignup);
        btnLogin.setOnClickListener(v -> performLogin());
        btnGoogle.setOnClickListener(v -> {
            Intent intent=googleSignInClient.getSignInIntent();
            startActivityForResult(intent,100);
        });
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performLogin() {
        String username = Objects.requireNonNull(etUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required");
            return;
        }
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account= null;
            try {
                account = task.getResult(ApiException.class);
                String idToken=account.getIdToken();
                sendTokenToBackend(idToken);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendTokenToBackend(String idToken) {
        ApiService apiService=ApiClient.getInstance().create(ApiService.class);
        TokenRequest tokenRequest=new TokenRequest(idToken);
        apiService.sendToken(tokenRequest).enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    String jwt=response.body().getJwt();
                    SharedPreferences sharedPreferences=getSharedPreferences("auth", MODE_PRIVATE);
                    sharedPreferences.edit().putString("jwt",jwt).apply();
                    Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(SignInActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SignInActivity.this, "Network Error ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}