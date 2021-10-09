class Button {

  float x, y;
  float r, d;
  color col, col1, col2;
  byte Val = 0;
  byte Pressed = 0;
  float txtOffSetX, txtOffSetY;
  boolean printVal = false;


  Button(float x_, float y_, float diameter, color col_, color col2_) {
    col1 = col_;
    col2 = col2_;
    col = col2;
    x = x_;
    y = y_;
    d = diameter;
    r = diameter/2;
  }

  void update() {
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
    ellipse(x, y, d*1.4, d*1.4);

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
