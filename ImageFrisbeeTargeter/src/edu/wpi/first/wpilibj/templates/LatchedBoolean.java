/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author GLaDOS
 */
public class LatchedBoolean {

    boolean value;
    boolean latch = false;

    public LatchedBoolean() {
        value = false;
    }

    public LatchedBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void set(boolean input) {
        if (input && !latch) {
            value = !value;
            latch = true;
        }
        else if (!input) {
            latch = false;
        }
    }
}
