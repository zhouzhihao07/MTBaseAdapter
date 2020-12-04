package com.zhouzhihao.mtbaseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouzhihao on 2017/3/16.
 */

public abstract class MTBaseAdapter<T,P extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public Context mContext;
    public List<T> data=new ArrayList<>();
    public boolean empty_flag = false;
    private ViewGroup viewGroup;

    public MTBaseAdapter setMultiType(boolean multiType) {
        isMultiType = multiType;
        return this;
    }

    public boolean isMultiType=false;
    //建立枚举 3个item 类型
    public enum ITEM_TYPE {
        ITEM1, TYPE_EMPTY, TYPE_FOOTER
    }
    public int footCount = 0;
    public MTBaseAdapter(Context context, List<T> data){

        this.mContext = context;

        if(data==null){
            empty_flag=true;
        }else {
            this.data=data;
            if(data.size()==0)
                empty_flag=true;
        }
        if(data!=null)
            this.data=data;
    }
    public void addFootView() {
//        footCount = 1;
        Snackbar.make(viewGroup,"没有更多数据了！", Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(mContext, "没有更多数据了！", Toast.LENGTH_SHORT).show();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        this.viewGroup=viewGroup;
        if (viewType == ITEM_TYPE.ITEM1.ordinal()){
            if(isMultiType){
                return  onCreateMultiViewHolder(viewGroup,viewType);
            }else
            return onCreateViewHolder(viewGroup);
        }else if(viewType == ITEM_TYPE.TYPE_FOOTER.ordinal()){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.list_foot_loading,viewGroup, false);
            return new FooterViewHolder(view);
        }else if(viewType== ITEM_TYPE.TYPE_EMPTY.ordinal()){
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_empty_status,viewGroup,false);
             return new EmptyViewHolder(view);
        }
        return null;
    }

    public RecyclerView.ViewHolder onCreateMultiViewHolder(ViewGroup viewGroup, int viewType) {
        return onCreateViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(!empty_flag)
       onBaseBindViewHolder(holder,position);
    }

    protected abstract void onBaseBindViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract P onCreateViewHolder(ViewGroup viewGroup);


    @Override
    public int getItemCount() {
        if (empty_flag) {
            return 1;
        }else{
            return data.size() + footCount;
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(isMultiType){
            if(empty_flag)
                return ITEM_TYPE.TYPE_EMPTY.ordinal();
            if (footCount > 0 && position == getItemCount() - 1) {
                return ITEM_TYPE.TYPE_FOOTER.ordinal();
            } else {
                return getMulitType(position);
            }
        }else {
            if(empty_flag)
                return ITEM_TYPE.TYPE_EMPTY.ordinal();
            if (footCount > 0 && position == getItemCount() - 1) {
                return ITEM_TYPE.TYPE_FOOTER.ordinal();
            } else {
                return ITEM_TYPE.ITEM1.ordinal();
            }
        }

    }

    public int getMulitType(int position) {
        return 1;
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.loadmore);
        }

    }
    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.empty_text);
        }
    }

}
