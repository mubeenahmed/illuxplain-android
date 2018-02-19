# illuxplain-android
Legacy Code (No Unit Test)

This is final year project for android application.

## Features
- Register user
- Login user
- Create session by sending invite to other user using their username
- If user accept the invitation
-- Canvas is open where user can interact by drawing, placing objects, share files and chating

## Technologies
- XMPP instant chat protocol
- JSON to drawing parsing

## How it works, internally
When user draws or places object on the canvas the connect user(s) recieves JSON object. This JSON acts has payload, which contains information about the activities performed by interactive user. After the JSON object is recieved these messages are parse back to recievers connected device into visual representations for example, if the user draws a line in the canvas, the connected user(s) will receives line coordinates, its color and width etc in JSON. These are parsed and consumed has canvas data.

## Java Server Jersey Rest
This project is also dependent on the REST API which was developed on the Java Jersey, however, since this is the old project, I cannot find the copy of the REST API in my old computer.

## Use cases
This application was developed for educational purpose. The use case for this application is to connect users remotely to plan, illustrate ideas by not just talking on phones, skype screen sharing but in fast and much more real time and with less latency.

Video link: https://illuxplain.herokuapp.com/illuxplain
