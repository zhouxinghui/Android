package ebag.core.http.image;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import ebag.core.R;

/**
 * @author cyulv@sina.com
 * @date 2015/10/17 10:53
 * @Description:
 */
public class SingleImageLoader {

    private volatile static SingleImageLoader singleImageLoader;
    private RequestOptions defaultOptions,optionsNoReplaceImg;


    private SingleImageLoader() {

        //清除磁盘缓存
//        Glide.get(context).clearDiskCache();
        //基础配置，磁盘缓存模式，图片精度
        optionsNoReplaceImg = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.NORMAL);
//                .transform(new GlideCircleTransform());
        defaultOptions =  new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .format(DecodeFormat.PREFER_RGB_565)
                .priority(Priority.NORMAL);
    }

    public static SingleImageLoader getInstance() {
        if (singleImageLoader == null) {
            synchronized (SingleImageLoader.class) {
                if(singleImageLoader == null){
                    singleImageLoader = new SingleImageLoader();
                }
            }
        }
        return singleImageLoader;
    }
    public void loadImage(File file, ImageView imageView){
        loadImage(Glide.with(imageView),imageView,file);
    }

    public void setImage(String url, ImageView imageView){
        loadImage(Glide.with(imageView),imageView,url, R.drawable.ic_launcher, R.drawable.ic_launcher);
    }

    /** 加载Activity中的图片 */
    public void setImage(Activity activity, ImageView imageView, String url){
        loadImage(Glide.with(activity),imageView,url, R.drawable.ic_launcher, R.drawable.ic_launcher);
    }

    /** 加载Fragment中的图片 */
    public void setImage(Fragment fragment, ImageView imageView, String url){
        loadImage(Glide.with(fragment),imageView,url, R.drawable.ic_launcher, R.drawable.ic_launcher);
    }
    /** 加载Context中的图片 */
    public void setImage(Context context, ImageView imageView, String url){
        loadImage(Glide.with(context),imageView,url, R.drawable.ic_launcher, R.drawable.ic_launcher);
    }


    public void setImage(Activity activity, ImageView imageView, String url, int placeHolderId, int errorId){
        loadImage(Glide.with(activity),imageView,url,placeHolderId,errorId);
    }

    public void setImage(Fragment fragment, ImageView imageView, String url, int placeHolderId, int errorId){
        loadImage(Glide.with(fragment),imageView,url,placeHolderId,errorId);
    }

    public void setImage(Context context, ImageView imageView, String url, int placeHolderId, int errorId){
        loadImage(Glide.with(context),imageView,url,placeHolderId,errorId);
    }

    private void loadImage(RequestManager requestManager, ImageView imageView, File file){
        requestManager.load(file)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false))//配置
                .into(imageView);
    }

    private void loadImage(RequestManager requestManager, ImageView imageView, String url, int placeHolderId, int errorId){
        defaultOptions = defaultOptions.placeholder(placeHolderId)//占位符
                .error(errorId)//加载失败
                .fallback(errorId);//URL或者model为空
        loadImage(requestManager,imageView,url,defaultOptions);
    }


    /**
     * 图片加载，这里用的是加载为Drawable的形式，另外还有，asbitmap 等等
     */
    private void loadImage(RequestManager requestManager, ImageView imageView, String url, RequestOptions options){
        requestManager.load(url)
//                .thumbnail(0.2f)
                .apply(options)//配置
//                .transition(transitionOptions)//动画效果
                .into(imageView);
    }

}


