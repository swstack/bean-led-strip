#define PIN_RED 2
#define PIN_GREEN 0
#define PIN_BLUE 1

#define START_FRAME 0x77

#define CMD_SET_LEDS 0

void setup() {
    Serial.begin();

    pinMode(PIN_RED, OUTPUT);
    pinMode(PIN_GREEN, OUTPUT);
    pinMode(PIN_BLUE, OUTPUT);
    digitalWrite(PIN_RED, LOW);
    digitalWrite(PIN_GREEN, LOW);
    digitalWrite(PIN_BLUE, LOW);

    turnOff();
}

void loop() {
    char buffer[14] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    size_t bytesRead = Serial.readBytes(buffer, 14);

    if (bytesRead > 0) {
      if (buffer[0] == START_FRAME) {
        if (buffer[1] == CMD_SET_LEDS) {
          // uint32_t red = buffer[2] + (buffer[3] << 8) + (buffer[4] << 16) + (buffer[5] << 24);
          // uint32_t green = buffer[6] + (buffer[7] << 8) + (buffer[8] << 16) + (buffer[9] << 24);
          // uint32_t blue = buffer[10] + (buffer[11] << 8) + (buffer[12] << 16) + (buffer[13] << 24);

          // uint32_t red = (buffer[2] << 24) + (buffer[3] << 16) + (buffer[4] << 8) + buffer[5];
          // uint32_t green = (buffer[6] << 24) + (buffer[7] << 16) + (buffer[8] << 8) + buffer[9];
          // uint32_t blue = (buffer[10] << 24) + (buffer[11] << 16) + (buffer[12] << 8) + buffer[13];

          // int red = fromBytes(buffer[2], buffer[3], buffer[4], buffer[5]);
          // int green = fromBytes(buffer[6], buffer[7], buffer[8], buffer[9]);
          // int blue = fromBytes(buffer[10], buffer[11], buffer[12], buffer[13]);

          uint8_t red = buffer[2];
          uint8_t green = buffer[3];
          uint8_t blue = buffer[4];

          handleCommandSetLEDs(red, green, blue);
        }
      }
    }
}

int fromBytes(char one, char two, char three, char four) {
    char t[4];
    t[0] = one;
    t[1] = two;
    t[2] = three;
    t[3] = four;
    return atoi(t);
}

void handleCommandSetLEDs(uint8_t red, uint8_t green, uint8_t blue) {
  analogWrite(PIN_RED, red);
  analogWrite(PIN_GREEN, green);
  analogWrite(PIN_BLUE, blue);
}

void flashGreen() {
  turnOff();
  digitalWrite(PIN_GREEN, HIGH);
  delay(1000);
  turnOff();
}

void flashRed() {
  turnOff();
  digitalWrite(PIN_RED, HIGH);
  delay(1000);
  turnOff();
}

void flashBlue() {
  turnOff();
  digitalWrite(PIN_BLUE, HIGH);
  delay(1000);
  turnOff();
}

void turnOff() {
  digitalWrite(PIN_GREEN, LOW);
  digitalWrite(PIN_RED, LOW);
  digitalWrite(PIN_BLUE, LOW);
}
