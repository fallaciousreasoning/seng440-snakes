# Tasks
The following tasks need to be completed as part of this assignment:

## Task 1: (10 marks)
Simple and essential features:

a)
    Create a simple version of the game using a 20x30 matrix to display the snake and the `food’ blocks which the snake can collect. (2 marks)
b)
    The screen should also have four buttons which can be used to change the direction of travel of the snake. (2 marks)
c)
    The application should have a setting screen which allows you to select the speed of the snake. (2 marks)
d)
    Create a controller which moves the snake adding new blocks at the head of the snake and removing blocks from the tail of the snake. (2 marks)
e)
    Create a model of the game to persist the snake and the location of food blocks (2 marks)

## Task 2: (8 marks)
Develop the desktop application:

a)
    Currently the desktop application does not know what sensors the mobile device has.  Allow the desktop application to send a list request to the mobile phone which will then reply with a JSON string describing the sensors available on the phone. (4 marks)

    Adapt the desktop application to allow the user to use the keyboard to:
        b) simulate the mobile being tilted forward, backward, left and right. (2 marks)
        c) simulate the mobile being rotated left and right, hence changing the compass heading. (2 marks)

## Task 3: (12 marks)
Advanced Android features:

a)
    Allow the snake to be controlled using the accelerometer of the Android device.  Tilting the mobile forward should change the direction of the snake to make it go up etc. When the phone is not tilted (held flat), the snake should continue travelling in the same direction as before. (4 marks)
b)
    Allow the snake to be controlled by the Android compass sensor.  Rotate the mobile in order to control the direction of travel.  Point the phone North to make the snake go up etc. (3 marks)
c)
    Allow the head of the snake to be controlled by your GPS location. (3 marks)
d)
    In a), b) and c), there will either need to be a different screen for each method of input, or new settings to determine the mode of play: (key presses, accelerometer, compass) (2 marks)

## Task 4: (3 marks)
Minecraft integration:

a)
    As the snake moves, create a virtual snake on the minecraft server by sending appropriate minecraft messages. NB the functionality should take into account the Activity life cycle and the event dispatch thread. (3 marks)

## Task 5: (5 marks)
Software engineering:

a)
    The key press method of controlling the game should be unit tested. (2 marks)
b)
    The project should be held under version control using git.  You should create a git repository on https://eng-git.canterbury.ac.nz with master access granted to the account `dbo50’. (1 mark)
c)
    Marks will be allocated to individuals who make a significant contribution each week (2/3 mark per week per developer for at least three weeks, indicated by git pushes) (2 marks)

## Task 6: (2 marks)
Hard but fun

a)
    Make the game 3D! i.e. play occurs in x,y and z dimensions. (2 marks)