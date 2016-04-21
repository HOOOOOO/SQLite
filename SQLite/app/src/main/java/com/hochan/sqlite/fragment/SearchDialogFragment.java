package com.hochan.sqlite.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hochan.sqlite.R;
import com.hochan.sqlite.tools.MyApplication;

import java.util.Stack;

/**
 * Created by Administrator on 2016/4/14.
 */
public class SearchDialogFragment extends DialogFragment implements View.OnClickListener{

    public final static int SEARCH_BY_ID = 0;
    public final static int SEARCH_BY_NAME = 1;
    public final static int SEARCH_BY_TOWERNUM = 2;
    public final static int SEARCH_BY_WORHSTATE = 3;


    private View mView;
    private Context mContext;
    private Button btnShowAll, btnSearchID, btnSearchName, btnSearchTowerNum, btnSearchWorkState;
    private EditText edSearch;

    private AnimationSet mDialogInAnim;
    private View mRootView;

    public static SearchDialogFragment newInstance(){
        SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
        return searchDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.search_dialog_layout, container, false);
        mDialogInAnim = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.modal_in);
        mRootView = getDialog().getWindow().getDecorView().findViewById(android.R.id.content);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        getDialog().getWindow().setLayout((int) (MyApplication.mWidthOfDialog), WindowManager.LayoutParams.WRAP_CONTENT);//这2行,和上面的一样,注意顺序就行;
        //mRootView.startAnimation(mDialogInAnim);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView = getView();
        mContext = getContext();

        btnShowAll = (Button) mView.findViewById(R.id.btn_show_all);
        btnSearchID = (Button) mView.findViewById(R.id.btn_search_id);
        btnSearchName = (Button) mView.findViewById(R.id.btn_search_name);
        btnSearchTowerNum = (Button) mView.findViewById(R.id.btn_search_towernum);
        btnSearchWorkState = (Button) mView.findViewById(R.id.btn_search_workstate);

        edSearch = (EditText) mView.findViewById(R.id.ed_search);

        btnShowAll.setOnClickListener(this);
        btnSearchID.setOnClickListener(this);
        btnSearchName.setOnClickListener(this);
        btnSearchTowerNum.setOnClickListener(this);
        btnSearchWorkState.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() != R.id.btn_show_all){
            if(TextUtils.isEmpty(edSearch.getText())){
                Toast.makeText(mContext, "请输入关键字", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        switch (v.getId()){
            case R.id.btn_show_all:
                mSearchListener.showAll();
                break;
            case R.id.btn_search_id:
                mSearchListener.searchDialog(edSearch.getText().toString(), SEARCH_BY_ID);
                break;
            case R.id.btn_search_name:
                mSearchListener.searchDialog(edSearch.getText().toString(), SEARCH_BY_NAME);
                break;
            case R.id.btn_search_towernum:
                mSearchListener.searchDialog(edSearch.getText().toString(), SEARCH_BY_TOWERNUM);
                break;
            case R.id.btn_search_workstate:
                mSearchListener.searchDialog(edSearch.getText().toString(), SEARCH_BY_WORHSTATE);
                break;
        }
        dismiss();
    }

    private OnSearchDialogListener mSearchListener;

    public void setSearchListener(OnSearchDialogListener searchListener){
        this.mSearchListener = searchListener;
    }

    public interface OnSearchDialogListener{
        public void searchDialog(String key, int type);
        public void showAll();
    }
}
