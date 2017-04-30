package es.uma.goingonapp.log_in.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import es.uma.goingonapp.R;
import es.uma.goingonapp.common.entities.User;
import es.uma.goingonapp.common.network.IUserProxy;
import es.uma.goingonapp.common.network.UserProxy;
import es.uma.goingonapp.log_in.activities.LogInActivity;

/**
 * Created by Alb_Erc on 18/04/2015.
 */
public class LogInFragment extends Fragment implements Response.Listener<User>, Response.ErrorListener {
    private EditText mNicknameEditText;
    private EditText mPasswordEditText;
    private Button mLogInButton;

    private IUserProxy mUserProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        this.mNicknameEditText = (EditText)view.findViewById(R.id.log_in_username);
        this.mPasswordEditText = (EditText)view.findViewById(R.id.log_in_password);
        this.mLogInButton = (Button)view.findViewById(R.id.log_in_button);

        this.mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = mNicknameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                mUserProxy = new UserProxy(getActivity());
                mUserProxy.getUser(nickname, password, LogInFragment.this, LogInFragment.this);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (this.mUserProxy != null) {
            this.mUserProxy.cancelRequests();
        }
    }

    @Override
    public void onResponse(User user) {
        Intent intent = this.getActivity().getIntent();
        intent.putExtra(LogInActivity.USER_EXTRA, user);
        this.getActivity().setResult(Activity.RESULT_OK, intent);
        this.getActivity().finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.networkResponse != null) {
            if (error.networkResponse.statusCode == 401) {
                Toast.makeText(this.getActivity(), "Unauthorized", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
