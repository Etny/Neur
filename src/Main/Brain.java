package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

import Input.Mouse;

public class Brain {
	
	Node[][] nodes = new Node[3][];
	
	int hidden, inputs, outputs, x, y;
	
	public Brain(int inputs, int hidden, int outputs, int x, int y){
		this.inputs = inputs;
		this.hidden = hidden;
		this.outputs = outputs;
		this.x = x;
		this.y = y;
		
		nodes[0] = new Node[inputs];
		nodes[1] = new Node[hidden];
		nodes[2] = new Node[outputs];
		
		int cx = x;
		int cy = y;
		
		for(int i=0; i<inputs; i++){
			nodes[0][i] = new Node(false, cx, cy);
			cy += 40;
		}
		
		cy = y;
		cx += 50;
		
		for(int i=0; i<hidden; i++){
			nodes[1][i] = new Node(true, cx, cy);
			nodes[1][i].genConnections(nodes[0]);
			cy += 40;
		}
		
		cy = y;
		cx += 50;
		
		for(int i=0; i<outputs; i++){
			nodes[2][i] = new Node(false, cx, cy);
			nodes[2][i].genConnections(nodes[1]);
			cy += 40;
		}
	}
	
	public Brain(int inputs, int hidden, int outputs, int x, int y, Node[][] nodes1){
		this.inputs = inputs;
		this.hidden = hidden;
		this.outputs = outputs;
		this.x = x;
		this.y = y;
		
		nodes[0] = new Node[inputs];
		nodes[1] = new Node[hidden];
		nodes[2] = new Node[outputs];
		
		int cx = x;
		int cy = y;
		
		for(int i=0; i<inputs; i++){
			nodes[0][i] = new Node(false, cx, cy);
			cy += 40;
		}
		
		cy = y;
		cx += 50;
		
		for(int i=0; i<hidden; i++){
			nodes[1][i] = new Node(true, cx, cy);
			nodes[1][i].setConnections(nodes1[1][i]);
			cy += 40;
		}
		
		cy = y;
		cx += 50;
		
		for(int i=0; i<outputs; i++){
			nodes[2][i] = new Node(false, cx, cy);
			nodes[2][i].setConnections(nodes1[2][i]);
			cy += 40;
		}
	}
	
	public Brain getMutation(){
		Random r = new Random();
		
		Node[][] nodes1 = new Node[3][];
		nodes1[0] = new Node[inputs];
		nodes1[1] = new Node[hidden];
		nodes1[2] = new Node[outputs];
		
		for(int i=0; i<nodes1.length; i++){
			for(int j=0; j<nodes1[i].length; j++){
				nodes1[i][j] = this.nodes[i][j].clone();
			}
		}
		
		int i = r.nextInt(3)+1;
		int index = r.nextInt(2)+1;
		

		for(int j=0; j<i; j++){
			int i1 = r.nextInt(nodes1[index].length);
			Node n = nodes1[index][i1];
			int cn = r.nextInt(n.cons.length);
			n.rerollCon(cn, r);
		}
		
		
		return new Brain(inputs, hidden, outputs, x, y, nodes1);
	}
	
	public void setInput(int index, float value){
		nodes[0][index].setValue(value);
	}
	
	public float getValue(int index){
		return nodes[2][index].getValue();
	}
	
	public void update(){
		for(int i=0; i<nodes[1].length; i++)
			nodes[1][i].calcValue(nodes[0]);
		for(int i=0; i<nodes[2].length; i++)
			nodes[2][i].calcValue(nodes[1]);
	}
	
	public Brain clone(){
		return new Brain(inputs, hidden, outputs, x, y, nodes);
	}
	
	public void drawBrain(Graphics2D g){

		for(int i=1; i<nodes.length; i++){
			for(int j=0; j<nodes[i].length; j++){
				if(nodes[i][j].isHovering()) nodes[i][j].drawConnections(g, nodes[i-1]);
			}
		}
		
		for(int i=0; i<nodes.length; i++){
			for(int j=0; j<nodes[i].length; j++){
				nodes[i][j].draw(g);
			}
		}
		
	
	}
	
	class Node{
		
		int x,y;
		
		float value = 0;
		
		boolean isHidden = false;
		
		float[] cons;
		
		public Node(boolean isHidden, int x, int y){
			this.isHidden = isHidden;
			this.x = x;
			this.y = y;
			this.cons = new float[10];
		}
		


		public void draw(Graphics2D g){
			g.setColor(Color.GRAY);
			g.fillOval(x, y, 30, 30);
			
			String text = value+"";
			
			if(text.length() > 5) text = text.substring(0, 5);
			
			g.setColor(Color.black);
			g.drawString(text, (x+15)-g.getFontMetrics().stringWidth(text)/2, y+20);
		}
		
		public void drawConnections(Graphics2D g, Node[] nodes){
			for(int i=0; i<cons.length; i++){
				int c = (int) ((cons[i]+1)*127);
				g.setColor(new Color(c, 0, 0));
				
				g.setStroke(new BasicStroke(3));			
				g.drawLine(x+15, y+15, nodes[i].getX()+15, nodes[i].getY()+15);
			}
		}
		
		public boolean isHovering(){
			Point2D loc = (Point2D)new Point(x+15, y+15);
			return Mouse.getMouseLocation().distance(loc)<=15;
		}
		
		public void genConnections(Node[] nodes){
			cons = new float[nodes.length];
			Random r = new Random();
			
			for(int i=0; i<nodes.length; i++)
				cons[i] = (r.nextFloat()*2)-1;
		}
		
		public void setConnections(Node n){
			cons = n.getConnections();
		}
		
		public float[] getConnections(){
			return cons.clone();
		}
		
		public void rerollCon(int index, Random r) {
			this.cons[index] = (r.nextFloat()*2)-1;
		}
		
		public void alterCon(int index, float amount){
			cons[index] += amount;
			
			if(cons[index] > 1) cons[index] -= 2;
			if(cons[index] < -1) cons[index ] += 2;
		}
		
		public float getValue(){
			return value;
		}
		
		public int getX(){
			return x;
		}
		
		public int getY(){
			return y;
		}
		
		public void calcValue(Node[] nodes){
			float result = 0;
			
			for(int i=0; i<cons.length; i++)
				result += nodes[i].getValue()*cons[i];
			
			value = isHidden?(float) (1/( 1 + Math.pow(Math.E,(-1*result)))):result;
		}
		
		public void setValue(float value){
			this.value = value;
		}		
		
		public Node clone(){
			Node clone = new Node(isHidden, x, y);
			clone.
			cons = 
					new float
					[cons.length];
			clone.cons = cons.clone();
			return clone;
		}
	}
}
