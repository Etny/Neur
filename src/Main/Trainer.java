package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

import API.MainClass;

public class Trainer {
	
	Point2D c;
	int i = 0;
	Point2D[] points = new Point2D[10];
	
	int dist = 40;
	
	Color cl;
	
	int time = 60*20;
	
	int score = 0;
	
	int lastScore = 0;
	
	public Trainer(){
		cl = Color.green;
		genLocs();
	}
	
	public void genLocs(){
		Random r = new Random();
		
		for(int i=0; i<points.length; i++)
			points[i] = (Point2D) new Point(r.nextInt(MainState.BOARD_WIDTH), r.nextInt(MainState.BOARD_HEIGHT));
		c = points[0];
	}
	
	public void draw(Graphics2D g){
		g.setColor(cl);	
		g.fillOval((int)c.getX()-(dist/2), (int)c.getY()-(dist/2), dist, dist);
		
		g.setColor(Color.white);	
		g.fillOval((int)c.getX()-((dist-6)/2), (int)c.getY()-((dist-6)/2), dist-6, dist-6);
		
		g.setColor(cl);	
		g.fillOval((int)c.getX()-(dist/8), (int)c.getY()-(dist/8), dist/4, dist/4);
		
		g.setColor(Color.black);
		g.drawString(time/60+"", MainState.BOARD_WIDTH/2-g.getFontMetrics().stringWidth(time/60+"")/2, 30);
		
		g.drawString(lastScore+"", (MainState.BOARD_WIDTH/2-g.getFontMetrics().stringWidth(lastScore+"")/2)+100, 30);
	}
	
	public int reset(){
		int sTemp = score;

		score = 0;
		time = 60*20;
		i=0;
		c = points[i];
		
		return sTemp;
	}
	
	public boolean update(Creature cr){
		if(c.distance((Point2D) new Point(Math.round(cr.x), Math.round(cr.y)))<=dist){
			score += MainState.BOARD_WIDTH-((60*20)-time);
			i++;
			if(i >= points.length){
				score += time;
				return true;
			}
			c = points[i];
		}
		
		lastScore = cr.lastScore;
		
		time--;
		
		if(time <= 0){
			int d = (int) c.distance((Point2D) new Point(Math.round(cr.x), Math.round(cr.y)));
			
			if(d <= MainState.BOARD_WIDTH/8) score += (MainState.BOARD_WIDTH/8)-d;
			return true;
		}
		
		return false;
	}
	

}
