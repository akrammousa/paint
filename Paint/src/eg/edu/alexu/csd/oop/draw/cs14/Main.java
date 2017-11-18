package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final DrawEngineImpl o = new DrawEngineImpl();
		final Point point = new Point();
		final Color color = new Color(0);
		point.x=(int) 5.0;
		point.y=(int) 4.0;
		final Map<String, Double> con = new HashMap<String, Double>();
		con.put("Radius", 1.0);
		final Shape test = new Circle();
		test.setPosition(point);
		test.setColor(color);
		test.setFillColor(color);
		test.setProperties(con);
		o.addShape(test);

		final Map<String, Double> con2 = new HashMap<String, Double>();
		con2.put("Height", 2.0);
		con2.put("Width", 2.0);
		final Shape test2 = new Rectangle();
		test2.setPosition(point);
		test2.setColor(color);
		test2.setFillColor(color);
		test2.setProperties(con2);
		o.addShape(test2);


		final Map<String, Double> con3 = new HashMap<String, Double>();
		con3.put("Radius", 3.0);
		final Shape test3 = new Circle();
		test3.setPosition(point);
		test3.setColor(color);
		test3.setFillColor(color);
		test3.setProperties(con3);
		o.addShape(test3);


		final Map<String, Double> con4 = new HashMap<String, Double>();
		con4.put("Radius", 4.0);
		final Shape test4 = new Circle();
		test4.setPosition(point);
		test4.setColor(color);
		test4.setFillColor(color);
		test4.setProperties(con4);
		o.addShape(test4);




		System.out.println(o.getShapes().length);



		o.save("akram.XmL");
		
		o.load("akram.XmL");
		
		o.save("akram.XmL");
		
		System.out.println(o.getSupportedShapes().size());
		o.reflect("D:\\test\\CircleTwo.class");
		System.out.println("D:\\test\\CircleTwo.class");
		System.out.println(o.getSupportedShapes().size());
		
		System.out.println(o.getShapes().length);

	}

}
