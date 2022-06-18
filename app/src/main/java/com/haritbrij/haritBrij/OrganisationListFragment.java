package com.haritbrij.haritBrij;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.haritbrij.haritBrij.models.Organisation;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrganisationListFragment extends Fragment {
    AdminViewModel viewModel;

    public OrganisationListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organisation_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        EditText searchOrganisationEditText = view.findViewById(R.id.searchOrganisationEditText);
        ImageView searchOrganisationImageView = view.findViewById(R.id.searchOrganisationIcon);

        RecyclerView searchOrganisationRecyclerView = view.findViewById(R.id.searchOrganisationRecyclerView);
        searchOrganisationRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        String baseUrl = VolleySingleton.getBaseUrl();

        String myUrl = baseUrl + "getalluser.php";
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try{
                        //Create a JSON object containing information from the API.
                        JSONObject myJsonObject = new JSONObject(response);
                        JSONArray jsonArray = myJsonObject.getJSONArray("body");

                        ArrayList<Organisation> orgList = new ArrayList<>();

                        for(int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
                            JSONObject indexedOrg = jsonArray.getJSONObject(jsonArrayIndex);
                            Organisation org = new Organisation();
                            org.id = indexedOrg.getString("uid");
                            org.name = indexedOrg.getString("name");
                            org.target = indexedOrg.getString("target");
                            org.image = indexedOrg.getString("display");

                            orgList.add(org);
                        }

                        Log.d(getClass().getSimpleName(), orgList.toString());

                        viewModel.setOrgList(orgList);
                        searchOrganisationRecyclerView.setAdapter(new OrgListAdapter(orgList));
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                },
                error -> {
                     Log.e(getClass().getSimpleName(), error.toString());
                });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);
    }
}
