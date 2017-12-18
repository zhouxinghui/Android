package ebag.hd.http.baseBean;

/**
 * Created by caoyu on 2017/8/28.
 */

public class ResponseBean<T> {

    private String millis;
    private String code;
    private String message;
    private T body;

    public String getMillis() {
        return millis;
    }

    public void setMillis(String millis) {
        this.millis = millis;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
