package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Square extends ShapeImpl {
	public Square() {
		super();
		final Map<String, Double> prop = new HashMap<String, Double>();
		//prop.put("Type", 4.0);
		prop.put("Length", 0.0);
		setProperties(prop);
		super.type = "Square";

	}

	@Override
	public void draw(Graphics canvas) {
		final int length=getProperties().get("Length").intValue();

		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).fillRect((int) getPosition().getX(), (int) getPosition().getY(), length, length);

		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).drawRect((int) getPosition().getX(), (int) getPosition().getY(), length, length);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final Shape square = new Square();
		square.setColor(getColor());
		square.setFillColor(getFillColor());
		square.setPosition(getPosition());
		final Map<String, Double> newprop = new HashMap<String, Double>();
		for (final Map.Entry<String, Double> s: getProperties().entrySet()) {
			newprop.put(s.getKey(), s.getValue());
		}
		square.setProperties(newprop);
		return square;
	}

}

