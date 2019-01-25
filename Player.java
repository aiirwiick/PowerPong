import java.awt.Color;
import java.awt.Graphics;

public class Player{

    public int playerNumber;
    private int x, y, w = 25, h = 300;
    private int score;


	//Accessor Methods
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth(){
        return w;
    }
    public int getHeight(){
        return h;
    }
    public int getScore(){
        return score;
    }

    public void updateScore(int newScore){			//Updates the score of the player
        score = newScore;
    }

    public Player(PowerPong powerPong, int playerNumber, int Xposition, int Yposition){		//Player Constructor
        this.playerNumber = playerNumber;
		this.x = Xposition;
		this.y = Yposition;

    }

    public void move(int direction){
        int speed = 6;

        if (direction == 1 && y - speed > 0){			//If the up button is pressed, the paddle moves up in + direction
            y -= speed;
        }
        else if (direction == 1 && y - speed <= 0){
            y = 0;
        }
        if(direction == -1 && y + h + speed < PowerPong.powerPong.h){				//If the down button is pressed, the paddel moves down in - direction
			y += speed;
		}
        else if (direction == -1 && y + h + speed >= PowerPong.powerPong.h){
            y = PowerPong.powerPong.h - h;
        }

    }

    public void shrink(){				//Shrinks the paddle to make game harder
        h = 150;
    }

    public void enlarge(){				//Resets paddle to be the original size
        h = 300;
    }

    public void render(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(x, y, w, h);
    }

}
