import TSim.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class Lab1 {

    public Lab1(int speed1, int speed2) {
        TSimInterface tsi = TSimInterface.getInstance();
        tsi.setDebug(true);


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
                                green.acquire();
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                                black.release();
                                tsi.setSpeed(id, speed);
                                // to North
                            } else {
                                black.acquire();
                                green.release();
                                fourWay.release();
                            }
                        }

                        // Red north fourway
                        if (se.getXpos() == 8 && se.getYpos() == 6 && se.getStatus() == SensorEvent.ACTIVE) {
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

                        // Red south fourway
                        if (se.getXpos() == 9 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
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
                        if (se.getXpos() == 18 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                                tsi.setSpeed(id, speed);
                                // to North
                            } else {
                                red.release();
                            }
                        }


                        // Green east
                        if (se.getXpos() == 18 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (black.availablePermits() == 0) {
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
                            } else {
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                            }
                        }

                        // Green south
                        if (se.getXpos() == 17 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                if (purple.availablePermits() != 0) {
                                    purple.acquire();
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
                                if (pink.availablePermits() != 0) {
                                    pink.acquire();
                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                                } else {
                                    teal.acquire();
                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                }
                                tsi.setSpeed(id, speed);
                            } else {
                                tsi.setSpeed(id, 0);
                                if (purple.availablePermits() != 0) {
                                    purple.acquire();
                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                                } else if(blue.availablePermits() != 0) {
                                    blue.acquire();
                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                }else {
                                  tsi.setSpeed(id, 0);
                                }
                                tsi.setSpeed(id, speed);
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
                                tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Teal west
                        if (se.getXpos() == 3 && se.getYpos() == 13 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                orange.release();
                                // to North
                            } else {
                                tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);

                                tsi.setSpeed(id, 0);
                                orange.acquire();
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

