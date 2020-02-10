import TSim.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();

    try {
      tsi.setSpeed(1, speed1);
      tsi.setSpeed(2, speed2);
      tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
      tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
      tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);


    } catch (CommandException e) {
      e.printStackTrace();    // or only e.getMessage() for the error
      System.exit(1);
    }

    Semaphore black = new Semaphore(0);
    Semaphore red = new Semaphore(1);
    Semaphore green = new Semaphore(0);
    Semaphore purple = new Semaphore(1);
    Semaphore blue = new Semaphore(1);
    Semaphore orange = new Semaphore(1);
    Semaphore pink = new Semaphore(0);
    Semaphore teal = new Semaphore(1);


    //tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);


    class Train implements Runnable {

      int id;
      int speed;
      // True = north
      boolean direction;

      public Train(int id, int speed, boolean dir) {
        this.id = id;
        this.speed = speed;
        this.direction = dir;
      }

      @Override
      public void run() {
        try {
          while (!!!false) {
            SensorEvent se = tsi.getSensor(id);

            // Black past Fourway
            if (se.getXpos() == 6 && se.getYpos() == 6) {
              if (direction) {
                tsi.setSpeed(id, 0);
                red.acquire();
                green.acquire();
                tsi.setSpeed(id, speed);
              }else {
                black.acquire();
                red.release();
                green.release();
              }
            }


            /// North Station
            if (se.getXpos() == 13 && (se.getYpos() == 5 || se.getYpos() == 3) && direction) {
              tsi.setSpeed(id, 0);
              sleep(2000);
              speed = -speed;
              tsi.setSpeed(id, speed);
              direction = !direction;
            }


            /// South Station
            if (se.getXpos() == 13 && (se.getYpos() == 11 || se.getYpos() == 13) && !direction) {
              tsi.setSpeed(id, 0);
              sleep(2000);
              speed = -speed;
              tsi.setSpeed(id, speed);
              direction = !direction;
            }

          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }




    }

    Thread train1 = new Thread(new Train(1, speed1, false));
    train1.run();

  }

}

