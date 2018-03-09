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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.IOException;
import java.lang.NumberFormatException;

/* import android.util.Log; */

public class wallpaperColor extends Activity implements
  SeekBar.OnSeekBarChangeListener,
  View.OnClickListener,
  View.OnFocusChangeListener
{
  private final int minColorComponentValue = 0x00;
  private final int maxColorComponentValue = 0xff;

  private final String name = "wallpaperColor";
  private final String colorRedName = "colorRed";
  private final String colorGreenName = "colorGreen";
  private final String colorBlueName = "colorBlue";
  private final String lastColorRedName = "lastColorRed";
  private final String lastColorGreenName = "lastColorGreen";
  private final String lastColorBlueName = "lastColorBlue";

  private int colorRed;
  private int colorGreen;
  private int colorBlue;
  private int lastColorRed;
  private int lastColorGreen;
  private int lastColorBlue;

  private EditText editTextRed;
  private EditText editTextRedHex;
  private SeekBar seekRed;
  private EditText editTextGreen;
  private EditText editTextGreenHex;
  private SeekBar seekGreen;
  private EditText editTextBlue;
  private EditText editTextBlueHex;
  private SeekBar seekBlue;
  private colorSampleBox colorSample;

  @Override public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

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
    colorSample = (colorSampleBox)findViewById(R.id.color_sample);

    SharedPreferences settings = getSharedPreferences(name, Context.MODE_PRIVATE);
    setRed(settings.getInt(colorRedName, 0));
    setGreen(settings.getInt(colorGreenName, 0));
    setBlue(settings.getInt(colorBlueName, 0));
    lastColorRed = settings.getInt(lastColorRedName, 0);
    lastColorGreen = settings.getInt(lastColorGreenName, 0);
    lastColorBlue = settings.getInt(lastColorBlueName, 0);
  }

  @Override public void onStop()
  {
    super.onStop();
    SharedPreferences settings = getSharedPreferences(name, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt(colorRedName, colorRed);
    editor.putInt(colorGreenName, colorGreen);
    editor.putInt(colorBlueName, colorBlue);
    editor.putInt(lastColorRedName, lastColorRed);
    editor.putInt(lastColorGreenName, lastColorGreen);
    editor.putInt(lastColorBlueName, lastColorBlue);
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
    try
    {
      return getColorComponent(Integer.parseInt(string, radix));
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
    editTextRedHex.setText(Integer.toHexString(colorRed));
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
    editTextGreenHex.setText(Integer.toHexString(colorGreen));
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
    editTextBlueHex.setText(Integer.toHexString(colorBlue));
    seekBlue.setProgress(colorBlue);
    updateSample();
  }

  private void updateSample()
  {
    colorSample.setColor(seekRed.getProgress(), seekGreen.getProgress(), seekBlue.getProgress());
  }

  private void setWallpaper()
  {
    final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
    final Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    new Canvas(bitmap).drawColor(Color.rgb(colorRed, colorGreen, colorBlue));
    try
    {
      wallpaperManager.setBitmap(bitmap);
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

  private void showAbout()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.app_name);
    builder.setMessage
    (
      "Version: 1.01\n"+
      "\n"+
      "Code:\n"+
      "Goran \"CHUPCKO\" Lazic\n"+
      "\n"+
      "Thanks:\n"+
      "Aleksandra \"Alexis\" Jovanic\n"+
      "Slavica Marinkovic"
    );
    builder.create();
    builder.show();
  }
}
