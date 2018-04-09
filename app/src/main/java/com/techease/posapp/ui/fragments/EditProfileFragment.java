package com.techease.posapp.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.posapp.R;
import com.techease.posapp.ui.models.JobsModel;
import com.techease.posapp.utils.AlertsUtils;
import com.techease.posapp.utils.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment {
    EditText ed_FirstName, ed_LastName, ed_Email, ed_MobileNo;
    String api_token, user_id;
    Button btn_done;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ed_FirstName = (EditText) view.findViewById(R.id.edit_firstName);
        ed_LastName = (EditText) view.findViewById(R.id.edit_lastName);
        ed_Email = (EditText) view.findViewById(R.id.edit_email);
        ed_MobileNo = (EditText) view.findViewById(R.id.edit_mobileNo);
        btn_done = view.findViewById(R.id.edit_done);

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        api_token = sharedPreferences.getString("api_token", "");
        user_id = sharedPreferences.getString("user_id", "");

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apicall();
                Fragment fragment = new UserProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_main, fragment).addToBackStack("").commit();
            }
        });
        return view;
    }

    private void apicall(){
        final String str_firstName = ed_FirstName.getText().toString().trim();
        final String str_lastName = ed_LastName.getText().toString().trim();
        final String str_Email = ed_Email.getText().toString().trim();
        final String str_Mobile = ed_MobileNo.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_EDIT_PROFILE
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "you got some error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("first_name", str_firstName);
                params.put("last_name", str_lastName);
                params.put("email", str_Email);
                params.put("mobile_no", str_Mobile);
                params.put("api_token", api_token);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

    }
}