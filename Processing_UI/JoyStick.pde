class JoyStick {

  float holderX, holderY, stickX, stickY;
  color holderCol, stickCol;
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


  JoyStick(float holderW_, float stickD_, float holderX_, float holderY_, color holderCol_, color stickCol_) {

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

  void update() {

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

    xVal = int(map(stickX, minX, maxX, minXVal, maxXVal));
    yVal = int(map(stickY, minY, maxY, maxYVal, minYVal));

    stroke(0);
    strokeWeight(6);
    fill(holderCol, 100);
    rectMode(CENTER);
    rect(holderX, holderY, holderW, holderW, 8);

    stroke(0);
    fill(40);
    strokeWeight(0);
    ellipseMode(CENTER);
    ellipse(stickX, stickY, stickD*1.3, stickD*1.3);

    stroke(0);
    fill(stickCol);
    strokeWeight(2);
    ellipseMode(CENTER);
    ellipse(stickX, stickY, stickD, stickD);

    fill(0);
    noStroke();
    ellipseMode(CENTER);
    ellipse(stickX, stickY, stickD*0.6, stickD*0.6);

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

  void setMode(String Mode_) {
    Mode = Mode_;
  }


  void listMode() {
    printArray(Modes);
  }

  void printValue(boolean permission) {
    printVal = permission;
  }

  void setRangeX(int minX, int maxX) {
    minXVal = minX;
    maxXVal = maxX;
  }

  void setRangeY(int minY, int maxY) {
    minYVal = minY;
    maxYVal = maxY;
  }
}
