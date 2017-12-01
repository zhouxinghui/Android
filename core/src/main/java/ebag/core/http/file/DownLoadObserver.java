package ebag.core.http.file;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by unicho on 17-9-23.
 */

public class DownLoadObserver implements Observer<DownloadInfo> {
    protected Disposable d;//可以用于取消注册的监听者
    protected DownloadInfo downloadInfo;

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }


}
