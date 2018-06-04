package com.fast0n.iliad.fragments.InfoFragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast0n.iliad.R;

import java.util.List;

public class CustomAdapterInfo extends RecyclerView.Adapter<CustomAdapterInfo.MyViewHolder> {

    private List<DataInfoFragments> infoList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView, textView2, textView3, textView4;
        ImageView icon_info;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            textView2 = view.findViewById(R.id.textView2);
            textView3 = view.findViewById(R.id.textView3);
            textView4 = view.findViewById(R.id.textView4);
            icon_info = view.findViewById(R.id.icon_info);
        }
    }

    public CustomAdapterInfo(List<DataInfoFragments> infoList) {
        this.infoList = infoList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataInfoFragments c = infoList.get(position);

        holder.textView2.setText(c.textView2);
        holder.textView3.setText(c.textView3);
        holder.textView4.setText(c.textView4);

        if (c.imgInfo == 0) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_adress);
            holder.textView.setText(c.textView);
        } else if (c.imgInfo == 1) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_credit_card);
            holder.textView.setText("");
        } else if (c.imgInfo == 2) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_email);
            holder.textView.setText(c.textView);
        } else if (c.imgInfo == 3) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_puk);
            holder.textView.setText("Visualizza");
        } else if (c.imgInfo == 4) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_password);
            holder.textView.setText(c.textView);

        }

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
}