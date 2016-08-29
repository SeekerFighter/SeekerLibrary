package com.seeker.demo.CropImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.seeker.demo.R;
import com.seeker.libraries.base.BaseActivity;
import com.seeker.libraries.logger.Logger;
import com.seeker.libraries.util.AppUtils;
import com.seeker.libraries.weight.cropView.CropImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Seeker on 2016/7/29.
 */

public class CropImageActivity extends BaseActivity{

    private static final String TAG = "CropImageActivity";

    private Unbinder unbinder;

    private static final int CAMERA = 0x001;
    private static final int ALBUM = 0x002;


    @BindView(R.id.cropedImage)
    ImageView cropedImage;

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cropimage;
    }

    @Override
    public void onbind() {
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void unbind() {
        unbinder.unbind();
    }

    @Override
    public void doWork() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            Logger.t(TAG).e("data == null");
            return;
        }
        switch (requestCode){
            case CAMERA:
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                cropImageView.setImageBitmap(bitmap);
                break;
            case ALBUM:
                cropImageView.setImageUriAsync(data.getData());
                break;
        }
    }

    @OnClick({R.id.takePic,R.id.album,R.id.crop})
    public void btnClick(View view){
        switch (view.getId()){
            case R.id.takePic:
                AppUtils.takePicture(this,null,CAMERA);
                break;
            case R.id.album:
                AppUtils.openAlbumPicture(this,ALBUM);
                break;
            case R.id.crop:
                Bitmap b = cropImageView.getCroppedImage();
                cropedImage.setImageBitmap(b);
                break;
        }
    }

}
