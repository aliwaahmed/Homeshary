package com.customer.shary.live.ui.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.CustomRecyclerView.VerticalSpacingItemDecorator;
import com.customer.shary.live.GetingDeviceInfo.GettingDeviceInfo;
import com.customer.shary.live.R;
import com.customer.shary.live.ads.loadads;
import com.customer.shary.live.ui.home.datamodel.apidata;
import com.customer.shary.live.ui.search.chips.chipsrec;
import com.customer.shary.live.ui.search.rec.adapter;
import com.customer.shary.live.ui.search.rec.adaptertrend;
import com.customer.shary.live.ui.search.searchkeyword.chipsclicked;
import com.customer.shary.live.ui.search.searchkeyword.data;
import com.customer.shary.live.ui.search.searchkeyword.searchkeyword;
import com.customer.shary.live.ui.search.trends.trendviewholder;
import com.google.android.gms.ads.InterstitialAd;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class search extends Fragment implements chipsclicked {


    private AutoCompleteTextView text1;
    private trendviewholder trendviewholder1;
    private RecyclerView mRecyclerView, trends, _chips_rec;
    private com.customer.shary.live.ui.search.searchkeyword.searchkeyword searchkeyword;
    String[] languages;
    ArrayAdapter adapter1;
    ArrayList<apidata> list;

    private com.customer.shary.live.ui.search.rec.adapter adapter;
    private Context context;
    private ImageView _close_search;
    adaptertrend adapter_trend;
    private ProgressDialog dialog;
    private InterstitialAd mInterstitialAd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        trendviewholder1 =
                ViewModelProviders.of(this).get(trendviewholder.class);
        loadads.getInstance().loadRewardedVideoAd(getActivity());

        searchkeyword = ViewModelProviders.of(this).get(searchkeyword.class);
        View v = inflater.inflate(R.layout.search_layout, container, false);
        context = getActivity().getApplicationContext();
        _close_search = v.findViewById(R.id._close_search);
        text1 = (AutoCompleteTextView) v.findViewById(R.id.multiAutoCompleteTextView1);
        _chips_rec = v.findViewById(R.id._chips_rec);
        mRecyclerView = v.findViewById(R.id.searchlist);
        trends = v.findViewById(R.id.trand);
        dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage(getResources().getString(R.string.load));
        dialog.setCancelable(false);
        dialog.show();
        searchkeyword.getkeyword(getActivity()).observe(getActivity(), new Observer<ArrayList<data>>() {
            @Override
            public void onChanged(ArrayList<data> data) {

                if (data.size() > 0) {

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    languages = new String[data.size()];
                    for (int i = 0; i < data.size(); i++) {
                        languages[i] = data.get(i).getKey();
                        Log.e("name", languages[i].toString());
                    }

                    adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
                    text1.setAdapter(adapter1);
                    // text1.showDropDown();


                }

            }
        });


        searchkeyword.getcat(getActivity()).observe(getActivity(), new Observer<ArrayList<data>>() {
            @Override
            public void onChanged(ArrayList<data> data) {
                if (data.size() > 0) {
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    manager.setOrientation(RecyclerView.HORIZONTAL);
                    _chips_rec.setLayoutManager(manager);
                    _chips_rec.setAdapter(new chipsrec(search.this::chips, data));
                }
            }
        });

        trendviewholder1.getvideos(getActivity()).observe(getActivity(), new Observer<ArrayList<apidata>>() {
            @Override
            public void onChanged(ArrayList<apidata> apidata) {


                Log.e("sizeeeeali", String.valueOf(apidata.size()));

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                trends.setLayoutManager(layoutManager);
                VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
                trends.addItemDecoration(itemDecorator);


                ArrayList<MediaObject> mediaObjects5 = new ArrayList<MediaObject>();

                for (int i = 0; i < apidata.size(); i++) {
                    mediaObjects5.add(apidata.get(i).getMediaObject());
                    Log.e("err", String.valueOf(i));
                }


                adapter_trend = new adaptertrend(apidata, getContext());
                trends.setAdapter(adapter_trend);

            }
        });


        text1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadSearch(text1.getText().toString());

            }
        });


        _close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text1.setText("");
            }
        });


        return v;
    }


    //TODO:fav_rec PRODUCT
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> list1 = new ArrayList<>();
    apidata m_apimodel = new apidata();
    MediaObject mediaObjects;
    ArrayList<apidata> Objects = new ArrayList<>();

    public void loadSearch(String s) {
        img = new ArrayList<>();
        list1 = new ArrayList<>();
        m_apimodel = new apidata();
        Objects = new ArrayList<>();


        GettingDeviceInfo gettingDeviceInfo = GettingDeviceInfo.getInstance(getActivity());
        SharedPreferences prefs;
        prefs = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/products/search/" + s + "?user_id=" + prefs.getString("login_id", "-2") + "&user_local_ip=" + gettingDeviceInfo.GetIp() +
                        "&user_mac_address=" + gettingDeviceInfo.getMacAddr() + "&user_location=" + gettingDeviceInfo.GetLocation())
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                String res = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {

                        if ((jsonArray.getJSONObject(i).getJSONArray("video").length() > 0)) {

                            img = new ArrayList<>();
                            m_apimodel = new apidata();
                            list1 = new ArrayList<>();

                            if (i == 0) {
                                m_apimodel.setMediatype(1);
                            }
                            m_apimodel.setMediatype(0);
                            m_apimodel.setUser_rate(jsonArray.getJSONObject(i).getString("user_rate"));
                            Log.e("product_link", jsonArray.getJSONObject(i).getString("product_link"));
                            m_apimodel.setProduct_link(jsonArray.getJSONObject(i).getString("product_link"));
                            m_apimodel.setProduct_id(jsonArray.getJSONObject(i).getInt("id"));
                            m_apimodel.setName(jsonArray.getJSONObject(i).getString("name"));
                            m_apimodel.setViews(jsonArray.getJSONObject(i).getString("views"));
                            m_apimodel.setDetails(jsonArray.getJSONObject(i).getString("details"));
                            m_apimodel.setCategory(jsonArray.getJSONObject(i).getString("category"));
                            m_apimodel.setStore_name(jsonArray.getJSONObject(i).getString("store_name"));
                            m_apimodel.setVideo_link(jsonArray.getJSONObject(i).getString("video_link") + jsonArray.getJSONObject(i).getJSONArray("video").get(0));
                            m_apimodel.setLikes(jsonArray.getJSONObject(i).getString("likes"));
                            m_apimodel.setUser_store_follow(jsonArray.getJSONObject(i).getString("user_store_follow"));
                            m_apimodel.setUser_share(jsonArray.getJSONObject(i).getString("user_share"));
                            m_apimodel.setComment_number(jsonArray.getJSONObject(i).getInt("comments"));
                            m_apimodel.setSeller_id(jsonArray.getJSONObject(i).getString("user_id"));
                            m_apimodel.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            m_apimodel.setNew_price(jsonArray.getJSONObject(i).getString("new_price"));
                            m_apimodel.setUser_like(jsonArray.getJSONObject(i).getString("user_like"));
                            m_apimodel.setStore_image(jsonArray.getJSONObject(i).getString("store_image"));
                            m_apimodel.setImage_link(jsonArray.getJSONObject(i).getString("video_thumb"));
                            m_apimodel.setShare(jsonArray.getJSONObject(i).getString("share"));
                            m_apimodel.setQuantity(String.valueOf(jsonArray.getJSONObject(i).optInt("quantity")));
                            m_apimodel.setRate_count(String.valueOf(jsonArray.getJSONObject(i).getInt("rate")));
                            m_apimodel.setVideo(list1);
                            for (int x = 0; x < jsonArray.getJSONObject(i).getJSONArray("video").length(); x++) {

                                list1.add(m_apimodel.getVideo_link() + jsonArray.getJSONObject(i).getJSONArray("video").get(x));
                            }
                            mediaObjects = new MediaObject("",
                                    m_apimodel.getVideo_link()
                                    , m_apimodel.getImage_link(), "");
                            m_apimodel.setMediaObject(mediaObjects);

                            Objects.add(m_apimodel);


                        }
                    }


                    list = Objects;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initRecyclerView(Objects, getContext());

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });

    }


    private void initRecyclerView(ArrayList<apidata> apidata, Context context) {


        Log.e("sizeeeeali", String.valueOf(apidata.size()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);


        ArrayList<MediaObject> mediaObjects5 = new ArrayList<MediaObject>();

        for (int i = 0; i < apidata.size(); i++) {
            mediaObjects5.add(apidata.get(i).getMediaObject());
            Log.e("err", String.valueOf(i));
        }


        adapter = new adapter(apidata, context);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void chips(String s) {

        text1.setText("");
        text1.setText(s);
        loadSearch(text1.getText().toString());


    }
//    private void filter(String text,ArrayList<apidata> apidataArrayList) {
//        ArrayList<apidata> filteredList = new ArrayList<>();
//
//        for (apidata item :apidataArrayList ) {
//            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(item);
//            }
//        }
//
//        adapter.filterList(filteredList);
//    }
//


}
