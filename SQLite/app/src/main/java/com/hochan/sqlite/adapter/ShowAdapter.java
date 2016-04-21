package com.hochan.sqlite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hochan.sqlite.R;
import com.hochan.sqlite.data.Worker;

import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class ShowAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<Worker> mWorkers;
    private boolean mChooseMore = false;

    public ShowAdapter(Context mContext, List<Worker> mWorkers) {
        this.mContext = mContext;
        this.mWorkers = mWorkers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new ShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShowViewHolder showViewHolder = (ShowViewHolder) holder;
        showViewHolder.tvId.setText(String.valueOf(mWorkers.get(position).getmID()));
        showViewHolder.tvName.setText(mWorkers.get(position).getmName());
        showViewHolder.tvPhoneNumber.setText(mWorkers.get(position).getmPhoneNumber());
        showViewHolder.tvTowerNumber.setText(mWorkers.get(position).getmTowerNumber());
        showViewHolder.tvWorkState.setText(mWorkers.get(position).getmWorkState());
        if(mWorkers.get(position).mIsChoosed){
            showViewHolder.llItemBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackground));
            showViewHolder.vBeChoose.setVisibility(View.VISIBLE);
        }else{
            showViewHolder.llItemBackground.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_selector));
            showViewHolder.vBeChoose.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(mWorkers == null){
            return 0;
        }
        return mWorkers.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView tvId, tvName, tvPhoneNumber, tvTowerNumber, tvWorkState;
        public LinearLayout llItemBackground;
        public View vBeChoose;

        public ShowViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tv_phone_number);
            tvTowerNumber = (TextView) itemView.findViewById(R.id.tv_tower_number);
            tvWorkState = (TextView) itemView.findViewById(R.id.tv_work_state);
            llItemBackground = (LinearLayout) itemView.findViewById(R.id.ll_item_background);
            llItemBackground.setOnClickListener(this);
            llItemBackground.setOnLongClickListener(this);
            vBeChoose = (View) itemView.findViewById(R.id.v_bechoosed);
        }

        @Override
        public void onClick(View v) {
            if(mAdapterListener != null){
                if(mChooseMore){
                    if(mWorkers.get(getPosition()).mIsChoosed){
                        v.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_selector));
                        v.findViewById(R.id.v_bechoosed).setVisibility(View.GONE);
                        mAdapterListener.cancleChoose(getPosition(), v);
                        mWorkers.get(getPosition()).mIsChoosed = false;
                    }else{
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackground));
                        v.findViewById(R.id.v_bechoosed).setVisibility(View.VISIBLE);
                        mAdapterListener.chooseMore(getPosition(), v);
                        mWorkers.get(getPosition()).mIsChoosed = true;
                    }
                    return;
                }
                mAdapterListener.shortClick(getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mAdapterListener != null && !mChooseMore){
                mAdapterListener.chooseMore(getPosition(), v);
                v.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackground));
                v.findViewById(R.id.v_bechoosed).setVisibility(View.VISIBLE);
                mWorkers.get(getPosition()).mIsChoosed = true;
                mChooseMore = true;
                return true;
            }
            return false;
        }
    }

    private OnAdapterListener mAdapterListener;

    public void setOnAdapterListener(OnAdapterListener adapterListener){
        this.mAdapterListener = adapterListener;
    }

    public interface OnAdapterListener{
        public void shortClick(int position);
        public void chooseMore(int position, View v);
        public void cancleChoose(int position, View v);
    }

    public void setmChooseMore(boolean mChooseMore) {
        this.mChooseMore = mChooseMore;
    }
}
