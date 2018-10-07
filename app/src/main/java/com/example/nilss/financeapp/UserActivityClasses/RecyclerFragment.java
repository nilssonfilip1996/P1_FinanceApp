package com.example.nilss.financeapp.UserActivityClasses;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nilss.financeapp.OnCreateViewLIstener;
import com.example.nilss.financeapp.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerFragment extends Fragment {
    private UserController mUserController;
    private ArrayList<Transaction> mArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainAdapter mAdapter;
    private OnCreateViewLIstener mListener;

    public void onInit(OnCreateViewLIstener listener){
        this.mListener = listener;
    }

    public RecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_incomes_recycler, container, false);
        mArrayList = new ArrayList<>();
        initRecycler(view);
        if(mListener != null){
            mListener.onViewsInitialized();
        }
        return view;
    }

    private void initRecycler(View view) {
        this.mRecyclerView = view.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        this.mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter. MainAdapter is a selfwritten class.
        mAdapter = new MainAdapter(mUserController, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                mUserController.incomeListClicked(mArrayList.get(position));
            }
        });
    }

    public void setUserController(UserController userController) {
        this.mUserController = userController;
    }

    public void updateList(ArrayList<Transaction> transactionList) {
        mArrayList.clear();
        mArrayList.addAll(transactionList);
        mAdapter.notifyDataSetChanged();
    }

    public void appendToList(Transaction transaction) {
        mArrayList.add(transaction);
        mAdapter.notifyDataSetChanged();
    }
}
