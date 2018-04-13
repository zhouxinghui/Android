package ebag.core.util

import java.io.*
import java.util.*
import java.util.regex.Pattern

/**
 * Created by caoyu on 2017/11/1.
 */
object StringUtils {
    /**
     * 描述：将null转化为“”.
     *
     * @param str 指定的字符串
     * @return 字符串的String类型
     */
    fun parseEmpty(str: String?): String {

        if ("null" == str?.trim()) {
            return ""
        }
        return str?.trim() ?: ""
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    fun isEmpty(str: String?): Boolean {
        return str?.trim()?.isEmpty() ?: true
    }

    /**
     * 获取字符串中文字符的长度（每个中文算2个字符）.
     *
     * @param str 指定的字符串
     * @return 中文字符的长度
     */
    fun chineseLength(str: String): Int {
        var valueLength = 0
        val chinese = "[\u0391-\uFFE5]"
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        if (!isEmpty(str)) {
            (0 until str.length)
                    /* 获取一个字符 */
                    .map { str.substring(it, it + 1) }
                    /* 判断是否为中文字符 */
                    .filter { it.matches(chinese.toRegex()) }
                    /* 是中文字符的话 +2 */
                    .forEach { valueLength += 2 }
        }
        return valueLength
    }

    /**
     * 描述：获取字符串的长度.
     *
     * @param str 指定的字符串
     * @return 字符串的长度（中文字符计2个）
     */
    fun strLength(str: String): Int {
        var valueLength = 0
        val chinese = "[\u0391-\uFFE5]"
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        if (!isEmpty(str)) {
            valueLength = str.length
            (0 until str.length)
                    /* 获取一个字符 */
                    .map { str.substring(it, it + 1) }
                    /* 判断是否为中文字符 */
                    .filter { it.matches(chinese.toRegex()) }
                    /* 是中文字符的话 在原有的长度上+1 */
                    .forEach { valueLength += 2 }
        }
        return valueLength
    }

    /**
     * 描述：获取指定长度的字符所在位置.
     *
     * @param str  指定的字符串
     * @param maxL 要取到的长度（字符长度，中文字符计2个）
     * @return 字符的所在位置
     */
    fun subStringLength(str: String, maxL: Int): Int {
        var currentIndex = 0
        var valueLength = 0
        val chinese = "[\u0391-\uFFE5]"
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (i in 0 until str.length) {
            // 获取一个字符
            val temp = str.substring(i, i + 1)
            // 判断是否为中文字符
            // 中文字符长度为2 其他字符长度为1
            valueLength += if (temp.matches(chinese.toRegex())) { 2 } else { 1 }
            if (valueLength >= maxL) {
                currentIndex = i
                break
            }
        }
        return currentIndex
    }

    /**
     * 描述：手机号格式验证.
     *
     * @param str 指定的手机号码字符串
     * @return 是否为手机号码格式:是为true，否则false
     */
    fun isMobileNo(str: String): Boolean {
        return Pattern.compile("^((17[0-9])|(13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$").matcher(str).matches()
    }

    /**
     * 验证密码 数字 字母混合输入 长度在 6--12 之间
     */
    fun isPassword(str: String): Boolean{
        return Pattern.compile("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,20}\$").matcher(str).matches()
    }

    /**
     * 描述：是否只是字母和数字.
     *
     * @param str 指定的字符串
     * @return 是否只是字母和数字:是为true，否则false
     */
    fun isNumberLetter(str: String): Boolean? {
        return str.matches("^[A-Za-z0-9]+$".toRegex())
    }

    /**
     * 描述：是否只是数字.
     *
     * @param str 指定的字符串
     * @return 是否只是数字:是为true，否则false
     */
    fun isNumber(str: String): Boolean? {
        return str.matches("^[0-9]+$".toRegex())
    }

    /**
     * 描述：是否是邮箱.
     *
     * @param str 指定的字符串
     * @return 是否是邮箱:是为true，否则false
     */
    fun isEmail(str: String): Boolean? {
        val expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
        return str.matches(expr.toRegex())
    }

    /**
     * 描述：是否是中文.
     *
     * @param str 指定的字符串
     * @return 是否是中文:是为true，否则false
     */
    fun isChinese(str: String): Boolean? {
        val chinese = "[\u0391-\uFFE5]"
        if (!isEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            (0 until str.length)
                    // 获取一个字符
                    .map { str.substring(it, it + 1) }
                    // 判断是否为中文字符
                    .filter { !it.matches(chinese.toRegex()) }
                    .forEach { return false }
        }
        return true
    }

    /**
     * 描述：是否包含中文.
     *
     * @param str 指定的字符串
     * @return 是否包含中文:是为true，否则false
     */
    fun isContainChinese(str: String): Boolean? {
        val chinese = "[\u0391-\uFFE5]"
        if (!isEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            (0 until str.length)
                    // 获取一个字符
                    .map { str.substring(it, it + 1) }
                    // 判断是否为中文字符
                    .filter { it.matches(chinese.toRegex()) }
                    .forEach { return true }
        }
        return false
    }

    /**
     * 描述：从输入流中获得String.
     *
     * @param inputStream 输入流
     * @return 获得的String
     */
    fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.readText()
    }

