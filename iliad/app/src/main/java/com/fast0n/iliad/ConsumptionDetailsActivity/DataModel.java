package com.fast0n.iliad.ConsumptionDetailsActivity;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import com.fast0n.iliad.R;

public class DataModel extends ChildViewHolder {

    private TextView a,b,c,d,e,f;

    public DataModel(View itemView) {
        super(itemView);

        a =  itemView.findViewById(R.id.a);
        b =  itemView.findViewById(R.id.b);
        c =  itemView.findViewById(R.id.c);
        d =  itemView.findViewById(R.id.d);
        e =  itemView.findViewById(R.id.e);
        f =  itemView.findViewById(R.id.f);

    }

    public void onBind(ModelChildren modelChildren) {
        a.setText(modelChildren.getA());
        b.setText(modelChildren.getB());
        c.setText(modelChildren.getC());
        d.setText(modelChildren.getD());
        e.setText(modelChildren.getE());
        f.setText(modelChildren.getF());

    }
}
