package controller;

import java.awt.Color;

/**
 *
 *
 * @author g54915
 */
public class Tile {

    private int value;

    /**
     *
     */
    public Tile() {
        this(0);
    }

    /**
     *
     *
     * @param num
     *
     */
    public Tile(final int num) {
        value = num;
    }

    /**
     *
     *
     * @return
     */
    public final Color getBackground() {
        switch (value) {
            case 0:
                return new Color(0xECE2D8);
            case 2:
                return new Color(0xEDE3D9);
            case 4:
                return new Color(0xECDFC7);
            case 8:
                return new Color(0xF1B078);
            case 16:
                return new Color(0xF49462);
            case 32:
                return new Color(0xF57B5E);
            case 64:
                return new Color(0xF65E3B);
            case 128:
                return new Color(0xEDCF72);
            case 256:
                return new Color(0xEDCC61);
            case 512:
                return new Color(0xEDC850);
            case 1024:
                return new Color(0xEDC53F);
            case 2048:
                return new Color(0xEDC22E);
            default:
                return new Color(0x66CCFF);
        }
    }

    /**
     *
     *
     * @return
     */
    public final Color getForeground() {
        return (value == 0) ? new Color(0xCCC0B6) : (value < 8) ? new Color(0x776E65) : new Color(0xF9F6F2);
    }

    public int getValue() {
        return value;
    }

    /**
     * @param value
     *
     */
    public void setValue(final int value) {
        this.value = value;
    }

    /**
     *
     *
     * @return
     */
    public final boolean isEmpty() {
        return (value == 0);
    }
}
