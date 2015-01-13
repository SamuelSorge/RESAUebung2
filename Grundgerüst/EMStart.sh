#!/bin/bash

#%ECHO OFF
#START "EVENT MANAGER REGISTRY" /MIN /NORMAL rmiregistry
rmiregistry &
#START "EVENT MANAGER" /MIN /NORMAL java MessageManager
java MessageManager

