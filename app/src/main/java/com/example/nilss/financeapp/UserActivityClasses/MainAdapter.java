package com.example.nilss.financeapp.UserActivityClasses;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nilss.financeapp.R;

import java.util.ArrayList;
import java.util.List;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    private List<Transaction> mArrayList;
    private UserController mUserController;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public MainAdapter(UserController mUserController, ArrayList arraylist) {
        this.mUserController = mUserController;
        this.mArrayList = arraylist;
    }


    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder viewHolder, int i) {
        viewHolder.datetv.setText(mArrayList.get(i).getDate());
        viewHolder.titletv.setText(mArrayList.get(i).getTitle());
        viewHolder.categorytv.setText(mArrayList.get(i).getCategory());
        String amount = String.valueOf(mArrayList.get(i).getAmount());
        viewHolder.amounttv.setText(amount+"kr");
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView datetv;
        public TextView titletv;
        public TextView categorytv;
        public TextView amounttv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.datetv = itemView.findViewById(R.id.datetv);
            this.titletv = itemView.findViewById(R.id.titletv);
            this.categorytv = itemView.findViewById(R.id.categorytv);
            this.amounttv = itemView.findViewById(R.id.amounttv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            mListener.onItemClicked(pos);
                        }
                    }
                }
            });
        }
    }
}
