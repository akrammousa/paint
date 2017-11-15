package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Rectangle extends ShapeImpl {
	public Rectangle() {
		super();
		final Map<String, Double> prop = new HashMap<String, Double>();
		//prop.put("Type", 5.0);
		prop.put("Height", 0.0);
		prop.put("Width", 0.0);
		setProperties(prop);
		super.type = "Rectangle";
	}

	@Override
	public void draw(Graphics canvas) {
		final int height=getProperties().get("Height").intValue();
		final int width=getProperties().get("Width").intValue();

		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).fillRect((int) getPosition().getX(), (int) getPosition().getY(), width, height);

		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).drawRect((int) getPosition().getX(), (int) getPosition().getY(), width, height);

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final Shape rectangle = new Rectangle();
		rectangle.setColor(getColor());
		rectangle.setFillColor(getFillColor());
		rectangle.setPosition(getPosition());
		final Map<String, Double> newprop = new HashMap<String, Double>();
		for (final Map.Entry<String, Double> s: getProperties().entrySet()) {
			newprop.put(s.getKey(),s.getValue());
		}
		rectangle.setProperties(newprop);
		return rectangle;
	}
}
