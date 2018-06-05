package com.fast0n.iliad.fragments.CreditEsteroFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast0n.iliad.R;
import java.util.List;

public class CustomAdapterCreditEstero extends RecyclerView.Adapter<CustomAdapterCreditEstero.MyViewHolder> {

    private List<DataCreditEsteroFragments> creditEsteroList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView, textView2, textView3;
        ImageView icon_info;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            textView2 = view.findViewById(R.id.textView2);
            textView3 = view.findViewById(R.id.textView3);
            icon_info = view.findViewById(R.id.icon_info);

        }
    }

    public CustomAdapterCreditEstero(List<DataCreditEsteroFragments> creditEsteroList) {
        this.creditEsteroList = creditEsteroList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataCreditEsteroFragments c = creditEsteroList.get(position);

        holder.textView2.setText(c.title);
        holder.textView3.setText(c.description);

        if (c.imgInfo == 0) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_call);
            holder.textView.setText(c.iconText);
        } else if (c.imgInfo == 1) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_email);
            holder.textView.setText(c.iconText);
        } else if (c.imgInfo == 2) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_gb);
            holder.textView.setText(c.iconText);
        } else if (c.imgInfo == 3) {
            holder.icon_info.setBackgroundResource(R.drawable.ic_mms);
            holder.textView.setText(c.iconText);
        }

    }

    @Override
    public int getItemCount() {
        return creditEsteroList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_info, parent, false);
        return new MyViewHolder(v);
    }
}