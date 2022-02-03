#!/bin/bash

#runs spelling.jar
$(java -jar -Djdk.gtk.version=2 --module-path /home/student/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml  kemukupu.jar) 2>/dev/null

#if doesnt run then change module path /home/student/javafx-sdk-11.0.2/javafx and try again
EXIT=$?
if [ $EXIT -ne 0 ]
then
    $(java -jar -Djdk.gtk.version=2 --module-path /home/student/javafx-sdk-11.0.2/javafx --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml  KemuKupuFinal.jar) 
fi
