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

/**
 * Keys for intent values
 */
public class Constants {
    
    public static final int SDK_INT = android.os.Build.VERSION.SDK_INT;

    public static final int SOCKET_CONN_TIMEOUT = 30 * 1000; //30 sec
    
    public static volatile int DEFAULT_PROTOCOL_PORT = 5900;
    public static final int DEFAULT_VNC_PORT = 5900;
    // URI Parameters
    public static final String PARAM_CONN_NAME = "ConnectionName";
    public static final String PARAM_VNC_USER = "VncUsername";
    public static final String PARAM_VNC_PWD = "VncPassword";
    public static final String PARAM_COLORMODEL = "ColorModel";
    public static final String PARAM_SAVE_CONN = "SaveConnection";
    public static final String PARAM_VIEW_ONLY = "ViewOnly";
    public static final String PARAM_SCALE_MODE = "ScaleMode";
    public static final String PARAM_EXTRAKEYS_TOGGLE = "ExtraKeysToggle";

    public static final int COLORMODEL_BLACK_AND_WHITE = 1;
    public static final int COLORMODEL_GREYSCALE = 2;
    public static final int COLORMODEL_8_COLORS = 3;
    public static final int COLORMODEL_64_COLORS = 4;
    public static final int COLORMODEL_256_COLORS = 5;
    public static final int COLORMODEL_16BIT = 6;
    public static final int COLORMODEL_24BIT = 7;
    public static final int COLORMODEL_32BIT = 8;
    

//    public static final int EXTRA_KEYS_OFF         = 0;
//    public static final int EXTRA_KEYS_ON          = 1;




    public static final String generalSettingsTag = "generalSettings";
    public static final String keepScreenOnTag = "keepScreenOn";
    public static final String disableImmersiveTag = "disableImmersive";
    public static final String forceLandscapeTag = "forceLandscape";
    public static final String rAltAsIsoL3ShiftTag = "rAltAsIsoL3Shift";
    public static final String leftHandedModeTag = "leftHandedModeTag";
    

    public static final int SHORT_VIBRATION        = 50;
}
