package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Elipse extends ShapeImpl {

	public Elipse() {
		super();
		final Map<String, Double> prop = new HashMap<String, Double>();
		//prop.put("Type", 2.0);
		prop.put("A", 0.0);
		prop.put("B", 0.0);
		setProperties(prop);
		super.type = "Elipse";
	}

	@Override
	public void draw(Graphics canvas) {
		final int a=getProperties().get("A").intValue();
		final int b=getProperties().get("B").intValue();
		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).fillArc((int) getPosition().getX() - (a / 2), (int) getPosition().getY() - (b / 2), a, b, 0, 360);

		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).drawArc((int) getPosition().getX() - (a / 2), (int) getPosition().getY() - (b / 2), a, b, 0, 360);

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final Shape elipse = new Elipse();
		elipse.setColor(getColor());
		elipse.setFillColor(getFillColor());
		elipse.setPosition(getPosition());
		final Map<String, Double> newprop = new HashMap<String, Double>();
		for (final Map.Entry<String, Double> s: getProperties().entrySet()) {
			newprop.put(s.getKey(), s.getValue());
		}
		elipse.setProperties(newprop);
		return elipse;
	}

}
