
#define PIN_RED 1
#define PIN_GREEN 2
#define PIN_BLUE 0


void setup() {
    Serial.begin(57600);
    Serial.setTimeout(25);
    pinMode(PIN_RED, OUTPUT);
    pinMode(PIN_GREEN, OUTPUT);
    pinMode(PIN_BLUE, OUTPUT);
    digitalWrite(PIN_RED, LOW);
    digitalWrite(PIN_GREEN, LOW);
    digitalWrite(PIN_BLUE, LOW);
    Serial.println("Setup Complete");
}

void loop() {
    char buffer[10];
    size_t length = 10;
    length = Serial.readBytes(buffer, length);

    if (length > 0) {
        Serial.println("Got Data");
    }

    digitalWrite(PIN_GREEN, HIGH);
    delay(1000);
}
