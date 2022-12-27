package model;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

import controller.Tile;
import static controller.Constants.*;

/**
 *
 *
 * @author g54915
 */
public class Game extends JPanel {

    /**
     *
     */
    private static final Color BACKGROUND_COLOR = new Color(0xBAAC9F);
    /**
     *
     */
    private static final String FONT = "Calibri";
    /**
     *
     */
    private static final int FONT_SIZE = 64;
    /**
     *
     */
    private static final int MARGIN = 35;
    /**
     *
     */
    private boolean isComplete = false;
    /**
     *
     */
    private boolean isFailed = false;
    /**
     *
     */
    private int score = 0;
    /**
     *
     */
    private Tile[] tiles;

    /**
     *
     */
    public Game() {
        setPreferredSize(new Dimension(640, 800));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    resetGame();
                }
                if (isStuck()) {
                    isFailed = true;
                }
                if (!isComplete && !isFailed) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            left();
                            break;
                        case KeyEvent.VK_RIGHT:
                            right();
                            break;
                        case KeyEvent.VK_DOWN:
                            down();
                            break;
                        case KeyEvent.VK_UP:
                            up();
                        default:
                            break;
                    }
                }
                if (!isComplete && isStuck()) {
                    isFailed = true;
                }
                repaint();
            }
        });
        resetGame();
    }

    /**
     *
     */
    private void addTile() {
        List<Tile> list = availableSpace();
        if (!availableSpace().isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Tile empty = list.get(index);
            empty.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    /**
     *
     *
     * @return
     */
    private List<Tile> availableSpace() {
        final List<Tile> list = new ArrayList<Tile>(16);
        for (Tile tile : tiles) {
            if (tile.isEmpty()) {
                list.add(tile);
            }
        }
        return list;
    }

    /**
     *
     *
     * @return
     */
    private boolean isStuck() {
        if (availableSpace().isEmpty()) {
            return false;
        }
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Tile tile = tileAt(x, y);
                boolean moveH
                        = x < (GRID_SIZE - 1) && tile.getValue() == tileAt(x + 1, y).getValue();
                boolean moveV
                        = y < (GRID_SIZE - 1) && tile.getValue() == tileAt(x, y + 1).getValue();
                if (moveH || moveV) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     *
     * @param line
     * @param merged
     *
     * @return
     */
    private boolean compare(final Tile[] line, final Tile[] merged) {
        if (line == merged) {
            return true;
        } else if (line.length != merged.length) {
            return false;
        }
        for (int i = 0; i < line.length; i++) {
            if (line[i].getValue() != merged[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    private void down() {
        tiles = rotate(INFERIOR_RIGHT);
        left();
        tiles = rotate(REFLEX_RIGHT);
    }

    /**
     *
     *
     * @param list
     */
    private void ensureSize(final List<Tile> list) {
        while (list.size() != GRID_SIZE) {
            list.add(new Tile());
        }
    }

    /**
     * @param index
     */
    private Tile[] getLine(final int index) {
        Tile[] result = new Tile[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            result[i] = tileAt(i, index);
        }
        return result;
    }

    /**
     *
     */
    private void left() {
        boolean needAddTile = false;
        for (int i = 0; i < GRID_SIZE; i++) {
            Tile[] line = getLine(i);
            Tile[] merged = mergeLine(moveLine(line));
            setLine(i, merged);
            if (!needAddTile && !compare(line, merged)) {
                needAddTile = true;
            }
        }
        if (needAddTile) {
            addTile();
        }
    }

    /**
     *
     *
     * @param oldLine
     *
     * @return
     */
    private Tile[] mergeLine(final Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < GRID_SIZE && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].getValue();
            if (i < (GRID_SIZE - 1)
                    && oldLine[i].getValue() == oldLine[i + 1].getValue()) {
                num *= 2;
                score += num;
                if (num == 2048) {
                    isComplete = true;
                }
                i++;
            }
            list.add(new Tile(num));
        }
        if (list.isEmpty()) {
            return oldLine;
        } else {
            ensureSize(list);
            return list.toArray(new Tile[4]);
        }
    }

    /**
     * @param oldLine
     *
     * @return
     */
    private Tile[] moveLine(final Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < GRID_SIZE; i++) {
            if (!oldLine[i].isEmpty()) {
                list.addLast(oldLine[i]);
            }
        }
        if (list.isEmpty()) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[GRID_SIZE];
            ensureSize(list);
            for (int i = 0; i < GRID_SIZE; i++) {
                newLine[i] = list.removeFirst();
            }
            return newLine;
        }
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                drawTile(graphics, tiles[x + GRID_SIZE * y], x, y);
            }
        }
    }

    private void drawTile(
            final Graphics graphics, final Tile tile, final int x, final int y
    ) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        graphics2D.setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE
        );
        int value = tile.getValue();
        int xOffset = offsetCoordinates(x);
        int yOffset = offsetCoordinates(y);
        graphics2D.setColor(tile.getBackground());
        graphics2D.fillRect(xOffset, yOffset, MARGIN * GRID_SIZE, MARGIN * GRID_SIZE);
        graphics2D.setColor(tile.getForeground());
        final int size = (value < 100) ? 80 : (value < 1000 ? 76 : 68);
        final Font font = new Font(FONT, Font.PLAIN, size);
        graphics2D.setFont(font);

        String string = String.valueOf(value);
        final FontMetrics metrics = getFontMetrics(font);

        final int width = metrics.stringWidth(string);
        final int height = -(int) metrics.getLineMetrics(string, graphics2D)
                .getBaselineOffsets()[2];

        graphics2D.drawString(
                string,
                xOffset + (MARGIN * GRID_SIZE - width) / 2,
                yOffset + MARGIN * GRID_SIZE - (MARGIN * GRID_SIZE - height) / 2 - 2
        );

        if (isComplete || isFailed) {
            graphics2D.setColor(new Color(255, 255, 255, 30));
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
            graphics2D.setColor(new Color(78, 139, 202));
            graphics2D.setFont(new Font(FONT, Font.BOLD, 48));
            if (isComplete) {
                graphics2D.drawString("You win !", 230, 330);
            }
            if (isFailed) {
                graphics2D.drawString("You lose !", 230, 330);
            }
            if (isComplete || isFailed) {
                graphics2D.setFont(new Font(FONT, Font.PLAIN, 16));
                graphics2D.setColor(new Color(128, 128, 128, 128));
                graphics2D.drawString("Press [Esc] to play again!", 80, getHeight() - 40);
            }
        }
        graphics2D.setFont(new Font(FONT, Font.PLAIN, 18));
        graphics2D.drawString("Score : " + score, 270, 25);
    }

    private static int offsetCoordinates(final int arg) {
        return arg * (MARGIN * GRID_SIZE) + MARGIN;
    }

    /**
     *
     */
    private void resetGame() {
        score = 0;
        isComplete = false;
        isFailed = false;
        tiles = new Tile[GRID_SIZE * GRID_SIZE];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile();
        }
        addTile();
        addTile();
    }

    /**
     *
     */
    private void right() {
        tiles = rotate(STRAIGHT_ANGLE);
        left();
        tiles = rotate(STRAIGHT_ANGLE);
    }

    /**
     *
     *
     * @param angle
     */
    private Tile[] rotate(final int angle) {
        Tile[] newTiles = new Tile[GRID_SIZE * GRID_SIZE];
        int offSetX = GRID_SIZE - 1, offSetY = GRID_SIZE - 1;
        if (angle == INFERIOR_RIGHT) {
            offSetY = 0;
        } else if (angle == REFLEX_RIGHT) {
            offSetX = 0;
        }
        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                int newX = (x * cos) - (y * sin) + offSetX;
                int newY = (x * sin) + (y * cos) + offSetY;
                newTiles[newX + newY * 4] = tileAt(x, y);
            }
        }
        return newTiles;
    }

    /**
     *
     *
     * @param index
     * @param merged
     */
    private void setLine(final int index, final Tile[] merged) {
        System.arraycopy(merged, 0, tiles, index * 4, 4);
    }

    /**
     *
     *
     * @param x
     * @param y
     *
     * @return
     */
    private Tile tileAt(final int x, final int y) {
        return tiles[x + GRID_SIZE * y];
    }

    private void up() {
        tiles = rotate(REFLEX_RIGHT);
        left();
        tiles = rotate(INFERIOR_RIGHT);
    }
}
