package es.uma.goingonapp.register.fragments;

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
import es.uma.goingonapp.register.activities.RegisterActivity;

/**
 * Created by Alb_Erc on 18/04/2015.
 */
public class RegisterFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {
    private EditText mNicknameEditText;
    private EditText mPasswordEditText;
    private EditText mCityEditText;
    private Button mRegisterButton;

    private User mUser;

    private IUserProxy mUserProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        this.mNicknameEditText = (EditText)view.findViewById(R.id.register_username);
        this.mPasswordEditText = (EditText)view.findViewById(R.id.register_password);
        this.mCityEditText = (EditText)view.findViewById(R.id.register_city);
        this.mRegisterButton = (Button)view.findViewById(R.id.register_button);

        this.mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = mNicknameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                String city = mCityEditText.getText().toString().trim();

                mUser = new User(nickname, password, city, null);

                mUserProxy = new UserProxy(getActivity());
                mUserProxy.createUser(mUser, RegisterFragment.this, RegisterFragment.this);
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
    public void onResponse(String message) {
        Intent intent = this.getActivity().getIntent();
        intent.putExtra(RegisterActivity.USER_EXTRA, this.mUser);
        this.getActivity().setResult(Activity.RESULT_OK, intent);
        this.getActivity().finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this.getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
    }
}
