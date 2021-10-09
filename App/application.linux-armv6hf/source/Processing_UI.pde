// Serial Sending Properties
import processing.serial.*;
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
void setup() {
  size(1600, 600);
  smooth(2);

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

void draw() {
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
    println(char(inByte));
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
void getValues() {

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
