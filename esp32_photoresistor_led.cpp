/**
* Interface photoresistor using ESP-32-s3
*/

const int sensorPin = 2;
const int redLedPin = 5;
const int blueLedPin = 12;

int lightInit; //initial value
int lightVal; //light reading

void setup() {
  pinMode(redLedPin, OUTPUT);
  pinMode(blueLedPin, OUTPUT);

  lightInit = analogRead(sensorPin); // gives initial value for comparison in loop

  digitalWrite(blueLedPin, HIGH);



}

void loop() {
  //if lightVal is less than our initial reading than its dark
  lightVal = analogRead(sensorPin); //read val

  if(lightVal - lightInit < 50){
    digitalWrite(redLedPin, HIGH);
    digitalWrite(blueLedPin, LOW);
  } else {
    digitalWrite(redLedPin, LOW);
    digitalWrite(blueLedPin, HIGH);
  }
}
