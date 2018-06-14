package com.fast0n.ipersonalarea.ConsumptionDetailsActivity;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Model extends ExpandableGroup<ModelChildren> {

    public Model(String title, List<ModelChildren> items) {
        super(title, items);
    }
}

