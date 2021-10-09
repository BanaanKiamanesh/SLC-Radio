String inputString = "Hi";
bool isComplete = false;
int Values[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
int operationNum = 0;
const int maxOperationNum = 9;
bool operationComplete = false;

// Radio Setup
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
const byte CE_P = 7;    // nRF CE
const byte CSN_P = 8;   // nRF CSN
RF24 radio(CE_P, CSN_P); // nRF Object
const uint64_t pipe = 0xF0F0F0F0E1LL;

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

void setup() {
  Serial.begin(115200);
  inputString.reserve(200);

//  lcdInitialize();
  Serial.write(operationNum + 65);

  RadioSetup();

  ControlPack.Buttons = 0;
  ControlPack.PButton = 0;
  ControlPack.S1 = 0;
  ControlPack.S2 = 0;
  ControlPack.S3 = 0;
  ControlPack.JL_Y = 0;
  ControlPack.JL_X = 0;
  ControlPack.JR_Y = 0;
  ControlPack.JR_X = 0;
}

void loop() {
  radio.write(&ControlPack, sizeof(ControlPack));
  delay(10);
}

void serialEvent() {
  while (Serial.available() > 0) {
    char inChar = Serial.read();
    if ( inChar == '\n') {
      isComplete = true;
      break;
    }
    inputString += inChar;
  }

  if (isComplete == true) {
    int value = inputString.toInt();

    Values[operationNum] = value;

    operationNum++;
    if (operationNum >= maxOperationNum) {
      operationNum = 0;
      operationComplete = true;
    }
    else
      operationComplete = false;

    inputString = "";
    isComplete = false;

    Serial.write(operationNum + 65);
  }
  if (operationComplete) {
    ControlPack.Buttons = Values[7];
    ControlPack.PButton = Values[8];
    ControlPack.S1 = Values[4];
    ControlPack.S2 = Values[5];
    ControlPack.S3 = Values[6];
    ControlPack.JL_Y = Values[1];
    ControlPack.JL_X = Values[0];
    ControlPack.JR_Y = Values[3];
    ControlPack.JR_X = Values[2];
  }
}

void RadioSetup() {

  // Radio Setup Start
  radio.begin();
  radio.openWritingPipe(pipe);
  radio.setPALevel(RF24_PA_MAX);
  radio.enableDynamicPayloads();
  radio.enableAckPayload();
  radio.setRetries(0, 15);
  radio.setChannel(0x6F);
  radio.setAutoAck(1);
  radio.powerUp();
  radio.stopListening();
  
  delay(100);
}
