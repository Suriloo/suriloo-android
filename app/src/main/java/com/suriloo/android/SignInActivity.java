package com.suriloo.android;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
public class SignInActivity extends AppCompatActivity {
    private TextInputEditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        TextView btnLogin = findViewById(R.id.tvToLogin);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button btnSignup = findViewById(R.id.btnSignup);
        btnLogin.setOnClickListener(v -> performLogin());
        btnGoogle.setOnClickListener(v ->Toast.makeText(SignInActivity.this, "Google Login Clicked", Toast.LENGTH_SHORT).show());
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
}