package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Circle extends ShapeImpl {

	public Circle() {
		super();
		final Map<String, Double> prop = new HashMap<String, Double>();
		//prop.put("Type", 1.0);
		prop.put("Radius", 0.0);
		setProperties(prop);
		super.type = "Circle";
	}

	@Override
	public void draw(Graphics canvas) {
		final int r = getProperties().get("Radius").intValue();
		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).fillOval((int)getPosition().getX() - (r / 2),(int) getPosition().getY() - (r / 2),r,r);
		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).drawOval((int) getPosition().getX() - (r / 2), (int) getPosition().getY() - (r / 2), r, r);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final Shape circle = new Circle();
		circle.setColor(getColor());
		circle.setFillColor(getFillColor());
		circle.setPosition(getPosition());
		final Map<String, Double> newprop = new HashMap<String, Double>();
		for (final Map.Entry<String, Double> s: getProperties().entrySet()) {
			newprop.put(s.getKey(), s.getValue());
		}
		circle.setProperties(newprop);
		return circle;
	}
}
