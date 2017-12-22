package com.yzy.ebag.student

import android.util.Log
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by unicho on 2017/11/13.
 */
class Demo2 {

    private fun test() {
        Observable.intervalRange(0, 1, 0, 1, TimeUnit.SECONDS)
                .map{60 - it}
                .subscribe({ Log.e("", "long") }) { Log.e("", "error") }
    }
}