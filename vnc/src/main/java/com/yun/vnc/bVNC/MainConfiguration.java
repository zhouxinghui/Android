package com.yun.vnc.bVNC;

import android.app.ActivityManager.MemoryInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.yun.vnc.R;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

public abstract class MainConfiguration extends FragmentActivity {
    private final static String TAG = "MainConfiguration";

    protected ConnectionBean selected;
    protected Database database;
    protected Spinner spinnerConnection;
    protected EditText textNickname;
    protected boolean startingOrHasPaused = true;
    protected int layoutID;
    private boolean isConnecting = false;

    protected abstract void updateViewFromSelected();
    protected abstract void updateSelectedFromView();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Utils.showMenu(this);
        setContentView(layoutID);
        System.gc();
        
        textNickname = (EditText) findViewById(R.id.textNickname);
        
        spinnerConnection = (Spinner)findViewById(R.id.spinnerConnection);
        spinnerConnection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> ad, View view, int itemIndex, long id) {
                selected = (ConnectionBean)ad.getSelectedItem();
                updateViewFromSelected();
            }
            @Override
            public void onNothingSelected(AdapterView<?> ad) {
                selected = null;
            }
        });
        
        database = ((App)getApplication()).getDatabase();
    }
    
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart called");
        super.onStart();
        System.gc();
    }
    
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume called");
        super.onResume();
        System.gc();
    }
    
    @Override
    protected void onResumeFragments() {
        Log.i(TAG, "onResumeFragments called");
        super.onResumeFragments();
        System.gc();
        arriveOnPage();
    }
    
    @Override
    public void onWindowFocusChanged (boolean visible) { }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConfigurationChanged called");
        super.onConfigurationChanged(newConfig);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop called");
        if (database != null)
            database.close();
        if ( selected == null ) {
            return;
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause called");
        if (database != null)
            database.close();
        if (!isConnecting) {
            startingOrHasPaused = true;
        } else {
            isConnecting = false;
        }
        updateSelectedFromView();
        selected.saveAndWriteRecent(false, database);
    }
    
    @Override
    protected void onDestroy() {
        if (database != null)
            database.close();
        System.gc();
        super.onDestroy();
    }
    
    protected void canvasStart() {
        if (selected == null) return;
        MemoryInfo info = Utils.getMemoryInfo(this);
        if (info.lowMemory)
            System.gc();
        start();
    }
    
    /**
     * Starts the activity which makes a VNC connection and displays the remote desktop.
     */
    private void start() {
        isConnecting = true;
        updateSelectedFromView();
        Intent intent = new Intent(this, RemoteCanvasActivity.class);
        intent.putExtra(Utils.getConnectionString(this), selected.Gen_getValues());
        startActivity(intent);
    }
    
    public void arriveOnPage() {
        Log.i(TAG, "arriveOnPage called");
        SQLiteDatabase db = database.getReadableDatabase();
        ArrayList<ConnectionBean> connections = new ArrayList<ConnectionBean>();
        ConnectionBean.getAll(db,
                              ConnectionBean.GEN_TABLE_NAME, connections,
                              ConnectionBean.newInstance);
        Collections.sort(connections);
        connections.add(0, new ConnectionBean(this));
        int connectionIndex = 0;
        if (connections.size() > 1) {
            MostRecentBean mostRecent = ConnectionBean.getMostRecent(db);
            if (mostRecent != null) {
                for (int i = 1; i < connections.size(); ++i) {
                    if (connections.get(i).get_Id() == mostRecent.getConnectionId()) {
                        connectionIndex = i;
                        break;
                    }
                }
            }
        }
        database.close();
        spinnerConnection.setAdapter(new ArrayAdapter<ConnectionBean>(this, R.layout.connection_list_entry,
                                     connections.toArray(new ConnectionBean[connections.size()])));
        spinnerConnection.setSelection(connectionIndex, false);
        selected = connections.get(connectionIndex);
        updateViewFromSelected();
        startingOrHasPaused = false;
    }
    
    public Database getDatabaseHelper() {
        return database;
    }
    
    /**
     * Returns the display height, or if the device has software
     * buttons, the 'bottom' of the view (in order to take into account the
     * software buttons.
     * @return the height in pixels.
     */
    public int getHeight () {
        View v    = getWindow().getDecorView().findViewById(android.R.id.content);
        Display d = getWindowManager().getDefaultDisplay();
        int bottom = v.getBottom();
        Point outSize = new Point();
        d.getSize(outSize);
        int height = outSize.y;
        int value = height;
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            ViewConfiguration vc = ViewConfiguration.get(this);
            if (vc.hasPermanentMenuKey())
                value = bottom;
        }
        if (Utils.isBlackBerry ()) {
            value = bottom;
        }
        return value;
    }

    /**
     * Returns the display width, or if the device has software
     * buttons, the 'right' of the view (in order to take into account the
     * software buttons.
     * @return the width in pixels.
     */
    public int getWidth () {
        View v    = getWindow().getDecorView().findViewById(android.R.id.content);
        Display d = getWindowManager().getDefaultDisplay();
        int right = v.getRight();
        Point outSize = new Point();
        d.getSize(outSize);
        int width = outSize.x;
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            ViewConfiguration vc = ViewConfiguration.get(this);
            if (vc.hasPermanentMenuKey())
                return right;
        }
        return width;
    }
    

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.androidvncmenu, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onMenuOpened(int, android.view.Menu)
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            menu.findItem(R.id.itemDeleteConnection).setEnabled(selected != null && !selected.isNew());
            menu.findItem(R.id.itemSaveAsCopy).setEnabled(selected != null && !selected.isNew());
            MenuItem keepScreenOn = menu.findItem(R.id.itemKeepScreenOn);
            keepScreenOn.setChecked(Utils.querySharedPreferenceBoolean(this, Constants.keepScreenOnTag,true));
            MenuItem disableImmersive = menu.findItem(R.id.itemDisableImmersive);
            disableImmersive.setChecked(Utils.querySharedPreferenceBoolean(this, Constants.disableImmersiveTag,true));
            MenuItem forceLandscape = menu.findItem(R.id.itemForceLandscape);
            forceLandscape.setChecked(Utils.querySharedPreferenceBoolean(this, Constants.forceLandscapeTag,true));
            MenuItem rAltAsIsoL3Shift = menu.findItem(R.id.itemRAltAsIsoL3Shift);
            rAltAsIsoL3Shift.setChecked(Utils.querySharedPreferenceBoolean(this, Constants.rAltAsIsoL3ShiftTag));
            MenuItem itemLeftHandedMode = menu.findItem(R.id.itemLeftHandedMode);
            itemLeftHandedMode.setChecked(Utils.querySharedPreferenceBoolean(this, Constants.leftHandedModeTag));
        }
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.itemSaveAsCopy) {
            if (selected.getNickname().equals(textNickname.getText().toString()))
                textNickname.setText("Copy of " + selected.getNickname());
            updateSelectedFromView();
            selected.set_Id(0);
            selected.saveAndWriteRecent(false, database);
            arriveOnPage();

        } else if (i == R.id.itemDeleteConnection) {
            Utils.showYesNoPrompt(this, "Delete?", "Delete " + selected.getNickname() + "?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            selected.Gen_delete(database.getWritableDatabase());
                            database.close();
                            arriveOnPage();
                        }
                    }, null);

        } else if (i == R.id.itemMainScreenHelp) {
            showDialog(R.id.itemMainScreenHelp);

        } else if (i == R.id.itemKeepScreenOn) {
            Utils.toggleSharedPreferenceBoolean(this, Constants.keepScreenOnTag);

        } else if (i == R.id.itemDisableImmersive) {
            Utils.toggleSharedPreferenceBoolean(this, Constants.disableImmersiveTag);

        } else if (i == R.id.itemForceLandscape) {
            Utils.toggleSharedPreferenceBoolean(this, Constants.forceLandscapeTag);

        } else if (i == R.id.itemRAltAsIsoL3Shift) {
            Utils.toggleSharedPreferenceBoolean(this, Constants.rAltAsIsoL3ShiftTag);

        } else if (i == R.id.itemLeftHandedMode) {
            Utils.toggleSharedPreferenceBoolean(this, Constants.leftHandedModeTag);

        }
        return true;
    }
    
    /**
     * This function is used to retrieve data returned by activities started with startActivityForResult.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch(requestCode) {
//        case (Constants.ACTIVITY_GEN_KEY):
//            if (resultCode == Activity.RESULT_OK) {
//                Bundle b = data.getExtras();
//                String privateKey = (String)b.get("PrivateKey");
//                if (!privateKey.equals(selected.getSshPrivKey()) && privateKey.length() != 0)
//                    Toast.makeText(getBaseContext(), "New key generated/imported successfully. Tap 'Generate/Export Key' " +
//                            " button to share, copy to clipboard, or export the public key now.", Toast.LENGTH_LONG).show();
//                selected.saveAndWriteRecent(true, database);
//            } else
//                Log.i (TAG, "The user cancelled SSH key generation.");
//            break;
//        }
    }
}
