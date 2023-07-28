import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.Timer;
import java.awt.Font;
import java.io.File;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Scanner;


public class Piano extends JComponent implements ActionListener {

    static final int WIDTH = 1250; //screen HEIGHT
    static final int HEIGHT = 750; //screen WIDTH

    //variables for fade effects in the game (they set the transparency of what you see)
    int fade = 0;
    int fadingIntoSettings = 0;
    int endGameFadeIn = 0;
    int fadeIntoChoosingDiffucilty = 0;
    int smoothFade = 0;
    int smoothFade2 = 255;
    int faderForSettings = 255;
    int smoothTransition = 255;
    int fadeFinal = 100;
    int noteFade = 255;
    int intro1Fade = 255;
    int intro2Fade = 255;
    int intro3Fade =255;
    int intro4Fade =255;
    int fadingOutOfSettings = 0;
    int fading = 0;

    //variables for specific timing to make the game look clean
    int tick = 0;
    int up = 0;
    int timetick =1;
    int settingsMovment = 0;
    int ticker = 0;
    int ticksForSetttingsTransistion = 0;
    int waitForSong = 0;

    //variables for the games settings
    int lives = 5; //the number of lives the player has
    int score = 0; //the number of keys the player got correct
    int min = 0; //number of minutes played
    int seconds = 0; //number of seconds played
    int tileSpeed = 1; //speed of tile
    int failCount = 0; //this is used to avoid the possibility of a bug occuring when spawing in the notes
    int index = Integer.MAX_VALUE; //used to see what key the player wants to change

    //variables that allow specific code to be ran in the game
    boolean shortenTime = false; //this will specificly be used to change the time slightly to make the game smoother
    boolean choosingDifficulty = false; //this will be used to see if you are choosing the level of difficulty
    boolean pause = false; //this will be used to check if the player has paused the game or not
    boolean typeLetter = true; //this will be used to see if the player has typed a letter
    boolean gameOver = false; //this will be used to see if the player has lost the game
    boolean playIntroSound = true; //this will be used to play the small tune at the begging of the game
    boolean intro1 = false; //this will be used to identify if the 1st part of the intro animation should be played
    boolean intro2 = false; //this will be used to identify if the 2nd part of the intro animation should be played
    boolean intro3 = false; //this will be used to identify if the 3rd part of the intro animation should be played
    boolean intro4 = false; //this will be used to identify if the 4th part of the intro animation should be played

    boolean titleScreen = false; //this will be used to identify if the player is on the title screen
    boolean loadControls = false; //this will be used to see if the player is on the controls screen
    boolean playGame = false; //this will be used to see if the player is currently playing the game
    boolean fadeSettings = false; //this will be used to create the animation effect of going smootly into the settings screen
    boolean fadeIntoGame = false; //this will be used to create the animation effect of going smoothly into the game

    //Keys functtionality
    boolean[] pressed = new boolean[3]; //checks what keys are currently being pressed
    String[] keys = new String[3]; //contains all the keys that are used in the game
    Colors[] colors = new Colors[keys.length]; //contains the colors of the keys (blank, red or green)

    //BufferedImages for all of the images used in the gmae
    BufferedImage startButton;
    BufferedImage settingsButton;
    BufferedImage controlsButton;
    BufferedImage pauseButton;
    BufferedImage backButton;
    BufferedImage rightArrowButton;
    BufferedImage leftArrowButton;
    BufferedImage rightArrowButton2;
    BufferedImage leftArrowButton2;
    BufferedImage easyButton;
    BufferedImage mediumButton;
    BufferedImage hardButton;
    BufferedImage home;
    BufferedImage replay;

    //notes functionality
    ArrayList<Integer> notesX = new ArrayList<>(); //contains the notes X position (which column the notes are in)
    ArrayList<Integer> notesY = new ArrayList<>(); //contains the notes Y position
    Color[] noteColor = {new Color(0, 0, 0), new Color(227, 142, 32)}; //contains the colors the notes can be (orange or black)
    int currentNoteColor = 0; //index of the current colors of the notes


    //all of these booleans are used to check weather or not the player is hovering there mouse over an image
    boolean isOnStart = false;
    boolean isOnSettings = false;
    boolean isOnControls = false;
    boolean settings = false;
    boolean active = false;
    boolean active2 = false;
    boolean active3 = false;
    boolean active4 = false;
    boolean active5 = false;
    boolean active6 = false;
    boolean active7 = false;
    boolean active8 = false;
    boolean active9 = false;
    boolean active10 = false;
    boolean active11 = false;
    boolean active12 = false;
    boolean active13 = false;


    //variables related to the design in the intro animation of the game
    int increaseSize = 100; //this was simply used to update the size of some variables quickly (easier to adjust)

    int titleBlock1X = 40 + increaseSize; //sets the X position of the 1st note in the animation
    int titleBlock1Y = -150 + increaseSize; //sets the Y position of the 1st note in the animation
    int titleBlock1Width = 240; //sets the width of the 1st note in the animation

    int titleBlock2X = 281 + increaseSize; //sets the X position of the 2nd note in the animation
    int titleBlock2Y = -180 + increaseSize; //sets the Y position of the 2nd note in the animation
    int titleBlock2Width = 245; //sets the width of the 2nd note in the animation

    int titleBlock3X = 549 + increaseSize; //sets the X position of the 3rd note in the animation
    int titleBlock3Y = -420 + increaseSize; //sets the Y position of the 3rd note in the animation
    int titleBlock3Width = 235; //sets the wifth of the 3rd note in the animation

    int titleBlock4X = 785 + increaseSize; //sets the X position of the 4th note in the animation
    int titleBlock4Y = -609 + increaseSize; //sets the Y position of the 4th note in the animaiton
    int titleBlock4Width = 175; //sets the width of the 4th note of the animation

    String title = "Piano Tiles"; //title of the game

    //speed of the game
    int desiredFPS = 100;
    int desiredTime = Math.round((1000 / desiredFPS));
    Timer gameTimer;

    public Piano(){
        
        JFrame frame = new JFrame(title);

        //sets all of the starting colors of the keys
        for(int i = 0; i < colors.length; i++){
            colors[i] = new Colors(206, 214, 208, 0);
        }

        //sets all of the starting keys that will be used in the game
        keys[0] = "1";
        keys[1] = "2";
        keys[2] = "3";

        int widthOfKey = 651 / keys.length;//finds the width that each key should be

        //creates 20 notes
        for(int i = 0; i < 20; i++){
            notesX.add((int)(Math.random() * keys.length));//sets the notes x posistion

            notesY.add((int)(Math.random() * -32000 - failCount)-25);//sets the notes y position

            //loops through the rest of the notes
            for(int j = 0; j < notesX.size()-1; j++){
                Rectangle note1 = new Rectangle(600+notesX.get(i)+widthOfKey, notesY.get(i), widthOfKey, 50);
                Rectangle note2 = new Rectangle(600+notesX.get(j)+widthOfKey, notesY.get(j), widthOfKey, 50);

                //checks if the newly created note intersects with any of the old notes
                if(note1.intersects(note2)){

                    //if the notes  have intersected, the newly made note will be removed
                    notesX.remove(notesX.size()-1);
                    notesY.remove(notesY.size()-1);
                    failCount += 100; //used to decrease the chance of another colision happening
                    i--; //ensures that exactly 20 notes will be made
                    break;
                }
                
            }

        }
     
        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();
        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);
        
        // Set things up for the game at startup
        setup();

