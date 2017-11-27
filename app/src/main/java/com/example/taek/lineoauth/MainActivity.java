package com.example.taek.lineoauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static LineApiClient lineApiClient;
    TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(getApplicationContext(), Constansts.CHANNEL_ID);
        lineApiClient = apiClientBuilder.build();

        loginButton = (TextView) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // App-to-app login
                    Intent loginIntent = LineLoginApi.getLoginIntent(view.getContext(), Constansts.CHANNEL_ID);
                    startActivityForResult(loginIntent, REQUEST_CODE);
                } catch (Exception e) {
                    Log.d("ERROR", e.toString());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            Log.d("ERROR", "Unsupported Request");
            return;
        }

        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);

        switch (result.getResponseCode()) {
            case SUCCESS:
                // Login successful
                String accessToken = result.getLineCredential().getAccessToken().getAccessToken();

                Toast.makeText(this, "line_profile: " + result.getLineProfile() + ", line_credential: " + result.getLineCredential(), Toast.LENGTH_SHORT).show();
                break;

            case CANCEL:
                // Login canceled by user
                Log.d("ERROR", "LINE Login Canceled by user!!");
                break;

            default:
                // Login canceled due to other error
                Log.d("ERROR", "Login FAILED!");
                Log.d("ERROR", result.getErrorData().toString());
                break;
        }
    }
}
