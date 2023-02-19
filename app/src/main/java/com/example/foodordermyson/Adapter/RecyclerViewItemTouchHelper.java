package com.example.foodordermyson.Adapter;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback{
    ItemTouchHelperListener listener ;
    public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs, ItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(this.listener!=null){
            listener.onSwipe(viewHolder);
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null){
            View foreGroundView = ((CartAdapter.ViewHolder)viewHolder).cartlayout;
            super.onSelectedChanged(viewHolder, actionState);
            getDefaultUIUtil().onSelected(foreGroundView);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGroundView = ((CartAdapter.ViewHolder)viewHolder).cartlayout;
        getDefaultUIUtil().onDraw(c,recyclerView,foreGroundView,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGroundView = ((CartAdapter.ViewHolder)viewHolder).cartlayout;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foreGroundView,dX,dY,actionState,isCurrentlyActive);
    }


    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foreGroundView = ((CartAdapter.ViewHolder)viewHolder).cartlayout;
        getDefaultUIUtil().clearView(foreGroundView);
    }
}
