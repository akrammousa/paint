package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class ShapeImpl implements Shape {
	protected Point position;
	protected Map<String, Double> properties;
	protected Color color,fillColor;
	String type;

	public ShapeImpl() {
		position = new Point();
		position.x = 0;
		position.y = 0;
		color = new Color(0);
		fillColor = new Color(255,255,255);
		type = "Null";
	}
	public String getType() {
		return type;
	}

	@Override
	public void setPosition(Point position) {
		this.position = position;
	}

	@Override
	public Point getPosition() {
		return this.position;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		this.properties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		return this.properties;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void setFillColor(Color color) {
		this.fillColor = color;
	}

	@Override
	public Color getFillColor() {
		return this.fillColor;
	}

	@Override
	public void draw(Graphics canvas) {

	}
	/* create a deep clone of the shape */
	@Override
	public Object clone() throws CloneNotSupportedException{
		return null;

	}


}
