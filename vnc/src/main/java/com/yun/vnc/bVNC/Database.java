/**
 * Copyright (C) 2012 Iordan Iordanov
 * Copyright (C) 2010 Michael A. MacDonald
 * 
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 * USA.
 */

package com.yun.vnc.bVNC;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * @author Michael A. MacDonald
 *
 */
public class Database extends SQLiteOpenHelper {
    private static final int CURRENT = 1;
    private static String dbName = "vnc_db";
    private static String password = "ysb123456";
    
    public final static String TAG = Database.class.toString();
    
    public Database(Context context) {
        super(context, dbName, null, CURRENT);
        SQLiteDatabase.loadLibs(context);
    }

    /* (non-Javadoc)
     * @see net.sqlcipher.database.SQLiteOpenHelper#onCreate(net.sqlcipher.database.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        
        db.execSQL(AbstractConnectionBean.GEN_CREATE);
        db.execSQL(MostRecentBean.GEN_CREATE);
        db.execSQL(MetaList.GEN_CREATE);
        db.execSQL(AbstractMetaKeyBean.GEN_CREATE);
        db.execSQL(SentTextBean.GEN_CREATE);
        
        db.execSQL("INSERT INTO " + MetaList.GEN_TABLE_NAME + " VALUES ( 1, 'DEFAULT')");
    }
    
    public SQLiteDatabase getWritableDatabase() {
        return getWritableDatabase(Database.password);
    }
    
    public SQLiteDatabase getReadableDatabase() {
        return getReadableDatabase(Database.password);
    }
    
    /* (non-Javadoc)
     * @see net.sqlcipher.database.SQLiteOpenHelper#onUpgrade(net.sqlcipher.database.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
