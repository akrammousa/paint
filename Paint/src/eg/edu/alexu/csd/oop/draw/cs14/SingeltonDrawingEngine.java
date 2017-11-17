package eg.edu.alexu.csd.oop.draw.cs14;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;

public class SingeltonDrawingEngine {
	static DrawEngineImpl singeltonObject = new DrawEngineImpl();

	static DrawEngineImpl getDrawingEnginInstance() {
		return singeltonObject;
	}
}
