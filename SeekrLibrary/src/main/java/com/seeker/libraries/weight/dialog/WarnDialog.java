package com.seeker.libraries.weight.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.seeker.libraries.R;
import com.seeker.libraries.util.DeviceUtil;
import java.io.Serializable;

/**
 * Created by Seeker on 2016/8/3.
 *
 * 主要用于提醒用户、展示信息的弹框
 */

public class WarnDialog extends DialogFragment implements View.OnClickListener{

    /**
     * 标题
     */
    private String title;

    /**
     * 显示的内容
     */
    private String content;

    /**
     * 右边按钮信息
     */
    private String positiveStr;
    /**
     * 左边按钮信息
     */
    private String negativeStr;

    private Button positiveBtn,negativeBtn;

    private TextView titleView,contentView;

    private boolean isNegativeBtnShow = true;

    private boolean isPositiveBtnShow = true;

    private PositiveClickListener positiveClickListener;

    private NegativeClickListener negativeClickListener;

    public static final int STYLE_IOS = 0x001;

    public static final int STYLE_MATERIAL = 0x002;

    private int style = STYLE_IOS;

    public WarnDialog(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        Bundle bundle = getArguments();
        Builder builder = (Builder) bundle.getSerializable(Builder.BUILDER);
        this.title = builder.title;
        this.content = builder.content;
        this.positiveStr = builder.positiveStr;
        this.negativeStr = builder.negativeStr;
        this.positiveClickListener = builder.positiveClickListener;
        this.negativeClickListener = builder.negativeClickListener;
        this.style = builder.style;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(),R.style.PerfectDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int layoutId = style == STYLE_IOS?R.layout.dialog_warn_ios_layout :R.layout.dialog_warn_material_layout;
        View rootView = inflater.inflate(layoutId,container);
        positiveBtn = (Button) rootView.findViewById(R.id.warnDialogPositiveBtn);
        negativeBtn = (Button) rootView.findViewById(R.id.warnDialogNegativeBtn);
        titleView = (TextView)rootView.findViewById(R.id.warnDialogTitle);
        contentView = (TextView)rootView.findViewById(R.id.warnDialogContent);
        bindData();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int deviceWidth = DeviceUtil.getDeviceWidth(getActivity());
        final int deviceHeight = DeviceUtil.getDeviceHeight(getActivity());
        final int width = Math.min(deviceWidth,deviceHeight);

        Dialog dialog = getDialog();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (width * 0.7f);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if(style == STYLE_MATERIAL){
            contentView.setGravity(Gravity.LEFT);
        }else{
            int contentWidth = (int) contentView.getPaint().measureText(content,0,content.length());
            if(params.width - getResources().getDimensionPixelOffset(R.dimen.padding)*2 < contentWidth){
                contentView.setGravity(Gravity.LEFT);
            }
        }
        dialog.getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if(id == R.id.warnDialogPositiveBtn && positiveClickListener != null){
            positiveClickListener.onPositiveClick();
        }else if(id == R.id.warnDialogNegativeBtn && negativeClickListener != null){
            negativeClickListener.onNegativeClick();
        }
        getDialog().cancel();
    }

    /**
     * 绑定内容
     */
    private void bindData(){
        if(TextUtils.isEmpty(title)){
            throw new NullPointerException("title is null!");
        }
        titleView.setText(title);

        if(TextUtils.isEmpty(content)){
            throw new NullPointerException("content is null!");
        }
        contentView.setText(content);

        if(TextUtils.isEmpty(negativeStr)){
            isNegativeBtnShow = false;
            negativeBtn.setVisibility(View.GONE);
        }else{
            isNegativeBtnShow = true;
            negativeBtn.setText(negativeStr);
            negativeBtn.setOnClickListener(this);
        }
        if(TextUtils.isEmpty(positiveStr)){
            isPositiveBtnShow = false;
            positiveBtn.setVisibility(View.GONE);
        }else{
            isPositiveBtnShow = true;
            positiveBtn.setText(positiveStr);
            positiveBtn.setOnClickListener(this);
        }

        if(style == STYLE_IOS){
            if(!isNegativeBtnShow && isPositiveBtnShow){
                positiveBtn.setBackgroundResource(R.drawable.selector_btn_back_bottom_radius);
            }else if(isNegativeBtnShow && !isPositiveBtnShow){
                negativeBtn.setBackgroundResource(R.drawable.selector_btn_back_bottom_radius);
            }
        }else if(style ==STYLE_MATERIAL){
            if(isNegativeBtnShow && !isPositiveBtnShow){
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)negativeBtn.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                negativeBtn.setLayoutParams(params);
            }
        }
    }

    public interface PositiveClickListener{
        void onPositiveClick();
    }

    public interface NegativeClickListener{
        void onNegativeClick();
    }

    public static final class Builder implements Serializable{

        private static final String BUILDER = "builder";

        private String title;

        private String content;

        private String positiveStr;

        private String negativeStr;

        private PositiveClickListener positiveClickListener;

        private NegativeClickListener negativeClickListener;

        private int style = STYLE_IOS;

        public Builder(){

        }

        public Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(@NonNull String content) {
            this.content = content;
            return this;
        }

        public Builder setPositiveStr(@Nullable String positiveStr) {
            this.positiveStr = positiveStr;
            return this;
        }

        public Builder setNegativeStr(@Nullable String negativeStr) {
            this.negativeStr = negativeStr;
            return this;
        }

        public Builder setPositiveClickListener(PositiveClickListener listener){
            this.positiveClickListener = listener;
            return this;
        }

        public Builder setNegativeClickListener(NegativeClickListener listener){
            this.negativeClickListener = listener;
            return this;
        }

        public Builder setStyle(@NonNull int style){
            this.style = style;
            return this;
        }

        public WarnDialog create(FragmentManager fm, String tag){
            WarnDialog libDialogFragment = new WarnDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(BUILDER,this);
            libDialogFragment.setArguments(bundle);
            libDialogFragment.show(fm,tag);
            return libDialogFragment;
        }
    }


}
