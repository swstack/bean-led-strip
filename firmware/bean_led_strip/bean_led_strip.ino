#define PIN_RED 2
#define PIN_GREEN 0
#define PIN_BLUE 1

void setup() {
    Serial.begin();

    pinMode(PIN_RED, OUTPUT);
    pinMode(PIN_GREEN, OUTPUT);
    pinMode(PIN_BLUE, OUTPUT);
    digitalWrite(PIN_RED, LOW);
    digitalWrite(PIN_GREEN, LOW);
    digitalWrite(PIN_BLUE, LOW);

    turnOff();
    analogWrite(PIN_GREEN, 200);

    Serial.println("Setup Complete");
}

void loop() {
    char buffer[10] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    size_t bytesRead = Serial.readBytes(buffer, 10);

    if (bytesRead > 0) {
        if (buffer[0] == 0) {
          turnOff();
        } else {
          turnOff();
          analogWrite(PIN_RED, 200);
          Bean.sleep(1000);
        }
    }
}

void turnOff() {
  digitalWrite(PIN_GREEN, LOW);
  digitalWrite(PIN_RED, LOW);
  digitalWrite(PIN_BLUE, LOW);
}
