class Monitor {

  float x, y;
  float Width, Height;
  color rectCol = color(0, 0, 255);
  color textCol = color(255);
  int[] buttonVals, pushButtonVals;
  int[] joyValsX, joyValsY;
  int[] sliderVals;
  int txtSize = 20;

  Monitor(float x_, float y_, float Width_, float Height_, color rectCol_, color textCol_) {
    x = x_;
    y = y_;
    Width = Width_;
    Height = Height_;
    textCol = textCol_;
    rectCol = rectCol_;
  }

  void update() {

    fill(40);
    stroke(0);
    strokeWeight(1);
    rectMode(CENTER);
    rect(x, y, Width*1.15, Height*1.15, 8);

    fill(rectCol);
    stroke(0);
    strokeWeight(5);
    rectMode(CENTER);
    rect(x, y, Width, Height, 15);

    // Print Values
    String Line1 = "";
    int i;
    for (i = 0; i < buttonVals.length; i++) 
      Line1 += buttonVals[i];

    Line1 += "     ";

    for (i = 0; i < pushButtonVals.length; i++) 
      Line1 += pushButtonVals[i];

    String X1 = "X1 =  "; 
    X1 += joyValsX[0]; 
    String X2 = "X2 =  "; 
    X2 += joyValsX[1]; 
    String Y1 = "Y1 =  "; 
    Y1 += joyValsY[0]; 
    String Y2 = "Y2 =  "; 
    Y2 += joyValsY[1]; 

    textSize(30);
    fill(textCol);
    text(Line1, x-Width/2 + txtSize, y-Height/2 +txtSize*2);

    textSize(30);
    fill(textCol);
    text(X1, x-Width/2 + txtSize, y-Height/2 +txtSize*4);

    textSize(30);
    fill(textCol);
    text(X2, x-Width/2 + txtSize + 200, y-Height/2 +txtSize*4);

    textSize(30);
    fill(textCol);
    text(Y1, x-Width/2 + txtSize, y-Height/2 +txtSize*6);

    textSize(30);
    fill(textCol);
    text(Y2, x-Width/2 + txtSize + 200, y-Height/2 +txtSize*6);

    String S1 = "S1 =  ";
    S1 += sliderVals[0];
    String S2 = "S2 =  ";
    S2 += sliderVals[1];
    String S3 = "S3 =  ";
    S3 += sliderVals[2];

    textSize(30);
    fill(textCol);
    text(S1, x-Width/2 + txtSize + 400, y-Height/2 +txtSize*4);

    textSize(30);
    fill(textCol);
    text(S2, x-Width/2 + txtSize + 400, y-Height/2 +txtSize*6);

    textSize(30);
    fill(textCol);
    text(S3, x-Width/2 + txtSize + 400, y-Height/2 +txtSize*8);
  }

  void getValues(Button[] buttons, JoyStick[] joySticks, PushButton[] pushButtons, Slider[] sliders) {

    int pushButtonLen = pushButtons.length;
    int buttonLen = buttons.length;
    int joyLen = joySticks.length;
    int sliderLen = sliders.length;
    int i;

    buttonVals = new int[buttonLen];
    pushButtonVals = new int[pushButtonLen];
    joyValsX = new int[joyLen];
    joyValsY = new int[joyLen];
    sliderVals = new int[sliderLen];

    for (i = 0; i< buttonLen; i++) 
      buttonVals[i] = buttons[i].Val;

    for (i = 0; i< pushButtonLen; i++) 
      pushButtonVals[i] = pushButtons[i].Val;

    for (i = 0; i< joyLen; i++) {
      joyValsX[i] = joySticks[i].xVal;
      joyValsY[i] = joySticks[i].yVal;
    }

    for (i = 0; i< sliderLen; i++)
      sliderVals[i] = sliders[i].Val;
  }

  void setTextSize(int i) {
    txtSize = i;
  }
}
