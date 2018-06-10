package com.fast0n.iliad.fragments.CreditFragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fast0n.iliad.R;

import java.util.List;

public class CustomAdapterCredit extends RecyclerView.Adapter<CustomAdapterCredit.MyViewHolder> {

    Context context;
    private List<DataCreditFragments> creditList;

    CustomAdapterCredit(Context context, List<DataCreditFragments> creditList) {
        this.context = context;
        this.creditList = creditList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataCreditFragments c = creditList.get(position);

        holder.textView2.setText(c.title);
        holder.textView3.setText(c.description);
        Glide.with(context).load(c.url).into(holder.icon_info);
        holder.textView.setText(c.iconText);

    }

    @Override
    public int getItemCount() {
        return creditList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_info, parent, false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView, textView2, textView3;
        ImageView icon_info;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
            textView3 = view.findViewById(R.id.textView3);
            icon_info = view.findViewById(R.id.icon_info);

        }
    }
}