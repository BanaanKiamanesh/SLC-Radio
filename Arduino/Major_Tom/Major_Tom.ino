/////////////////////////////////////////////LCD I2C Setup
#include <LiquidCrystal_I2C.h>
LiquidCrystal_I2C lcd(0x27 , 20, 4);

////////////////////////////////////////////Initial Vars for Serial Input
int Values[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
const int valuesLen = 9;
int posX[] = {3, 3, 3, 3, 12, 12, 12, 9, 15};
int posY[] = {0, 1, 2, 3, 0, 1, 2, 3, 3};

////////////////////////////////////////////Radio Setup and Properties
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

////////////////////////////////////////////Radio Specs
RF24 radio(7, 8); // CE, CSN
const uint64_t pipe = 0xF0F0F0F0E1LL;

///////////////////////////////////////////Data Package to be Recieved
typedef struct {
  int Buttons;   // Buttons (Mode Indicator!)
  int PButton;   // Push Button #
  int S1;        // Potentiometer #1
  int S2;        // Potentiometer #2
  int S3;        // Potentiometer #3
  int JL_Y;      // Left Joystick Y Axis ( Throttle Perhaps)
  int JL_X;      // Left Joystick X Axis ( Yaw Perhaps)
  int JR_Y;      // Right Joystick Y Axis ( Pitch Perhaps)
  int JR_X;      // Right Joystick X Axis ( Roll Perhaps)

} ControlDef;

ControlDef ControlPack;

//////////////////////////////////////////////////////////Initial Setup
void setup() {
  RadioInit();

  lcdInitialize();

  ControlPack.Buttons = 0;
  ControlPack.PButton = 0;
  ControlPack.S1 = 0;
  ControlPack.S2 = 0;
  ControlPack.S3 = 0;
  ControlPack.JL_Y = 0;
  ControlPack.JL_X = 0;
  ControlPack.JR_Y = 0;
  ControlPack.JR_X = 0;
  delay(1000);
}

////////////////////////////////////////////////////Read UpComing Signals for NRF and Write into LCD
void loop() {
  if (radio.available()) {
    radio.read(&ControlPack, sizeof(ControlPack));
    ControlPack.Buttons = constrain(ControlPack.Buttons,0,31);
    ControlPack.PButton = constrain(ControlPack.PButton,0,4);
    ValueSet();
  }
  lcdPrint();
  delay(1);
}

////////////////////////////////////////////////////Take Care of Upcoming Data
void ValueSet() {
  Values[7] = ControlPack.Buttons;
  Values[8] = ControlPack.PButton;
  Values[4] = ControlPack.S1;
  Values[5] = ControlPack.S2;
  Values[6] = ControlPack.S3;
  Values[1] = ControlPack.JL_Y;
  Values[0] = ControlPack.JL_X;
  Values[3] = ControlPack.JR_Y;
  Values[2] = ControlPack.JR_X;
}

//////////////////////////////////////////////////Do the LCD Job...!
void lcdPrint() {

  for (int i = 0; i < valuesLen - 2 ; i++) {
    lcd.setCursor(posX[i], posY[i]);
    lcd.print("      ");
    lcd.setCursor(posX[i], posY[i]);
    lcd.print(Values[i]);
  }
  lcd.setCursor(posX[7], posY[7]);
  lcd.print("      ");
  lcd.setCursor(posX[7], posY[7]);
  lcd.print(Values[7]);

  lcd.setCursor(posX[8], posY[8]);
  lcd.print("     ");
  lcd.setCursor(posX[8], posY[8]);
  lcd.print(Values[8]);
}

//////////////////////////////////////////////////Init LCD and Set Const Txt in Place
void lcdInitialize() {
  lcd.init();
  lcd.backlight();
  lcd.clear();

  lcd.setCursor(0, 0);
  lcd.print("X1=");
  lcd.setCursor(0, 1);
  lcd.print("Y1=");
  lcd.setCursor(0, 2);
  lcd.print("X2=");
  lcd.setCursor(0, 3);
  lcd.print("Y2=");
  lcd.setCursor(9, 0);
  lcd.print("S1=");
  lcd.setCursor(9, 1);
  lcd.print("S2=");
  lcd.setCursor(9, 2);
  lcd.print("S3=");
  delay(100);
}

/////////////////////////////////////////////////////////Init Radio Chip (NRF24L01)
void RadioInit() {
  radio.begin();
  radio.openReadingPipe(0, pipe);
  radio.setPALevel(RF24_PA_MAX);
  radio.enableDynamicPayloads();
  radio.enableAckPayload();
  radio.setRetries(0, 15);
  radio.setChannel(0x6F);
  radio.setAutoAck(1);
  radio.powerUp();
  radio.startListening();
  delay(100);
}
