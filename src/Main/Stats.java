package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Stats {

	List<Integer> scores = new ArrayList();
	List<Integer> avScores = new ArrayList();
	int toAdd = -1;
	int toAddAv = -1;
	
	int maxScore = 0;
	
	int scaleWidth = 40;
	
	public void addScore(int s){
		toAdd = s;
	}
	
	public void addScoreAv(int s){
		toAddAv = s;
	}
	
	public void drawGraph(Graphics2D g, int x, int y, int width, int height){
		if(toAdd > -1){
			scores.add(toAdd);
			if(toAdd > maxScore) maxScore = toAdd;
			toAdd = -1;
		}
		
		if(toAddAv > -1){
			avScores.add(toAddAv);
			toAddAv = -1;
		}
		
		if(scores.size() == 0 || maxScore == 0) return;
		
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(3));
		
		float cx = x;
		float cy = y;
		
		float lx = -1, ly = -1;;
		
		float dist = (float)(width-scaleWidth)/(float)scores.size();
		
		for(int s : scores){
			
			cy = (y+height) - ((((float)s/(float)maxScore)*(float)height));
			
			if(lx > 0)
				g.drawLine((int)cx, (int)cy, (int)lx, (int)ly);
			
			
			lx = cx;
			ly = cy;
			
			cx+=dist;
		}
		
		
		cx = x;
		cy = y;
		
		lx = -1;
		
		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(1));
		
		for(int s : avScores){
			
			cy = (y+height) - ((((float)s/(float)maxScore)*(float)height));
			
			if(lx > 0)
				g.drawLine((int)cx, (int)cy, (int)lx, (int)ly);
			
			
			lx = cx;
			ly = cy;
			
			cx+=dist;
		}
		
		g.setColor(Color.black);
		String s = ""+maxScore;
		int sy = (int) (y + (g.getFontMetrics().getStringBounds(s, g).getHeight()/2));
		
		g.drawString(s, x+(width-scaleWidth)+5, sy);
		
		int cAv = avScores.get(avScores.size()-1);
		s = ""+cAv;
		
		sy = (int) ((y+height) - ((((float)cAv/(float)maxScore)*(float)height)) + (g.getFontMetrics().getStringBounds(s, g).getHeight()/2));
		
		g.drawString(s, x+(width-scaleWidth)+5, sy);
		

	}
	
	
}
