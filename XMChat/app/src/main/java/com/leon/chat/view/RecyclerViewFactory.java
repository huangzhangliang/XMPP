package com.leon.chat.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


public class RecyclerViewFactory {


    public static RecyclerView createVerticalRecyclerView(Context context){
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(layoutParams);
        return recyclerView;
    }

    public static RecyclerView createNoRefreshVerticalRecyclerView(Context context){
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return recyclerView;
    }

    public static RecyclerView createNoRefreshHorizontalXRecyclerView(Context context){
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        return recyclerView;
    }

}
