package com.example.timecapsule.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timecapsule.R;
import com.example.timecapsule.adapter.MyNewsRecyclerAdapter;
import com.example.timecapsule.bean.NewsList;
import com.example.timecapsule.http.DataBean;
import com.example.timecapsule.http.HttpReq;
import com.example.timecapsule.http.HttpResponse;
import com.example.timecapsule.http.RetrofitManager;
import com.example.timecapsule.utils.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class NewsListFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    private String mParam1;
    private String mParam2;

    private List<NewsList> mList = new ArrayList<>();
    private MyNewsRecyclerAdapter adapter;

    public NewsListFragment() {

    }


    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerview.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), 1));
        adapter = new MyNewsRecyclerAdapter(mList, getActivity());
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyNewsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewDetailsActivity.class);
                intent.putExtra("item", mList.get(position));
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        getNewsList("top");

        return view;
    }

    private void getNewsList(String type) {
        RetrofitManager.getInstance().getNewsList(type, HttpReq.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResponse<DataBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull HttpResponse<DataBean> listHttpResponse) {
                        if (listHttpResponse.getErrorCode() == 0) {
                            List<NewsList> list = listHttpResponse.getResults().getData();
                            mList.clear();
                            mList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("qe2",e.toString());
                        Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}