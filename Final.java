import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
    private int start_x;
    private int start_y;
    private int x;
    private int y;
    private int map_size_x;
    private int map_size_y;
    private int type = 0; // 0: one block, 1: two blocks (horizontal), 2: two blocks (vertical)

    Block(int x, int y, int map_size_x, int map_size_y) {
        this.start_x = x;
        this.start_y = y;
        this.x = x;
        this.y = y;
        this.map_size_x = map_size_x;
        this.map_size_y = map_size_y;
    }

    public void init(int x, int y, int map_size_x, int map_size_y) {
        this.start_x = x;
        this.start_y = y;
        this.x = this.start_x;
        this.y = this.start_y;
        this.map_size_x = map_size_x;
        this.map_size_y = map_size_y;
        this.type = 0;
    }

    public void goto_start() {
        this.x = this.start_x;
        this.y = this.start_y;
        this.type = 0;
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

    public void move(int event, Map map) {
        int ori_x = this.x;
        int ori_y = this.y;
        int ori_type = this.type;
        switch (event) {
            case KeyEvent.VK_A:
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
            case KeyEvent.VK_D:
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
            case KeyEvent.VK_W:
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
            case KeyEvent.VK_S:
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

        // System.out.println("x: " + this.x + " y: " + this.y + " type: " + this.type);
    }

    public boolean is_fall_in_hole(Map map) {
        if (this.type == 0) {
            if (map.get_block(this.x, this.y) == 2)
                return true;
        } else if (this.type == 1) {
            if (map.get_block(this.x, this.y) == 2 && map.get_block(this.x + 1, this.y) == 2)
                return true;
        } else if (this.type == 2) {
            if (map.get_block(this.x, this.y) == 2 && map.get_block(this.x, this.y + 1) == 2)
                return true;
        }
        return false;
    }

    public boolean is_finish(Map map) {
        return this.type == 0 && map.get_block(this.x, this.y) == 4;
    }
}

class Map {

    private int map_size_row;
    private int map_size_col;

    private int[][] map;

    private int[] x_path = { 0, 1, 0, -1 };
    private int[] y_path = { -1, 0, 1, 0 };

    private boolean[][] visited;

    private int keys[] = { KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A };

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

    public void set_block(Block block, int type) {
        if (block.get_type() == 0) {
            this.set_block(block.get_x(), block.get_y(), type);
        } else if (block.get_type() == 1) {
            this.set_block(block.get_x(), block.get_y(), type);
            this.set_block(block.get_x() + 1, block.get_y(), type);
        } else if (block.get_type() == 2) {
            this.set_block(block.get_x(), block.get_y(), type);
            this.set_block(block.get_x(), block.get_y() + 1, type);
        }
    }

    public int get_block(int x, int y) {
        if (x < 0 || x >= this.map_size_col || y < 0 || y >= this.map_size_row)
            return -1;
        return this.map[y][x];
    }

    public void create_map(int x, int y, int steps) {
        boolean flag = false;

        ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        Random random = new Random();
        do {
            random.setSeed(System.currentTimeMillis());
            this.init();
            list.clear();
            Block block = new Block(x, y,
                    this.map_size_col, this.map_size_row);
            // System.out.println("create x: " + block.get_x() + " y: " + block.get_y() + "
            // type: " + block.get_type());

            list.add(new ArrayList<Integer>(Arrays.asList(block.get_x(), block.get_y(), block.get_type())));
            this.set_block(block, 3);
            int c = 0;
            for (int i = 0; i < steps || list.get(list.size() - 1).get(2) != 0; i++) {
                c = 0;
                do {
                    block.move(keys[random.nextInt(4)], this);
                    tmp.clear();
                    tmp.add(block.get_x());
                    tmp.add(block.get_y());
                    tmp.add(block.get_type());
                    c++;
                } while (list.contains(tmp) && c < 10);
                if (c >= 10) {
                    flag = false;
                    break;
                }

                // System.out.println("move x: " + block.get_x() + " y: " + block.get_y() + "
                // type: " + block.get_type());
                list.add(new ArrayList<Integer>(tmp));
                this.set_block(block, 1);
                flag = true;
            }

            if (!flag) {
                continue;
            }

            set_block(block, 4);

            // System.out.println("list size: " + list.size());
            // for (int i = 0; i < list.size(); i++) {
            // tmp = list.get(i);
            // System.out.println("x: " + tmp.get(0) + " y: " + tmp.get(1) + " type: " +
            // tmp.get(2));
            // }

            for (int i = 0; i < this.map_size_row; i++) {
                for (int j = 0; j < this.map_size_col; j++) {
                    if (this.map[i][j] == 1)
                        this.map[i][j] = -1;
                }
            }

            for (int i = 0; i < this.map_size_row; i++) {
                for (int j = 0; j < this.map_size_col; j++) {
                    if (this.map[i][j] == 0) {
                        visited = new boolean[this.map_size_row][this.map_size_col];
                        if (check_out(j, i, 0)) {
                            fill_map(j, i, 1);
                        } else {
                            fill_map(j, i, 2);
                        }
                    }
                }
            }

            // System.out.println("create finish");

        } while (!flag);

    }

    private boolean check_out(int x, int y, int co) {
        if (x < 0 || x >= this.map_size_col || y < 0 || y >= this.map_size_row)
            return false;
        if (this.map[y][x] != 0 || visited[y][x])
            return false;
        if (co >= 30)
            return true;
        visited[y][x] = true;
        for (int i = 0; i < 4; i++) {
            if (check_out(x + x_path[i], y + y_path[i], ++co))
                return true;
        }
        return false;
    }

    private void fill_map(int x, int y, int type) {
        if (x < 0 || x >= this.map_size_col || y < 0 || y >= this.map_size_row)
            return;
        if (this.map[y][x] == 0) {
            this.map[y][x] = type;
            for (int i = 0; i < 4; i++) {
                fill_map(x + x_path[i], y + y_path[i], type);
            }
        }
    }

    public void init() {
        for (int i = 0; i < this.map_size_row; i++) {
            for (int j = 0; j < this.map_size_col; j++) {
                this.map[i][j] = 0;
            }
        }
    }

}

class final_game_panel extends JPanel implements KeyListener, MouseMotionListener {

    private static int MAP_BLOCK_SIZE = 20;
    private static int MAP_ROWS = 40;
    private static int MAP_COLS = 60;

    private static int TOOLBAR_HEIGHT = 50;
    private static int SCREEN_WIDTH = MAP_COLS * MAP_BLOCK_SIZE;
    private static int SCREEN_HEIGHT = MAP_ROWS * MAP_BLOCK_SIZE + TOOLBAR_HEIGHT;

    private static Block BLOCK = new Block(15, 10, MAP_COLS, MAP_ROWS);
    private static Map MAP = new Map(MAP_ROWS, MAP_COLS);

    private static int DIFFICULTY = 50;

    final_game_panel() {
        super();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        init_game();
        // Add buttons
        JButton restartButton = new JButton("重新開始");
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BLOCK.goto_start();
                repaint();
            }
        });

        JButton difficultyButton = new JButton("選擇難度");
        difficultyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 難度

                // create a dialog Box
                final JDialog d = new JDialog();
                // create a label
                JLabel l = new JLabel("選擇難度");
                // create a new buttons
                JButton b1 = new JButton("簡單");
                JButton b2 = new JButton("中等");
                JButton b3 = new JButton("困難");
                // create a panel
                JPanel p = new JPanel();
                // add action listeners
                b1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DIFFICULTY = 50;
                        d.setVisible(false);
                        init_game();
                    }
                });

                b2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DIFFICULTY = 100;
                        d.setVisible(false);
                        init_game();
                    }
                });

                b3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        DIFFICULTY = 150;
                        d.setVisible(false);
                        init_game();
                    }
                });

                // add buttons to panel
                p.add(b1);
                p.add(b2);
                p.add(b3);
                // add panel to dialog
                d.add(l, BorderLayout.NORTH);
                d.add(p, BorderLayout.CENTER);
                // setsize of dialog
                d.setSize(300, 300);
                // set dialog in center
                d.setLocationRelativeTo(null);
                // set visibility of dialog
                d.setVisible(true);

            }
        });

        JButton exitButton = new JButton("離開");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add buttons to the tool bar
        JPanel buttonPanel = new JPanel();

        // buttonPanel.setFloatable(false);
        buttonPanel.add(restartButton);
        buttonPanel.add(difficultyButton);
        buttonPanel.add(exitButton);

        // Set layout for the main panel
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        // set tool bar height
        buttonPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, TOOLBAR_HEIGHT));
    }

    private void init_game() {
        // init map
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int x = random.nextInt(MAP_COLS / 2) + MAP_COLS / 4,
                y = random.nextInt(MAP_ROWS / 2) + MAP_ROWS / 4;
        System.out.println("start pos x: " + x + " y: " + y);
        MAP.create_map(x, y, DIFFICULTY);
        BLOCK.init(x, y, MAP_COLS, MAP_ROWS);
        // MAP.init();
        repaint();
    }

    private void draw_map(Graphics g) {
        for (int i = 0; i < MAP_COLS; i++) {
            for (int j = 0; j < MAP_ROWS; j++) {

                if (i == BLOCK.get_x() && j == BLOCK.get_y()
                        || BLOCK.get_type() == 1 && i == BLOCK.get_x() + 1 && j == BLOCK.get_y()
                        || BLOCK.get_type() == 2 && i == BLOCK.get_x() && j == BLOCK.get_y() + 1)
                    g.setColor(new Color(117, 64, 8));
                else if (MAP.get_block(i, j) == 0 || MAP.get_block(i, j) == -1)// path
                    g.setColor(Color.WHITE);
                else if (MAP.get_block(i, j) == 1)// wall
                    g.setColor(Color.BLACK);
                else if (MAP.get_block(i, j) == 2)// hole
                    g.setColor(Color.GRAY);
                else if (MAP.get_block(i, j) == 3)// start
                    g.setColor(Color.GREEN);
                else if (MAP.get_block(i, j) == 4)// finish
                    g.setColor(Color.yellow);
                else
                    g.setColor(Color.BLUE);
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
        if (e.getKeyCode() == KeyEvent.VK_R)
            BLOCK.goto_start();
        else
            BLOCK.move(e.getKeyCode(), MAP);
        if (BLOCK.is_fall_in_hole(MAP)) {
            System.out.println("fall in hole");
            // init_game();
            BLOCK.goto_start();
        } else if (BLOCK.is_finish(MAP)) {
            System.out.println("You Win");
            init_game();
        }
        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
