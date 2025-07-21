package org.chupcko.wallpaperColor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;

/* import android.util.Log; */

public class WallpaperColor extends Activity implements
  SeekBar.OnSeekBarChangeListener,
  View.OnClickListener,
  View.OnFocusChangeListener
{
  private static final int minColorComponentValue = 0x00;
  private static final int maxColorComponentValue = 0xff;

  private static final String name = "WallpaperColor";
  private static final String colorRedName = "colorRed";
  private static final String colorGreenName = "colorGreen";
  private static final String colorBlueName = "colorBlue";
  private static final String lastColorRedName = "lastColorRed";
  private static final String lastColorGreenName = "lastColorGreen";
  private static final String lastColorBlueName = "lastColorBlue";
  private static final String lastSystemColorRedName = "lastSystemColorRed";
  private static final String lastSystemColorGreenName = "lastSystemColorGreen";
  private static final String lastSystemColorBlueName = "lastSystemColorBlue";
  private static final String lastLockColorRedName = "lastLockColorRed";
  private static final String lastLockColorGreenName = "lastLockColorGreen";
  private static final String lastLockColorBlueName = "lastLockColorBlue";

  private SharedPreferences settings;

  private int colorRed;
  private int colorGreen;
  private int colorBlue;
  private int lastColorRed;
  private int lastColorGreen;
  private int lastColorBlue;
  private int lastSystemColorRed;
  private int lastSystemColorGreen;
  private int lastSystemColorBlue;
  private int lastLockColorRed;
  private int lastLockColorGreen;
  private int lastLockColorBlue;

  private LinearLayout layout;
  private EditText editTextRed;
  private EditText editTextRedHex;
  private SeekBar seekRed;
  private EditText editTextGreen;
  private EditText editTextGreenHex;
  private SeekBar seekGreen;
  private EditText editTextBlue;
  private EditText editTextBlueHex;
  private SeekBar seekBlue;

  @Override public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    layout = (LinearLayout)findViewById(R.id.layout);

    editTextRed = (EditText)findViewById(R.id.edit_red);
    editTextRed.setOnFocusChangeListener(this);
    editTextRedHex = (EditText)findViewById(R.id.edit_red_hex);
    editTextRedHex.setOnFocusChangeListener(this);
    ((Button)findViewById(R.id.button_minus_red)).setOnClickListener(this);
    seekRed = (SeekBar)findViewById(R.id.seek_red);
    seekRed.setOnSeekBarChangeListener(this);
    ((Button)findViewById(R.id.button_plus_red)).setOnClickListener(this);

    editTextGreen = (EditText)findViewById(R.id.edit_green);
    editTextGreen.setOnFocusChangeListener(this);
    editTextGreenHex = (EditText)findViewById(R.id.edit_green_hex);
    editTextGreenHex.setOnFocusChangeListener(this);
    ((Button)findViewById(R.id.button_minus_green)).setOnClickListener(this);
    seekGreen = (SeekBar)findViewById(R.id.seek_green);
    seekGreen.setOnSeekBarChangeListener(this);
    ((Button)findViewById(R.id.button_plus_green)).setOnClickListener(this);

    editTextBlue = (EditText)findViewById(R.id.edit_blue);
    editTextBlue.setOnFocusChangeListener(this);
    editTextBlueHex = (EditText)findViewById(R.id.edit_blue_hex);
    editTextBlueHex.setOnFocusChangeListener(this);
    ((Button)findViewById(R.id.button_minus_blue)).setOnClickListener(this);
    seekBlue = (SeekBar)findViewById(R.id.seek_blue);
    seekBlue.setOnSeekBarChangeListener(this);
    ((Button)findViewById(R.id.button_plus_blue)).setOnClickListener(this);

    ((Button)findViewById(R.id.button_set)).setOnClickListener(this);
    ((Button)findViewById(R.id.button_set_system)).setOnClickListener(this);
    ((Button)findViewById(R.id.button_set_lock)).setOnClickListener(this);

    settings = getSharedPreferences(name, Context.MODE_PRIVATE);
    setRed(settings.getInt(colorRedName, 0));
    setGreen(settings.getInt(colorGreenName, 0));
    setBlue(settings.getInt(colorBlueName, 0));
    lastColorRed = settings.getInt(lastColorRedName, 0);
    lastColorGreen = settings.getInt(lastColorGreenName, 0);
    lastColorBlue = settings.getInt(lastColorBlueName, 0);
    lastSystemColorRed = settings.getInt(lastSystemColorRedName, 0);
    lastSystemColorGreen = settings.getInt(lastSystemColorGreenName, 0);
    lastSystemColorBlue = settings.getInt(lastSystemColorBlueName, 0);
    lastLockColorRed = settings.getInt(lastLockColorRedName, 0);
    lastLockColorGreen = settings.getInt(lastLockColorGreenName, 0);
    lastLockColorBlue = settings.getInt(lastLockColorBlueName, 0);
  }

  @Override public void onStop()
  {
    super.onStop();
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt(colorRedName, colorRed);
    editor.putInt(colorGreenName, colorGreen);
    editor.putInt(colorBlueName, colorBlue);
    editor.putInt(lastColorRedName, lastColorRed);
    editor.putInt(lastColorGreenName, lastColorGreen);
    editor.putInt(lastColorBlueName, lastColorBlue);
    editor.putInt(lastSystemColorRedName, lastSystemColorRed);
    editor.putInt(lastSystemColorGreenName, lastSystemColorGreen);
    editor.putInt(lastSystemColorBlueName, lastSystemColorBlue);
    editor.putInt(lastLockColorRedName, lastLockColorRed);
    editor.putInt(lastLockColorGreenName, lastLockColorGreen);
    editor.putInt(lastLockColorBlueName, lastLockColorBlue);
    editor.commit();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item)
  {
    switch(item.getItemId())
    {
      case R.id.menu_previously_set:
        setPreviously();
        return true;
      case R.id.menu_previously_set_system:
        setPreviouslySystem();
        return true;
      case R.id.menu_previously_set_lock:
        setPreviouslyLock();
        return true;
      case R.id.menu_about:
        showAbout();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
  {
    switch(seekBar.getId())
    {
      case R.id.seek_red:
        setRed(progress);
        break;
      case R.id.seek_green:
        setGreen(progress);
        break;
      case R.id.seek_blue:
        setBlue(progress);
        break;
    }
  }

  public void onStartTrackingTouch(SeekBar seekBar)
  {
  }

  public void onStopTrackingTouch(SeekBar seekBar)
  {
  }

  public void onClick(View view)
  {
    switch(view.getId())
    {
      case R.id.button_minus_red:
        setRed(colorRed-1);
        break;
      case R.id.button_plus_red:
        setRed(colorRed+1);
        break;
      case R.id.button_minus_green:
        setGreen(colorGreen-1);
        break;
      case R.id.button_plus_green:
        setGreen(colorGreen+1);
        break;
      case R.id.button_minus_blue:
        setBlue(colorBlue-1);
        break;
      case R.id.button_plus_blue:
        setBlue(colorBlue+1);
        break;
      case R.id.button_set:
        setWallpaper();
        break;
      case R.id.button_set_system:
        setWallpaperSystem();
        break;
      case R.id.button_set_lock:
        setWallpaperLock();
        break;
    }
  }

  public void onFocusChange(View view, boolean hasFocus)
  {
    switch(view.getId())
    {
      case R.id.edit_red:
        setRed(((TextView)view).getText().toString(), 10);
        break;
      case R.id.edit_red_hex:
        setRed(((TextView)view).getText().toString(), 16);
        break;
      case R.id.edit_green:
        setGreen(((TextView)view).getText().toString(), 10);
        break;
      case R.id.edit_green_hex:
        setGreen(((TextView)view).getText().toString(), 16);
        break;
      case R.id.edit_blue:
        setBlue(((TextView)view).getText().toString(), 10);
        break;
      case R.id.edit_blue_hex:
        setBlue(((TextView)view).getText().toString(), 16);
        break;
    }
  }

  private void setPreviously()
  {
    setRed(lastColorRed);
    setGreen(lastColorGreen);
    setBlue(lastColorBlue);
  }

  private void setPreviouslySystem()
  {
    setRed(lastSystemColorRed);
    setGreen(lastSystemColorGreen);
    setBlue(lastSystemColorBlue);
  }

  private void setPreviouslyLock()
  {
    setRed(lastLockColorRed);
    setGreen(lastLockColorGreen);
    setBlue(lastLockColorBlue);
  }

  private int getColorComponent(int colorComponent)
  {
    if(colorComponent < minColorComponentValue)
      return minColorComponentValue;
    if(colorComponent > maxColorComponentValue)
      return maxColorComponentValue;
    return colorComponent;
  }

  private int getColorComponent(String string, int radix)
  {
    String value;
    if(radix == 16 && string.startsWith("0x"))
      value = string.substring(2);
    else
      value = new String(string);
    try
    {
      return getColorComponent(Integer.parseInt(value, radix));
    }
    catch(NumberFormatException exception)
    {
      return minColorComponentValue;
    }
  }

  private void setRed(int red)
  {
    colorRed = getColorComponent(red);
    updateRed();
  }

  private void setRed(String stringRed, int radix)
  {
    colorRed = getColorComponent(stringRed, radix);
    updateRed();
  }

  private void updateRed()
  {
    editTextRed.setText(Integer.toString(colorRed));
    editTextRedHex.setText(String.format("0x%02x", colorRed));
    seekRed.setProgress(colorRed);
    updateSample();
  }

  private void setGreen(int green)
  {
    colorGreen = getColorComponent(green);
    updateGreen();
  }

  private void setGreen(String stringGreen, int radix)
  {
    colorGreen = getColorComponent(stringGreen, radix);
    updateGreen();
  }

  private void updateGreen()
  {
    editTextGreen.setText(Integer.toString(colorGreen));
    editTextGreenHex.setText(String.format("0x%02x", colorGreen));
    seekGreen.setProgress(colorGreen);
    updateSample();
  }

  private void setBlue(int blue)
  {
    colorBlue = getColorComponent(blue);
    updateBlue();
  }

  private void setBlue(String stringBlue, int radix)
  {
    colorBlue = getColorComponent(stringBlue, radix);
    updateBlue();
  }

  private void updateBlue()
  {
    editTextBlue.setText(Integer.toString(colorBlue));
    editTextBlueHex.setText(String.format("0x%02x", colorBlue));
    seekBlue.setProgress(colorBlue);
    updateSample();
  }

  private void updateSample()
  {
    layout.setBackgroundColor(Color.rgb(seekRed.getProgress(), seekGreen.getProgress(), seekBlue.getProgress()));
  }

  private void setWallpaperDo(int where)
  {
    final DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
    final Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    new Canvas(bitmap).drawColor(Color.rgb(colorRed, colorGreen, colorBlue));
    final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, displayMetrics.widthPixels, displayMetrics.heightPixels, true);
    try
    {
      wallpaperManager.setBitmap(scaledBitmap, null, true, where);
      setResult(RESULT_OK);
      finish();
    }
    catch(IOException exception)
    {
      return;/*# error */
    }
    lastColorRed = colorRed;
    lastColorGreen = colorGreen;
    lastColorBlue = colorBlue;
  }

  private void setWallpaper()
  {
    setWallpaperDo(WallpaperManager.FLAG_SYSTEM|WallpaperManager.FLAG_LOCK);
    lastSystemColorRed = colorRed;
    lastSystemColorGreen = colorGreen;
    lastSystemColorBlue = colorBlue;
    lastLockColorRed = colorRed;
    lastLockColorGreen = colorGreen;
    lastLockColorBlue = colorBlue;
  }

  private void setWallpaperSystem()
  {
    setWallpaperDo(WallpaperManager.FLAG_SYSTEM);
    lastSystemColorRed = colorRed;
    lastSystemColorGreen = colorGreen;
    lastSystemColorBlue = colorBlue;
  }

  private void setWallpaperLock()
  {
    setWallpaperDo(WallpaperManager.FLAG_LOCK);
    lastLockColorRed = colorRed;
    lastLockColorGreen = colorGreen;
    lastLockColorBlue = colorBlue;
  }

  private void showAbout()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.app_name);
    builder.setMessage
    (
      "Version: 1.07\n"+
      "\n"+
      "Code:\n"+
      "Goran \"CHUPCKO\" Lazic\n"+
      "\n"+
      "Thanks:\n"+
      "Aleksandra \"Alexis\" Jovanic"
    );
    builder.create();
    builder.show();
  }
}
