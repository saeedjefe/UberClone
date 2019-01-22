package saeed.example.com.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton radioDriver, radioPassenger;
    Button btnOneTimeLogIn, btnSignUpOrLogIn;
    EditText edtUserName, edtPassword, edtOneTimeLogIn;

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnOneTimeLogIn:



                     if (ParseUser.getCurrentUser() == null) {
                        if(edtOneTimeLogIn.getText().toString().equals( "driver" )||edtOneTimeLogIn.getText().toString().equals( "passenger" )) {




                            final ProgressDialog progressDialogOneTime = new ProgressDialog( MainActivity.this );
                        progressDialogOneTime.setMessage( "logging in " );
                        progressDialogOneTime.show();

                        ParseAnonymousUtils.logIn( new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {

                                if (e == null && user != null) {
//
//
                                    if (edtOneTimeLogIn.getText().toString().equals( "driver" )) {
                                        user.put( "as", edtOneTimeLogIn.getText().toString());
                                        Toast.makeText( MainActivity.this, "anonymously logged in as a driver", Toast.LENGTH_LONG ).show();
                                        transitionToDriverActivity();

                                    }
                                    if (edtOneTimeLogIn.getText().toString().equals( "passenger" )) {
                                        user.put( "as", edtOneTimeLogIn.getText().toString() );
                                        Toast.makeText( MainActivity.this, "anonymously logged in as a passenger", Toast.LENGTH_LONG ).show();
                                        transitionToThePassengerActivity();


                                    }

                                    user.saveInBackground(  );

                                }
                                else
                                {
                                    Toast.makeText( MainActivity.this, "failed", Toast.LENGTH_LONG ).show();

                                }
                                progressDialogOneTime.dismiss();

                            }


                        } );
                    }
                }

                else
                {
                    Toast.makeText( MainActivity.this, "Please fill the text with either driver or passenger keyword", Toast.LENGTH_LONG ).show();

                }


                break;


            case R.id.btnSignUpOrLogIn:

                if(radioDriver.isChecked()==false&&radioPassenger.isChecked()==false)
                {
                    Toast.makeText( this, "Please choose either the passenger or the driver options", Toast.LENGTH_LONG).show();
                }
                else {

                    if (state == State.SIGNUP) {

                        final ProgressDialog progressDialog = new ProgressDialog( MainActivity.this );
                        progressDialog.setMessage( "signing up" );
                        progressDialog.show();


                        ParseUser parseUser = new ParseUser();
                        parseUser.setUsername( edtUserName.getText().toString() );
                        parseUser.setPassword( edtPassword.getText().toString() );

                        if(radioPassenger.isChecked())
                        {
//                                        Toast.makeText( MainActivity.this, "welcome new passenger", Toast.LENGTH_LONG ).show();

                            parseUser.put( "as", "passenger" );
                        }
                        else if(radioDriver.isChecked())
                        {
//                                        Toast.makeText( MainActivity.this, "welcome new driver", Toast.LENGTH_LONG ).show();
                            parseUser.put( "as", "driver" );


                        }

                        parseUser.signUpInBackground( new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {


                                if (e == null) {
                                    Toast.makeText( MainActivity.this, "successfully signed up", Toast.LENGTH_LONG ).show();

                                    if(ParseUser.getCurrentUser().get( "as" ).equals( "passenger" ))
                                    {
                                        transitionToThePassengerActivity();
                                    }

                                    if(ParseUser.getCurrentUser().get("as").equals( "driver" ))
                                    {
                                        transitionToDriverActivity();
                                    }

//                                    if(radioPassenger.isChecked())
//                                    {
//                                        Toast.makeText( MainActivity.this, "welcome new passenger", Toast.LENGTH_LONG ).show();
//
//                                    }
//                                    else if(radioDriver.isChecked())
//                                    {
//                                        Toast.makeText( MainActivity.this, "welcome new driver", Toast.LENGTH_LONG ).show();
//
//
//                                    }

                                } else {
                                    Toast.makeText( MainActivity.this, "failed", Toast.LENGTH_LONG ).show();

                                }
                                progressDialog.dismiss();
                            }
                        } );
                    } else if (state == State.LOGIN) {
                        final ProgressDialog progressDialog = new ProgressDialog( MainActivity.this );
                        progressDialog.setMessage( "logging in" );
                        progressDialog.show();

                        ParseUser.logInInBackground( edtUserName.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {

                                if (e == null && user != null) {

                                    if(radioPassenger.isChecked()) {
                                        Toast.makeText( MainActivity.this, "successfully logged in", Toast.LENGTH_LONG ).show();
                                        transitionToThePassengerActivity();
                                    }
                                    else if(radioDriver.isChecked())
                                    {
                                        transitionToDriverActivity();
                                    }

                                } else {
                                    Toast.makeText( MainActivity.this, "failed", Toast.LENGTH_LONG ).show();

                                }
                                progressDialog.dismiss();
                            }
                        } );

                    }

                    break;
                }
        }
    }

    enum State
    {
        SIGNUP, LOGIN
    }

    State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        ParseInstallation.getCurrentInstallation().saveInBackground();


        //initialization
        radioDriver = findViewById( R.id.radioDriver );
        radioPassenger = findViewById( R.id.radioPassenger );
        btnOneTimeLogIn = findViewById( R.id.btnOneTimeLogIn );
        btnSignUpOrLogIn = findViewById( R.id.btnSignUpOrLogIn );
        edtPassword = findViewById( R.id.edtPassworld );
        edtUserName = findViewById( R.id.edtUserName );
        edtOneTimeLogIn = findViewById( R.id.edtOneTimeLogIn );

        state = State.SIGNUP;

        //set on click
        btnSignUpOrLogIn.setOnClickListener( this );
        btnOneTimeLogIn.setOnClickListener( this );

        //kick out the current user
        if(ParseUser.getCurrentUser()!=null)
        {
            ParseUser.logOutInBackground(  );
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.sign_up_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.sign_up_menu:

                if(state == State.SIGNUP) {

                    item.setTitle( "SignUP" );
                    btnSignUpOrLogIn.setText( "LogIn" );
                    state = State.LOGIN;
                }
                else if(state ==state.LOGIN)
                {

                    item.setTitle( "LogIn" );
                    btnSignUpOrLogIn.setText( "SignUp" );
                    state = State.SIGNUP;

                }
                break;
        }

        return true;
    }

    private void transitionToThePassengerActivity()
    {
        Intent intent  = new Intent( MainActivity.this, PassengerActivity.class );
        startActivity( intent );
    }

    private void transitionToDriverActivity()
    {
        Intent intent = new Intent( MainActivity.this, DriverActivity.class );
        startActivity( intent );
    }
}
