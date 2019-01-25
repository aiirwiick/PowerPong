import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball{

    private int x, y, w, h, rebounds;
    private double velX, velY;
    public Random random = new Random();
    private PowerPong powerPong;

    public int getRebounds(){
        return rebounds;
    }

    public Ball(PowerPong powerPong, int width, int height){
        this.powerPong = powerPong;
		this.w = width;
		this.h = height;
        start();
    }

    public int checkCollision(Player player){				//Checks the balls position relative to the paddle
        if (x < player.getX() + player.getWidth() && x + w > player.getX() && y < player.getY() + player.getHeight() && y + h > player.getY()){
            return 1; //bounce
        }
        else if ((player.getX() > x && player.playerNumber == 1) || (player.getX() < x - w && player.playerNumber == 2)){
            return 2; //score
        }

        return 0; //nothing
    }

    public int update(Player player1, Player player2){					//Updates the position of the ball
        x += velX;
        y += velY;

        if (y + h - velY > powerPong.h || y + velY < 0){				//Makes the ball "bounce" when it is about to cross the top or bottom border
            if (velY < 0){
                velY = 3;
				y = 0;
            }
            else{
                velY = -3;
				y = powerPong.h - h;
            }
        }

        if (checkCollision(player1) == 1){								//Checks if ball contacts the paddle and makes the ball "bounce" if true
            velX = 3 + ((double)(rebounds)/5.0);
            rebounds++;
        }
        else if (checkCollision(player2) == 1){
            velX = -3 - ((double)(rebounds)/5.0);
            rebounds++;
        }
        if (checkCollision(player1) == 2){								//Increments score by 1 and restarts the game when ball crosses paddle
            rebounds = 0;
			player2.updateScore(player2.getScore()+1);
            start();
            return 1;
        }
        else if (checkCollision(player2) == 2){
            rebounds = 0;												//Resets # of rebounds to make ball slower
			player1.updateScore(player1.getScore()+1);
            start();
            return 1;
        }
        return 0;
    }

    public void start(){
        x = powerPong.w / 2 - w / 2;		//Resets location of the ball to be at the center of the frame
        y = powerPong.h / 2 - h / 2;
		boolean one = random.nextBoolean();					//Generates two random booleans and decides starting velocity with them
		boolean two = random.nextBoolean();
        if(one && two){
			velX = 3;
			velY = 3;
		}
		else if(one && !two){
			velX = 3;
			velY = -3;
		}
		else if(!one && two){
			velX = -3;
			velY = 3;
		}
		else if(!one && !two){
			velX = -3;
			velY = -3;
		}
    }

    public void render(Graphics g){
        g.setColor(Color.WHITE);
        g.fillOval(x, y, w, h);
    }

}