       // Start the game loop
        gameTimer = new Timer(desiredTime,this);
        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {

        //used to start the animation for going out of the settings
        if(fadeSettings){
            if(fadingOutOfSettings < 255){
                fadingOutOfSettings+=1;
            }

            else{
                //resets all of the game values
                settings = false;
                fadingIntoSettings = 0;
                faderForSettings = 255;

                settingsMovment = 0;

                isOnSettings = false;
                isOnControls = false;
             
                settings = false;
                fadeSettings = false;
                fadingOutOfSettings = 0;

                titleScreen = true;
                choosingDifficulty = false;
                smoothFade = 0;
                smoothFade2 = 255;

                gameOver = false;
                playGame = false;

                score = 0;
             
            }
        }

        //checks if the player is on the title screen and is fading into settings
        if(titleScreen && fadingIntoSettings != 255){

            //sets background color
            g.setColor(new Color(206, 214, 208));
            g.fillRect(0,0, WIDTH, HEIGHT);

            g.setColor(Color.BLACK);

            //if settings screen is not on
            if(settings == false){

            int widthOfKey = 651 / keys.length; //calculates the width of each key

            //spawns in all of the notes
            for(int i = 0; i < notesX.size(); i++){

                g.setColor(noteColor[currentNoteColor]);//sets the color of the note
                g.fillRect(600+notesX.get(i)*widthOfKey, notesY.get(i), widthOfKey, 25); //draws the note to the screen

            }

            g.setColor(Color.BLACK);

            //loops through all of the keys the player has
            for(int i = 0; i < keys.length; i++){

                g.drawLine(600 + i *widthOfKey, 0, 600 + i * widthOfKey, 675); //draws the colums

                
                g.drawRect(600 + i * widthOfKey, 675, widthOfKey, 75); //draws the outline where the player has to hit the note

                g.setColor(colors[i].color); //sets the color of the area where the player hits the note

                g.fillRect(600 + i * widthOfKey, 675, widthOfKey, 75); //fills the area where the player has to hit the note

                g.setColor(Color.BLACK);//sets colors for text to black
                
                //sets the font of each of the keys (depends on how many keys there are)
                if(keys.length > 5 && keys.length < 10){
                    g.setFont(new Font("STSong", Font.PLAIN, widthOfKey - 30));
                    }
                    else if(keys.length == 10){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-5));
                    }
                    else if(keys.length == 5){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-40));
                    }
                    else if(keys.length == 4){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-70));
                    }
                    else if(keys.length == 3){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-120));
                    }
                    else if(keys.length == 2){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-240));
                    }
                    else{
                        g.setFont(new Font("STSong", Font.PLAIN, 100));
                    }
                
                    //draws the key to the screen
                g.drawString(keys[i], 600+i*widthOfKey + widthOfKey/2-20, 750);

                //draws the final column line
                if(i == keys.length -1){
                    g.drawLine(600 + (i+1) *widthOfKey, 0, 600 + (i+1) * widthOfKey, 675);
                }

            }


        }

        //draws the title "Piano Tiles" to the screen
        g.setFont(new Font("STSong", Font.PLAIN, 100));
        g.drawString("Piano Tiles", 60, 200);

        
            if(fade > 0){//used for a smooth fade effect
            fade--;
            }

            g.drawImage(startButton, 125, 300, 300, 100, null); //draws the start button image

            g.drawImage(settingsButton, 125, 450, 300, 100, null); //draws the settings button image

            g.drawImage(controlsButton, 125, 600, 300, 100, null); //draws the controls button image

            //creates the fade effect on screen
            g.setColor(new Color(206, 214, 208, fade));
            g.fillRect(0,0, WIDTH, HEIGHT);

        }

        //checks if the player is no longer on the ttile screen
        if(titleScreen == false){
            
        //draws the backfounds of the screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //creates the fade effect
        if(fadeIntoGame){
            g.setColor(new Color(206, 214, 208, fading));

            g.fillRect(0,0, WIDTH, HEIGHT);
        }

        //fades the "Piano Tiles" from the title screen
        g.setColor(new Color(206, 214, 208, fade));
        g.setFont(new Font("STSong", Font.PLAIN, 150));
        g.drawString("PIANO TILES", 40+increaseSize, HEIGHT-100);

        //fades the "PLAY" message from the title screen
        if(intro4Fade < 150){
            g.setColor(new Color(206, 214, 208, fadeFinal));
            g.drawString("PLAY", WIDTH/2-200, HEIGHT-300-up);
            if(HEIGHT-300-up > HEIGHT/2-20){
            up++;
            if (fadeFinal < 255){
            fadeFinal+=5;
            }
            }
            else{
                fadeIntoGame = true;
            }
        }
  
        //draws the first tile in the title animation
        if(intro1){

            g.setColor(new Color(51, 232, 99, intro1Fade));

        }
        g.drawRect(40+increaseSize, HEIGHT-230, 240, 150);

        g.setColor(new Color(206, 214, 208, fade));

        //draws the second tile in the title animation
        if(intro2){

            g.setColor(new Color(51, 232, 99, intro1Fade));

        }

        g.drawRect(281+increaseSize, HEIGHT-230, 245, 150);

        g.setColor(new Color(206, 214, 208, fade));

        //draws the third tile in the title animation
        if(intro3){
            
            g.setColor(new Color(51, 232, 99, intro3Fade));

        }

        g.drawRect(549+increaseSize, HEIGHT-230, 235, 150);

        g.setColor(new Color(206, 214, 208, fade));

        //draws the fourth tile in the title animation
        if(intro4){
            
            g.setColor(new Color(51, 232, 99, intro4Fade));

        }

        g.drawRect(785+increaseSize, HEIGHT-230, 175, 150);

        g.setColor(new Color(222, 100, 75, 100));

        //draws the first box in the title screen
        if(intro1 == false){
        g.fillRect(titleBlock1X, titleBlock1Y, titleBlock1Width, 150);
        }

        g.setColor(new Color(149, 40, 168, 100));

        //draws the second box in the title screen
        if(intro2 == false){
            g.fillRect(titleBlock2X, titleBlock2Y, titleBlock2Width, 150);
        }

        g.setColor(new Color(47, 98, 186, 100));

        //draws the third box in the title screen
        if(intro3 == false){
            g.fillRect(titleBlock3X, titleBlock3Y, titleBlock3Width, 150);
        }

        g.setColor(new Color(186, 59, 47, 100));

        //draws the fourth box in the title screen
        if(intro4 == false){
            g.fillRect(titleBlock4X, titleBlock4Y, titleBlock4Width, 150);
        }

    }

    //checks if the player is on the settings screen
    if(settings){

        //creates an animation to fade into the settings
        if(fadingIntoSettings == 255){
        g.setColor(new Color(206, 214, 208));
        g.fillRect(0,0, WIDTH, HEIGHT);
        }

        g.setColor(new Color(206, 214, 208, fadingIntoSettings));
        g.fillRect(0, 0, 559, HEIGHT);
        
        //checks if the fade has completed
        if(fadingIntoSettings == 255){

            //draws all of the images in the settings
            g.drawImage(backButton, 0, 0, 300, 100, null); //draws the back button
            g.drawImage(rightArrowButton, 1050, 300, 100, 150, null); //draws the upper right arrow
            g.drawImage(leftArrowButton, 200, 300, 100, 150, null); //draws the upper left arrow
            g.drawImage(rightArrowButton2, 1050, 675, 50, 50, null); //draws the lower right arrow
            g.drawImage(leftArrowButton2, 250, 675, 50, 50, null);  //draws the lower left arrow

            //creates the fade effect for the images
            g.setColor(new Color(206, 214, 208, faderForSettings));
            g.fillRect(0, 0, 350, HEIGHT);
            g.fillRect(1000, 0, 250, HEIGHT);

        }

        
        g.setColor(Color.BLACK);

            int widthOfKey = 651 / keys.length; //calculates the width that each key should be

            //creates the the animation for the small gameboard that you see in the title screen settings and the gmame
            if(fadingIntoSettings == 255 && settingsMovment > 350){
                settingsMovment--;
            }

            //draws all of hte notes to the screen
            for(int i = 0; i < notesX.size(); i++){

                g.setColor(noteColor[currentNoteColor]); //sets the color of the note
                g.fillRect(600+notesX.get(i)*widthOfKey-settingsMovment, notesY.get(i), widthOfKey, 25); //draws the note to the screen

            }

            g.setColor(Color.BLACK);

            //draws the game board
            for(int i = 0; i < keys.length; i++){


                g.drawLine(600 - settingsMovment + i *widthOfKey, 0, 600 + i * widthOfKey-settingsMovment, 675); //draws the columns for the gameboard

                g.drawRect(600 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75); //draws the key box to the screen

                g.setColor(colors[i].color); //sets the key areas color

                g.fillRect(600 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75); //fills the key with the previous color

                //highlights which key is currently being changed by the user
                if(i == index){
                    g.setColor(new Color(250, 215, 215));
                    g.fillRect(600 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75);
                }

                g.setColor(Color.BLACK);
                
                //sets the size of each key (size depends on the the amount og keys)
                if(keys.length > 5 && keys.length < 10){
                    g.setFont(new Font("STSong", Font.PLAIN, widthOfKey - 30));
                    }
                    else if(keys.length == 10){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-5));
                    }
                    else if(keys.length == 5){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-40));
                    }
                    else if(keys.length == 4){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-70));
                    }
                    else if(keys.length == 3){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-120));
                    }
                    else if(keys.length == 2){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-240));
                    }
                    else{
                        g.setFont(new Font("STSong", Font.PLAIN, 100));
                    }
                
                g.drawString(keys[i], 600-settingsMovment+i*widthOfKey + widthOfKey/2-20, 750);//draws the key to the screen

                //draws the final column line to the screen
                if(i == keys.length -1){
                    g.drawLine(600-settingsMovment + (i+1) *widthOfKey, 0, 600 - settingsMovment + (i+1) * widthOfKey, 675);
                }

            }

            

    }

    //checks if player is on the choosing diffuclty screen
    if(choosingDifficulty){

        //creates the fade animation
        g.setColor(new Color(206, 214, 208, fadeIntoChoosingDiffucilty));
        if(fadeIntoChoosingDiffucilty < 255){
        fadeIntoChoosingDiffucilty++;
        }
        else{
            titleScreen = false; //stops loading the title screen
        }

        g.fillRect(0, 0, WIDTH, HEIGHT);//draws the background of the page

        //contributes to the smooth transition to the choosing difficulty screen
        if(smoothTransition > 0 && fadeIntoChoosingDiffucilty == 255){
            smoothTransition--;
        } 
         if(fadeIntoChoosingDiffucilty == 255){

        //draws the images to the screen
        g.drawImage(backButton, 0, 0, 300, 100, null); //draws the back button
        g.drawImage(easyButton, 150, 300, 300, 200, null); //draws the easy button
        g.drawImage(mediumButton, 500, 300, 300, 200, null); //draws the medium button
        g.drawImage(hardButton, 850, 300, 300, 200, null); //draws the hard button

        //used to screate a smooth fade effect for the images
        g.setColor(new Color(206, 214, 208, smoothTransition));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        }



        

    }

    //checks if the player is currently playing the game
    if(playGame){

            //loops through all of the notes in the game
            for(int i = 0; i < notesY.size(); i++){

                //checks if the player has missed the note (the note went through the screen)
                if(notesY.get(i) >= 751){
                lives--; //decreases the players life

                //plays the missed note sound
                File file = new File("Missed A Note.wav");
                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        

                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }

            notesX.remove(i);
            notesY.remove(i);
            i--; //makes sure that all notes are checked to see if they went through the screen or not

              
            //adds a new note to the screen
                while(true){

                    notesX.add((int)(Math.random() * keys.length)); //sets the X position of the note
                    notesY.add((int)(Math.random() * -32000 - failCount)-25); //sets the Y position of the note

                    int widthOfKey = 651 / keys.length; //finds the width of the note (which is equal to the width of the key)

                    boolean leave = true;//used to check if the note has been sucessfully added

                    for(int k = 0; k < notesX.size()-1; k++){
                        Rectangle note1 = new Rectangle(600+notesX.get(notesX.size()-1)+widthOfKey, notesY.get(notesY.size()-1), widthOfKey, 50);
                        Rectangle note2 = new Rectangle(600+notesX.get(k)+widthOfKey, notesY.get(k), widthOfKey, 50);

                        //checks if the newly created note intersects any other note
                        if(note1.intersects(note2)){
                            leave = false; //means that the note that not been sucessfully added

                            //removes the newly created note
                            notesX.remove(notesX.size()-1); 
                            notesY.remove(notesY.size()-1);
                        }
                    }
                    //if the note has been successfuly added, there is no need to create another note
                    if(leave){
                        break;
                    }    
                    
                }
                }

            }

            //checks if the game is running (pause button has not been pressed)
            if(pause == false){

        //updates the timer for the game
        if(timetick % 65 == 0){
            timetick = 1;
        seconds++; //increases seconds by one
        }
        else{
            timetick++;
        }
        //if 60 seconds have passed
        if(seconds == 60){
            seconds = 0; //resets seconds to zero
            min++; //increases the minutes by one
        }
    }

    //creates a fade effect into the game
        g.setColor(new Color(206, 214, 208, smoothFade));

        g.fillRect(0, 0, WIDTH, HEIGHT);

        if(smoothFade < 255){
            smoothFade++;
        }

        //draws the entire gameboard into the game
        if(smoothFade == 255){

            int widthOfKey = 651 / keys.length; //clauclates the width of the key

            //loops through all fo the notes
            for(int i = 0; i < notesX.size(); i++){

                g.setColor(noteColor[currentNoteColor]); //sets the color of the key
                g.fillRect(300+notesX.get(i)*widthOfKey-settingsMovment, notesY.get(i), widthOfKey, 25); //draws the key to the screen

            }

            g.setColor(Color.BLACK);

            for(int i = 0; i < keys.length; i++){

                g.drawLine(300 - settingsMovment + i *widthOfKey, 0, 300 + i * widthOfKey-settingsMovment, 675); //draws the column line

          
                g.drawRect(300 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75); //draws the key outline

                g.setColor(colors[i].color); //sets the keys color

                g.fillRect(300 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75); //fills the key with its color

                //checks what key the user has selected
                if(i == index){
                    g.setColor(new Color(250, 215, 215));
                    g.fillRect(300 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75);
                }

                g.setColor(Color.BLACK);

                //sets the size of the keys (depends on how many keys there are)
                if(keys.length > 5 && keys.length < 10){
                    g.setFont(new Font("STSong", Font.PLAIN, widthOfKey - 30));
                    }
                    else if(keys.length == 10){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-5));
                    }
                    else if(keys.length == 5){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-40));
                    }
                    else if(keys.length == 4){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-70));
                    }
                    else if(keys.length == 3){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-120));
                    }
                    else if(keys.length == 2){
                        g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-240));
                    }
                    else{
                        g.setFont(new Font("STSong", Font.PLAIN, 100));
                    }
                g.drawString(keys[i], 300-settingsMovment+i*widthOfKey + widthOfKey/2-20, 750); //draws the key to the screen

                //draws the final colmun line
                if(i == keys.length -1){
                    g.drawLine(300-settingsMovment + (i+1) *widthOfKey, 0, 300 - settingsMovment + (i+1) * widthOfKey, 675);
                }

            }

            g.setColor(Color.BLACK);
            g.setFont(new Font("STSong", Font.PLAIN, 100));

            //draws the Time on the screen
            g.drawString("Time", 50, 350);
            if(min < 10){//formating
            if(seconds < 10){//formating
            g.drawString("0" +min +":0" + seconds, 50, 450);
            }
            else{//formating
                g.drawString("0" +min +":"+ seconds, 50, 450);
            }
            }
            else{//formating
                if(seconds < 10){//formating
                    g.drawString(min +":0" + seconds, 50, 450);
                    }
                    else{//formating
                        g.drawString(min +":"+ seconds, 50, 450);
                    }
            }

            //draws the score to the screen
            g.drawString("Score", 1000, 200);

            if(score < 10){//formating
            g.drawString("" + score, 1075, 300);
            }
            else if(score < 100){//formaating
                g.drawString("" + score, 1050, 300);
            }
            else{//formating
                g.drawString("" + score, 1025, 300);
            }

            //draws the amount of lives the player has to the screen
            g.drawString("Lives", 1000, 600);
            g.drawString("" + lives, 1075, 700);

            g.drawImage(pauseButton, 0, 0, 50, 50, null); //draws the pause button

            //creates the smooth fade animation into the game
            g.setColor(new Color(206, 214, 208, smoothFade2));
            if(smoothFade2 > 0){
                smoothFade2--;
            }
            g.fillRect(0, 0, WIDTH, HEIGHT);

            //checls to see if the player has lost the game
            if(gameOver){

                //creates the fade animation for when you loose the game
                g.setColor(new Color(47, 171, 196, endGameFadeIn));
                if(endGameFadeIn < 255){
                    endGameFadeIn++;
                }
                g.fillRect(0, 0, WIDTH, HEIGHT);

                //checks if the fade is done
                if(endGameFadeIn == 255){
                    //draws the players stats from the game
                    g.setColor(new Color(206, 214, 208));
                    g.setFont(new Font("STSong", Font.PLAIN, 100));
                    g.drawString("GAME OVER", 300, 200);
                    if(score < 10){//formating
                    g.drawString("SCORE: " + score, 375, 350);
                    }
                    else if(score < 100){//formating
                        g.drawString("SCORE: " + score, 350, 350);
                        }
                    else{//formating
                        g.drawString("SCORE: " + score, 325, 350);
                    }
                    if(min < 10){//formating
                        if(seconds < 10){
                        g.drawString("TIME: 0" +min +":0" + seconds, 325, 500);
                        }
                        else{//formating
                            g.drawString("TIME: 0" +min +":"+ seconds, 325, 500);
                        }
                        }
                        else{//formating
                            if(seconds < 10){//formating
                                g.drawString("TIME: " +min +":0" + seconds, 325, 500);
                                }
                                else{//formating
                                    g.drawString("TIME: " +min +":"+ seconds, 325, 500);
                                }
                        }

                        g.drawImage(home, 475, 550, 100, 100, null); //draws the home icon to the screen
                        g.drawImage(replay, 625, 550, 100, 100, null); //draws the replay/reset icon to the secreen

                    

                }
            }

        }



    }

    //checks if the player is fading out of the settings
    if(fadeSettings){

        //fading animation
        g.setColor(new Color(206, 214, 208, fadingOutOfSettings));
        g.fillRect(0,0, WIDTH, HEIGHT);
        if(settingsMovment > 0){
        settingsMovment--;
        }
    }

    //checks if the player is currently on the settings
    if(settings){

        int widthOfKey = 651 / keys.length; //calculates the width of the key

        //loops through all of the notes
        for(int i = 0; i < notesX.size(); i++){

            g.setColor(noteColor[currentNoteColor]);//sets the color of the note
            g.fillRect(600+notesX.get(i)*widthOfKey-settingsMovment, notesY.get(i), widthOfKey, 25); //draws the note to the screen

        }

        g.setColor(Color.BLACK);

        //loops through all of the keys
        for(int i = 0; i < keys.length; i++){

            g.drawLine(600 - settingsMovment + i *widthOfKey, 0, 600 + i * widthOfKey-settingsMovment, 675); //draws the column lines in the game
      
            g.drawRect(600 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75); //draws the key box to the screen

            g.setColor(colors[i].color); //sets the key box colors of the keys

            g.fillRect(600 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75); //draws the filling color for the key to the screen

            //draws the selected key color to the screen
            if(i == index){
                g.setColor(new Color(250, 215, 215));
                g.fillRect(600 - settingsMovment + i * widthOfKey, 675, widthOfKey, 75);
            }

            g.setColor(Color.BLACK);

            //sets the key font (depends on how many keys there are)
            if(keys.length > 5 && keys.length < 10){
                g.setFont(new Font("STSong", Font.PLAIN, widthOfKey - 30));
                }
                else if(keys.length == 10){
                    g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-5));
                }
                else if(keys.length == 5){
                    g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-40));
                }
                else if(keys.length == 4){
                    g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-70));
                }
                else if(keys.length == 3){
                    g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-120));
                }
                else if(keys.length == 2){
                    g.setFont(new Font("STSong", Font.PLAIN, widthOfKey-240));
                }
                else{
                    g.setFont(new Font("STSong", Font.PLAIN, 100));
                }
            g.drawString(keys[i], 600-settingsMovment+i*widthOfKey + widthOfKey/2-20, 750); //draws the key to the scren

            //draws the final column line
            if(i == keys.length -1){
                g.drawLine(600-settingsMovment + (i+1) *widthOfKey, 0, 600 - settingsMovment + (i+1) * widthOfKey, 675);
            }

        }
    }

   //checks if the user is currently on the controls creen
    if(loadControls){
        g.setColor(new Color(206, 214, 208));
        g.fillRect(0,0, WIDTH, HEIGHT);

        //displays how to play the game to the screen
        g.setColor(Color.BLACK);
        g.setFont(new Font("STSong", Font.PLAIN, 200));
        g.drawString("CONTROLS", 100, 200);

        g.setFont(new Font("STSong", Font.PLAIN, 50));
        g.drawString("When A Note Is About To Hit The Bottom Of The Screen", 25, 300);      
        g.drawString("Press The Key That Is In The Column With The Note", 50, 350);
        g.drawString("You Have Five Lives Every Run", 320, 450);
        g.drawString("You Will Loose A Life If You Miss A Note", 200, 500);
        g.drawString("Or If You Hit The Wrong Key", 325, 550);

        g.drawImage(backButton, 450, 600, 300, 100, null); //draws the back button image onto the screen
        
    }

         }
    public void setup() {

        //sets all of the bufferedimages to an actual image added in the images folder

        try {
            startButton = ImageIO.read(new File("Start Button.png"));
        } catch (Exception e) {
        }

        try {
            backButton = ImageIO.read(new File("Back Button.png"));
        } catch (Exception e) {
        }

        try {
            leftArrowButton = ImageIO.read(new File("Back Square Button.png"));
        } catch (Exception e) {
        }

        try {
            rightArrowButton = ImageIO.read(new File("Next Square Button.png"));
        } catch (Exception e) {
        }

        try {
            leftArrowButton2 = ImageIO.read(new File("Back Square Button.png"));
        } catch (Exception e) {
        }

        try {
            rightArrowButton2 = ImageIO.read(new File("Next Square Button.png"));
        } catch (Exception e) {
        }

        try {
            settingsButton = ImageIO.read(new File("Settings Button.png"));
        } catch (Exception e) {
        }

        try {
            controlsButton = ImageIO.read(new File("Controls Button.png"));
        } catch (Exception e) {
        }

        try {
            easyButton = ImageIO.read(new File("Easy.png"));
        } catch (Exception e) {
        }
        try {
            mediumButton = ImageIO.read(new File("Medium.png"));
        } catch (Exception e) {
        }
        try {
            hardButton = ImageIO.read(new File("Hard.png"));
        } catch (Exception e) {
        }

        try {
            pauseButton = ImageIO.read(new File("Pause Square Button.png"));
        } catch (Exception e) {
        }

        try {
            home = ImageIO.read(new File("Home Square Button.png"));
        } catch (Exception e) {
        }

        try {
            replay = ImageIO.read(new File("Return Square Button.png"));
        } catch (Exception e) {
        }

    }

    public void loop() {

        //checks if the players lives is less then or equal to zero
        if(lives <= 0){
            pause = true;
            gameOver = true;
        }

        //checks if the user is currently on the setting page
        if(settings){

            //creates fading effect for the settings
            if(fadingIntoSettings != 255 && ticksForSetttingsTransistion % 1 == 0){
            fadingIntoSettings++;
            ticksForSetttingsTransistion++;
            settingsMovment++;
            noteFade--;
            
            }
            else if(fadingIntoSettings != 255){
                ticksForSetttingsTransistion++;
                
            }

            if(fadingIntoSettings == 255){
                if(faderForSettings > 0){
                    faderForSettings--;
                    }
            }

        }

        //checks if the player is on the title screen or is currently playing the game (that is not paused)
        if((titleScreen || playGame) && pause == false){

            //updates the colors of all of the keys
            for(int i = 0; i < colors.length; i++){
                colors[i].updateColor();
            }

            if(ticker % 1 == 0){
                ticker++;
            
            //updates the Y position of all of the notes
            for(int i = 0; i < notesY.size(); i++){

                //checks if the notes Y position should be update normally (if the note can be seen on the screen)
                if(notesY.get(i)+25+20 > 0 && (smoothFade == 255 || choosingDifficulty == false)){
                notesY.set(i, notesY.get(i)+tileSpeed); //increases the Y position of the note by one
                }
                //checks to see if the notes Y position is above the screen
                else if(notesY.get(i)+25+20 <= 0){
                    
                    notesY.set(i, notesY.get(i)+20); //increases the Y position by 20 each tick so that it enters the screen quickly
                }

                //checks if the player is currently on the title screen and the notes Y position is about to hit the bttom of the screen
                if(titleScreen && notesY.get(i) >= 675){
          
                    colors[notesX.get(i)].setColor(153, 186, 161, 255); //sets the keys color to a light green

                    //removes the note
                    notesX.remove(i);
                    notesY.remove(i);

                    int widthOfKey = 651 / keys.length; //finds the width of each key (same as the width of the notes)

                    //adds a new note
                    for(int j = 0; j < 1; j++){

                        notesX.add((int)(Math.random() * keys.length)); //sets the new notes X position
                        notesY.add((int)(Math.random() * -32000 - failCount)-25); //sets the new notes Y position

                        //loops through the rest of the notes
                        for(int k = 0; k < notesX.size()-1; k++){
                            Rectangle note1 = new Rectangle(600+notesX.get(notesX.size()-1)+widthOfKey, notesY.get(notesY.size()-1), widthOfKey, 50);
                            Rectangle note2 = new Rectangle(600+notesX.get(k)+widthOfKey, notesY.get(k), widthOfKey, 50);

                            //checks if any note hits the newly made note
                            if(note1.intersects(note2)){
                                //removes the new note
                                notesX.remove(notesX.size()-1);
                                notesY.remove(notesY.size()-1);
                                j--; //ensures that a new note will be created
                                failCount += 100; //makes it so that the possibility of a collision decreases
                                break;
                            }
                        }
            
                    }


                }

            }
        }
        else{
            ticker++;
        }
        }
        else{
            //loads the title screen when the intro of the game ends
            if(waitForSong == 100){
                titleScreen = true;
                fade = 255;
            }

        //used for the fade effect in the intro
        if(fading == 255){
            waitForSong++;

        }
        if(fadeIntoGame && fading < 255){
        fading++;
     
        }

        //checks if the intro sound should be played
        if(playIntroSound){
            //plays the intro sound


            try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("intro.wav"));

                Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            }
            catch(Exception e){
                // System.out.println(e);
            }

            playIntroSound = false;

        }

        //used for fade effect
        if(fade < 255){
        fade++;
        }

        //updates the first tiles Y position for the first tile in the intro animation
        if(titleBlock1Y < HEIGHT-230){

            titleBlock1Y+=6;
        }
        else{
            intro1 = true;
        }

        //updates the second tiles Y position for the first tile in the intro animation
        if(titleBlock2Y < HEIGHT-230){

            titleBlock2Y+=6;
        }
        else{
            intro2 = true;
        }

        //updates the third tiles Y position for the first tile in the intro animation
        if(titleBlock3Y < HEIGHT-230){

            titleBlock3Y+=6;
        }
        else{
            intro3 = true;
        }

        //updates the fourth tiles Y position for the first tile in the intro animation
        if(titleBlock4Y < HEIGHT-230){

            titleBlock4Y+=6;
        }
        else{
            intro4 = true;
        }

        //fades the first keys tile in the intro animation
        if(intro1){
            if(intro1Fade != 0){
                intro1Fade-=3;
            }
        }

        //fades the second keys tile in the intro animation
        if(intro2){
            if(intro2Fade != 0){
                intro2Fade-=3;
            }
        }
        //fades the third keys tile in the intro animation
        if(intro3){
            if(intro3Fade != 0){
                intro3Fade-=3;
            }
        }
        //fades the fourth keys tile in the intro animation
        if(intro4){
            if(intro4Fade != 0){
                intro4Fade-=3;
            }
        }

    }
       
    }
    

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {

        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e) {

            //checks if the player is currently on the controls scrren
            if(loadControls){
                //checks if the player has clicked on the back button of the screen
                if(e.getX() > 450 && e.getX() < 750 && e.getY() > 600 && e.getY() < 700){

                    loadControls = false;
                    titleScreen = true;

                    File file = new File("Enter Sound.wav");

                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }
                }
            }

            //cheks if the player is currently on the title scrren and not fading into the settings
            if(titleScreen && settings == false){

                //checks if the player clicked the controls button
                if(e.getX() > 125 && e.getX() < 425 && e.getY() > 600 && e.getY() < 700){
                    loadControls = true;
                    titleScreen = false;

                    File file = new File("Enter Sound.wav");

                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }
                }
            }

            //checks to see of the player is currently playing the game and clicked the paue button
            if(playGame && e.getX() > 0 && e.getX() < 50 && e.getY() > 0 && e.getY() < 50){

                if(!gameOver){
                //if the pause button is on
                if(pause){
                    pause = false; //sets pause button to off
                }
                else{ //if pause button if off
                    pause = true; //sets pause button on
                }

                File file = new File("Enter Sound.wav");
                    try{
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                        }
                        catch(Exception ee){
                        }
            }

            }

            //checks if the player is currently choosing the difficulty of the game and if the player is not fading into the game nor loading into the settings
            if(choosingDifficulty && playGame == false && settings == false){

                //checks if the player clicked the back button
                if(e.getX() > 0 && e.getX() < 300 && e.getY() > 0 && e.getY() < 100){

                    fadeSettings = true;

                    File file = new File("Enter Sound.wav");
                    try{
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                        }
                        catch(Exception ee){
                        }
                    
                    
                }

                //checks if the player clicked the hard difficulty button
                if(e.getX() > 850 && e.getX() < 1150 && e.getY() > 300 && e.getY() < 500){

                    tileSpeed = 8;
                    playGame = true;

                    //removes all of the notesin the game
                    notesX = new ArrayList<>();
                    notesY = new ArrayList<>();

                    if(shortenTime){
                        seconds = 0;
                        shortenTime = false;
                    }
                    else{
                        seconds = -1;
                    }
                    
                    
                    min = 0;

                    int widthOfKey = 651 / keys.length; //calculates the width of the keys (same as the width of the notes)

                    //adds 20 new notes to the game
                    for(int i = 0; i < 20; i++){
            
                        while(true){

                            notesX.add((int)(Math.random() * keys.length)); //adds the X position for the note
                            notesY.add((int)(Math.random() * -32000 - failCount)-25); //adds the Y position for the note
    
                            boolean leave = true; //used to see if the note was added successfully

                            //loops through the rest of the notes
                            for(int k = 0; k < notesX.size()-1; k++){
                                Rectangle note1 = new Rectangle(600+notesX.get(notesX.size()-1)+widthOfKey, notesY.get(notesY.size()-1), widthOfKey, 50);
                                Rectangle note2 = new Rectangle(600+notesX.get(k)+widthOfKey, notesY.get(k), widthOfKey, 50);

                                //checks if the new note intersects with any of the other notes in the game
                                if(note1.intersects(note2)){
                                    leave = false; //means that the note was not added sucessfully

                                    //deletes the new note
                                    notesX.remove(notesX.size()-1);
                                    notesY.remove(notesY.size()-1);
                                }
                            }
                            //if the note was added successfully it will break out of the while loop
                            if(leave){
                                break;
                            }    
                            
                        }
            
                    }

                    //plays the enter sound
                    File file = new File("Enter Sound.wav");

                    try{
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("Enter Sound.wav"));
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                        }
                        catch(Exception ee){
                        }
                }

                //checks if the player clicked the medium diffuculty button
                if(e.getX() > 500 && e.getX() < 800 && e.getY() > 300 && e.getY() < 500){

                    tileSpeed = 3;
                    playGame = true;

                    //deletes all of the notes on the screen
                    notesX = new ArrayList<>();
                    notesY = new ArrayList<>();

                    seconds = -2;
                    min = 0;

                    int widthOfKey = 651 / keys.length; //calculates the width of the keys (same as the width of the notes)

                    //adds 20 new notes to the game
                    for(int i = 0; i < 20; i++){
            
                        while(true){

                            notesX.add((int)(Math.random() * keys.length)); //adds the X position for the note
                            notesY.add((int)(Math.random() * -32000 - failCount)-25); //adds the Y position for the note
    
                            boolean leave = true; //used to see if the note was added successfully

                            //loops through the rest of the notes
                            for(int k = 0; k < notesX.size()-1; k++){
                                Rectangle note1 = new Rectangle(600+notesX.get(notesX.size()-1)+widthOfKey, notesY.get(notesY.size()-1), widthOfKey, 50);
                                Rectangle note2 = new Rectangle(600+notesX.get(k)+widthOfKey, notesY.get(k), widthOfKey, 50);

                                //checks if the new note intersects with any of the other notes in the game
                                if(note1.intersects(note2)){
                                    leave = false; //means that the note was not added sucessfully

                                    //deletes the new note
                                    notesX.remove(notesX.size()-1);
                                    notesY.remove(notesY.size()-1);
                                }
                            }
                            //if the note was added successfully it will break out of the while loop
                            if(leave){
                                break;
                            }    
                            
                        }
            
                    }

                    //plays the enter sound
                    File file = new File("Enter Sound.wav");
                    try{
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                        }
                        catch(Exception ee){
                        }

                }

                //checks if the medium difficulty button was clicked
                if(e.getX() > 150 && e.getX() < 450 && e.getY() > 300 && e.getY() < 500){

                    tileSpeed = 1;
                    playGame = true;

                    
                    //deletes all of the notes on the screen
                    notesX = new ArrayList<>();
                    notesY = new ArrayList<>();

                    seconds = -2;
                    min = 0;

                    int widthOfKey = 651 / keys.length; //calculates the width of the keys (same as the width of the notes)

                    //adds 20 new notes to the game
                    for(int i = 0; i < 20; i++){
            
                        while(true){

                            notesX.add((int)(Math.random() * keys.length)); //adds the X position for the note
                            notesY.add((int)(Math.random() * -32000 - failCount)-25); //adds the Y position for the note
    
                            boolean leave = true; //used to see if the note was added successfully

                            //loops through the rest of the notes
                            for(int k = 0; k < notesX.size()-1; k++){
                                Rectangle note1 = new Rectangle(600+notesX.get(notesX.size()-1)+widthOfKey, notesY.get(notesY.size()-1), widthOfKey, 50);
                                Rectangle note2 = new Rectangle(600+notesX.get(k)+widthOfKey, notesY.get(k), widthOfKey, 50);

                                //checks if the new note intersects with any of the other notes in the game
                                if(note1.intersects(note2)){
                                    leave = false; //means that the note was not added sucessfully

                                    //deletes the new note
                                    notesX.remove(notesX.size()-1);
                                    notesY.remove(notesY.size()-1);
                                }
                            }
                            //if the note was added successfully it will break out of the while loop
                            if(leave){
                                break;
                            }    
                            
                        }
            
                    }

                    //plays the enter sound
                    File file = new File("Enter Sound.wav");
                    try{
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                        }
                        catch(Exception ee){
                        }

                }

            }

            else{

            //checks if the player is fading into the settings screen
            if(settings && fadingIntoSettings == 255){
                
                int widthOfKey = 651 / keys.length; //calculates the width of the key

                //loops through all of the keys
                for(int i = 0; i < keys.length; i++){

                    //checks if the place the player clicked in the settings was one of the keys
                    if((e.getX() > 350+i* widthOfKey) && (e.getX() < 350+i* widthOfKey + widthOfKey) && (e.getY() > 675) && (e.getY() < 750)){
                        //means that player is trying to update their keybinds
                        index = i;
                        typeLetter = true;
                    }

                }

                //if the player the player clicked the upper right arrow button (means the player is trying to change the notes colors)
                if(e.getX() > 1050 && e.getX() < 1150 && e.getY() > 300 && e.getY() < 450){
                
                    //plays the enter sound
                    File file = new File("Enter Sound.wav");

                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }

                    //changes the color of the notes
                        if(currentNoteColor == noteColor.length-1){
                            currentNoteColor = 0;
                        }
                        else{
                            currentNoteColor++;
                        }
                    }

                //if the player clicked the upper left arrow button (means the player is trying to change the notes color)
                if(e.getX() > 200 && e.getX() < 300 && e.getY() > 300 && e.getY() < 450){

                    //plays the enter sound
                    File file = new File("Enter Sound.wav");

                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }

                    //changes the colors of the notes
                        if(currentNoteColor == 0){
                            currentNoteColor = noteColor.length-1;
                        }
                        else{
                            currentNoteColor--;
                        }
                    }


                //checks if the player clicked the lower right arrow button (means that the player is trying to increase how many keys they have)
                if(e.getX() > 1050 && e.getX() < 1100 && e.getY() > 675 && e.getY() < 725 && keys.length < 9){
                
                    //plays the enter sound
                    File file = new File("Enter Sound.wav");

                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }

                    keys = new String[keys.length+1]; //increases the amount of keys by one

                    pressed = new boolean[keys.length]; //sets the amount of keys pressed to the new keys length

                    colors = new Colors[keys.length]; //sets the amount of colors for the keys to the new keys length


                    //sets the defeault keys to equal 1 to the length of the keys (E.X "1", "2", "3" ...)
                    for(int i = 0; i < keys.length; i++){
                        keys[i] = "" + (i+1);
                    }

                    //sets the default colors of the keys to be completely transparent
                    for(int i = 0; i < colors.length; i++){
                        colors[i] = new Colors(206, 214, 208, 0);
                    }


                    //adds three more notes
                    for(int l = 0; l < 3; l++){

                            notesX.add((int)(Math.random() * keys.length)); //sets the notes X position
                
                            notesY.add((int)(Math.random() * -32000)-25); //sets the notes Y position
                
                    }

                }

                //checks if the player clicked the lower left arrow button (meaning that the player is trying to decrease the amount of keys they have)
                if(e.getX() > 250 && e.getX() < 300 && e.getY() > 675 && e.getY() < 725 && keys.length > 1){

                    //plays the enter sound
                    File file = new File("Enter Sound.wav");

                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }

                    index = Integer.MAX_VALUE; //sets the current index to infinity (since there should not be a current index)

                    keys = new String[keys.length-1]; //removes 1 key
                    pressed = new boolean[keys.length]; //sets the length of the keys pressed to the keys new length
                    colors = new Colors[keys.length];  //sets the amount of colors for the keys to the new keys length

                    //sets the defeault keys to equal 1 to the length of the keys (E.X "1", "2", "3" ...)
                    for(int i = 0; i < keys.length; i++){
                        keys[i] = "" + (i+1);
                    }

                    //sets the default colors of the keys to be completely transparent
                    for(int i = 0; i < colors.length; i++){
                        colors[i] = new Colors(206, 214, 208, 0);
                    }

                    //loops thtough all of the notes
                    for(int i = 0; i < notesX.size(); i++){
                        //checks if the notes x position is equal to keys.length
                        if(notesX.get(i) == keys.length){
                            //removes the key
                            notesX.remove(i);
                            notesY.remove(i);
                            i--; //ensures that every keys X position is checked
                        }
                    }
                }

                //checks if player clicked the back button
                if(e.getX() > 0 && e.getX() < 300 && e.getY() > 0 && e.getY() < 100){

                    //plays the enter sound
                    File file = new File("Enter Sound.wav");

                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    fadeSettings = true; //starts to fade out of the settings and into the title screen
                }
            }

            //checks if the player clicked the settings button from the title screen
            if(e.getX() > 125 && e.getX() < 425 && e.getY() > 450 && e.getY() < 550 && settings == false && choosingDifficulty == false){

                settings = true; //means that player is not transitioning into the settings screen

                //plays the enter sound
                File file = new File("Enter Sound.wav");

                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }

            }
        }

        //checks if player is on the home page and nothing else
            if(titleScreen && choosingDifficulty == false && settings == false){

            //checks if the player clicked the start button on home page 
             if(e.getX() > 125 && e.getX() < 425 && e.getY() > 300 && e.getY() < 400){
                File file = new File("Enter Sound.wav");

                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }

                //transitioning into the choosing difficulty page
                choosingDifficulty = true;
                smoothFade = 0;
                fadeIntoChoosingDiffucilty = 0;
                smoothFade2 = 255;
                smoothTransition = 255;

            }
        }
        //checks to see if the game is over (player lost the game)
        if(gameOver){

            //checks to see if the player clicked the replay/restart button
            if(e.getX() > 625 && e.getX() < 725 && e.getY() > 550 && e.getY() < 650){

                //restars the game (reseting game values)
                playGame = true;
                seconds = 0;
                min = 0;
                score = 0;
                shortenTime = true;
                pause = false;
                gameOver = false;
                endGameFadeIn = 0;
                lives = 5;

                //remvoes all of the current notes from the screen
                notesX = new ArrayList<>();
                notesY = new ArrayList<>();


                int widthOfKey = 651 / keys.length; //calculates the width of the keys (same as the width of the notes)
                
                //adds 20 new notes to the game
                for(int i = 0; i < 20; i++){
        
                    while(true){

                        notesX.add((int)(Math.random() * keys.length)); //adds the X position for the note
                        notesY.add((int)(Math.random() * -32000 - failCount)-25); //adds the Y position for the note

                        boolean leave = true; //used to see if the note was added successfully

                        //loops through the rest of the notes
                        for(int k = 0; k < notesX.size()-1; k++){
                            Rectangle note1 = new Rectangle(600+notesX.get(notesX.size()-1)+widthOfKey, notesY.get(notesY.size()-1), widthOfKey, 50);
                            Rectangle note2 = new Rectangle(600+notesX.get(k)+widthOfKey, notesY.get(k), widthOfKey, 50);

                            //checks if the new note intersects with any of the other notes in the game
                            if(note1.intersects(note2)){
                                leave = false; //means that the note was not added sucessfully

                                //deletes the new note
                                notesX.remove(notesX.size()-1);
                                notesY.remove(notesY.size()-1);
                            }
                        }
                        //if the note was added successfully it will break out of the while loop
                        if(leave){
                            break;
                        }    
                        
                    }
        
                }

                //sets all of the keys colors to complete transparent (0)
                for(int i = 0; i < colors.length; i++){
                    colors[i] = new Colors(206, 214, 208, 0);
                }

                //plays the enter sound
                File file = new File("Enter Sound.wav");

                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){}
            }

            //checks to see if the player clicked the home button
            if(e.getX() > 475 && e.getX() < 575 && e.getY() > 550 && e.getY() < 650){

                //resets variables that were declared when the game was loaded
                settings = false;
                fadingIntoSettings = 0;
                faderForSettings = 255;
                tileSpeed = 1;
                lives = 5;
                pause = false;
                min = 0;
                seconds = 0;
                fade = 0;
                settingsMovment = 0;
                isOnSettings = false;
                isOnControls = false;
                settings = false;
                fadeSettings = false;
                fadingOutOfSettings = 0;
                fadeSettings = false;
                titleScreen = true;
                choosingDifficulty = false;
                smoothFade = 0;
                smoothFade2 = 255;
                gameOver = false;
                playGame = false;
                score = 0;

                //sets all of the keys colors to be transparent
                for(int i = 0; i < colors.length; i++){
                    colors[i] = new Colors(206, 214, 208, 0);
                }

                //removes all of the notes from the screen
                notesX = new ArrayList<>();
                notesY = new ArrayList<>();

        int widthOfKey = 651 / keys.length;//finds the width of the column

        //creates 20 notes
        for(int i = 0; i < 20; i++){
            notesX.add((int)(Math.random() * keys.length));//creates X pos of note

            notesY.add((int)(Math.random() * -32000 - failCount)-25);//creates Y pos of note

            for(int j = 0; j < notesX.size()-1; j++){
                Rectangle note1 = new Rectangle(600+notesX.get(i)+widthOfKey, notesY.get(i), widthOfKey, 50);
                Rectangle note2 = new Rectangle(600+notesX.get(j)+widthOfKey, notesY.get(j), widthOfKey, 50);

                //checks if the newly created note hits another note
                if(note1.intersects(note2)){
                  //if two notes hit, the newly made note is removed
                    notesX.remove(notesX.size()-1);//removes X pos of newly made note
                    notesY.remove(notesY.size()-1);//removes Y pos of newly made note
                    failCount += 100;//increases fail count to make the game run smoother in the future
                    i--; //ensures that 20 notes will be created since one was just deleted
                    break;
                }
                
            }

        }

            //plays enter sound
            File file = new File("Enter Sound.wav");

            try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            }
            catch(Exception ee){
            }

            }
          
        }
     
           
        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {

            //checks if the player is currently on the controls screen
            if(loadControls){
                //checks if the player was hovering the back button
                if(e.getX() > 450 && e.getX() < 750 && e.getY() > 600 && e.getY() < 700 && active13){
                    active13 = false;

                    //sets the back button to the colored version
                    try {
                        backButton = ImageIO.read(new File("Back  col_Button.png"));
                    } catch (Exception ee) {}

                    //plays the click sound effect
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    }

                //checks if the player moved there mouse away from the back button
                else if(e.getX() < 450 || e.getX() > 750 || e.getY() < 600 || e.getY() > 700){
                    active13 = true;

                    //sets the back button to the regular version
                    try {
                        backButton = ImageIO.read(new File("Back Button.png"));
                    } catch (Exception ee) {}

                }
            }

            //checks to see if the player is currently play ground
            if(playGame){

                //checks to see if the player has lost the game
                if(gameOver){

                    //checks to see if the user hovering over the replay/restart button
                    if(e.getX() > 625 && e.getX() < 725 && e.getY() > 550 && e.getY() < 650 && active12){
                        active12 = false;

                        

                        //sets the replay button to the coloured version
                        try {
                            replay = ImageIO.read(new File("Return col_Square Button.png"));
                        } catch (Exception ee) {}

                        //plays the click sound effect
                        File file = new File("clicksounds.wav");
                        try{
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                        }
                        catch(Exception ee){
                        }

                    }
                    //cheks if the user is no longer hovering over the replay/start button
                    else if(e.getX() < 625 || e.getX() > 725 || e.getY() < 550 || e.getY() > 650){
                        active12 = true;

                        //sets the replay button to the regular version
                        try {
                            replay = ImageIO.read(new File("Return Square Button.png"));
                        } catch (Exception ee) {}
                    }

                    //checks if the user is hovering over the home button
                    if(e.getX() > 475 && e.getX() < 575 && e.getY() > 550 && e.getY() < 650 && active11){
                        active11 = false;

                        //sets the home button to the colored version
                        try {
                            home = ImageIO.read(new File("Home col_Square Button.png"));
                        } catch (Exception ee) {}
    
                        //plays the click sound
                        File file = new File("clicksounds.wav");
                        try{
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                        }
                        catch(Exception ee){
                        }

                    }

                    //checks if the user is no longer hovering the honme button
                    else if(e.getX() < 475 || e.getX() > 575 || e.getY() < 550 || e.getY() > 650){
                        active11 = true;

                        //sets the home buttion to the regular version
                        try {
                            home = ImageIO.read(new File("Home Square Button.png"));
                        } catch (Exception ee) {}
                    }



                }

                //checks if the game is not over
                if(!gameOver){
                    //checks if the user is hovering over the pause button
                    if(e.getX() > 0 && e.getX() < 50 && e.getY() > 0 && e.getY() < 50 && active10){
                        active10 = false;

                        //sets the pause button to the colored version
                        try {
                            pauseButton = ImageIO.read(new File("Pause col_Square Button.png"));
                        } catch (Exception ee) {}

                        //plays the click sound
                        File file = new File("clicksounds.wav");
                        try{
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                        }
                        catch(Exception ee){
                        }
                        
                    }
                    //checks if the user is no longer hovering of the pause button
                    else if(e.getX() < 0 || e.getX() > 50 || e.getY() < 0 || e.getY() > 50){
                        active10 = true;

                        //sets the pause button to the regular version
                        try {
                            pauseButton = ImageIO.read(new File("Pause Square Button.png"));
                        } catch (Exception ee) {}

                    }
                }
            }

            //checks if the player is currently chooing the difficult of the game and if the actual game has not started to load in
            if(choosingDifficulty && playGame == false){
                //checks if the player is currently hovering over the back button
                if(e.getX() > 0 && e.getX() < 300 && e.getY() > 0 && e.getY() < 100 && active6 == false){
                    active6 = true;

                    //sets the back button to the colored version
                    try {
                        backButton = ImageIO.read(new File("Back  col_Button.png"));
                    } catch (Exception ee) {}

                    //plays the click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                }
                
                //checks if the player is no longer hovering over the back button
                else if(e.getX() < 0 || e.getX() > 300 || e.getY() < 0 || e.getY() > 100){
                    active6 = false;
                    //sets the back button to the regular version
                    try {
                        backButton = ImageIO.read(new File("Back Button.png"));
                    } catch (Exception ee) {}
                }

                //checks if the player is hovering ofer the easy difficulty button
                if(e.getX() > 150 && e.getX() < 450 && e.getY() > 300 && e.getY() < 500 && active7 == false){
                    active7 = true;

                    //sets the easy button to the colored version
                    try {
                        easyButton = ImageIO.read(new File("Easy Color.png"));
                    } catch (Exception ee) {}

                    //plays the click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    
                }

                //checks if the player is no longer govering the easy button
                else if(e.getX() < 150 || e.getX() > 450 || e.getY() < 300 || e.getY() > 500){
                    active7 = false;

                    //sets the easy button to the regular version
                    try {
                        easyButton = ImageIO.read(new File("Easy.png"));
                    } catch (Exception ee) {}
                }

                //checks if the player is currently hovering over the meduim difficult button
                if(e.getX() > 500 && e.getX() < 800 && e.getY() > 300 && e.getY() < 500 && active8 == false){
                    active8 = true;

                    //sets the medium button to the colored version
                    try {
                        mediumButton = ImageIO.read(new File("Medium Color.png"));
                    } catch (Exception ee) {}

                    //plays the click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    
                }

                //checks if the player is no longer hoevring over the medium difficulty button
                else if(e.getX() < 500 || e.getX() > 800 || e.getY() < 300 || e.getY() > 500){
                    active8 = false;

                    //sets the medium button to the regular version
                    try {
                        mediumButton = ImageIO.read(new File("Medium.png"));
                    } catch (Exception ee) {}
                }

                //checks if the player is currently hovering over the hard difficult button
                if(e.getX() > 850 && e.getX() < 1150 && e.getY() > 300 && e.getY() < 500 && active9 == false){
                    active9 = true;

                    //sets the hard button to the colored version
                    try {
                        hardButton = ImageIO.read(new File("Hard Color.png"));
                    } catch (Exception ee) {}

                    //plays click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    
                }
                //checks if the player is no longer hovering over the hard difficulty button
                else if(e.getX() < 850 || e.getX() > 1150 || e.getY() < 300 || e.getY() > 500){
                    active9 = false;
                    //sets the hard ddifficulty button to the normal version
                    try {
                        hardButton = ImageIO.read(new File("Hard.png"));
                    } catch (Exception ee) {}
                }

            }

            //checks if the player is currently on the settings screen
            if(settings){

                //checks if the player is hovering over the back button
                if(e.getX() > 0 && e.getX() < 300 && e.getY() > 0 && e.getY() < 100 && active == false){
                    active = true;
                    //plays the click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    //sets the back button to the colored version
                    try {
                        backButton = ImageIO.read(new File("Back  col_Button.png"));
                    } catch (Exception ee) {}
                }
                //checks if the the player is no longer hovering over the back button
                else if(e.getX() < 0 || e.getX() > 300 || e.getY() < 0 || e.getY() > 100){
                    active = false;

                    //sets the back button to its regular version
                    try {
                        backButton = ImageIO.read(new File("Back Button.png"));
                    } catch (Exception ee) {}
                }

                //checks if the player is currently hovering over the upper right arrow
                if(e.getX() > 1050 && e.getX() < 1150 && e.getY() > 300 && e.getY() < 450 && active2 == false){
                    active2 = true;
                    //plays the click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    //sets the upper right arrow button to the colored version
                    try {
                        rightArrowButton = ImageIO.read(new File("Next col_Square Button.png"));
                    } catch (Exception ee) {}
                }
                //checks if the player is no longer hovering over the upper right arrow 
                else if(e.getX() < 1050 || e.getX() > 1150 || e.getY() < 300 || e.getY() > 450){
                    active2 = false;

                    //sets the upper right arrow but to its regular version
                    try {
                        rightArrowButton = ImageIO.read(new File("Next Square Button.png"));
                    } catch (Exception ee) {}
                }

                //checks if the player is hovering over the upper left arrow button
                if(e.getX() > 200 && e.getX() < 300 && e.getY() > 300 && e.getY() < 450 && active3 == false){
                    active3 = true;
                    //plays the click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }

                    //sets the upper left arrow button to the colored version
                    try {
                        leftArrowButton = ImageIO.read(new File("Back col_Square Button.png"));
                    } catch (Exception ee) {}
                }
                //checks if the player is no longer hovering the upper left arrow
                else if(e.getX() < 200 || e.getX() > 300 || e.getY() < 300 || e.getY() > 450){
                    active3 = false;

                    //sets the upper left arrow to the regular version
                    try {
                        leftArrowButton = ImageIO.read(new File("Back Square Button.png"));
                    } catch (Exception ee) {}
                }

                //checks if the player is currently hovering over the bottom right arrow byutton
                if(e.getX() > 1050 && e.getX() < 1100 && e.getY() > 675 && e.getY() < 725 && active4 == false){
                    active4 = true;

                    //plays the click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    //sets the bottom right arrow button to the the the color version
                    try {
                        rightArrowButton2 = ImageIO.read(new File("Next col_Square Button.png"));
                    } catch (Exception ee) {}
                }
                //checks if the player is no longer hovering over the bottom right arrow button
                else if(e.getX() < 1050 || e.getX() > 1100 || e.getY() < 675 || e.getY() > 725){
                    active4 = false;
                    //sets the bottom right arrow button to the regulat version
                    try {
                        rightArrowButton2 = ImageIO.read(new File("Next Square Button.png"));
                    } catch (Exception ee) {}
                }

                //checks if the player is currently hovering over the bottom left arrow button
                if(e.getX() > 250 && e.getX() < 300 && e.getY() > 675 && e.getY() < 725 && active5 == false){
                    active5 = true;
                    //plays the click sound
                    File file = new File("clicksounds.wav");
                    try{
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    }
                    catch(Exception ee){
                    }
                    //sets the bottom left arrow button to the the colored version
                    try {
                        leftArrowButton2 = ImageIO.read(new File("Back col_Square Button.png"));
                    } catch (Exception ee) {}
                }
                //checks if the player is no longer hovering over the bottom left arrow button
                else if(e.getX() < 250 || e.getX() > 300 || e.getY() < 675 || e.getY() > 725){
                    active5 = false;
                    //sets the bottom left arrow bbutton to the regular version
                    try {
                        leftArrowButton2 = ImageIO.read(new File("Back Square Button.png"));
                    } catch (Exception ee) {}
                }
            }

            else{

                //checks if the player is currently on the tittle screen and is not transistioning into the settings screen
            if(titleScreen && settings == false){

                //checks if the player is crrently hovering over the start buttion
            if(e.getX() > 125 && e.getX() < 425 && e.getY() > 300 && e.getY() < 400 && isOnStart == false){
                isOnStart = true;

                //sets the start button to its colored version
                try {
                    startButton = ImageIO.read(new File("Start  col_Button.png"));
                } catch (Exception ee) {}

                //plays the click sound
                File file = new File("clicksounds.wav");
                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }
            }

            //checks if the player is no longer hovering over the start button
            else if((e.getX() < 125 || e.getX() > 425 || e.getY() < 300 || e.getY() > 400) && isOnStart){
                isOnStart = false;

                //sets the start button to its regular version
                try {
                    startButton = ImageIO.read(new File("Start Button.png"));
                } catch (Exception ee) {}
            }

            //checls to see if the player is hovering over the settings button
            if(e.getX() > 125 && e.getX() < 425 && e.getY() > 450 && e.getY() < 550 && isOnSettings == false){
                isOnSettings = true;

                //sets the settings button to the colored version
                try {
                    settingsButton = ImageIO.read(new File("Settings  col_Button.png"));
                } catch (Exception ee) {}

                //plays the click sound
                File file = new File("clicksounds.wav");
                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }
            }

            //checks to see if the player is no longer hovering over the settings button
            else if((e.getX() < 125 || e.getX() > 425 || e.getY() < 450 || e.getY() > 550) && isOnSettings){
                isOnSettings = false;

                //sets the settings button to the regular version
                try {
                    settingsButton = ImageIO.read(new File("Settings Button.png"));
                } catch (Exception ee) {}
            }

            //checks to see if the player is hovering over the controls button
            if(e.getX() > 125 && e.getX() < 425 && e.getY() > 600 && e.getY() < 700 && isOnControls == false){
                isOnControls = true;

                //sets the controls button to the colored version
                try {
                    controlsButton = ImageIO.read(new File("Controls  col_Button.png"));
                } catch (Exception ee) {}

                //plays the click sound
                File file = new File("clicksounds.wav");
                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }
            }

            //checks to see if the player is no longer hovering over the controls button
            else if((e.getX() < 125 || e.getX() > 425 || e.getY() < 600 || e.getY() > 700) && isOnControls){
                isOnControls = false;
                //sets the controls button to the normal version
                try {
                    controlsButton = ImageIO.read(new File("Controls Button.png"));
                } catch (Exception ee) {}
            }

            

        }
    }

        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {

        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e) {

            //checks to see if the game is running and if the pause button has not been pushed
            if(playGame && pause == false){

                int indexx = -1;

                boolean incorrect = true;

                //searches for the index in "pressed" that matches with the key that has been pressed
                    for(int l = 0; l < keys.length; l++){
                    if(keys[l].equals("" + (char) e.getKeyCode()) && pressed[l] == false){
                        indexx = l;
                        pressed[l] = true;
                    }
                }

                //loops through all of the notes Y positions
                for(int i = 0; i < notesY.size(); i++){

                    //checks if you hit the note in the correct location with the correct key
                if(notesY.get(i)+25 >= 675 && indexx == notesX.get(i)){
                    score++; //increases score
                    incorrect = false; //means you got it correct
                    colors[indexx].setColor(153, 186, 161, 255);//sets key color to green

                    //removes the note
                    notesX.remove(i);
                    notesY.remove(i);
                    i--;//ensures that all Y position notes are checked

                    //adds a new note to the screen
                while(true){

                    notesX.add((int)(Math.random() * keys.length)); //sets the X position of the note
                    notesY.add((int)(Math.random() * -32000 - failCount)-25); //sets the Y position of the note

                    int widthOfKey = 651 / keys.length; //finds the width of the note (which is equal to the width of the key)

                    boolean leave = true;//used to check if the note has been sucessfully added

                    for(int k = 0; k < notesX.size()-1; k++){
                        Rectangle note1 = new Rectangle(600+notesX.get(notesX.size()-1)+widthOfKey, notesY.get(notesY.size()-1), widthOfKey, 50);
                        Rectangle note2 = new Rectangle(600+notesX.get(k)+widthOfKey, notesY.get(k), widthOfKey, 50);

                        //checks if the newly created note intersects any other note
                        if(note1.intersects(note2)){
                            leave = false; //means that the note that not been sucessfully added

                            //removes the newly created note
                            notesX.remove(notesX.size()-1); 
                            notesY.remove(notesY.size()-1);
                        }
                    }
                    //if the note has been successfuly added, there is no need to create another note
                    if(leave){
                        break;
                    }    
                    
                }

                }


            }

          //checks if the note is incorrect and if you chose a valid key
        if(incorrect && indexx != -1){
            colors[indexx].setColor(214, 75, 75, 255);//sets key color to red
            lives--;//lives decrease by 1

            //plays the "missed a note" sound
            File file = new File("Missed A Note.wav");
                try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                }
                catch(Exception ee){
                }
        }

        //checks if your lives are lower then one (meaning you lost the game)
        if(lives <= 0){
            pause = true; //pauses game
            gameOver = true; //signifies that the game is over

        }

            }

            //checks if you are allowed to add the key that you entered
            boolean allowed = true;
            int count = 0;
            for(int i = 0; i < keys.length; i++){
                if(keys[i].equals("" + (char)e.getKeyCode())){
                    count++;

                }
            }
            if(count >= 1){
                allowed = false;
            }

            //checks if you are in the settings screen and you are trying to type a letter that is "allowed"
            if(settings && typeLetter && e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_BACK_SPACE && e.getKeyCode() != KeyEvent.VK_DELETE && e.getKeyCode() != KeyEvent.VK_SHIFT && allowed){
                //updates the key to the new letter chosen by the user
                if(index < keys.length){
                keys[index] = "" + (char) e.getKeyCode();
                }
                if(index < keys.length-1){
                index++;
                }
                else{
                    index = 1000;
                }
            }
        


        }

        //if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            //this checks when the player has stoped holding the key they originally pressed
            for(int l = 0; l < keys.length; l++){
                if(keys[l].equals("" + (char) e.getKeyCode()) && pressed[l]){
                    pressed[l] = false;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        loop();
        repaint();
    }

    public static void main(String[] args) {
        // creates an instance of my game
        Piano game = new Piano();
    }

    //this Color object is used to store the colors of the keys
    public class Colors{
        Color color;
        int r; //redness
        int g; //greeness
        int b; //blueness
        int t; //transparency

        //contructor
        public Colors(int r, int g, int b, int t){

            color = new Color(r, g ,b , t);

            this.t = t;
            this.r = r;
            this.b = b;
            this.g = g;

        }

        //updates all of the objects instance variables
        public void setColor(int r, int g, int b, int t){
            color = new Color(r, g ,b , t);

            this.t = t;
            this.r = r;
            this.b = b;
            this.g = g;
        }

        //updates the transparecy level of the color (making it fade)
        public void updateColor(){

            if(t > 0){
                t--;
                color = new Color(r, g ,b , t);
            }

        }

    }
}