    /**
     * 描述：标准化日期时间类型的数据，不足两位的补0.
     *
     * @param dateTime 预格式的时间字符串，如:2012-3-2 12:2:20
     * @return String 格式化好的时间字符串，如:2012-03-20 12:02:20
     */
    fun dateTimeFormat(dateTime: String): String? {
        val sb = StringBuilder()
        try {
            if (isEmpty(dateTime)) {
                return null
            }
            val dateAndTime = dateTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (dateAndTime.size > 0) {
                for (str in dateAndTime) {
                    if (str.indexOf("-") != -1) {
                        val date = str.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (i in date.indices) {
                            val str1 = date[i]
                            sb.append(strFormat2(str1))
                            if (i < date.size - 1) {
                                sb.append("-")
                            }
                        }
                    } else if (str.indexOf(":") != -1) {
                        sb.append(" ")
                        val date = str.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (i in date.indices) {
                            val str1 = date[i]
                            sb.append(strFormat2(str1))
                            if (i < date.size - 1) {
                                sb.append(":")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return sb.toString()
    }

    /**
     * 描述：不足2个字符的在前面补“0”.
     *
     * @param str 指定的字符串
     * @return 至少2个字符的字符串
     */
    fun strFormat2(str: String): String {
        var str = str
        try {
            if (str.length <= 1) {
                str = "0" + str
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return str
    }

    /**
     * 描述：截取字符串到指定字节长度.
     *
     * @param str    the str
     * @param length 指定字节长度
     * @return 截取后的字符串
     */
    fun cutString(str: String, length: Int): String {
        return cutString(str, length, "")
    }

    /**
     * 描述：截取字符串到指定字节长度.
     *
     * @param str    文本
     * @param length 字节长度
     * @param dot    省略符号
     * @return 截取后的字符串
     */
    fun cutString(str: String, length: Int, dot: String?): String {
        val strBLen = strlen(str, "GBK")
        if (strBLen <= length) {
            return str
        }
        var temp = 0
        val sb = StringBuffer(length)
        val ch = str.toCharArray()
        for (c in ch) {
            sb.append(c)
            if (c.toInt() > 256) {
                temp += 2
            } else {
                temp += 1
            }
            if (temp >= length) {
                if (dot != null) {
                    sb.append(dot)
                }
                break
            }
        }
        return sb.toString()
    }

    /**
     * 描述：截取字符串从第一个指定字符.
     *
     * @param str1   原文本
     * @param str2   指定字符
     * @param offset 偏移的索引
     * @return 截取后的字符串
     */
    fun cutStringFromChar(str1: String, str2: String, offset: Int): String {
        if (isEmpty(str1)) {
            return ""
        }
        val start = str1.indexOf(str2)
        if (start != -1) {
            if (str1.length > start + offset) {
                return str1.substring(start + offset)
            }
        }
        return ""
    }

    /**
     * 描述：获取字节长度.
     *
     * @param str     文本
     * @param charset 字符集（GBK）
     * @return the int
     */
    fun strlen(str: String?, charset: String): Int {
        if (str == null || str.length == 0) {
            return 0
        }
        var length = 0
        try {
            length = str.toByteArray(charset(charset)).size
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return length
    }

    /**
     * 获取大小的描述.
     *
     * @param size 字节个数
     * @return 大小的描述
     */
    fun getSizeDesc(size: Long): String {
        var size = size
        var suffix = "B"
        if (size >= 1024) {
            suffix = "K"
            size = size shr 10
            if (size >= 1024) {
                suffix = "M"
                // size /= 1024;
                size = size shr 10
                if (size >= 1024) {
                    suffix = "G"
                    size = size shr 10
                    // size /= 1024;
                }
            }
        }
        return size.toString() + suffix
    }

    /**
     * 描述：ip地址转换为10进制数.
     *
     * @param ip the ip
     * @return the long
     */
    fun ip2int(ip: String): Long {
        var ip = ip
        ip = ip.replace(".", ",")
        val items = ip.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return (java.lang.Long.valueOf(items[0])!! shl 24 or (java.lang.Long.valueOf(items[1])!! shl 16)
                or (java.lang.Long.valueOf(items[2])!! shl 8) or java.lang.Long.valueOf(items[3])!!)
    }

    /**
     * 描述：身份证验证
     */

    fun idNumPattern(identityId: String): Boolean {
        // 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        val idNumPattern = Pattern
                .compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])")
        // 通过Pattern获得Matcher
        val idNumMatcher = idNumPattern.matcher(identityId)
        // 判断用户输入是否为身份证号
        return idNumMatcher.matches()
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        println(dateTimeFormat("2012-3-2 12:2:20"))
    }

    /**
     * 获取UUID
     *
     * @return 32UUID小写字符串
     */
    fun gainUUID(): String {
        var strUUID = UUID.randomUUID().toString()
        strUUID = strUUID.replace("-".toRegex(), "").toLowerCase()
        return strUUID
    }

    /**
     * 判断字符串是否非空非null
     *
     * @param strParm 需要判断的字符串
     * @return 真假
     */
    fun isNoBlankAndNoNull(strParm: String?): Boolean {
        return !(strParm == null || strParm == "")
    }

    /**
     * 将文件转成字符串
     *
     * @param file 文件
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getStringFromFile(file: File): String {
        val fin = FileInputStream(file)
        val ret = convertStreamToString(fin)
        // Make sure you close all streams.
        fin.close()
        return ret
    }

    /**
     * 过滤空字符
     *
     * @param str
     * @return
     */
    fun filterBlank(str: String?): String {
        var dest = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            dest = m.replaceAll("")
        }
        return dest
    }

    /**
     * 空判断加强版
     * (空格回车符计为空)
     *
     * @param str
     * @return
     */
    fun isEmptyPlus(str: String?): Boolean {
        var dest: String? = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            dest = m.replaceAll("")

            if (dest != null && dest.length > 0) {
                return false
            }
        }


        return true
    }

    fun isChinese2(text: String): Boolean {
        val array = text.toCharArray()
        for (i in array.indices) {
            if (array[i].toByte().toChar() != array[i]) {
                return true
            }
        }
        return false
    }

    /**
     * 假如obj对象 是null返回""
     *
     * @param obj 待转换的对象
     * @return String数据
     */
    fun nullToStr(obj: Any?): String {
        return if (obj == null || obj.toString() == "null") {
            ""
        } else obj.toString().trim { it <= ' ' }
    }

    /*处理123546789为汉字*/
    fun formatInteger(num: String): String {
        val numArray = charArrayOf('零', '一', '二', '三', '四', '五', '六', '七', '八', '九')
        val units = arrayOf("", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿")
        val `val` = num.toString().toCharArray()
        val len = `val`.size
        val sb = StringBuilder()
        for (i in 0 until len) {
            val m = `val`[i] + ""
            val n = Integer.valueOf(m)!!
            val isZero = n == 0
            val unit = units[len - 1 - i]
            if (isZero) {
                if ('0' == `val`[i - 1]) {
                    continue
                } else {
                    sb.append(numArray[n])
                }
            } else {
                sb.append(numArray[n])
                sb.append(unit)
            }
        }
        return sb.toString()
    }

    fun isChineseCharacter(string: String?): Boolean {
        if (string == null)
            return false
        return string.toCharArray().any {
            isChinese(it)// 有一个中文字符就返回
        }
    }

    // 判断一个字符是否是中文
    private fun isChinese(c: Char): Boolean {
        return c.toInt() in 0x4E00..0x9FA5// 根据字节码判断
    }
}