import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Processing_UI extends PApplet {

// Serial Sending Properties

Serial myPort;
int operationNum = 0;
int[] values = {0, 0, 0, 0, 0, 0, 0, 0, 0};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Buttons, Joysticks, Sliders, Monitor n Stuff....
Button[] buttons;
JoyStick[] joySticks;
PushButton[] pushButtons;
Monitor LCD;
Slider[] sliders;


////////////////////////////////////////////Initialization/////////////////////////////////////////////////////////////////
public void setup() {
  
  

  buttons = new Button[5];
  joySticks = new JoyStick[2];
  pushButtons = new PushButton[4];
  sliders =new Slider[3];

  myPort = new Serial(this, Serial.list()[0], 115200);

  LCD = new Monitor(800, 175, 700, 250, color(0, 0, 255), color(255));

  sliders[0] = new Slider(1275, 175, 10, 250, color(50, 50, 255));
  sliders[1] = new Slider(1375, 175, 10, 250, color(50, 50, 255));
  sliders[2] = new Slider(1470, 175, 10, 250, color(50, 50, 255));

  buttons[0] = new Button(560, 450, 70, color(0, 100, 0), color(200, 0, 0));
  buttons[1] = new Button(680, 450, 70, color(0, 100, 0), color(200, 0, 0));
  buttons[2] = new Button(800, 450, 70, color(0, 100, 0), color(200, 0, 0));
  buttons[3] = new Button(920, 450, 70, color(0, 100, 0), color(200, 0, 0));
  buttons[4] = new Button(1040, 450, 70, color(0, 100, 0), color(200, 0, 0));

  joySticks[0] = new JoyStick(150, 50, 225, 450, color(150), color(100));
  joySticks[1] = new JoyStick(150, 50, 1375, 450, color(150), color(100));

  pushButtons[0] = new PushButton(300, 100, 90, color(220), color(180));
  pushButtons[1] = new PushButton(150, 100, 90, color(220), color(180));
  pushButtons[2] = new PushButton(300, 250, 90, color(220), color(180));
  pushButtons[3] = new PushButton(150, 250, 90, color(220), color(180));

  joySticks[0].setMode("YFree");
  joySticks[1].setMode("Stable");

  for (int i = 0; i < sliders.length; i++)
    sliders[i].setRange(0, 1000);

  for (int i = 0; i < joySticks.length; i++) {
    joySticks[i].setRangeX(2048, 4095);
    joySticks[i].setRangeY(2048, 4095);
  }

}

public void draw() {
  background(35, 40, 60);
  //background(255);
  for (int i = 0; i < buttons.length; i++) {
    buttons[i].update();
    buttons[i].textOffset(9, -10);
  }

  for (int i=0; i<joySticks.length; i++) 
    joySticks[i].update();

  for (int i = 0; i < pushButtons.length; i++) {
    pushButtons[i].update();
    pushButtons[i].textOffset(12, -12);
  }

  for (int i = 0; i < sliders.length; i++) 
    sliders[i].update();

  LCD.getValues(buttons, joySticks, pushButtons, sliders);
  LCD.update();


  //-------------------------------------------------------------------------------------------
  int inByte = 0;
  boolean operationDone = false;

  while (myPort.available() > 0) {
    inByte = myPort.read();
    println(PApplet.parseChar(inByte));
    operationNum = inByte - 65;
    operationDone = true;
  }

  if (operationDone) {
    int num = values[operationNum];
    String inStr = "";
    inStr += num;
    inStr += '\n';
    myPort.write(inStr);
    operationDone = false;
  }
  getValues();
  //-------------------------------------------------------------------------------------------
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Input Reading Sequence
public void getValues() {

  values[0] = joySticks[0].xVal;
  values[1] = joySticks[0].yVal;
  values[2] = joySticks[1].xVal;
  values[3] = joySticks[1].yVal;

  values[4] = sliders[0].Val;
  values[5] = sliders[1].Val;
  values[6] = sliders[2].Val;

  int buttonsVal = 0;
  for (int i=0; i < buttons.length; i++) 
    buttonsVal |= (buttons[i].Val << i);

  values[7] = buttonsVal;

  int pushVals = 0;
  for (int i=0; i < pushButtons.length; i++) 
    pushVals += pushButtons[i].Val * (i+1);

  values[8] = pushVals;
}
class Button {

  float x, y;
  float r, d;
  int col, col1, col2;
  byte Val = 0;
  byte Pressed = 0;
  float txtOffSetX, txtOffSetY;
  boolean printVal = false;


  Button(float x_, float y_, float diameter, int col_, int col2_) {
    col1 = col_;
    col2 = col2_;
    col = col2;
    x = x_;
    y = y_;
    d = diameter;
    r = diameter/2;
  }

  public void update() {
    if (mousePressed) {
      float dist = dist(mouseX, mouseY, x, y);
      if (dist < r) {  
        switch(mouseButton) {
        case LEFT:
          Pressed = 1;
          Val = 1;
          col = col1;
          break;
        case RIGHT:
          Pressed = 2;
          Val = 0;
          col = col2;          
          break;
        default:
          break;
        }
      }
    } else 
    Pressed = 0;

    fill(40);
    stroke(0);
    strokeWeight(1);
    ellipseMode(CENTER);
    ellipse(x, y, d*1.4f, d*1.4f);

    fill(col);
    stroke(0);
    strokeWeight(2);
    ellipseMode(CENTER);
    ellipse(x, y, d, d);

    if (printVal) {
      textSize(d*0.4f);
      fill(0);
      text(Val, x - txtOffSetX, y - txtOffSetY);
    }
  }

  public void textOffset(float x, float y) {
    txtOffSetX = x;
    txtOffSetY = y;
  }

  public void printValue(boolean permission) {
    printVal = permission;
  }
}
class JoyStick {

  float holderX, holderY, stickX, stickY;
  int holderCol, stickCol;
  float holderW, stickD;
  byte Holded;
  float maxX;
  float minX;
  float maxY;
  float minY;
  float dist;
  int xVal, yVal;
  String[] Modes = {"XStable", "YStable", "Stable", "Free", "XFree", "YFree"};
  String Mode = Modes[3];  //  Mode can be choosed from the set "Modes" 
  boolean printVal = false;
  int maxXVal = 1023;
  int minXVal = 0;
  int maxYVal = 1023;
  int minYVal = 0;


  JoyStick(float holderW_, float stickD_, float holderX_, float holderY_, int holderCol_, int stickCol_) {

    holderX = holderX_;
    holderY = holderY_;
    stickX = holderX;
    stickY = holderY;
    holderCol = holderCol_;
    stickCol = stickCol_;
    holderW = holderW_;
    stickD = stickD_;

    maxX = holderX + holderW/2;
    minX = holderX - holderW/2;
    maxY = holderY + holderW/2;
    minY = holderY - holderW/2;
  }

  public void update() {

    if (mousePressed) {

      dist = dist(mouseX, mouseY, stickX, stickY);

      if (dist < stickD/2) {
        switch(mouseButton) {
        case LEFT:
          Holded = 1;
          break;
        case RIGHT:
          Holded = 2;
          break;
        default:
          break;
        }
      }
    } else 
    Holded = 0;

    if (Mode == Modes[3] ) {
      switch(Holded) {
      case 1:
        //case 2:
        stickX = mouseX;
        if (stickX > maxX)
          stickX = maxX;
        if (stickX < minX)
          stickX = minX;

        stickY = mouseY;
        if (stickY > maxY)
          stickY = maxY;
        if (stickY < minY)
          stickY = minY;
        break;
      default:
        break;
      }
    }

    if (Mode == Modes[0] || Mode == Modes[5]) {

      switch(Holded) {
      case 1:
        //case 2:
        stickY = mouseY;
        if (stickY > maxY)
          stickY = maxY;
        if (stickY < minY)
          stickY = minY;
        stickX = mouseX;
        if (stickX > maxX)
          stickX = maxX;
        if (stickX < minX)
          stickX = minX;    
        break;

      case 0:
        stickX = holderX;
        break;

      default:
        break;
      }
    }

    if (Mode == Modes[1] || Mode == Modes[4]) {

      switch(Holded) {
      case 1:
        //case 2:
        stickY = mouseY;
        if (stickY > maxY)
          stickY = maxY;
        if (stickY < minY)
          stickY = minY;
        stickX = mouseX;
        if (stickX > maxX)
          stickX = maxX;
        if (stickX < minX)
          stickX = minX;    
        break;

      case 0:
        stickY = holderY;
        break;

      default:
        break;
      }
    }

    if (Mode == Modes[2]) {
      switch(Holded) {
      case 1:
        //case 2:
        stickY = mouseY;
        if (stickY > maxY)
          stickY = maxY;
        if (stickY < minY)
          stickY = minY;
        stickX = mouseX;
        if (stickX > maxX)
          stickX = maxX;
        if (stickX < minX)
          stickX = minX;    
        break;

      case 0:
        stickX = holderX;
        stickY = holderY;
        break;

      default:
        break;
      }
    }

    xVal = PApplet.parseInt(map(stickX, minX, maxX, minXVal, maxXVal));
    yVal = PApplet.parseInt(map(stickY, minY, maxY, maxYVal, minYVal));

    stroke(0);
    strokeWeight(6);
    fill(holderCol, 100);
    rectMode(CENTER);
    rect(holderX, holderY, holderW, holderW, 8);

    stroke(0);
    fill(40);
    strokeWeight(0);
    ellipseMode(CENTER);
    ellipse(stickX, stickY, stickD*1.3f, stickD*1.3f);

    stroke(0);
    fill(stickCol);
    strokeWeight(2);
    ellipseMode(CENTER);
    ellipse(stickX, stickY, stickD, stickD);

    fill(0);
    noStroke();
    ellipseMode(CENTER);
    ellipse(stickX, stickY, stickD*0.6f, stickD*0.6f);

    if (printVal) {
      String ValStr = "" ;
      ValStr = "" ;
      ValStr += "(";
      ValStr += xVal;
      ValStr += " , ";
      ValStr += yVal;
      ValStr += ")";
      textSize(20);
      fill(255);
      text(ValStr, holderX-60, holderY-holderW/2-50);
    }
  }

  public void setMode(String Mode_) {
    Mode = Mode_;
  }


  public void listMode() {
    printArray(Modes);
  }

  public void printValue(boolean permission) {
    printVal = permission;
  }

  public void setRangeX(int minX, int maxX) {
    minXVal = minX;
    maxXVal = maxX;
  }

  public void setRangeY(int minY, int maxY) {
    minYVal = minY;
    maxYVal = maxY;
  }
}
class Monitor {

  float x, y;
  float Width, Height;
  int rectCol = color(0, 0, 255);
  int textCol = color(255);
  int[] buttonVals, pushButtonVals;
  int[] joyValsX, joyValsY;
  int[] sliderVals;
  int txtSize = 20;

  Monitor(float x_, float y_, float Width_, float Height_, int rectCol_, int textCol_) {
    x = x_;
    y = y_;
    Width = Width_;
    Height = Height_;
    textCol = textCol_;
    rectCol = rectCol_;
  }

  public void update() {

    fill(40);
    stroke(0);
    strokeWeight(1);
    rectMode(CENTER);
    rect(x, y, Width*1.15f, Height*1.15f, 8);

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

  public void getValues(Button[] buttons, JoyStick[] joySticks, PushButton[] pushButtons, Slider[] sliders) {

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

  public void setTextSize(int i) {
    txtSize = i;
  }
}
class PushButton {

  float x, y;
  float d, r;
  int col, col1, col2;
  byte Val = 0;
  byte Pressed = 0;
  float txtOffSetX, txtOffSetY;
  float dist;
  boolean printVal = false;

  PushButton(float x_, float y_, float diameter, int col_, int col2_) {
    col1 = col_;
    col2 = col2_;
    col = col2;
    x = x_;
    y = y_;
    d = diameter;
    r = d/2;
  }

  public void update() {

    if (mousePressed) {
      dist = dist(mouseX, mouseY, x, y);
      if (dist < r && mouseButton == RIGHT) {  
        Pressed = 1;
        Val = 1;
        col = col2;
      }
    } else {
      Pressed = 0;
      Val = 0;
      col = col1;
    }

    fill(100);
    stroke(0);
    strokeWeight(1);
    ellipseMode(CENTER);
    ellipse(x, y, d*1.3f, d*1.3f);

    fill(col);
    stroke(0);
    strokeWeight(2);
    ellipseMode(CENTER);
    ellipse(x, y, d, d);

    if (printVal) {
      textSize(d*0.4f);
      fill(0);
      text(Val, x - txtOffSetX, y - txtOffSetY);
    }
  }

  public void textOffset(float x, float y) {
    txtOffSetX = x;
    txtOffSetY = y;
  }

  public void printValue(boolean permission) {
    printVal = permission;
  }
}
class Slider {

  float x, y;
  float Width, Height, stickY;
  float d, r;
  int col;
  boolean Pressed = false;
  int Val = 0;
  float maxY, minY;
  int Max = 1023;
  int Min = 0;

  Slider(float x_, float y_, float Width_, float Height_, int col_) {
    x = x_;
    y = y_;
    Width = Width_;
    Height = Height_;
    d = Width*1.8f;
    r = d/2;
    col = col_;
    maxY = y + Height/2;
    minY = y - Height/2;

    stickY = maxY;
  }

  public void update() {

    if (mousePressed) {

      float dist = dist(mouseX, mouseY, x, stickY);

      if (dist < d && mouseButton == RIGHT) 
        Pressed = true;
    } else 
    Pressed = false;

    if (Pressed) {

      stickY = mouseY;
      if (stickY > maxY)
        stickY = maxY;
      if (stickY < minY)
        stickY = minY;
    }

    Val = PApplet.parseInt(map(stickY, minY, maxY, Max, Min));

    stroke(0);
    strokeWeight(1);
    fill(60);
    rectMode(CENTER);
    rect(x, y, Width*1.5f, Height*1.05f, Width/2);

    stroke(0);
    strokeWeight(2);
    fill(col);
    rectMode(CENTER);
    rect(x, y, Width, Height, Width/2);

    stroke(0);
    fill(60);
    strokeWeight(1);
    ellipseMode(CENTER);
    ellipse(x, stickY, d*2, d*2);

    stroke(0);
    fill(col);
    strokeWeight(2);
    ellipseMode(CENTER);
    ellipse(x, stickY, d, d);
  }

  public void setRange(int max, int min) {
    Max = min;
    Min = max;
  }
}
  public void settings() {  size(1600, 600);  smooth(2); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Processing_UI" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
