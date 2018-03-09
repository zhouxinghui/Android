/**
 * Copyright (C) 2012 Iordan Iordanov
 * Copyright (C) 2010 Michael A. MacDonald
 * Copyright (C) 2004 Horizon Wimba.  All Rights Reserved.
 * Copyright (C) 2001-2003 HorizonLive.com, Inc.  All Rights Reserved.
 * Copyright (C) 2001,2002 Constantin Kaplinsky.  All Rights Reserved.
 * Copyright (C) 2000 Tridia Corporation.  All Rights Reserved.
 * Copyright (C) 1999 AT&T Laboratories Cambridge.  All Rights Reserved.
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

//
// RemoteCanvas is a subclass of android.view.SurfaceView which draws a VNC
// desktop on it.
//

package com.yun.vnc.bVNC;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.AppCompatImageView;
import android.text.ClipboardManager;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import com.yun.vnc.R;
import com.yun.vnc.android.bc.BCFactory;
import com.yun.vnc.bVNC.input.RemoteKeyboard;
import com.yun.vnc.bVNC.input.RemotePointer;
import com.yun.vnc.bVNC.input.RemoteVncKeyboard;
import com.yun.vnc.bVNC.input.RemoteVncPointer;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

public class RemoteCanvas extends AppCompatImageView {
    private final static String TAG = "RemoteCanvas";
    
    public AbstractScaling canvasZoomer;
    
    // Variable indicating that we are currently scrolling in simulated touchpad mode.
    public boolean cursorBeingMoved = false;
    
    // Connection parameters
    ConnectionBean connection;
    Database database;
    // VNC protocol connection
    public RfbConnectable rfbconn   = null;
    private RfbProto rfb            = null;
    private Socket sock             = null;
    
    boolean maintainConnection = true;
    
    // RFB Decoder
    Decoder decoder = null;
    
    // The remote pointer and keyboard
    RemotePointer pointer;
    RemoteKeyboard keyboard;
    
    // Internal bitmap data
    private int capacity;
    public AbstractBitmapData myDrawable;
    boolean useFull = false;
    boolean compact = false;
    
    // Progress dialog shown at connection time.
    ProgressDialog pd;
    
    // Used to set the contents of the clipboard.
    ClipboardManager clipboard;
    Timer clipboardMonitorTimer;
    ClipboardMonitor clipboardMonitor;
    public boolean serverJustCutText = false;
    
    private Runnable setModes;
    
    // This variable indicates whether or not the user has accepted an untrusted
    // security certificate. Used to control progress while the dialog asking the user
    // to confirm the authenticity of a certificate is displayed.
    private boolean certificateAccepted = false;
    
    /*
     * Position of the top left portion of the <i>visible</i> part of the screen, in
     * full-frame coordinates
     */
    int absoluteXPosition = 0, absoluteYPosition = 0;
    
    /*
     * How much to shift coordinates over when converting from full to view coordinates.
     */
    float shiftX = 0, shiftY = 0;

    /*
     * This variable holds the height of the visible rectangle of the screen. It is used to keep track
     * of how much of the screen is hidden by the soft keyboard if any.
     */
    int visibleHeight = -1;

    /*
     * These variables contain the width and height of the display in pixels
     */
    int displayWidth = 0;
    int displayHeight = 0;
    float displayDensity = 0;
    
    /*
     * This flag indicates whether this is the SPICE 'version' or not.
     */
    boolean spiceUpdateReceived = false;
    
    /*
     * Variable used for BB workarounds.
     */
    boolean bb = false;
    
    /**
     * Constructor used by the inflation apparatus
     * 
     * @param context
     */
    public RemoteCanvas(final Context context, AttributeSet attrs) {
        super(context, attrs);
        
        clipboard = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        
        decoder = new Decoder(this);
        
        final Display display = ((Activity)context).getWindow().getWindowManager().getDefaultDisplay();
        displayWidth  = display.getWidth();
        displayHeight = display.getHeight();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        displayDensity = metrics.density;
        
        if (android.os.Build.MODEL.contains("BlackBerry") ||
            android.os.Build.BRAND.contains("BlackBerry") ||
            android.os.Build.MANUFACTURER.contains("BlackBerry")) {
            bb = true;
        }
    }


    /**
     * Create a view showing a remote desktop connection
     * @param bean Connection settings
     * @param setModes Callback to run on UI thread after connection is set up
     */
    void initializeCanvas(ConnectionBean bean, Database db, final Runnable setModes) {
        this.setModes = setModes;
        connection = bean;
        database = db;
        decoder.setColorModel(COLORMODEL.valueOf(bean.getColorModel()));

        // Startup the connection thread with a progress dialog
        pd = ProgressDialog.show(getContext(), getContext().getString(R.string.info_progress_dialog_connecting),
                                                getContext().getString(R.string.info_progress_dialog_establishing),
                                                true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                closeConnection();
                handler.post(new Runnable() {
                    public void run() {
                        Utils.showFatalErrorMessage(getContext(), getContext().getString(R.string.info_progress_dialog_aborted));
                    }
                });
            }
        });

        // Make this dialog cancellable only upon hitting the Back button and not touching outside.
        pd.setCanceledOnTouchOutside(false);

        Thread t = new Thread() {
            public void run() {
                try {
                    startVncConnection();
                } catch (Throwable e) {
                    if (maintainConnection) {
                        Log.e(TAG, e.toString());
                        e.printStackTrace();
                        // Ensure we dismiss the progress dialog before we finish
                        if (pd.isShowing())
                            pd.dismiss();

                        if (e instanceof OutOfMemoryError) {
                            disposeDrawable ();
                            showFatalMessageAndQuit (getContext().getString(R.string.error_out_of_memory));
                        } else {
                            String error = getContext().getString(R.string.error_connection_failed);
                            if (e.getMessage() != null) {
                                if (e.getMessage().indexOf("SSH") < 0 &&
                                        ( e.getMessage().indexOf("authentication") > -1 ||
                                          e.getMessage().indexOf("Unknown security result") > -1 ||
                                          e.getMessage().indexOf("password check failed") > -1)
                                        ) {
                                    error = getContext().getString(R.string.error_vnc_authentication);
                                }
                                error = error + "<br>" + e.getLocalizedMessage();
                            }
                            showFatalMessageAndQuit(error);
                        }
                    }
                }
            }
        };
        t.start();

        clipboardMonitor = new ClipboardMonitor(getContext(), this);
        if (clipboardMonitor != null) {
            clipboardMonitorTimer = new Timer();
            if (clipboardMonitorTimer != null) {
                try {
                    clipboardMonitorTimer.schedule(clipboardMonitor, 0, 500);
                } catch (NullPointerException e){}
            }
        }
    }


    /**
     * Starts a VNC connection using the TightVNC backend.
     * @throws Exception
     */
    private void startVncConnection() throws Exception {
        Log.i(TAG, "Connecting to: " + connection.getAddress() + ", port: " + connection.getPort());
        String address = getAddress();
        int vncPort = getPort(connection.getPort());

        try {
            rfb = new RfbProto(decoder, this, address, vncPort, connection.getPrefEncoding(), connection.getViewOnly(),
                                connection.getUseLocalCursor(), Constants.SOCKET_CONN_TIMEOUT);
            Log.v(TAG, "Connected to server: " + address + " at port: " + vncPort);
            rfb.initializeAndAuthenticate(connection.getUserName(), connection.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(getContext().getString(R.string.error_vnc_unable_to_connect) +
                                 Utils.messageAndStackTraceAsString(e));
        }

        rfbconn = rfb;
        pointer = new RemoteVncPointer(rfbconn, RemoteCanvas.this, handler);
        boolean rAltAsIsoL3Shift = Utils.querySharedPreferenceBoolean(this.getContext(),
                                                                      Constants.rAltAsIsoL3ShiftTag);
        keyboard = new RemoteVncKeyboard(rfbconn, RemoteCanvas.this, handler, rAltAsIsoL3Shift);

        rfb.writeClientInit();
        rfb.readServerInit();
        initializeBitmap (displayWidth, displayHeight);
        decoder.setPixelFormat(rfb);

        handler.post(new Runnable() {
            public void run() {
                pd.setMessage(getContext().getString(R.string.info_progress_dialog_downloading));
            }
        });

        if (connection.getUseLocalCursor())
            initializeSoftCursor();

        handler.post(drawableSetter);
        handler.post(setModes);

        // Hide progress dialog
        if (pd.isShowing())
            pd.dismiss();

        rfb.processProtocol();
    }


    /**
     * Closes the connection and shows a fatal message which ends the activity.
     * @param error
     */
    void showFatalMessageAndQuit (final String error) {
        closeConnection();
        handler.post(new Runnable() {
            public void run() {
                Utils.showFatalErrorMessage(getContext(), error);
            }
        });
    }


    /**
     * If necessary, initializes an SSH tunnel and returns local forwarded port, or
     * if SSH tunneling is not needed, returns the given port.
     * @return
     * @throws Exception
     */
    int getPort(int port) throws Exception {

        return port;
    }


    /**
     * Returns localhost if using SSH tunnel or saved VNC address.
     * @return
     * @throws Exception
     */
    String getAddress() {
        return connection.getAddress();
    }


    /**
     * Initializes the drawable and bitmap into which the remote desktop is drawn.
     * @param dx
     * @param dy
     * @throws IOException
     */
    void initializeBitmap (int dx, int dy) throws IOException {
        Log.i(TAG, "Desktop name is " + rfbconn.desktopName());
        Log.i(TAG, "Desktop size is " + rfbconn.framebufferWidth() + " x " + rfbconn.framebufferHeight());
        int fbsize = rfbconn.framebufferWidth() * rfbconn.framebufferHeight();
        capacity = BCFactory.getInstance().getBCActivityManager().getMemoryClass(Utils.getActivityManager(getContext()));

        if (connection.getForceFull() == BitmapImplHint.AUTO) {
            if (fbsize * CompactBitmapData.CAPACITY_MULTIPLIER <= capacity*1024*1024) {
                useFull = true;
                compact = true;
            } else if (fbsize * FullBufferBitmapData.CAPACITY_MULTIPLIER <= capacity*1024*1024) {
                useFull = true;
            } else {
                useFull = false;
            }
        } else
            useFull = (connection.getForceFull() == BitmapImplHint.FULL);

        if (!useFull) {
            myDrawable=new LargeBitmapData(rfbconn, this, dx, dy, capacity);
            Log.i(TAG, "Using LargeBitmapData.");
        } else {
            try {
                // TODO: Remove this if Android 4.2 receives a fix for a bug which causes it to stop drawing
                // the bitmap in CompactBitmapData when under load (say playing a video over VNC).
                if (!compact) {
                    myDrawable=new FullBufferBitmapData(rfbconn, this, capacity);
                    Log.i(TAG, "Using FullBufferBitmapData.");
                } else {
                    myDrawable=new CompactBitmapData(rfbconn, this, false);
                    Log.i(TAG, "Using CompactBufferBitmapData.");
                }
            } catch (Throwable e) { // If despite our efforts we fail to allocate memory, use LBBM.
                disposeDrawable ();

                useFull = false;
                myDrawable=new LargeBitmapData(rfbconn, this, dx, dy, capacity);
                Log.i(TAG, "Using LargeBitmapData.");
            }
        }

        decoder.setBitmapData(myDrawable);
    }


    /**
     * Disposes of the old drawable which holds the remote desktop data.
     */
    private void disposeDrawable () {
        if (myDrawable != null)
            myDrawable.dispose();
        myDrawable = null;
        System.gc();
    }


    /**
     * The remote desktop's size has changed and this method
     * reinitializes local data structures to match.
     */
    public void updateFBSize () {
        try {
            myDrawable.frameBufferSizeChanged ();
        } catch (Throwable e) {
            boolean useLBBM = false;

            // If we've run out of memory, try using another bitmapdata type.
            if (e instanceof OutOfMemoryError) {
                disposeDrawable ();

                // If we were using CompactBitmapData, try FullBufferBitmapData.
                if (compact == true) {
                    compact = false;
                    try {
                        myDrawable = new FullBufferBitmapData(rfbconn, this, capacity);
                    } catch (Throwable e2) {
                        useLBBM = true;
                    }
                } else
                    useLBBM = true;

                // Failing FullBufferBitmapData or if we weren't using CompactBitmapData, try LBBM.
                if (useLBBM) {
                    disposeDrawable ();

                    useFull = false;
                    myDrawable = new LargeBitmapData(rfbconn, this, getWidth(), getHeight(), capacity);
                }
                decoder.setBitmapData(myDrawable);
            }
        }
        handler.post(drawableSetter);
        handler.post(setModes);
        myDrawable.syncScroll();
    }


    /**
     * Displays a short toast message on the screen.
     * @param message
     */
    public void displayShortToastMessage (final CharSequence message) {
        screenMessage = message;
        handler.removeCallbacks(showMessage);
        handler.post(showMessage);
    }


    /**
     * Displays a short toast message on the screen.
     * @param messageID
     */
    public void displayShortToastMessage (final int messageID) {
        screenMessage = getResources().getText(messageID);
        handler.removeCallbacks(showMessage);
        handler.post(showMessage);
    }


    /**
     * Lets the drawable know that an update from the remote server has arrived.
     */
    public void doneWaiting () {
        myDrawable.doneWaiting();
    }


    /**
     * Indicates that RemoteCanvas's scroll position should be synchronized with the
     * drawable's scroll position (used only in LargeBitmapData)
     */
    public void syncScroll () {
        myDrawable.syncScroll();
    }


    /**
     * Requests a remote desktop update at the specified rectangle.
     */
    public void writeFramebufferUpdateRequest (int x, int y, int w, int h, boolean incremental) throws IOException {
        myDrawable.prepareFullUpdateRequest(incremental);
        rfbconn.writeFramebufferUpdateRequest(x, y, w, h, incremental);
    }


    /**
     * Requests an update of the entire remote desktop.
     */
    public void writeFullUpdateRequest (boolean incremental) {
        myDrawable.prepareFullUpdateRequest(incremental);
        rfbconn.writeFramebufferUpdateRequest(myDrawable.getXoffset(), myDrawable.getYoffset(),
                                              myDrawable.bmWidth(),    myDrawable.bmHeight(), incremental);
    }


    /**
     * Set the device clipboard text with the string parameter.
     */
    public void setClipboardText(String s) {
        if (s != null && s.length() > 0) {
            clipboard.setText(s);
        }
    }


    /**
     * Method that disconnects from the remote server.
     */
    public void closeConnection() {
        maintainConnection = false;

        if (keyboard != null) {
            // Tell the server to release any meta keys.
            keyboard.clearMetaState();
            keyboard.keyEvent(0, new KeyEvent(KeyEvent.ACTION_UP, 0));
        }
        // Close the rfb connection.
        if (rfbconn != null) {
            rfbconn.close();
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        onDestroy();
    }


    /**
     * Cleans up resources after a disconnection.
     */
    public void onDestroy() {
        Log.v(TAG, "Cleaning up resources");

        removeCallbacksAndMessages();
        if (clipboardMonitorTimer != null) {
            clipboardMonitorTimer.cancel();
            // Occasionally causes a NullPointerException
            //clipboardMonitorTimer.purge();
            clipboardMonitorTimer = null;
        }
        clipboardMonitor = null;
        clipboard        = null;
        setModes         = null;
        decoder          = null;
        canvasZoomer          = null;
        drawableSetter   = null;
        screenMessage    = null;
        desktopInfo      = null;

        disposeDrawable ();
    }


    public void removeCallbacksAndMessages() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /*
     * f(x,s) is a function that returns the coordinate in screen/scroll space corresponding
     * to the coordinate x in full-frame space with scaling s.
     *
     * This function returns the difference between f(x,s1) and f(x,s2)
     *
     * f(x,s) = (x - i/2) * s + ((i - w)/2)) * s
     *        = s (x - i/2 + i/2 + w/2)
     *        = s (x + w/2)
     *
     *
     * f(x,s) = (x - ((i - w)/2)) * s
     * @param oldscaling
     * @param scaling
     * @param imageDim
     * @param windowDim
     * @param offset
     * @return
     */

    /**
     * Computes the X and Y offset for converting coordinates from full-frame coordinates to view coordinates.
     */
    public void computeShiftFromFullToView () {
        shiftX = (rfbconn.framebufferWidth()  - getWidth())  / 2;
        shiftY = (rfbconn.framebufferHeight() - getHeight()) / 2;
    }

    /**
     * Change to Canvas's scroll position to match the absoluteXPosition
     */
    void resetScroll()    {
        float scale = getZoomFactor();
        scrollTo((int)((absoluteXPosition - shiftX) * scale),
                 (int)((absoluteYPosition - shiftY) * scale));
    }


    /**
     * Make sure mouse is visible on displayable part of screen
     */
    public void movePanToMakePointerVisible() {
        if (rfbconn == null)
            return;

        boolean panX = true;
        boolean panY = true;

        // Don't pan in a certain direction if dimension scaled is already less
        // than the dimension of the visible part of the screen.
        if (rfbconn.framebufferWidth()  <= getVisibleWidth())
            panX = false;
        if (rfbconn.framebufferHeight() <= getVisibleHeight())
            panY = false;

        // We only pan if the current scaling is able to pan.
        if (canvasZoomer != null && ! canvasZoomer.isAbleToPan())
            return;

        int x = pointer.getX();
        int y = pointer.getY();
        boolean panned = false;
        int w = getVisibleWidth();
        int h = getVisibleHeight();
        int iw = getImageWidth();
        int ih = getImageHeight();
        int wthresh = 30;
        int hthresh = 30;

        int newX = absoluteXPosition;
        int newY = absoluteYPosition;

        if (x - absoluteXPosition >= w - wthresh) {
            newX = x - (w - wthresh);
            if (newX + w > iw)
                newX = iw - w;
        } else if (x < absoluteXPosition + wthresh) {
            newX = x - wthresh;
            if (newX < 0)
                newX = 0;
        }
        if ( panX && newX != absoluteXPosition ) {
            absoluteXPosition = newX;
            panned = true;
        }

        if (y - absoluteYPosition >= h - hthresh) {
            newY = y - (h - hthresh);
            if (newY + h > ih)
                newY = ih - h;
        } else if (y < absoluteYPosition + hthresh) {
            newY = y - hthresh;
            if (newY < 0)
                newY = 0;
        }
        if ( panY && newY != absoluteYPosition ) {
            absoluteYPosition = newY;
            panned = true;
        }

        if (panned) {
            //scrollBy(newX - absoluteXPosition, newY - absoluteYPosition);
            resetScroll();
        }
    }

    /**
     * Pan by a number of pixels (relative pan)
     * @param dX
     * @param dY
     * @return True if the pan changed the view (did not move view out of bounds); false otherwise
     */
    public boolean relativePan(int dX, int dY) {

        // We only pan if the current scaling is able to pan.
        if (canvasZoomer != null && ! canvasZoomer.isAbleToPan())
            return false;

        double scale = getZoomFactor();

        double sX = (double)dX / scale;
        double sY = (double)dY / scale;

        if (absoluteXPosition + sX < 0)
            // dX = diff to 0
            sX = -absoluteXPosition;
        if (absoluteYPosition + sY < 0)
            sY = -absoluteYPosition;

        // Prevent panning right or below desktop image
        if (absoluteXPosition + getVisibleWidth() + sX > getImageWidth())
            sX = getImageWidth() - getVisibleWidth() - absoluteXPosition;
        if (absoluteYPosition + getVisibleHeight() + sY > getImageHeight())
            sY = getImageHeight() - getVisibleHeight() - absoluteYPosition;

        absoluteXPosition += sX;
        absoluteYPosition += sY;
        if (sX != 0.0 || sY != 0.0) {
            //scrollBy((int)sX, (int)sY);
            resetScroll();
            return true;
        }
        return false;
    }

    /**
     * Absolute pan.
     * @param x
     * @param y
     */
    public void absolutePan(int x, int y) {
        if (canvasZoomer != null) {
            int vW = getVisibleWidth();
            int vH = getVisibleHeight();
            int w = getImageWidth();
            int h = getImageHeight();
            if (x + vW > w) x = w - vW;
            if (y + vH > h) y = h - vH;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            absoluteXPosition = x;
            absoluteYPosition = y;
            resetScroll();
        }
    }

    /* (non-Javadoc)
     * @see android.view.View#onScrollChanged(int, int, int, int)
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (myDrawable != null) {
            myDrawable.scrollChanged(absoluteXPosition, absoluteYPosition);
            pointer.movePointerToMakeVisible();
        }
    }


    /**
     * This runnable sets the drawable (contained in myDrawable) for the VncCanvas (ImageView).
     */
    private Runnable drawableSetter = new Runnable() {
        public void run() {
            if (myDrawable != null)
                myDrawable.setImageDrawable(RemoteCanvas.this);
            }
    };


    /**
     * This runnable displays a message on the screen.
     */
    CharSequence screenMessage;
    private Runnable showMessage = new Runnable() {
            public void run() { Toast.makeText( getContext(), screenMessage, Toast.LENGTH_SHORT).show(); }
    };


    /**
     * This runnable causes a toast with information about the current connection to be shown.
     */
    private Runnable desktopInfo = new Runnable() {
        public void run() {
            showConnectionInfo();
        }
    };


    /**
     * Causes a redraw of the myDrawable to happen at the indicated coordinates.
     */
    public void reDraw(int x, int y, int w, int h) {
        float scale = getZoomFactor();
        float shiftedX = x-shiftX;
        float shiftedY = y-shiftY;
        // Make the box slightly larger to avoid artifacts due to truncation errors.
        postInvalidate ((int)((shiftedX-1)*scale),   (int)((shiftedY-1)*scale),
                        (int)((shiftedX+w+1)*scale), (int)((shiftedY+h+1)*scale));
    }


    /**
     * This is a float-accepting version of reDraw().
     * Causes a redraw of the myDrawable to happen at the indicated coordinates.
     */
    public void reDraw(float x, float y, float w, float h) {
        float scale = getZoomFactor();
        float shiftedX = x-shiftX;
        float shiftedY = y-shiftY;
        // Make the box slightly larger to avoid artifacts due to truncation errors.
        postInvalidate ((int)((shiftedX-1.f)*scale),   (int)((shiftedY-1.f)*scale),
                        (int)((shiftedX+w+1.f)*scale), (int)((shiftedY+h+1.f)*scale));
    }

    /**
     * Displays connection info in a toast message.
     */
    public void showConnectionInfo() {
        if (rfbconn == null)
            return;

        String msg = null;
        int idx = rfbconn.desktopName().indexOf("(");
        if (idx > 0) {
            // Breakup actual desktop name from IP addresses for improved
            // readability
            String dn = rfbconn.desktopName().substring(0, idx).trim();
            String ip = rfbconn.desktopName().substring(idx).trim();
            msg = dn + "\n" + ip;
        } else
            msg = rfbconn.desktopName();
        msg += "\n" + rfbconn.framebufferWidth() + "x" + rfbconn.framebufferHeight();
        String enc = rfbconn.getEncoding();
        // Encoding might not be set when we display this message
        if (decoder.getColorModel() != null) {
            if (enc != null && !enc.equals(""))
                msg += ", " + rfbconn.getEncoding() + getContext().getString(R.string.info_encoding) + decoder.getColorModel().toString();
            else
                msg += ", " + decoder.getColorModel().toString();
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * Invalidates (to redraw) the location of the remote pointer.
     */
    public void invalidateMousePosition() {
        if (myDrawable != null) {
            myDrawable.moveCursorRect(pointer.getX(), pointer.getY());
            RectF r = myDrawable.getCursorRect();
            reDraw(r.left, r.top, r.width(), r.height());
        }
    }


    /**
     * Moves soft cursor into a particular location.
     * @param x
     * @param y
     */
    synchronized void softCursorMove(int x, int y) {
        if (myDrawable.isNotInitSoftCursor()) {
            initializeSoftCursor();
        }

        if (!cursorBeingMoved) {
            pointer.setX(x);
            pointer.setY(y);
            RectF prevR = new RectF(myDrawable.getCursorRect());
            // Move the cursor.
            myDrawable.moveCursorRect(x, y);
            // Show the cursor.
            RectF r = myDrawable.getCursorRect();
            reDraw(r.left, r.top, r.width(), r.height());
            reDraw(prevR.left, prevR.top, prevR.width(), prevR.height());
        }
    }


    /**
     * Initializes the data structure which holds the remote pointer data.
     */
    void initializeSoftCursor () {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
        int w = bm.getWidth();
        int h = bm.getHeight();
        int [] tempPixels = new int[w*h];
        bm.getPixels(tempPixels, 0, w, 0, 0, w, h);
        // Set cursor rectangle as well.
        myDrawable.setCursorRect(pointer.getX(), pointer.getY(), w, h, 0, 0);
        // Set softCursor to whatever the resource is.
        myDrawable.setSoftCursor (tempPixels);
        bm.recycle();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        Log.d(TAG, "onCreateInputConnection called");
        BaseInputConnection bic = new BaseInputConnection(this, false);
        outAttrs.actionLabel = null;
        outAttrs.inputType = InputType.TYPE_NULL;
        String currentIme = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        Log.d(TAG, "currentIme: " + currentIme);
        outAttrs.imeOptions |= EditorInfo.IME_FLAG_NO_FULLSCREEN;
        return bic;
    }

    public RemotePointer getPointer() {
        return pointer;
    }

    public RemoteKeyboard getKeyboard() {
        return keyboard;
    }

    public float getZoomFactor() {
        if (canvasZoomer == null)
            return 1;
        return canvasZoomer.getZoomFactor();
    }

    public int getVisibleWidth() {
        return (int)((double)getWidth() / getZoomFactor() + 0.5);
    }

    public void setVisibleHeight(int newHeight) {
        visibleHeight = newHeight;
    }

    public int getVisibleHeight() {
        if (visibleHeight > 0)
            return (int)((double)visibleHeight / getZoomFactor() + 0.5);
        else
            return (int)((double)getHeight() / getZoomFactor() + 0.5);
    }

    public int getImageWidth() {
        return rfbconn.framebufferWidth();
    }

    public int getImageHeight() {
        return rfbconn.framebufferHeight();
    }

    public int getCenteredXOffset() {
        return (rfbconn.framebufferWidth() - getWidth()) / 2;
    }

    public int getCenteredYOffset() {
        return (rfbconn.framebufferHeight() - getHeight()) / 2;
    }

    public float getMinimumScale() {
        if (myDrawable != null) {
            return myDrawable.getMinimumScale();
        } else
            return 1.f;
    }

    public float getDisplayDensity() {
        return displayDensity;
    }

    public boolean isColorModel(COLORMODEL cm) {
        return (decoder.getColorModel() != null) && decoder.getColorModel().equals(cm);
    }

    public void setColorModel(COLORMODEL cm) {
        decoder.setColorModel(cm);
    }

    public boolean getMouseFollowPan() {
        return connection.getFollowPan();
    }

    public int getAbsX () {
        return absoluteXPosition;
    }

    public int getAbsY () {
        return absoluteYPosition;
    }

    /**
     * Used to wait until getWidth and getHeight return sane values.
     */
    private void waitUntilInflated() {
        synchronized (this) {
            while (getWidth() == 0 || getHeight() == 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Used to detect when the view is inflated to a sane size other than 0x0.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w > 0 && h > 0) {
            synchronized (this) {
                this.notify();
            }
        }
    }

    /**
     * Handler for the dialogs that display the x509/RDP/SSH key signatures to the user.
     * Also shows the dialogs which show various connection failures.
     */
    public Handler handler = new Handler();

    public boolean isCertificateAccepted() {
        return certificateAccepted;
    }
}
