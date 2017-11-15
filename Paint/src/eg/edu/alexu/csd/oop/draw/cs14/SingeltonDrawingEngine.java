package eg.edu.alexu.csd.oop.draw.cs14;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;

public class SingeltonDrawingEngine {
	static DrawingEngine singeltonObject = new DrawEngineImpl();

	static DrawingEngine getDrawingEnginInstance() {
		return singeltonObject;
	}
}
