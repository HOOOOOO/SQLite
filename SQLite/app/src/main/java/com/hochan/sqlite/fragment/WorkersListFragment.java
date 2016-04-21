package com.hochan.sqlite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hochan.sqlite.MainActivity;
import com.hochan.sqlite.R;
import com.hochan.sqlite.adapter.ShowAdapter;
import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.sql.DataHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class WorkersListFragment extends Fragment implements ShowAdapter.OnAdapterListener,
        View.OnClickListener, EditDialogFragment.OnDialogListener{

    public final static String TAG = "WorkersListFragment: ";

    private View mView;
    private RecyclerView recyShow;
    private ShowAdapter mShowAdapter;
    private Context mContext;
    private List<Worker> mWorkers;
    private LinearLayoutManager mLayoutManager;
    private TextView tvChoosedItems;
    private List<Integer> mChoosedItems;

    private Button btnDeleteMore, btnUploadMore, btnCancleMore;
    private LinearLayout llChooseMore;
    private EditDialogFragment mEditDialogFragment;

    public static WorkersListFragment newInstance(){
        WorkersListFragment workersListFragment = new WorkersListFragment();
        return workersListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worker_list_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView = getView();
        mContext = getContext();

        btnDeleteMore = (Button) mView.findViewById(R.id.btn_delete_more);
        btnUploadMore = (Button) mView.findViewById(R.id.btn_upload_more);
        btnCancleMore = (Button) mView.findViewById(R.id.btn_cancle_more);
        btnCancleMore.setOnClickListener(this);
        btnDeleteMore.setOnClickListener(this);
        btnUploadMore.setOnClickListener(this);

        mChoosedItems = new ArrayList<>();

        tvChoosedItems = (TextView) mView.findViewById(R.id.tv_choosed_items);
        llChooseMore = (LinearLayout) mView.findViewById(R.id.ll_choose_more);

        recyShow = (RecyclerView) mView.findViewById(R.id.recy_show);
        mLayoutManager = new LinearLayoutManager(mContext);
        recyShow.setLayoutManager(mLayoutManager);
        recyShow.setItemAnimator(new DefaultItemAnimator());
        DataHelper dataHelper = new DataHelper(mContext);
        mWorkers = dataHelper.getWorkersInfo();
        mShowAdapter = new ShowAdapter(mContext, mWorkers);
        mShowAdapter.setOnAdapterListener(this);
        recyShow.setAdapter(mShowAdapter);
    }

    public void clear(){
        System.out.println("清空");
        mWorkers.clear();
        mShowAdapter = new ShowAdapter(mContext, null);
        recyShow.setAdapter(mShowAdapter);
        //mLayoutManager.removeAllViews();
        //mShowAdapter.notifyDataSetChanged();
    }

    public void add(Worker worker){
        mWorkers.add(worker);
        mShowAdapter.notifyItemInserted(mWorkers.size() - 1);
        mShowAdapter.notifyItemRangeInserted(mWorkers.size() - 1, mWorkers.size());
    }

    public void showAll(){
        DataHelper dataHelper = new DataHelper(mContext);
        mWorkers = dataHelper.getWorkersInfo();
        mShowAdapter = new ShowAdapter(mContext, mWorkers);
        mShowAdapter.setOnAdapterListener(this);
        recyShow.setAdapter(mShowAdapter);
    }

    public void search(String key, int type){
        DataHelper dataHelper = new DataHelper(mContext);
        List<Worker> results = new ArrayList<>();
        switch (type){
            case SearchDialogFragment.SEARCH_BY_ID:
                try {
                    results = dataHelper.getWorkerByID(Integer.parseInt(key));
                }catch (Exception e){
                    Toast.makeText(mContext, "输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case SearchDialogFragment.SEARCH_BY_NAME:
                results = dataHelper.getWorkersByName(key);
                break;
            case SearchDialogFragment.SEARCH_BY_TOWERNUM:
                results = dataHelper.getWorkersByTowerNumber(key);
                break;
            case SearchDialogFragment.SEARCH_BY_WORHSTATE:
                results = dataHelper.getWorkersByWorkState(key);
                break;
        }
        if(results == null)
            Toast.makeText(mContext, "查询结果为空", Toast.LENGTH_SHORT).show();
        //}else{
            mWorkers = results;
            mShowAdapter = new ShowAdapter(mContext, results);
            mShowAdapter.setOnAdapterListener(this);
            recyShow.setAdapter(mShowAdapter);
        //}
    }

    @Override
    public void shortClick(final int position) {
        mEditDialogFragment = EditDialogFragment.newInstance(EditDialogFragment.DELETEEDIT_DIALOG, mWorkers.get(position).getmName(),
                mWorkers.get(position).getmPhoneNumber(),
                mWorkers.get(position).getmTowerNumber(),
                mWorkers.get(position).getmWorkState(),
                position);
        mEditDialogFragment.setOnDialogListener(this);
        mEditDialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "editdialog");
    }

    @Override
    public void chooseMore(int position, View v) {
        mShowFragmentListener.chooseMore(true);
        llChooseMore.setVisibility(View.VISIBLE);
        tvChoosedItems.setVisibility(View.VISIBLE);
        mChoosedItems.add(position);
        mWorkers.get(position).mIsChoosed = true;
        notifyTextViewDataChange();
    }

    @Override
    public void cancleChoose(int position, View v) {

        for(int i = 0; i < mChoosedItems.size(); i++){
            if(position == mChoosedItems.get(i)) {
                mChoosedItems.remove(i);
                mWorkers.get(position).mIsChoosed = false;
            }
        }
        notifyTextViewDataChange();
        if(mChoosedItems.size() == 0){
            mShowAdapter.setmChooseMore(false);
            llChooseMore.setVisibility(View.GONE);
            tvChoosedItems.setVisibility(View.GONE);
            mShowFragmentListener.chooseMore(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancle_more:
                mShowAdapter.setmChooseMore(false);
                llChooseMore.setVisibility(View.GONE);
                tvChoosedItems.setVisibility(View.GONE);
                mShowFragmentListener.chooseMore(false);
                notifyTextViewDataChange();
                for(int i = 0; i < mChoosedItems.size(); i++){
                    mWorkers.get(mChoosedItems.get(i)).mIsChoosed = false;
                }
                mShowAdapter.notifyDataSetChanged();
                mChoosedItems.clear();
                break;
            case R.id.btn_delete_more:
                DataHelper dataHelper = new DataHelper(mContext);
                for(int i = 0; i < mChoosedItems.size(); i++){
                    dataHelper.deleteByID(Integer.parseInt(mWorkers.get(mChoosedItems.get(i)).getmID()));
                    //mShowAdapter.notifyItemRemoved(mChoosedItems.get(i));
                    //mShowAdapter.notifyItemRangeChanged(mChoosedItems.get(i), mWorkers.size());
                }
                /*Collections.sort(mChoosedItems);
                for(int i = mChoosedItems.size()-1; i >= 0; i--){
                    System.out.println(String.valueOf(mChoosedItems.get(i)));
                    mWorkers.remove(mChoosedItems.size());
                    System.out.println(String.valueOf(mWorkers.size()));
                }*/
                for(int i = 0; i < mWorkers.size(); i++){
                    if (mWorkers.get(i).mIsChoosed){
                        mWorkers.remove(i);
                        i -= i;
                    }
                }
                mShowAdapter.notifyDataSetChanged();
                mShowAdapter.setmChooseMore(false);
                llChooseMore.setVisibility(View.GONE);
                tvChoosedItems.setVisibility(View.GONE);
                mShowFragmentListener.chooseMore(false);
                mChoosedItems.clear();
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void notifyTextViewDataChange(){
        StringBuilder sb = new StringBuilder("已选条目ID：");
        for(int i = 0; i < mChoosedItems.size(); i++){
            sb.append(mWorkers.get(mChoosedItems.get(i)).getmID());
            sb.append(" ");
        }
        tvChoosedItems.setText(sb.toString());
    }

    private ShowFragmentListener mShowFragmentListener;

    public void setmShowFragmentListener(ShowFragmentListener fragmentListener){
        this.mShowFragmentListener = fragmentListener;
    }

    @Override
    public void clicked(int index, Worker worker, int position) {
        switch (index) {
            case EditDialogFragment.DELETE:
                DataHelper dataHelper = new DataHelper(mContext);
                dataHelper.deleteByID(Integer.parseInt(mWorkers.get(position).getmID()));
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                mWorkers.remove(position);
                //mShowAdapter.notifyDataSetChanged();
                mShowAdapter.notifyItemRemoved(position);
                mShowAdapter.notifyItemRangeChanged(position, mWorkers.size());
                //mShowAdapter.notifyDataSetChanged();
                break;
            case EditDialogFragment.EDIT:
                DataHelper tmpDataHelper = new DataHelper(mContext);
                tmpDataHelper.updateByID(Integer.parseInt(mWorkers.get(position).getmID()), worker);
                Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                mWorkers.get(position).setmName(worker.getmName());
                mWorkers.get(position).setmPhoneNumber(worker.getmPhoneNumber());
                mWorkers.get(position).setmTowerNumber(worker.getmTowerNumber());
                mWorkers.get(position).setmWorkState(worker.getmWorkState());
                mShowAdapter.notifyDataSetChanged();
                break;
        }
    }

    public interface ShowFragmentListener{
        public void chooseMore(boolean isChooseMore);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(TAG+"onResume!");
        if (mEditDialogFragment != null){
            mEditDialogFragment.setOnDialogListener(this);
        }else{
            mEditDialogFragment = (EditDialogFragment) ((MainActivity)mContext).getSupportFragmentManager().findFragmentByTag("editdialog");
            if(mEditDialogFragment != null){
                mEditDialogFragment.setOnDialogListener(this);
            }
            System.out.println(TAG+"mEditDialogFragment is null!");
        }
    }
}
