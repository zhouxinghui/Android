package ebag.core.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.JSONSerializer
import com.alibaba.fastjson.serializer.SerializeWriter
import com.alibaba.fastjson.serializer.SerializerFeature





/**
 * Created by unicho on 2017/12/1.
 */
object JSONUtil {
    private val CONFIG = arrayOf(SerializerFeature.WriteNullBooleanAsFalse, //boolean为null时输出false
            SerializerFeature.WriteMapNullValue, //输出空置的字段
            SerializerFeature.WriteNonStringKeyAsString, //如果key不为String 则转换为String 比如Map的key为Integer
            SerializerFeature.WriteNullListAsEmpty, //list为null时输出[]
            SerializerFeature.WriteNullNumberAsZero, //number为null时输出0
            SerializerFeature.WriteNullStringAsEmpty//String为null时输出""
    )

    fun toJSON(javaObject: Any): JSONObject {

        val out = SerializeWriter()
        var jsonStr: String? = null
        out.use {
            val serializer = JSONSerializer(it)

            for (feature in CONFIG) {
                serializer.config(feature, true)
            }
            serializer.config(SerializerFeature.WriteEnumUsingToString, false)
            serializer.write(javaObject)

            jsonStr = it.toString()
        }
        return JSON.parseObject(jsonStr)
    }
}