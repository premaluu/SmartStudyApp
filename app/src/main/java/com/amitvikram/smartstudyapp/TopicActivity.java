package com.amitvikram.smartstudyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private Intent intent;
    //Todo: Change URL when integrate on server
    private static String URL_RESULT = "http://smartstudyapp.000webhostapp.com/read_detail.php";
    List<Topic> topicList;
    RecyclerView recyclerView;
    int previousSIZE;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorLayout;
    private TopicAdapter adapter;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        intent = getIntent();
        category = intent.getStringExtra("category");
        Toast.makeText(this, category, Toast.LENGTH_LONG).show();
        recyclerView = findViewById(R.id.result_recyclerView_topic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        topicList = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.refresh_layout_topic);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//        loadResults();
        onLoadingSwipeRefresh();
        errorLayout = findViewById(R.id.errorLayout1);
        previousSIZE = topicList.size();
    }

    @Override
    public void onRefresh() {
        if (previousSIZE <= topicList.size()) {
            swipeRefreshLayout.setRefreshing(false);
        } else {
            loadResults();
        }
    }

    private void onLoadingSwipeRefresh() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadResults();
            }
        });
    }

    private void loadResults() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        swipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESULT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject topic = array.getJSONObject(i);

                                //adding the product to product list
                                topicList.add(new Topic(
                                        topic.getString("topic_name").trim(), topic.getString("topic_details").trim(), topic.getString("model_name").trim(), topic.getString("model_link").trim()
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            adapter = new TopicAdapter(topicList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            initListener();
                            if (topicList.size() == 0) {
                                errorLayout.setVisibility(View.VISIBLE);
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error " + e.toString(), Toast.LENGTH_LONG).show();
                            errorLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorLayout.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("details_type", "topic");
                params.put("category_name", category);
                return params;
            }
        };


        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void initListener() {
        adapter.setOnItemClickListener(new TopicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(TopicActivity.this, DetailsActivity.class);
                intent.putExtra("topic_name", topicList.get(position).getTopicName());
                intent.putExtra("topic_details", topicList.get(position).getTopicDetails());
                intent.putExtra("model_link", topicList.get(position).getModelLink());
                intent.putExtra("model_name", topicList.get(position).getModelName());
                startActivity(intent);
            }
        });
    }
}