package com.yzy.ebag.student.activity.album

import android.content.Context
import android.content.Intent
import ebag.hd.activity.BAlbumActivity

/**
 * Created by unicho on 2018/3/1.
 */
class AlbumActivity: BAlbumActivity() {
    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context,AlbumActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }
    override fun getRole(): Int {
        return BAlbumActivity.STUDENT
    }
}