package Main;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import API.MainClass;

public class Main extends MainClass{
	
	public static void main(String[] args){
		new Main().openWindow(false, false);
	}

	public Main() {
		super(1280, 900, "Neur");  
	}

	@Override
	protected void initialize() {
		manager.startWith(new MainState());
	}

}
