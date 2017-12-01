package ebag.core.http.network;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * Created by 90323 on 2016/10/28 0028.
 */

public class FastJsonConverterFactory extends Converter.Factory{

    public static FastJsonConverterFactory create() {
        return new FastJsonConverterFactory();
    }

    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换服务器返回数据
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseBodyConverter<>(type);
    }

    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换发送给服务器的数据
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>();
    }

    public class FastJsonResponseBodyConverter <T> implements Converter<ResponseBody, T> {
        private final Type type;

        public FastJsonResponseBodyConverter(Type type) {
            this.type = type;
        }

        /*
        * 转换方法
        */
        @Override
        public T convert(@NonNull ResponseBody value) throws IOException {
            BufferedSource bufferedSource = Okio.buffer(value.source());
            String tempStr = bufferedSource.readUtf8();
            bufferedSource.close();
//            tempStr = tempStr.replaceAll("\\\\\"","\"")
//                    .replaceAll("\"\\{","{")
//                    .replaceAll("\\}\"","}")
//                    .replaceAll("\"\\[","[")
//                    .replaceAll("\\]\"","]");
            return JSON.parseObject(tempStr, type, Feature.AllowArbitraryCommas);
        }
    }

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    public class FastJsonRequestBodyConverter <T> implements Converter<T, RequestBody> {


        @Override
        public RequestBody convert(@NonNull T value) throws IOException {
            return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value));
        }
    }

}