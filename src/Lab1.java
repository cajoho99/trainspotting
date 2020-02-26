import TSim.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class Lab1 {

    public Lab1(int speed1, int speed2) {
        TSimInterface tsi = TSimInterface.getInstance();
        tsi.setDebug(false);


        try {
            tsi.setSpeed(1, speed1);
            tsi.setSpeed(2, speed2);
            tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);


        } catch (CommandException e) {
            e.printStackTrace();    // or only e.getMessage() for the error
            System.exit(1);
        }

        Semaphore fourWay = new Semaphore(1);
        Semaphore black = new Semaphore(0);
        Semaphore red = new Semaphore(1);
        Semaphore green = new Semaphore(1);
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
                        if (se.getXpos() == 6 && se.getYpos() == 6 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                fourWay.acquire();
                                tsi.setSpeed(id, speed);
                                // to North
                            } else {
                                fourWay.release();
                            }
                        }

                        // Black east
                        if (se.getXpos() == 14 && se.getYpos() == 7 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                black.release();
                                System.out.println("Black released! - " + black.availablePermits());
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                                tsi.setSpeed(id, speed);
                                // to North
                            }else{
                               green.release();
                            }
                        }

                        // Red north fourway
                        if (se.getXpos() == 8 && se.getYpos() == 6 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                fourWay.acquire();
                                tsi.setSpeed(id, speed);
                                // to North
                            } else {
                               fourWay.release();
                            }
                        }

                        // Red south fourway
                        if (se.getXpos() == 9 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                               fourWay.release();
                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                fourWay.acquire();
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Green east fourway
                        if (se.getXpos() == 10 && se.getYpos() == 7 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                fourWay.release();
                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                fourWay.acquire();
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Red east Tcrossing
                        if (se.getXpos() == 16 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                red.release();
                                System.out.println("Red released! - " + red.availablePermits());


                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
                                tsi.setSpeed(id, speed);
                                // to North
                            } else {
                                green.release();

                            }
                        }


                        // Green east
                        if (se.getXpos() == 19 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE && direction) {
                            // to South
                            tsi.setSpeed(id, 0);


                            if (red.tryAcquire()) {
                                System.out.println("Red acquired! - " + red.availablePermits());

                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);

                            } else {
                                black.acquire();
                                System.out.println("Black acquired! - " + black.availablePermits());


                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                            }
                            tsi.setSpeed(id, speed);

                        }

                        // Green south
                        if (se.getXpos() == 17 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                if (purple.tryAcquire()) {
                                    tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
                                } else {
                                    blue.acquire();
                                    tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);
                                }
                                tsi.setSpeed(id, speed);
                                // to North
                            }
                        }

                        // Purple east
                        if (se.getXpos() == 13 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                green.release();
                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                purple.release();
                                tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);

                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Blue east
                        if (se.getXpos() == 13 && se.getYpos() == 10 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                green.release();
                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                blue.release();
                                tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);

                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Purple west
                        if (se.getXpos() == 6 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                orange.acquire();

                                tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
                                purple.release();
                                tsi.setSpeed(id, speed);
                            } else {
                                orange.release();

                            }
                        }

                        // Blue west
                        if (se.getXpos() == 6 && se.getYpos() == 10 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                orange.acquire();

                                tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
                                tsi.setSpeed(id, speed);
                                blue.release();
                            } else {
                                orange.release();

                            }
                        }

                        // Orange
                        if (se.getXpos() == 2 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                if (pink.tryAcquire()) {


                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                    tsi.setSpeed(id, speed);
                                } else {
                                    teal.acquire();

                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                                    tsi.setSpeed(id, speed);
                                }
                                tsi.setSpeed(id, speed);
                            } else {
                                tsi.setSpeed(id, 0);
                                if (purple.tryAcquire()) {
                                    tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
                                    tsi.setSpeed(id, speed);
                                } else if (blue.tryAcquire()) {

                                    tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
                                    tsi.setSpeed(id, speed);
                                }
                            }


                        }

                        // Pink west
                        if (se.getXpos() == 5 && se.getYpos() == 11 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                orange.release();

                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                orange.acquire();

                                pink.release();
                                tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Teal west
                        if (se.getXpos() == 4 && se.getYpos() == 13 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                orange.release();

                                // to North
                            } else {

                                tsi.setSpeed(id, 0);
                                orange.acquire();
                                tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                                teal.release();

                                tsi.setSpeed(id, speed);
                            }
                        }

                        /// North Station
                        if (se.getXpos() == 13 && (se.getYpos() == 5 || se.getYpos() == 3) && direction && se.getStatus() == SensorEvent.ACTIVE) {
                            tsi.setSpeed(id, 0);
                            sleep(2000);
                            speed = -speed;
                            tsi.setSpeed(id, speed);
                            direction = !direction;
                        }


                        /// South Station
                        if (se.getXpos() == 13 && (se.getYpos() == 11 || se.getYpos() == 13) && !direction && se.getStatus() == SensorEvent.ACTIVE) {
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

        Thread train2 = new Thread(new Train(2, speed2, true));
        train2.start();
        Thread train1 = new Thread(new Train(1, speed1, false));
        train1.start();


    }

}

