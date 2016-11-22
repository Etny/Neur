package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import API.MainClass;
import API.State;

public class MainState extends State{
	
	Creature[] cs = new Creature[8];
	int current = 0;
	
	int speed = 0;
	
	long gen = 0;
	
	List<Creature> old = new ArrayList();
	
	int[] speeds = new int[]{1, 2, 4, 10, 100, 200, 400, 1000};
	
	Trainer t;
	Stats s;
		
	public static int BOARD_WIDTH = 1280;
	public static int BOARD_HEIGHT = 720;

	@Override
	public void draw(Graphics2D g) {
		 RenderingHints rh = new RenderingHints(
				 RenderingHints.KEY_ANTIALIASING,
		         RenderingHints.VALUE_ANTIALIAS_ON);
		 g.setRenderingHints(rh);
		 
		g.setColor(Color.white);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		
		cs[current].draw(g);
		t.draw(g);
		
		g.setColor(Color.black);
		g.drawString(speeds[speed]+"x", (MainClass.WIDTH/2-g.getFontMetrics().stringWidth(speeds[speed]+"x")/2)-100, 30);
		
		g.setColor(Color.gray);
		g.fillRect(0, BOARD_HEIGHT, MainClass.WIDTH, MainClass.HEIGHT-BOARD_HEIGHT);
		
		s.drawGraph(g, 20, BOARD_HEIGHT+20, MainClass.WIDTH-40, MainClass.HEIGHT-BOARD_HEIGHT-40);
	}

	@Override
	public void init() {
		t = new Trainer();
		s = new Stats();
		genCreatures();
	}

	@Override
	public void update() {
		for(int i=0; i<speeds[speed]; i++){
			cs[current].update(t);
			if(t.update(cs[current])){
				cs[current].score = t.reset();
				if(current < cs.length-1)
					current++;
				else{
					newGen();
					//t.genLocs();
				}			
			}
		}
	}
	
	private void newGen(){
		quicksort(cs, 0, cs.length-1);
		old.clear();
		
		HashMap<Creature, Integer> scores = new HashMap();
		
		System.out.print("gen "+gen+": ");
		
		s.addScore(cs[cs.length-1].score);
		
		int total = 0;
		
		for(Creature c : cs){
			System.out.print(c.score+" ");
			
			total += c.score;
			scores.put(c, c.score);
			c.reset();
		}
		System.out.println();
		
		for(int i=cs.length/2; i<cs.length; i++){
			old.add(cs[i]);
		}
		
		s.addScoreAv(total/cs.length);

		
		for(int i=0; i<cs.length/2; i++){
			if(scores.get(cs[cs.length-i-1])>0)
				cs[i] = cs[cs.length-i-1].getMutation();
			else
				cs[i] = new Creature();
		}
		
		gen++;
		current = 0;
	}
	
	 private void quicksort(Creature[] css, int low, int high) {
         int i = low, j = high;

         int pivot = css[low + (high-low)/2].score;
         while (i <= j) {

                 while (css[i].score < pivot) 
                         i++;
                 
                 while (css[j].score > pivot) 
                         j--;
                 

                 if (i <= j) {
                         exchange(css, i, j);
                         i++;
                         j--;
                 }
         }
         // Recursion
         if (low < j)
                 quicksort(css, low, j);
         if (i < high)
                 quicksort(css, i, high);
	 }

	 private void exchange(Creature[] css, int i, int j) {
         Creature temp = css[i];
         css[i] = css[j];
         css[j] = temp;
	 }
	
	private void genCreatures(){
		for(int i=0; i<cs.length; i++) cs[i] = new Creature();
	}
	
	@Override
	public void keyPressed(KeyEvent k){
		if(k.getKeyCode() == KeyEvent.VK_ENTER){
			speed += 1;
			
			if(speed >= speeds.length) speed = 0;
		}
	}

	@Override
	public void updateAlways() {
		
	}

}
