package com.fast0n.ipersonalarea.fragments.InfoFragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fast0n.ipersonalarea.R;
import com.fast0n.ipersonalarea.fragments.AboutFragment.DataAboutFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterInfo extends RecyclerView.Adapter<CustomAdapterInfo.MyViewHolder> {

    private List<DataInfoFragments> infoList;
    private Context context;

    public CustomAdapterInfo(Context context, List<DataInfoFragments> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    public CustomAdapterInfo(ArrayList<DataAboutFragment> dataAboutFragments, Context context) {
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataInfoFragments c = infoList.get(position);

        holder.textView2.setText(c.textView2);
        holder.textView3.setText(c.textView3);
        holder.textView4.setText(c.textView4);
        Glide.with(context).load(c.url).into(holder.icon_info);
         Glide.with(context).load(c.url1).into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_info, parent, false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView2, textView3, textView4;
        ImageView icon_info, icon;

        MyViewHolder(View view) {
            super(view);
            textView2 = view.findViewById(R.id.textView2);
            textView3 = view.findViewById(R.id.textView3);
            textView4 = view.findViewById(R.id.textView4);
            icon_info = view.findViewById(R.id.icon_info);
            icon = view.findViewById(R.id.icon);

        }
    }
}