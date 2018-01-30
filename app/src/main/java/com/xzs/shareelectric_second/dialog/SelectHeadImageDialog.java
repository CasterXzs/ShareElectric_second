package com.xzs.shareelectric_second.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.activity.MainActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lenovo on 2018/1/30.
 */

public class SelectHeadImageDialog extends Dialog {

    private Activity context;
    private TextView selectheadimage_tv_camera;
    private TextView selectheadimage_tv_photo;
    public static Uri imageUri;

    public static final int TAKE_PHOTO=10;

    public SelectHeadImageDialog(Activity context,int style) {
        super(context,style);
        this.context=context;
        setContentView(R.layout.dialog_selectheadimage);
        init();
    }

    private void init(){
        selectheadimage_tv_camera=(TextView)findViewById(R.id.selectheadimage_tv_camera);
        selectheadimage_tv_camera.setOnClickListener(new MyOnClickListener());
        selectheadimage_tv_photo=(TextView)findViewById(R.id.selectheadimage_tv_photo);
        selectheadimage_tv_photo.setOnClickListener(new MyOnClickListener());
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.selectheadimage_tv_camera:
                    Toast.makeText(context, "点击了selectheadimage_tv_camera", Toast.LENGTH_SHORT).show();
                    camera();
                    break;
                case R.id.selectheadimage_tv_photo:
                    Toast.makeText(context, "点击了selectheadimage_tv_photo", Toast.LENGTH_SHORT).show();
                    photo();
                    break;
                default:
            }
        }
    }

    private void camera(){
        File outputImage=new File(context.getExternalCacheDir(),"camera.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(context,"com.xzs.shareelectric_second.fileprovider",outputImage);
        }else{
            imageUri=Uri.fromFile(outputImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        context.startActivityForResult(intent,TAKE_PHOTO);
    }

    private void photo(){

    }



}
