package org.chupcko.wallpaperColor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class colorSampleBox extends View
{
  private int colorRed;
  private int colorGreen;
  private int colorBlue;

  public colorSampleBox(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    colorRed = 0;
    colorGreen = 0;
    colorBlue = 0;
  }

  @Override protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    canvas.drawColor(Color.rgb(colorRed, colorGreen, colorBlue));
  }

  public void setColor(int newColorRed, int newColorGreen, int newColorBlue)
  {
    colorRed = newColorRed;
    colorGreen = newColorGreen;
    colorBlue = newColorBlue;
    invalidate();
  }
}
