import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.awt.event.KeyListener;

import javax.swing.*;

public class Final {

    static JFrame frm = new JFrame("final");
    static JPanel panel = new final_game_panel();

    public static void main(String[] args) {
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setResizable(false);
        frm.setLocationRelativeTo(null);
        frm.setContentPane(panel);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);

    }
}

class Block {
    private int x;
    private int y;
    private int map_size_x;
    private int map_size_y;
    private int type = 0; // 0: one block, 1: two blocks (horizontal), 2: two blocks (vertical)

    Block(int x, int y, int map_size_x, int map_size_y) {
        this.x = x;
        this.y = y;
        this.map_size_x = map_size_x;
        this.map_size_y = map_size_y;
    }

    public int get_x() {
        return this.x;
    }

    public int get_y() {
        return this.y;
    }

    public int get_type() { // 0: one block, 1: two blocks (horizontal), 2: two blocks (vertical)
        return this.type;
    }

    public void move(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (this.type == 0) {
                    if (x < 2)
                        return;
                    this.x -= 2;
                    this.type = 1;
                } else if (this.type == 1) {
                    if (x < 1)
                        return;
                    this.x--;
                    this.type = 0;
                } else if (this.type == 2) {
                    if (x < 1)
                        return;
                    this.x--;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (this.type == 0) {
                    if (x > map_size_x - 3)
                        return;
                    this.x++;
                    this.type = 1;
                } else if (this.type == 1) {
                    if (x > map_size_x - 3)
                        return;
                    this.x += 2;
                    this.type = 0;
                } else if (this.type == 2) {
                    if (x > map_size_x - 2)
                        return;
                    this.x++;
                }
                break;
            case KeyEvent.VK_UP:
                if (this.type == 0) {
                    if (y < 2)
                        return;
                    this.y -= 2;
                    this.type = 2;
                } else if (this.type == 1) {
                    if (y < 1)
                        return;
                    this.y--;
                } else if (this.type == 2) {
                    if (y < 1)
                        return;
                    this.y--;
                    this.type = 0;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (this.type == 0) {
                    if (y > map_size_y - 3)
                        return;
                    this.y++;
                    this.type = 2;
                } else if (this.type == 1) {
                    if (y > map_size_y - 2)
                        return;
                    this.y++;
                } else if (this.type == 2) {
                    if (y > map_size_y - 3)
                        return;
                    this.y += 2;
                    this.type = 0;
                }
                break;
            default:
                return;
        }

        System.out.println("x: " + this.x + " y: " + this.y + " type: " + this.type);
    }
}

class final_game_panel extends JPanel implements KeyListener, MouseMotionListener {

    private static int MAP_BLOCK_SIZE = 20;
    private static int MAP_ROWS = 20;
    private static int MAP_COLS = 30;

    private static int SCREEN_WIDTH = MAP_COLS * MAP_BLOCK_SIZE;
    private static int SCREEN_HEIGHT = MAP_ROWS * MAP_BLOCK_SIZE;

    private static Block BLOCK = new Block(15, 10, MAP_COLS, MAP_ROWS);

    final_game_panel() {
        super();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        repaint();
        init_game();
    }

    private void init_game() {
        repaint();
    }

    private void draw_map(Graphics g) {
        for (int i = 0; i < MAP_COLS; i++) {
            for (int j = 0; j < MAP_ROWS; j++) {

                if (i == BLOCK.get_x() && j == BLOCK.get_y()
                        || BLOCK.get_type() == 1 && i == BLOCK.get_x() + 1 && j == BLOCK.get_y()
                        || BLOCK.get_type() == 2 && i == BLOCK.get_x() && j == BLOCK.get_y() + 1)
                    g.setColor(Color.RED);
                else
                    g.setColor(Color.WHITE);
                g.fillRect(i * MAP_BLOCK_SIZE, j * MAP_BLOCK_SIZE, MAP_BLOCK_SIZE, MAP_BLOCK_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(i * MAP_BLOCK_SIZE, j * MAP_BLOCK_SIZE, MAP_BLOCK_SIZE, MAP_BLOCK_SIZE);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponents(g);
        draw_map(g);

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        BLOCK.move(e);
        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
