#!/bin/bash

#%ECHO OFF
#%ECHO Starting ECS System
#PAUSE
#%ECHO ECS Monitoring Console
#START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE" /NORMAL java ECSConsole %1
#%ECHO Starting Temperature Controller Console
#START "TEMPERATURE CONTROLLER CONSOLE" /MIN /NORMAL java TemperatureController %1
java DoorDestructionSensor &
java MotionDetectionSensor &
java SecurityController &
java SecurityMonitor &
java WindowDestructionSensor &
java TemperatureController  &
#%ECHO Starting Humidity Sensor Console
#START "HUMIDITY CONTROLLER CONSOLE" /MIN /NORMAL java HumidityController %1
java HumidityController  &
#START "TEMPERATURE SENSOR CONSOLE" /MIN /NORMAL java TemperatureSensor %1
java TemperatureSensor  &
#%ECHO Starting Humidity Sensor Console
#START "HUMIDITY SENSOR CONSOLE" /MIN /NORMAL java HumiditySensor %1
java HumiditySensor  &
java ECSConsole
