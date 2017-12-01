package ebag.core.http.image;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * Created by cyu on 16-5-25.
 */
@GlideModule
public class OkHttpGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

//        RequestOptions options = new RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .placeholder(R.drawable.ic_launcher)
//                .error(R.drawable.ic_launcher)
//                .fallback(R.drawable.ic_launcher)
//                .format(DecodeFormat.PREFER_RGB_565)
//                .priority(Priority.HIGH);
//        builder.setDefaultRequestOptions(options);
        /** 设置内存缓存大小10M */
        builder.setMemoryCache(new LruResourceCache(10*1024*1024));

        /**setMemoryCacheScreens设置MemoryCache应该能够容纳的像素值的设备屏幕数，说白了就是缓存多少屏图片，默认值是2。*/
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
//                .setMemoryCacheScreens(2)
//                .build();
//        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        /** 设置磁盘缓存大小100M，默认在App的缓存文件夹下 */
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));


        new DiskCache.Factory() {
            @Nullable
            @Override
            public DiskCache build() {
                return null;
            }
        };
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "cacheFolderName", diskCacheSizeBytes));
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context));


//        // Do nothing.
//        int cacheSize100MegaBytes = 104857600;
//
//        //设置磁盘缓存路径
////        String downloadDirectoryPath = MtaTravelApplication.TEMP_IMAGE_DIR;
//        //设置磁盘缓存路径
//        String downloadDirectoryPath = "";
//
//        builder.setDiskCache(
//                new DiskLruCacheFactory( downloadDirectoryPath, cacheSize100MegaBytes )
//        );
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.append(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
