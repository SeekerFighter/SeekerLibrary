package com.seeker.libraries.widget.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.seeker.libraries.R;
import com.seeker.libraries.widget.progressView.LoadClassicView;
import com.seeker.libraries.widget.progressView.LoadMaterialView;
import java.io.Serializable;

/**
 * Created by Seeker on 2016/8/4.
 */

public class LoadDialog extends DialogFragment{

    public static final int STYLE_CLASSIC = 0x001;

    public static final int STYLE_MATERIAL = 0x002;

    private int style;

    private View loadView;

    /**
     * 适用于style = STYLE_MATERIAL风格
     */
    private int barColor;

    public LoadDialog(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        Bundle bundle = getArguments();
        LoadDialog.Builder builder = (LoadDialog.Builder) bundle.getSerializable(LoadDialog.Builder.BUILDER);
        this.style = builder.style;
        this.barColor = builder.barColor;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       return new Dialog(getActivity(),R.style.PerfectDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final int layoutId = style == STYLE_CLASSIC?R.layout.dialog_load_classic_layout:R.layout.dialog_load_material_layout;

        View rootView = inflater.inflate(layoutId,container);

        loadView = rootView.findViewById(R.id.dialogLoadView);

        if(style == STYLE_CLASSIC){
            ((LoadClassicView) loadView).startLoad();
        }else if(style == STYLE_MATERIAL){
            LoadMaterialView progressWheel = (LoadMaterialView)loadView;
            progressWheel.setBarColor(barColor);
            progressWheel.spin();
        }

        return rootView;
    }

    public static final class Builder implements Serializable{

        private static final String BUILDER = "builder";

        private int style = STYLE_CLASSIC;

        /**
         * 适用于style = STYLE_MATERIAL风格
         */
        private int barColor = Color.parseColor("#ff009688");

        public Builder(){

        }

        public Builder setStyle(@NonNull int style){
            this.style = style;
            return this;
        }

        public Builder setBarColor(int color){
            this.barColor = color;
            return this;
        }

        public LoadDialog create(FragmentManager fm, String tag){
            LoadDialog loadDialog = new LoadDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(BUILDER,this);
            loadDialog.setArguments(bundle);
            loadDialog.show(fm,tag);
            return loadDialog;
        }
    }

}
