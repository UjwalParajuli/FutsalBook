package com.example.futsalbook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.example.futsalbook.models.User;
import com.example.futsalbook.utils.ErrorUtils;
import com.example.futsalbook.utils.ItemClickSupport;
import com.example.futsalbook.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarksFragment extends Fragment {
    private TextView text_view_saved_futsals, text_view_not_saved_message;
    private RecyclerView recycler_view_saved_items;
    private ArrayList<FutsalModel> futsalModelArrayList;
    private FutsalAdapter futsalAdapter;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        getActivity().setTitle("Bookmarked");

        text_view_saved_futsals = view.findViewById(R.id.text_view_saved_futsals);
        text_view_not_saved_message = view.findViewById(R.id.text_view_not_saved_message);
        recycler_view_saved_items = view.findViewById(R.id.recycler_view_saved_items);

        futsalModelArrayList = new ArrayList<>();
        futsalAdapter = new FutsalAdapter(futsalModelArrayList, getContext());

        user = SharedPrefManager.getInstance(getContext()).getUser();

        getUserBookmarks();

        return view;
    }

    private void getUserBookmarks(){
        String url = "https://rajkumargurung.com.np/futsal/get_user_bookmarks.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    text_view_saved_futsals.setVisibility(View.GONE);
                    recycler_view_saved_items.setVisibility(View.GONE);
                    text_view_not_saved_message.setVisibility(View.VISIBLE);

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
                        recycler_view_saved_items.setLayoutManager(linearLayoutManager);
                        recycler_view_saved_items.setAdapter(futsalAdapter);
                        //recycler_view_futsals.addItemDecoration(new SpacesItemDecoration(20));
                        futsalAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recycler_view_saved_items).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getUser_id()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
