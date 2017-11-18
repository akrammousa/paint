package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Canvas;

public class SingeltonCanvas {
	private static Canvas canvas = new Canvas();
	
	static Canvas getCanvas(){
		return canvas;
	}
}
