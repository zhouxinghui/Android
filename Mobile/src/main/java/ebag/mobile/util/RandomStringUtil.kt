package ebag.mobile.util

import java.util.*



/**
 *  Created by fansan on 2018/5/21 17:18
 */
 
class RandomStringUtil{

    companion object {
        fun getString():String{
            val str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890"
            val random = Random()
            val sb = StringBuffer()
            for (i in 0 until 40) {
                val number = random.nextInt(62)
                sb.append(str[number])
            }

            return sb.toString()
        }
    }
}