package com.customer.shary.live.ui.live;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.customer.shary.live.R;
import com.customer.shary.live.ads.loadads;
import com.customer.shary.live.ui.live.datamodel.livemodel;
import com.customer.shary.live.ui.live.recycleview.adapter;
import com.customer.shary.live.ui.live.viewmodel.viewmodel;

import java.util.ArrayList;

public class live_fragment extends Fragment {

    private RecyclerView _live_recycler ;

    private ProgressDialog dialog;

    private com.customer.shary.live.ui.live.viewmodel.viewmodel viewmodel1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loadads.getInstance().mInterstitialAd(getActivity());

        View root = inflater.inflate(R.layout.live_list_layout, container, false);
        _live_recycler=root.findViewById(R.id._live_recycler);
        _live_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));


        viewmodel1 =new ViewModelProvider(this).get(viewmodel.class);
        dialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        dialog.setMessage(getResources().getString(R.string.load));
        dialog.setCancelable(false);

        dialog.show();

        final Observer<ArrayList<livemodel>> arrayListObservable  = new Observer<ArrayList<livemodel>>() {

            @Override
            public void onChanged(ArrayList<livemodel> livemodels) {

                if(livemodels.size()>0) {

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    adapter adapter = new adapter(getActivity(), livemodels);
                    _live_recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }



            }
        };


       viewmodel1.getText(getActivity()).observe(getActivity(),arrayListObservable);





        return root;
    }
}
