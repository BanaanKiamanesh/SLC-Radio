class PushButton {

  float x, y;
  float d, r;
  color col, col1, col2;
  byte Val = 0;
  byte Pressed = 0;
  float txtOffSetX, txtOffSetY;
  float dist;
  boolean printVal = false;

  PushButton(float x_, float y_, float diameter, color col_, color col2_) {
    col1 = col_;
    col2 = col2_;
    col = col2;
    x = x_;
    y = y_;
    d = diameter;
    r = d/2;
  }

  void update() {

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
    ellipse(x, y, d*1.3, d*1.3);

    fill(col);
    stroke(0);
    strokeWeight(2);
    ellipseMode(CENTER);
    ellipse(x, y, d, d);

    if (printVal) {
      textSize(d*0.4);
      fill(0);
      text(Val, x - txtOffSetX, y - txtOffSetY);
    }
  }

  void textOffset(float x, float y) {
    txtOffSetX = x;
    txtOffSetY = y;
  }

  void printValue(boolean permission) {
    printVal = permission;
  }
}
