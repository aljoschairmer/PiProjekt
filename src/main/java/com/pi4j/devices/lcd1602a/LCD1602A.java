/*
 *
 *
 *     *
 *     * -
 *     * #%L
 *     * **********************************************************************
 *     * ORGANIZATION  :  Pi4J
 *     * PROJECT       :  Pi4J :: EXTENSION
 *     * FILENAME      :  LCD1602A.java
 *     *
 *     * This file is part of the Pi4J project. More information about
 *     * this project can be found here:  https://pi4j.com/
 *     * **********************************************************************
 *     * %%
 *     *   * Copyright (C) 2012 - 2022 Pi4J
 *      * %%
 *     *
 *     * Licensed under the Apache License, Version 2.0 (the "License");
 *     * you may not use this file except in compliance with the License.
 *     * You may obtain a copy of the License at
 *     *
 *     *      http://www.apache.org/licenses/LICENSE-2.0
 *     *
 *     * Unless required by applicable law or agreed to in writing, software
 *     * distributed under the License is distributed on an "AS IS" BASIS,
 *     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     * See the License for the specific language governing permissions and
 *     * limitations under the License.
 *     * #L%
 *     *
 *
 *
 *
 */

package com.pi4j.devices.lcd1602a;


import com.pi4j.context.Context;
import com.pi4j.devices.hd44780u.HD44780U;
import com.pi4j.devices.hd44780u.HD44780U_Declares;

import com.pi4j.util.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class LCD1602A {


    protected Console console = null;
    protected final Context pi4j;

    private String traceLevel = "";
    protected Logger logger;
    protected boolean clearDisplay = false;


    protected LCD1602A(Context pi4j, Console console, boolean clearIt, String traceLevel) {
        super();
        this.console = console;
        this.pi4j = pi4j;
        this.clearDisplay = clearIt;
        this.traceLevel = traceLevel;
    }


    protected void init() {
        System.setProperty("org.slf4j.simpleLogger.log." + HD44780U.class.getName(), this.traceLevel);
        this.logger = LoggerFactory.getLogger(LCD1602A.class);
        this.logger.trace(">>> Enter: init");

        this.logger.trace("<<< Exit: init  device  ");
    }

    public void shiftLeft(int places) {
        this.logger.trace(">>> Enter: shiftLeft  : " + places);
        for (int i = 0; i < places; i++) {
            this.sendCommand(HD44780U_Declares.cursorCMD | HD44780U_Declares.displayLeftBit);
        }
        this.logger.trace("<<< Exit: shiftLeft  ");

    }

    public void clearDisplay() {
        this.logger.trace(">>> Enter: clearDisplay   ");
        this.sendCommand(HD44780U_Declares.clearDispCMD);
        this.logger.trace("<<< Exit: clearDisplay   ");
    }


    public void sendStringLineOne(String str, int offset) {
        this.logger.trace(">>> Enter: sendStringLineOne   : " + str + "  Offset  : " + offset);
        char[] chars = str.toCharArray();
        this.sendCommand(HD44780U_Declares.setDDRAMCMD | offset);
        for (int i = 0; i < chars.length; i++) {
            this.sendChar(chars[i]);
        }
        this.logger.trace("<<<  Exit: sendStringLineOne  ");
    }


    public void sendStringLineTwo(String str, int offset) {
        this.logger.trace(">>> Enter: sendStringLineTwo   : " + str + "  Offset  : " + offset);
        char[] chars = str.toCharArray();
        this.sendCommand(HD44780U_Declares.setDDRAMCMD | 0x40 | offset);
        this.sleepTimeMS(4);
        for (int i = 0; i < chars.length; i++) {
            this.sendChar(chars[i]);
        }
        this.logger.trace("<<<  Exit: sendStringLineTwo  ");
    }

    protected boolean lcdAvailable() {
        int c = 0;
        this.logger.trace(">>> Enter: lcdAvailable  ");
        this.sleepTimeMS(10);
        boolean rval = this.isBfLow();
        while (rval == false) {
            this.logger.info("\n\n\n !!!!!   BF was busy  \n\n");
            this.sleepTimeMS(400);
            c++;
            if (c > 10) {
                this.logger.info(" LCD remained busy state ");
                console.println(" LCD remained busy state ");
                System.exit(100);
            }
            rval = this.isBfLow();
        }
        this.logger.trace("<<< Exit: lcdAvailable  : " + rval);
        return (rval);
    }

    protected void sleepTimeNS(int nanoSec) {
        try {
            Thread.sleep(0, nanoSec);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    protected void sleepTimeMS(int milliSec) {
        try {
            Thread.sleep(milliSec);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    // Specific to SubClass
    protected void sendChar(char c) {
        this.logger.trace(">>> Enter: sendChar   : " + c);
        this.logger.trace("<<<  Exit: sendChar  ");
    }

    // do required gpio->LCD_input dance before and after actual LCD pin update
    protected void sendCommand(int cmd) {
        this.logger.trace(">>> Enter: sendCommand   ");
        this.logger.trace("<<< Exit: sendCommand   ");
    }


    /**
     * Value of 0 indicates the device is not performing internal
     * operations and will accept commands
     * Not possible with the Pi GPIOs. Later if the D0_D7 interface is on an
     * MCP230xx, this maybe possible.  For present time, use timing values
     * documented in datasheet
     *
     * @return bit value of DB7
     */
    protected boolean isBfLow(){
        this.logger.trace(">>> Enter: isBfLow   ");
        this.logger.trace("<<< Exit: isBfLow   ");
        return(true);
    }


    protected void pulseEnable() {
        this.logger.trace(">>> Enter: pulseEnable   ");
        this.logger.trace("<<< Exit: pulseEnable   ");
    }


}
