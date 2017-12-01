package ebag.core.http.file;

/**
 * Created by unicho on 17-9-20.
 */

public interface OnUploadListener {

    void onTotalProgress(long bytesWritten, long contentLength, boolean done);

    void onCurrentProgress(int files, int currentFile, long currentBytesWritten, long currentLength, boolean done);

}
