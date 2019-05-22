
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.KeyEvent;
import javax.tools.Tool;

class Self{
    int x;
    int y;
    boolean Up;
    boolean Down;
    boolean Left;
    boolean Right;

    public Self(int x, int y) {
        this.x = x;
        this.y = y;
        Up = false;
        Down = false;
        Left = false;
        Right = false;
    }
}

class Bullet {
    int x;
    int y;
    int speed;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 8;
    }
}

class Enemy {
    int x;
    int y;
    int speed;

    public Enemy (int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 8;
    }
}

public class Plane extends JFrame implements ActionListener{
    public final static int WIDTH1 = 600;
    public final static int HEIGHT1 = 800;
    public final static int SPEED = 10;
    JPanel gp;
    Toolkit tool;
    Image background;
    Image plane;
    Image bullet;
    Image enemy;
    Self self;
    Timer timer;
    ArrayList<Bullet> bulletArr;
    ArrayList<Enemy> enemyArr;
    Random random;
    int failNum;
    int killNum;
    int frequency; // bullet frequency

    public Plane() {
        super("Hit the pane!");
        setSize(new Dimension(WIDTH1, HEIGHT1 + 40));
        gp = new graphPaint();
        self = new Self(200, 600);
        this.add(gp);
        tool = gp.getToolkit();
        background = tool.getImage("img/background.jpg");
        plane = tool.getImage("img/plane.jpg");

        setVisible(true);
        setResizable(false);
        this.addKeyListener(new whichKey());
        timer = new Timer(100, this);
        timer.start();

        bullet = tool.getImage("img/bullet.jpg");
        enemy = tool.getImage("img/enemy.jpg");
        bulletArr = new ArrayList<Bullet>();
        enemyArr = new ArrayList<Enemy>();
        frequency = 0;
        random = new Random();
        new Timer(2000, event -> {
            enemyArr.add(new Enemy(random.nextInt(WIDTH1 - 50),0));
        }).start();
        failNum = 0;
        killNum = 0;
    }

    public void actionPerformed(ActionEvent e) {
        gp.repaint();
    }
    public class graphPaint extends JPanel {

        public void paint (Graphics g) {
            super.paint(g);
            g.drawImage(background, 0, 0, this);
            g.setFont(new Font("Times New Roman", Font.BOLD, 30));
            g.drawString("Avoided Plane #" + failNum, 0, 30);
            g.drawString("Killed Plane #" + killNum, 0, 70);
            if (self.Up) {
                self.y -= SPEED;
                if (self.y <= 0) {
                    self.y += SPEED;
                }
            }
            if (self.Down) {
                self.y += SPEED;
                if (self.y >= HEIGHT1 - 50) {
                    self.y -= SPEED;
                }
            }
            if (self.Right) {
                self.x += SPEED;
                if (self.x >= WIDTH1 - 50) {
                    self.x -= SPEED;
                }
            }
            if (self.Left) {
                self.x -= SPEED;
                if (self.x <= 0) {
                    self.x += SPEED;
                }
            }
            g.drawImage(plane, self.x, self.y, this);

            for (int i = 0; i < bulletArr.size(); i++) {
                bulletArr.get(i).y -= bulletArr.get(i).speed;
                for (int j = 0; j < enemyArr.size(); j++) {
                    if (bulletArr.get(i).x >= enemyArr.get(j).x - 10 && bulletArr.get(i).x <= enemyArr.get(j).x + 50 &&
                            bulletArr.get(i).y <= enemyArr.get(j).y + 40) {
                        bulletArr.remove(i);
                        enemyArr.remove(j);
                        killNum++;
                        break;
                    }
                    if (bulletArr.get(i).y <= -50) {
                        bulletArr.remove(i);
                    }
                }
            }
            for (int i = 0; i < bulletArr.size(); i++) {
                g.drawImage(bullet, bulletArr.get(i).x, bulletArr.get(i).y, this);
            }
            for (int i = 0; i < enemyArr.size(); i++) {
                enemyArr.get(i).y += enemyArr.get(i).speed;
                if (enemyArr.get(i).y >= HEIGHT1) {
                    failNum++;
                    enemyArr.remove(i);
                }
            }
            for (int i = 0; i < enemyArr.size(); i++) {
                g.drawImage(enemy, enemyArr.get(i).x, enemyArr.get(i).y, this);
            }
        }
    }

    class whichKey implements KeyListener {
        public void keyTyped(KeyEvent e) {

        }
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case 87: //w
                    self.Up = true;
                    break;
                case 65: //a
                    self.Left = true;
                    break;
                case 83: //s
                    self.Down = true;
                    break;
                case 68: //d
                    self.Right = true;
                    break;
                case 75:// k, shoot bullets
                    bulletArr.add(new Bullet(self.x + 15, self.y - 50));
                    break;
                default:
                    break;
            }
        }

        public void keyReleased(KeyEvent e){
            switch(e.getKeyCode()){
                case 87: //w
                    self.Up = false;
                    break;
                case 65: //a
                    self.Left = false;
                    break;
                case 83: //s
                    self.Down = false;
                    break;
                case 68: //d
                    self.Right = false;
                    break;
                default:
                    break;
            }
        }
    }
    public static void main(String[] args) {
        new Plane();
    }
}