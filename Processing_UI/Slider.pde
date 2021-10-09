class Slider {

  float x, y;
  float Width, Height, stickY;
  float d, r;
  color col;
  boolean Pressed = false;
  int Val = 0;
  float maxY, minY;
  int Max = 1023;
  int Min = 0;

  Slider(float x_, float y_, float Width_, float Height_, color col_) {
    x = x_;
    y = y_;
    Width = Width_;
    Height = Height_;
    d = Width*1.8;
    r = d/2;
    col = col_;
    maxY = y + Height/2;
    minY = y - Height/2;

    stickY = maxY;
  }

  void update() {

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

    Val = int(map(stickY, minY, maxY, Max, Min));

    stroke(0);
    strokeWeight(1);
    fill(60);
    rectMode(CENTER);
    rect(x, y, Width*1.5, Height*1.05, Width/2);

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

  void setRange(int max, int min) {
    Max = min;
    Min = max;
  }
}
