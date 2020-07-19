package com.customer.shary.live.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.CustomRecyclerView.VerticalSpacingItemDecorator;
import com.customer.shary.live.CustomRecyclerView.VideoPlayerRecyclerView;
import com.customer.shary.live.R;
import com.customer.shary.live.Sockets.SignallingClient;
import com.customer.shary.live.ui.home.Adapters.VideoPlayerRecyclerAdapter;
import com.customer.shary.live.ui.home.Callback.callback;
import com.customer.shary.live.ui.home.datamodel.ads;
import com.customer.shary.live.ui.home.datamodel.apidata;
import com.customer.shary.live.ui.home.datamodel.collection;

import java.util.ArrayList;
import java.util.Random;

public class HomeFragment extends Fragment implements callback {

    private HomeViewModel homeViewModel;

    static VideoPlayerRecyclerView mRecyclerView;

    private VideoPlayerRecyclerAdapter adapter;
    private ProgressDialog dialog;

    collection mcollection;
    ArrayList<collection> list;


    ArrayList<apidata> apidataArrayList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage(getResources().getString(R.string.load));
        dialog.setCancelable(false);

        list = new ArrayList<>();
        dialog.show();

        homeViewModel.getvideos(getActivity()).observe(getActivity(), new Observer<ArrayList<apidata>>() {
            @Override
            public void onChanged(ArrayList<apidata> apidata) {
                if (apidata.size() > 0) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        apidataArrayList = apidata;



                        for (int i = 0; i < apidata.size(); i++) {

                            Random r = new Random();
                            int i1 = r.nextInt(apidata.size() - i + 1) + i;
                            if(i==0)
                            {

                                // ads with shary
                                mcollection = new collection();

                                mcollection.setTypeee("0");
                                list.add(mcollection);

                                mcollection = new collection();

                                mcollection.setTypeee("1");
                                mcollection.setApidata(apidata.get(i));
                                //     mcollection.setApidata(apidata.get(i));
                                list.add(mcollection);


                            }
                            else if(i1==i)
                            {

                                // google ads
                                mcollection = new collection();
                                mcollection.setTypeee("2");

                                list.add(mcollection);
                                mcollection = new collection();


                                mcollection.setTypeee("1");
                                mcollection.setApidata(apidata.get(i));
                                //     mcollection.setApidata(apidata.get(i));
                                list.add(mcollection);

                            }
                         else {


                                mcollection = new collection();
                                mcollection.setTypeee("1");
                                mcollection.setApidata(apidata.get(i));
                                //     mcollection.setApidata(apidata.get(i));
                                list.add(mcollection);


                            }
                        }

                        Log.e("ds", "fds");


                        initRecyclerView(list, getActivity());
                    }
                }


            }
        });
        SignallingClient.getInstance(getActivity()).init();
        return root;
    }


    private void initRecyclerView(ArrayList<collection> apidata, Context context) {


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);


        ArrayList<MediaObject> mediaObjects5 = new ArrayList<MediaObject>();

        for (int i = 0; i < apidata.size(); i++) {
            if (apidata.get(i).getTypeee().equals("1")) {
                mediaObjects5.add(apidata.get(i).getApidata().getMediaObject());
                Log.e("err", String.valueOf(i));
            } else {
                mediaObjects5.add(new MediaObject("", "", "", ""));
            }
        }

        //.....................................

        mRecyclerView.setMediaObjects(mediaObjects5);

        adapter = new VideoPlayerRecyclerAdapter(getActivity(), this, context, apidata, initGlide());
        mRecyclerView.setAdapter(adapter);
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.mainbg)
                .error(R.drawable.mainbg);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    public void position(int x, String like_dis) {

    }

    @Override
    public void share_position(int y, String share_un) {

    }

    @Override
    public void unfollow_position(int u, String un_follow) {

    }
}