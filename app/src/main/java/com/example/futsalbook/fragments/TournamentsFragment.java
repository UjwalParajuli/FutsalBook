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
import com.example.futsalbook.TournamentDetailsActivity;
import com.example.futsalbook.adapters.FutsalAdapter;
import com.example.futsalbook.adapters.TournamentAdapter;
import com.example.futsalbook.models.FutsalModel;
import com.example.futsalbook.models.TournamentModel;
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

public class TournamentsFragment extends Fragment {
    private RecyclerView recycler_view_tournaments;
    private TextView text_view_not_found_tournaments, text_view_upcoming_tournaments;
    private ArrayList<TournamentModel> tournamentModelArrayList;
    private TournamentAdapter tournamentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournaments, container, false);

        getActivity().setTitle("Tournaments");

        recycler_view_tournaments = view.findViewById(R.id.recycler_view_tournaments);
        text_view_not_found_tournaments = view.findViewById(R.id.text_view_not_found_tournaments);
        text_view_upcoming_tournaments = view.findViewById(R.id.text_view_upcoming_tournaments);

        tournamentModelArrayList = new ArrayList<>();
        tournamentAdapter = new TournamentAdapter(tournamentModelArrayList, getContext());

        getTournaments();

        return view;
    }

    private void getTournaments(){
        String url = "https://rajkumargurung.com.np/futsal/get_tournaments.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    text_view_upcoming_tournaments.setVisibility(View.GONE);
                    recycler_view_tournaments.setVisibility(View.GONE);
                    text_view_not_found_tournaments.setVisibility(View.VISIBLE);

                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            int futsal_id = jsonResponse.getInt("futsal_id");
                            int tournament_id = jsonResponse.getInt("tournament_id");
                            String tournament_name = jsonResponse.getString("tournament_name");
                            String description = jsonResponse.getString("description");
                            String start_date = jsonResponse.getString("start_date");
                            String end_date = jsonResponse.getString("end_date");
                            String registration_deadline_date = jsonResponse.getString("registration_deadline_date");
                            String registration_fee = jsonResponse.getString("registration_fee");
                            String first_prize = jsonResponse.getString("first_prize");
                            String second_prize = jsonResponse.getString("second_prize");
                            String organized_by = jsonResponse.getString("organized_by");
                            String organizer_contact = jsonResponse.getString("organizer_contact");
                            String futsal_name = jsonResponse.getString("futsal_name");
                            String futsal_location = jsonResponse.getString("location");
                            String banner = jsonResponse.getString("banner");

                            TournamentModel tournamentModel = new TournamentModel(tournament_id, futsal_id, tournament_name, description, start_date, end_date, registration_deadline_date, registration_fee, first_prize, second_prize, organized_by, organizer_contact, futsal_name, futsal_location, banner);
                            tournamentModelArrayList.add(tournamentModel);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recycler_view_tournaments.setLayoutManager(linearLayoutManager);
                        recycler_view_tournaments.setAdapter(tournamentAdapter);
                        //recycler_view_futsals.addItemDecoration(new SpacesItemDecoration(20));
                        tournamentAdapter.notifyDataSetChanged();

                        ItemClickSupport.addTo(recycler_view_tournaments).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                TournamentModel tournamentModel = tournamentModelArrayList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("tournament_details", tournamentModel);
                                Intent intent = new Intent(getContext(), TournamentDetailsActivity.class);
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
