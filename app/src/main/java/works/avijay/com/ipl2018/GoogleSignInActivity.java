package works.avijay.com.ipl2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import works.avijay.com.ipl2018.helper.BackendHelper;

public class GoogleSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    static Context context;
    SharedPreferences sharedPreferences;
    View view;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);
        context = GoogleSignInActivity.this;
        sharedPreferences = getSharedPreferences("ipl_sp", MODE_PRIVATE);
        view = findViewById(android.R.id.content);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        SignInButton googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GOOGLE - SIGNIN", connectionResult.getErrorMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }
    }


    private void handleResult(GoogleSignInResult signInResult) {

        if (signInResult.isSuccess()) {

            Log.d("GOOGLE- SIGNUP", "PASS");
            GoogleSignInAccount googleSignInAccount = signInResult.getSignInAccount();
            String username = googleSignInAccount.getEmail().split("@")[0];

            Log.i("GOOGLE- SIGNUP", username);

            BackendHelper.user_signup user_signup = new BackendHelper.user_signup();
            user_signup.execute(context, username, view);


        }else{
            Log.d("GOOGLE- SIGNUP", "FAIL");
            Snackbar.make(view, "Something went wrong, please try again later", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);

    }
}
