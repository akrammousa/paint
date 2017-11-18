package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Triangle extends ShapeImpl {


	public Triangle() {
		super();
		final Map<String, Double> prop = new HashMap<String, Double>();
		//prop.put("Type", 6.0);
		prop.put("secondPointX", 0.0);
		prop.put("secondPointY", 0.0);
		prop.put("thirdPointX", 0.0);
		prop.put("thirdPointY", 0.0);
		setProperties(prop);
		super.type = "Triangle";

	}

	@Override
	public void draw(Graphics canvas) {
		final int sPX = getProperties().get("secondPointX").intValue();
		final int sPY = getProperties().get("secondPointY").intValue();
		final int thPX = getProperties().get("thirdPointX").intValue();
		final int thPY = getProperties().get("thirdPointY").intValue();
		int[] xPoints = { (int)super.getPosition().getX(), sPX, thPX};
		int[] yPoints = {(int)super.getPosition().getY(), sPY, thPY};
		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).fillPolygon(xPoints, yPoints, 3);
		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).drawPolygon(xPoints, yPoints, 3);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final Shape triangle = new Triangle();
		triangle.setColor(getColor());
		triangle.setFillColor(getFillColor());
		triangle.setPosition(getPosition());
		final Map<String, Double> newprop = new HashMap<>();
		for (final Map.Entry<String, Double> s: getProperties().entrySet()) {
			newprop.put(s.getKey().toString(), (Double)s.getValue());
		}
		triangle.setProperties(newprop);
		return triangle;
	}
}
