package ebag.core.http.image;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;

/**
 * Created by caoyu on 2017/8/28.
 */

public class RecyclerViewPreloader<T> extends RecyclerView.OnScrollListener {

    private final RecyclerToListViewScrollListener recyclerScrollListener;

    /**
     * Helper constructor that accepts an {@link Activity}.
     */
    public RecyclerViewPreloader(Activity activity,
                                 ListPreloader.PreloadModelProvider<T> preloadModelProvider,
                                 ListPreloader.PreloadSizeProvider<T> preloadDimensionProvider, int maxPreload) {
        this(Glide.with(activity), preloadModelProvider, preloadDimensionProvider, maxPreload);
    }

    /**
     * Helper constructor that accepts an {@link FragmentActivity}.
     */
    public RecyclerViewPreloader(FragmentActivity fragmentActivity,
                                 ListPreloader.PreloadModelProvider<T> preloadModelProvider,
                                 ListPreloader.PreloadSizeProvider<T> preloadDimensionProvider,
                                 int maxPreload) {
        this(Glide.with(fragmentActivity), preloadModelProvider, preloadDimensionProvider, maxPreload);
    }

    /**
     * Helper constructor that accepts an {@link Fragment}.
     */
    public RecyclerViewPreloader(Fragment fragment,
                                 ListPreloader.PreloadModelProvider<T> preloadModelProvider,
                                 ListPreloader.PreloadSizeProvider<T> preloadDimensionProvider,
                                 int maxPreload) {
        this(Glide.with(fragment), preloadModelProvider, preloadDimensionProvider, maxPreload);
    }

    /**
     * Helper constructor that accepts an {@link android.support.v4.app.Fragment}.
     */
    public RecyclerViewPreloader(android.support.v4.app.Fragment fragment,
                                 ListPreloader.PreloadModelProvider<T> preloadModelProvider,
                                 ListPreloader.PreloadSizeProvider<T> preloadDimensionProvider,
                                 int maxPreload) {
        this(Glide.with(fragment), preloadModelProvider, preloadDimensionProvider, maxPreload);
    }
    /**
     * Constructor that accepts interfaces for providing the dimensions of images to preload, the list
     * of models to preload for a given position, and the request to use to load images.
     *
     * @param preloadModelProvider     Provides models to load and requests capable of loading them.
     * @param preloadDimensionProvider Provides the dimensions of images to load.
     * @param maxPreload               Maximum number of items to preload.
     */
    public RecyclerViewPreloader(RequestManager requestManager,
                                 ListPreloader.PreloadModelProvider<T> preloadModelProvider,
                                 ListPreloader.PreloadSizeProvider<T> preloadDimensionProvider, int maxPreload) {

        ListPreloader<T> listPreloader = new ListPreloader<>(requestManager, preloadModelProvider,
                preloadDimensionProvider, maxPreload);
        recyclerScrollListener = new RecyclerToListViewScrollListener(listPreloader);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        recyclerScrollListener.onScrolled(recyclerView, dx, dy);
    }
}
