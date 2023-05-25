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

    public void move(KeyEvent event, Map map) {
        int ori_x = this.x;
        int ori_y = this.y;
        int ori_type = this.type;
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

        if (this.type == 0) {
            if (map.get_block(this.x, this.y) == 1) {
                this.x = ori_x;
                this.y = ori_y;
                this.type = ori_type;
                return;
            }
        } else if (this.type == 1) {
            if (map.get_block(this.x, this.y) == 1 || map.get_block(this.x + 1, this.y) == 1) {
                this.x = ori_x;
                this.y = ori_y;
                this.type = ori_type;
                return;
            }
        } else if (this.type == 2) {
            if (map.get_block(this.x, this.y) == 1 || map.get_block(this.x, this.y + 1) == 1) {
                this.x = ori_x;
                this.y = ori_y;
                this.type = ori_type;
                return;
            }
        }

        System.out.println("x: " + this.x + " y: " + this.y + " type: " + this.type);
    }
}

class Map {

    private int map_size_row;
    private int map_size_col;

    private int[][] map;

    Map(int map_size_row, int map_size_col) {
        this.map_size_row = map_size_row;
        this.map_size_col = map_size_col;
        this.map = new int[map_size_row][map_size_col];
    }

    public int get_map_size_row() {
        return this.map_size_row;
    }

    public int get_map_size_col() {
        return this.map_size_col;
    }

    public void set_map_size_row(int map_size_row) {
        this.map_size_row = map_size_row;
        this.map = new int[map_size_row][map_size_col];
    }

    public void set_map_size_y(int map_size_col) {
        this.map_size_col = map_size_col;
        this.map = new int[map_size_row][map_size_col];
    }

    public void set_block(int x, int y, int type) {
        this.map[y][x] = type;
    }

    public int get_block(int x, int y) {
        if (x < 0 || x >= this.map_size_col || y < 0 || y >= this.map_size_row)
            return -1;
        return this.map[y][x];
    }

}

class final_game_panel extends JPanel implements KeyListener, MouseMotionListener {

    private static int MAP_BLOCK_SIZE = 20;
    private static int MAP_ROWS = 20;
    private static int MAP_COLS = 30;

    private static int SCREEN_WIDTH = MAP_COLS * MAP_BLOCK_SIZE;
    private static int SCREEN_HEIGHT = MAP_ROWS * MAP_BLOCK_SIZE;

    private static Block BLOCK = new Block(15, 10, MAP_COLS, MAP_ROWS);
    private static Map MAP = new Map(MAP_ROWS, MAP_COLS);

    final_game_panel() {
        super();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        init_game();
    }

    private void init_game() {
        // init map
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int tmp_x, tmp_y;
        for (int i = 0; i < 10; i++) {
            do {
                tmp_x = random.nextInt(MAP_COLS);
                tmp_y = random.nextInt(MAP_ROWS);
            } while (MAP.get_block(tmp_x, tmp_y) != 0);
            MAP.set_block(tmp_x, tmp_y, 1);
        }
        repaint();
    }

    private void draw_map(Graphics g) {
        for (int i = 0; i < MAP_COLS; i++) {
            for (int j = 0; j < MAP_ROWS; j++) {

                if (i == BLOCK.get_x() && j == BLOCK.get_y()
                        || BLOCK.get_type() == 1 && i == BLOCK.get_x() + 1 && j == BLOCK.get_y()
                        || BLOCK.get_type() == 2 && i == BLOCK.get_x() && j == BLOCK.get_y() + 1)
                    g.setColor(Color.RED);
                else if (MAP.get_block(i, j) == 0)
                    g.setColor(Color.WHITE);
                else if (MAP.get_block(i, j) == 1)
                    g.setColor(Color.BLACK);
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
        BLOCK.move(e, MAP);
        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
