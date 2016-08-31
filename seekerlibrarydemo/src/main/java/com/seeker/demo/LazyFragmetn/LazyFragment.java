package com.seeker.demo.LazyFragmetn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.seeker.demo.R;
import com.seeker.libraries.base.BaseFragment;
import com.seeker.libraries.widget.progressView.LoadClassicView;

/**
 * Created by Seeker on 2016/8/31.
 */

public class LazyFragment extends BaseFragment {
    private static final String TAG = "LazyFragment";
    public static final String TAB_NAME = "tab_name";

    TextView textView;

    LoadClassicView loadClassicView;

    private String tabName;

    public static LazyFragment newLazyFragment(String tabName){
        LazyFragment lazyFragment = new LazyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LazyFragment.TAB_NAME,tabName);
        lazyFragment.setArguments(bundle);
        return lazyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        tabName = bundle.getString(TAB_NAME);
    }

    @Override
    public void onbind(View rootView) {
        textView  = (TextView) rootView.findViewById(R.id.text);
        loadClassicView = (LoadClassicView) rootView.findViewById(R.id.load);
    }

    @Override
    public void unbind() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.lazyfragment_layout;
    }

    @Override
    public void onLoad() {
        asyncTask.execute();
    }

    @Override
    public void onSaveCurrentState(Bundle bundle) {
        bundle.putString("textViewStr",textView.getText().toString());
        bundle.putInt("textViewVisibility",textView.getVisibility());
        bundle.putInt("loadClassicViewVisibility",loadClassicView.getVisibility());
    }

    @Override
    public void onResetBeforeState(Bundle bundle) {
        textView.setText(bundle.getString("textViewStr"));
        textView.setVisibility(bundle.getInt("textViewVisibility") == View.VISIBLE?View.VISIBLE:View.GONE);
        loadClassicView.setVisibility(bundle.getInt("loadClassicViewVisibility") == View.VISIBLE?View.VISIBLE:View.GONE);
    }

    private AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: tabName = "+tabName);
            loadClassicView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            loadClassicView.startLoad();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            loadClassicView.finishLoad();
            loadClassicView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(tabName);
        }
    };
}
