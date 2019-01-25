import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.Timer;

public class PowerPong implements ActionListener, KeyListener{

    public static PowerPong powerPong;
    public JFrame jframe;
    public Ball ball, ball2, ball3;
    public Renderer render;
    public Player player1;
    public Player player2;
    public boolean q, a, up, down;
    public int state = 1;   //1 = Menu, 2 = Paused, 3 = In Game, 4 = End
    public int scoreLimit = 5;
    public int playerWins;
    public int w = 1900, h = 1000;

    public PowerPong(){
        jframe = new JFrame("Power Pong");
        render = new Renderer();
        jframe.setSize(w, h);					//changes graphic's size so that the ball won't get cut off when bouncing off the bottom and paddle also won't get cut off
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(render);
        jframe.addKeyListener(this);

        Timer t = new Timer(5, this);
        t.start();
    }
    /*
    *Sets program into In Game mode
    *Creates fresh players and a ball
    */
    public void start(){
        state = 3;
        player1 = new Player(this, 1, 0, 350);
        player2 = new Player(this, 2, 1860, 350);
        ball = new Ball(this, 35, 35);
        ball2 = new Ball(this, 25, 25);
        ball3 = new Ball(this, 15, 15);
    }
    /*
    *Moves the players when prompted.
    */
    public void update(){
        if(player1.getScore() >= scoreLimit){
            state = 4;
            playerWins = 1;
        }

        if(player2.getScore() >= scoreLimit){
            state = 4;
            playerWins = 2;
        }

        if(up){
            player2.move(1);
        }
        else if(down){
            player2.move(-1);
        }

        if(q){
            player1.move(1);
        }
        else if(a){
            player1.move(-1);
        }
		//Adds a new ball when a milestone is reached
        int i = ball.update(player1, player2);
        int a = 0;
        int b = 0;

        if(ball.getRebounds() >= 4){
            a = ball2.update(player1, player2);
        }

        if(ball.getRebounds() >= 12){
            b = ball3.update(player1, player2);
        }

        if(i == 1 || a == 1 || b == 1){
            ball.start();
            ball2.start();
            ball3.start();
        }

        if(ball.getRebounds() >= 8){
            player1.shrink();
            player2.shrink();
        }

        if(ball.getRebounds() == 0){
            player1.enlarge();
            player2.enlarge();
        }
    }

    public void render(Graphics2D g){
        g.setColor(Color.GREEN.darker().darker().darker());
        g.fillRect(0, 0, w, h);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Creates initial text to prompt player to start game
        if(state == 1){
            g.setColor(Color.WHITE);

            g.setFont(new Font("Rockwell", 1, 100));
            g.drawString("POWER PONG", w / 2 - 390, 300);

            g.setFont(new Font("Rockwell", 1, 50));
            g.drawString("Press Space to Play", w / 2 - 275, h / 2 - 50);
            g.drawString("Press P to Pause", w / 2 - 245, h / 2 + 25);
            g.drawString("Press Esc in Game to Quit", w / 2 - 350, h / 2 + 100);
        }

        //Pauses game and projects the Paused text
        if(state == 2){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Rockwell", 1, 50));
            g.drawString("PAUSED", w / 2 - 103, h / 2 - 25);
        }

        //Projects the score of each player
        if(state == 2 || state == 3){
            g.setColor(Color.WHITE);
            g.drawLine(w / 2, 0, w / 2, h);
            g.setStroke(new BasicStroke(5f));
            g.setStroke(new BasicStroke(2f));
            g.setFont(new Font("Rockwell", 1, 50));
            g.drawString(String.valueOf(player1.getScore()), w / 2 - 55, 60);
            g.drawString(String.valueOf(player2.getScore()), w / 2 + 30, 60);

            player1.render(g);
            player2.render(g);
            ball.render(g);

            if(ball.getRebounds() >= 4){
                ball2.render(g);
            }

            if(ball.getRebounds() >= 12){
                ball3.render(g);
            }
        }

        /*
        *Declares winner of the match
        *Reprompts for another match
        */
        if(state == 4){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Rockwell", 1, 100));
            g.drawString("POWER PONG", w / 2 - 390, 300);

            g.setFont(new Font("Rockwell", 1, 50));
            g.drawString("Player " + playerWins + " Wins!", w / 2 - 200, 200);

            g.setFont(new Font("Rockwell", 1, 50));
            g.drawString("Press Space to Play Again", w / 2 - 330, h / 2 - 25);
        }
    }

    /*
    *Prompts the program to perform task when an action is performed
    */
    public void actionPerformed(ActionEvent e){
        if(state == 3)
        {
            update();
        }

        render.repaint();
    }


    //Method which tells what to do when a key is pressed
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            up = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            down = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_Q){
            q = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_A){
            a = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            state = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE && (state == 1 || state == 4)){
            start();
        }
        else if(e.getKeyCode() == KeyEvent.VK_P){
            if(state == 2){
                state = 3;
            }
            else if(state == 3){
                state = 2;
            }
        }
    }

    //Method which tells what to do when a key is released
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            up = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            down = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_Q){
            q = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_A){
            a = false;
        }
    }

    public void keyTyped(KeyEvent e){} //Added because it is part of interface

    public static void main(String[] args){
        powerPong = new PowerPong();
    }
}
