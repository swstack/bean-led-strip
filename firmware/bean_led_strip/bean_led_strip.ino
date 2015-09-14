

void setup() {
    Serial.begin(57600);
    Serial.setTimeout(25);
    Serial.println("Setup Complete");
}

void loop() {
    char buffer[10];
    size_t length = 10;
    length = Serial.readBytes(buffer, length);

    if (length > 0) {
        Serial.println("Got Data");
    }
    delay(1000);
}
