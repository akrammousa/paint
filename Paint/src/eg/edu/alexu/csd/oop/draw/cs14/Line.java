package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Line extends ShapeImpl {


	public Line() {
		super();
		final Map<String, Double> prop = new HashMap<>();
		//prop.put("Type", 3.0);
		prop.put("secondPointX", 0.0);
		prop.put("secondPointY", 0.0);
		setProperties(prop);
		super.type = "Line";

	}

	@Override
	public void draw(Graphics canvas) {
		final int sPX = getProperties().get("secondPointX").intValue();
		final int sPY = getProperties().get("secondPointY").intValue();
		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).drawLine((int)getPosition().getX(), (int)getPosition().getY(), sPX, sPY);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		final Shape line = new Line();
		line.setColor(getColor());
		line.setFillColor(getFillColor());
		line.setPosition(getPosition());
		final Map<String, Double> newprop = new HashMap<>();
		for (final Map.Entry<String, Double> s: getProperties().entrySet()) {
			newprop.put(s.getKey(), s.getValue());
		}
		line.setProperties(newprop);
		return line;
	}
}
