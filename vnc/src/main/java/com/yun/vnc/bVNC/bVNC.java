/** 
 * Copyright (C) 2012 Iordan Iordanov
 * Copyright (C) 20?? Michael A. MacDonald
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yun.vnc.R;

/**
 * bVNC is the Activity for setting up VNC connections.
 */
public class bVNC extends MainConfiguration {
    private final static String TAG = "androidVNC";
    private LinearLayout layoutAdvancedSettings;
    private EditText ipText;
    private EditText portText;
    private EditText passwordText;
    private Button goButton;
    private ToggleButton toggleAdvancedSettings;
    private RadioGroup groupForceFullScreen;
    private Spinner colorSpinner;
    private EditText textNickname;
    private EditText textUsername;
    private CheckBox checkboxKeepPassword;
    private CheckBox checkboxUseDpadAsArrows;
    private CheckBox checkboxLocalCursor;
    private CheckBox checkboxPreferHextile;
    private CheckBox checkboxViewOnly;

    @Override
    public void onCreate(Bundle icicle) {
        layoutID = R.layout.main;
        super.onCreate(icicle);
        
        ipText = (EditText) findViewById(R.id.textIP);
        portText = (EditText) findViewById(R.id.textPORT);
        passwordText = (EditText) findViewById(R.id.textPASSWORD);
        textNickname = (EditText) findViewById(R.id.textNickname);
        textUsername = (EditText) findViewById(R.id.textUsername);

        // The advanced settings button.
        toggleAdvancedSettings = (ToggleButton) findViewById(R.id.toggleAdvancedSettings);
        layoutAdvancedSettings = (LinearLayout) findViewById(R.id.layoutAdvancedSettings);
        toggleAdvancedSettings.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean checked) {
                if (checked)
                    layoutAdvancedSettings.setVisibility(View.VISIBLE);
                else
                    layoutAdvancedSettings.setVisibility(View.GONE);
            }
        });
        
        colorSpinner = (Spinner)findViewById(R.id.colorformat);
        COLORMODEL[] models=COLORMODEL.values();
        ArrayAdapter<COLORMODEL> colorSpinnerAdapter = new ArrayAdapter<COLORMODEL>(this, R.layout.connection_list_entry, models);
        groupForceFullScreen = (RadioGroup)findViewById(R.id.groupForceFullScreen);
        checkboxKeepPassword = (CheckBox)findViewById(R.id.checkboxKeepPassword);
        checkboxUseDpadAsArrows = (CheckBox)findViewById(R.id.checkboxUseDpadAsArrows);
        checkboxLocalCursor = (CheckBox)findViewById(R.id.checkboxUseLocalCursor);
        checkboxPreferHextile = (CheckBox)findViewById(R.id.checkboxPreferHextile);
        checkboxViewOnly = (CheckBox)findViewById(R.id.checkboxViewOnly);
        colorSpinner.setAdapter(colorSpinnerAdapter);
        colorSpinner.setSelection(0);

        goButton = (Button) findViewById(R.id.buttonGO);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ipText.getText().length() != 0 && portText.getText().length() != 0)
                    canvasStart();
                else
                    Toast.makeText(view.getContext(), R.string.vnc_server_empty, Toast.LENGTH_LONG).show();
            }
        });
    }
    

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == R.id.itemMainScreenHelp)
            return createHelpDialog();
        return null;
    }
    
    /**
     * Creates the help dialog for this activity.
     */
    private Dialog createHelpDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this)
                .setMessage(R.string.main_screen_help_text)
                .setPositiveButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                                // We don't have to do anything.
                            }
                        });
        Dialog d = adb.setView(new ListView(this)).create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.show();
        d.getWindow().setAttributes(lp);
        return d;
    }
    
    public void updateViewFromSelected() {
        if (selected == null)
            return;
        if (selected.getAddress().equals(""))
            ipText.setText("localhost");
        else
            ipText.setText(selected.getAddress());

        // If we are doing automatic X session discovery, then disable
        // vnc address, vnc port, and vnc password, and vice-versa
        ipText.setVisibility(View.VISIBLE);
        portText.setVisibility(View.VISIBLE);
        textUsername.setVisibility(View.VISIBLE);
        passwordText.setVisibility(View.VISIBLE);
        checkboxKeepPassword.setVisibility(View.VISIBLE);

        portText.setText(String.valueOf(selected.getPort()));
        
        if (selected.getKeepPassword() || selected.getPassword().length()>0) {
            passwordText.setText(selected.getPassword());
        }
        groupForceFullScreen.check(selected.getForceFull()==BitmapImplHint.AUTO ?
                            R.id.radioForceFullScreenAuto : R.id.radioForceFullScreenOn);
        checkboxKeepPassword.setChecked(selected.getKeepPassword());
        checkboxUseDpadAsArrows.setChecked(selected.getUseDpadAsArrows());
        checkboxLocalCursor.setChecked(selected.getUseLocalCursor());
        checkboxPreferHextile.setChecked(selected.getPrefEncoding() == RfbProto.EncodingHextile);
        checkboxViewOnly.setChecked(selected.getViewOnly());
        textNickname.setText(selected.getNickname());
        textUsername.setText(selected.getUserName());
        COLORMODEL cm = COLORMODEL.valueOf(selected.getColorModel());
        COLORMODEL[] colors=COLORMODEL.values();
        for (int i=0; i<colors.length; ++i)
        {
            if (colors[i] == cm) {
                colorSpinner.setSelection(i);
                break;
            }
        }
    }

    /**
     * Returns the current ConnectionBean.
     */
    public ConnectionBean getCurrentConnection () {
        return selected;
    }

    protected void updateSelectedFromView() {
        if (selected == null) {
            return;
        }
        selected.setAddress(ipText.getText().toString());
        try    {
            selected.setPort(Integer.parseInt(portText.getText().toString()));
        }
        catch (NumberFormatException nfe) {}
        
        selected.setNickname(textNickname.getText().toString());


        // If we are using an SSH key, then the ssh password box is used
        // for the key pass-phrase instead.
        selected.setUserName(textUsername.getText().toString());
        selected.setForceFull(groupForceFullScreen.getCheckedRadioButtonId()== R.id.radioForceFullScreenAuto ? BitmapImplHint.AUTO : (groupForceFullScreen.getCheckedRadioButtonId()== R.id.radioForceFullScreenOn ? BitmapImplHint.FULL : BitmapImplHint.TILE));
        selected.setPassword(passwordText.getText().toString());
        selected.setKeepPassword(checkboxKeepPassword.isChecked());
        selected.setUseDpadAsArrows(checkboxUseDpadAsArrows.isChecked());
        selected.setUseLocalCursor(checkboxLocalCursor.isChecked());
        if (checkboxPreferHextile.isChecked())
            selected.setPrefEncoding(RfbProto.EncodingHextile);
        else
            selected.setPrefEncoding(RfbProto.EncodingTight);
        selected.setViewOnly(checkboxViewOnly.isChecked());

        selected.setColorModel(((COLORMODEL)colorSpinner.getSelectedItem()).nameString());
    }
}
