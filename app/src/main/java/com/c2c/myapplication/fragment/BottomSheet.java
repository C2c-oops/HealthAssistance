package com.c2c.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.c2c.myapplication.R;
import com.c2c.myapplication.utils.Config;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class BottomSheet extends BottomSheetDialogFragment {

    private TextView tVDateTime, tVAffectCount, tVDAffect, tVTestCount,
            tVActiveCount, tVRecoverCount, tVDRecover, tVDeceaseCount, tVDDecease;
    private RequestQueue mQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_bottom_sheet, container, false);

        tVDateTime = v.findViewById(R.id.tVBSDateTime);
        tVAffectCount = v.findViewById(R.id.tvAffectCountIndia);
        tVDAffect = v.findViewById(R.id.tvDeltaAffectCountIndia);
        tVTestCount = v.findViewById(R.id.tvTestCountIndia);
        tVActiveCount = v.findViewById(R.id.tvActiveCountIndia);
        tVRecoverCount = v.findViewById(R.id.tvRecoverCountIndia);
        tVDRecover = v.findViewById(R.id.tvDeltaRecoverCountIndia);
        tVDeceaseCount = v.findViewById(R.id.tvDeceasedCountIndia);
        tVDDecease = v.findViewById(R.id.tvDeltaDeceasedCountIndia);

        mQueue = Volley.newRequestQueue(v.getContext());

        getTotalData();
        return v;
    }

    private void getTotalData() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        @SuppressLint("SetTextI18n")
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Config.DATA_URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray(Config.JSON_ARRAY);
                        for (int i = 0; i < 1; i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            String confirmi = json.getString(Config.TAG_CONFIRMED);
                            String recoveri = json.getString(Config.TAG_RECOVERED);
                            String deathi = json.getString(Config.TAG_DEATHS);
                            String activei = json.getString(Config.TAG_ACTIVE);
                            String datetimei = json.getString(Config.TAG_DATETIME);
                            String deltaconfirmi = json.getString(Config.TAG_DCONFIRMED);
                            String deltarecoveri = json.getString(Config.TAG_DRECOVERED);
                            String deltadeathi = json.getString(Config.TAG_DDEATHS);

                            nf.setGroupingUsed(true);
                            tVAffectCount.setText(nf.format(Integer.parseInt(confirmi)));
                            tVRecoverCount.setText(nf.format(Integer.parseInt(recoveri)));
                            tVDeceaseCount.setText(nf.format(Integer.parseInt(deathi)));
                            tVActiveCount.setText(nf.format(Integer.parseInt(activei)));
                            tVDateTime.setText(datetimei);
                            tVDAffect.setText(nf.format(Integer.parseInt("+"+deltaconfirmi)));
                            tVDRecover.setText(nf.format(Integer.parseInt("+"+deltarecoveri)));
                            tVDDecease.setText(nf.format(Integer.parseInt("+"+deltadeathi)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Config.DATA_URLT, null,
                response -> {
                    try {
                        JSONObject jsonArray = response.getJSONObject(Config.JSON_TOBJECT);
                        String tested = jsonArray.getString(Config.TAG_TESTED);
                        tVTestCount.setText(nf.format(Integer.parseInt(tested)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        mQueue.add(request);
        mQueue.add(jsonObjectRequest);
    }

}
