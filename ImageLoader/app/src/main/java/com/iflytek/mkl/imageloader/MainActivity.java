package com.iflytek.mkl.imageloader;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.iflytek.mkl.imageloader.R.id.iv_image;

/***
 * 1、Universal-ImageLoader的配置
 *
 * 2、用Universal-ImageLoader加载网络图片和本地图炮
 *
 */

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";

    private ImageLoader imageLoader;
    private ImageView imageView;
    private ImageView imageViewGlide;
    private ImageView imageViewPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageLoader = ImageLoader.getInstance();
        imageView = (ImageView) findViewById(iv_image);

        String netImageUrl = "http://img2.niutuku.com/1312/0800/0800-niutuku.com-14339.jpg";
        String nativeImageUrl = "file:///"+"storage/emulated/0/DCIM/Camera/IMG_20170711_183115_HDR.jpg";
        imageLoader.displayImage(netImageUrl, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d(TAG, "onLoadingStarted");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d(TAG, "onLoadingFailed");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d(TAG, "onLoadingComplete");
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.d(TAG, "onLoadingCancelled");
            }
        });

        imageViewGlide = (ImageView) findViewById(R.id.iv_glide);
        Glide.with(this).load(nativeImageUrl).crossFade().placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(imageViewGlide);

        imageViewPicasso = (ImageView) findViewById(R.id.iv_picasso);
        Picasso.with(this).load(nativeImageUrl).resize(100,50).error(R.mipmap.ic_launcher).into(imageViewPicasso);
    }


}
