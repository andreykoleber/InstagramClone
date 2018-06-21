package com.parse.starter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AuthorizationFragment extends Fragment {

    enum AuthorizationMode {REGISTRATION, LOGIN}

    private AuthorizationMode mAuthorizationMode;
    private Button btnSignUp;
    private Button btnLogin;
    private String userName;
    private String password;
    private ParseUser user;
    private EditText edtUsername;
    private EditText edtPassword;


    public static AuthorizationFragment newInstance() {

        Bundle args = new Bundle();

        AuthorizationFragment fragment = new AuthorizationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main, container, false);

        mAuthorizationMode = AuthorizationMode.REGISTRATION;

        RelativeLayout rlBackground = (RelativeLayout) view.findViewById(R.id.rlBackground);
        rlBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });

        edtUsername = (EditText) view.findViewById(R.id.edtUsername);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    performAccess(getUsername(), getPassword());
                }
                return false;
            }
        });

        user = new ParseUser();

        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUsernameAndPasswordValid()) {
                    if (isUsernameAndPasswordValid()) {
                        performAccess(getUsername(), getPassword());
                    }
                }
            }
        });


        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUsernameAndPasswordValid()) {
                    performAccess(getUsername(), getPassword());
                }
            }
        });

        TextView txvSwitch = (TextView) view.findViewById(R.id.txvSwitch);
        txvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mAuthorizationMode) {
                    case REGISTRATION:
                        mAuthorizationMode = AuthorizationMode.LOGIN;
                        switchButtons();
                        break;
                    case LOGIN:
                        mAuthorizationMode = AuthorizationMode.REGISTRATION;
                        switchButtons();
                        break;
                }
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ((InstagramActivity) getActivity()).showFragment(UserListFragment.newInstance());
        }


        ParseAnalytics.trackAppOpenedInBackground(getActivity().getIntent());

        return view;
    }

    private void performAccess(String username, String password) {
        if (isUsernameAndPasswordValid()) {
            switch (mAuthorizationMode) {
                case LOGIN:
                    logIn(username, password);
                    break;
                case REGISTRATION:
                    signUp(username, password);
                    break;
            }
        }
    }

    private String getPassword() {
        return edtPassword.getText().toString();
    }

    private String getUsername() {
        return edtUsername.getText().toString();
    }

    private void logIn(String userName, String password) {
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    showMessage(R.string.your_are_successfully_loged_in);
                } else {
                    showMessage(e.getMessage());
                }
            }
        });
    }

    private void signUp(String userName, String password) {
        user.setUsername(userName);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    showMessage(R.string.your_are_successfully_sign_up);
                } else {
                    showMessage(e.getMessage());
                }
            }
        });
    }

    private boolean isUsernameAndPasswordValid() {
        userName = edtUsername.getText().toString();
        if (isUsernameInvalid()) {
            showMessage(R.string.please_enter_your_username);
            return false;
        }

        password = edtPassword.getText().toString();
        if (isPasswordInvalid()) {
            showMessage(R.string.please_enter_your_password);
            return false;
        }

        return true;
    }

    private void showMessage(int stringResourceId) {
        Toast.makeText(getActivity(), stringResourceId, Toast.LENGTH_LONG).show();
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private boolean isUsernameInvalid() {
        return (userName.matches("") || userName == null);
    }

    private boolean isPasswordInvalid() {
        return (password.matches("") || password == null);
    }

    private void switchButtons() {
        switch (mAuthorizationMode) {
            case REGISTRATION:
                btnSignUp.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);
                break;
            case LOGIN:
                btnSignUp.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                break;
        }
    }
}
