package com.example.futsalbook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.futsalbook.FutsalDetailsActivity;
import com.example.futsalbook.R;
import com.example.futsalbook.adapters.FutsalAdapter;
import com.example.futsalbook.models.FutsalModel;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.ItemClickSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    private RecyclerView recycler_view_futsals;
    private TextView text_view_heading_1, text_view_not_found;
    private ArrayList<FutsalModel> futsalModelArrayList;
    private FutsalAdapter futsalAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recycler_view_futsals = view.findViewById(R.id.recycler_view_futsals);
        text_view_heading_1 = view.findViewById(R.id.text_view_heading_1);
        text_view_not_found = view.findViewById(R.id.text_view_not_found);

        futsalModelArrayList = new ArrayList<>();

        futsalAdapter = new FutsalAdapter(futsalModelArrayList, getContext());

        getFutsals();

        return view;
    }

    private void getFutsals(){
        String url = "https://rajkumargurung.com.np/futsal/get_futsals.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    text_view_heading_1.setVisibility(View.GONE);
                    recycler_view_futsals.setVisibility(View.GONE);
                    text_view_not_found.setVisibility(View.VISIBLE);

                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            int futsal_id = jsonResponse.getInt("futsal_id");
                            int price_per_hour = jsonResponse.getInt("price_per_hour");
                            String futsal_name = jsonResponse.getString("futsal_name");
                            String location = jsonResponse.getString("location");
                            String description = jsonResponse.getString("description");
                            String phone = jsonResponse.getString("phone");
                            String image = jsonResponse.getString("image");

                            FutsalModel futsalModel = new FutsalModel(futsal_id, price_per_hour, futsal_name, location, description, phone, image);
                            futsalModelArrayList.add(futsalModel);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recycler_view_futsals.setLayoutManager(linearLayoutManager);
                        recycler_view_futsals.setAdapter(futsalAdapter);
                        //recycler_view_futsals.addItemDecoration(new SpacesItemDecoration(20));
                        futsalAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recycler_view_futsals).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                FutsalModel futsalModel = futsalModelArrayList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("futsal_details", futsalModel);
                                Intent intent = new Intent(getContext(), FutsalDetailsActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });


                    }

                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);

    }

}
