package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import API.MainClass;
import Input.Mouse;

public class Creature {
	
	Brain brain;
	
	float x,y;
	
	int time;
	
	int radius = 20;
	float rot = 0;
	
	float fov = 45;
	boolean canSee = false;
	
	int score = 0;
	int lastScore = 0;
	
	public Creature(){
		x = MainClass.WIDTH/2;
		y = MainClass.HEIGHT/2;
		
		brain = new Brain(2, 4, 3, 20, 20);
	}
	
	public Creature(Brain b){
		x = MainClass.WIDTH/2;
		y = MainClass.HEIGHT/2;
		
		brain = b;
	}
	
	public Creature getMutation(){
		return new Creature(brain.getMutation());
	}
	
	public void reset(){
		x = MainClass.WIDTH/2;
		y = MainClass.HEIGHT/2;
		
		
		time = 0;
		rot = 0;
		fov = 45;
		canSee = false;
		lastScore = score;
		score = 0;
		
		brain = brain.clone();
	}
	
	public Creature clone(){
		return new Creature(brain);
	}
	
	public void draw(Graphics2D g){
		AffineTransform old = g.getTransform();
		g.rotate(Math.toRadians(rot), x, y);
		
		g.setColor(Color.black);
		double t = Math.tan(Math.toRadians(fov/2));
		g.drawLine((int)x, (int)y, (int)(x-(t*MainClass.HEIGHT)), (int)y-MainClass.HEIGHT);
		g.drawLine((int)x, (int)y, (int)(x+(t*MainClass.HEIGHT)), (int)y-MainClass.HEIGHT);
		
		if(!canSee) g.setColor(Color.red);
		else g.setColor(Color.orange);
		g.fillOval((int)x-radius, (int)y-radius, radius*2, radius*2);
		
		g.setColor(Color.blue);
		g.fillOval((int)x-radius/4, (int)y-radius, radius/2, radius/2);
	
		g.setTransform(old);
		
		brain.drawBrain(g);
	}
	
	public void move(float dist){
		x += (Math.cos(Math.toRadians(rot-90))*dist);
		y += (Math.sin(Math.toRadians(rot-90))*dist);
		 
		if(x-radius < 0) x = radius;
		if(y-radius < 0) y = radius;
		
		if(x+radius > MainClass.WIDTH) x = MainClass.WIDTH-radius;
		if(y+radius > MainClass.HEIGHT) y = MainClass.HEIGHT-radius;
	}
	
	public void update(Trainer t){
		Point2D p = t.c;
		
		int rot1 = Math.round(rot)%360;
		
		if(rot1<0) rot1 = 360+rot1;
		
		double dX = ((x) - p.getX());
		double dY = ((y) - p.getY());
		
		double dD = Math.sqrt(Math.pow(dX, 2)+Math.pow(dY, 2));
		
		int angle = (int) Math.toDegrees(Math.acos(dY/dD));
		
		if(dX > 0) angle += (180-angle)*2;
		
		canSee = angle>=(rot1-(fov/2)) && angle<=(rot1+(fov/2));	
		
		brain.setInput(0, (time%60)/6);
		brain.setInput(1,  canSee?10:0);
		
		time++;
		
		brain.update();
		
		move(brain.getValue(0));
		rot += brain.getValue(1);
		fov += brain.getValue(2);
		
		if(fov < 10) fov = 10;
		if(fov > 90) fov = 90;
	}
}
