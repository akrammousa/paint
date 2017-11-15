package eg.edu.alexu.csd.oop.draw.cs14;

import java.lang.reflect.Constructor;
import java.util.List;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;

public class ShapeFactory {
	static DrawingEngine drawer = SingeltonDrawingEngine.getDrawingEnginInstance();
	static List<Class<? extends Shape>> supportedClasses = drawer.getSupportedShapes();

	public static Shape getShape(String typeOfShape) {
		for (int i = 0; i < supportedClasses.size(); i++) {
			if (typeOfShape
					.equals(supportedClasses
							.get(i)
							.getSimpleName())) {
				try {
					final Constructor<? extends Shape> constructor = supportedClasses.get(i).getConstructor();
					final Shape instance = constructor.newInstance();
					return instance;
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		throw new RuntimeException("type is not exist in the factory");
	}
}
