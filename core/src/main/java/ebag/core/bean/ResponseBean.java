package ebag.core.bean;

/**
 * Created by caoyu on 2017/8/28.
 */

public class ResponseBean<T> {

    private String success;
    private String message;
    private T data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
