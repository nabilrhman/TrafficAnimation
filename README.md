# Project 1: Traffic Animation

* Author: Nabil Rahman
* Class: CS121 Section 4
* Semester: Spring 2017

## Overview

This Java application displays an animated scene consisting of several vehicles
with an interesting animated background. 

## Compiling and Using

To compile, execute the following command in the main project directory:
```
$ javac TrafficAnimation.java
```

Run the compiled class with the command:
```
$ java TrafficAnimation
```

It will show the animation when you run it.

## Discussion

At first, I drew a truck along with a trailer moving from left to the right. 
The provided xOffset variable was used to animate it. To smoothen out the wrapping,
I multiplied the width variable by 2 which was used in calculating the xOffset . In
addition to that, I moved the staring X point of the truck more to left so that it
starts coming to the screen smoothly. To rotate the wheels, I used the rotate
function provided by the graphics object.  

After I was done with the first truck, I just copied the codes that were
responsible for drawing the parts of it and pasted it modifying the variable
names for truck 2. I modified some of the drawings for truck 2 and removed 
the unnecessary duplicate variables to make the codes cleaner. Then, I 
used methods from AffineTransform class to flip the truck.

After drawing both of the trucks, I started working on the background. At first,
I made a grass landscape using texture image of grass. Then, I drew an image
of the avatar using the drawImage() method. Both the the images were imported from online.
I had to reduce the size and bitrate of the images so that the performance of the 
program remains good enough. Then, I drew the road and the road lines in addition to
drawing the sky and coloring it using gradient. I used a separate xOffsetColor to
animate the sky color. After drawing the sky, I drew a moon which actually changes
its color and becomes the sun based on the condition of the sky.

The project was a bit challenging as it required uses of a lot of methods. Additionally,
the codes were harder to manage as I could only write codes in a single class file. If 
I had more flexibility in terms of making classes of my own, the codes could be much more
cleaner. However, drawing all the elements of the scene with proper scaling based on the
screen height and width was interesting. Animating the objects added more fun to it.

## Testing

As I did the whole project in the Onyx server using MobaXterm, I didn't have to do
any additional testing required if moved from personal computer to the server. For
testing, I just ran the application using the "Run" button in Eclipse. The "Run" 
command successfully ran the application.

## Extra Credit

For extra credit, I added the vehicles traveling in different directions in separate
lanes and smoothened out the wrapping. In addition to that, I made the wheels of 
the cars animate.

## Sources used

- I gathered several concepts from Stack Overflow.
[Stack Overflow](http://www.stackoverflow.com)
- I studies several Java Tutorials made by Oracle.
[The Java™ Tutorials](https://docs.oracle.com/javase/tutorial/)
- I collected the images from Google Images.
[Google Images](https://images.google.com/)