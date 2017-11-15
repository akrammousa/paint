package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs14.json.JSONArray;
import eg.edu.alexu.csd.oop.draw.cs14.json.JSONObject;
import eg.edu.alexu.csd.oop.draw.cs14.json.parser.JSONParser;

public class DrawEngineImpl extends ClassLoader implements DrawingEngine {

	private final List<Shape> drawnShapes;
	private final Deque<Operation> undo = new LinkedList<Operation>();
	private final Deque<Operation> redo = new LinkedList<Operation>();
	private LinkedList<Class<? extends Shape>> supportedShapes
	= new LinkedList<Class<? extends Shape>>();

	public DrawEngineImpl() {
		drawnShapes = new ArrayList<Shape>();
		supportedShapes.add(Circle.class);
		supportedShapes.add(Line.class);
		supportedShapes.add(Rectangle.class);
		supportedShapes.add(Square.class);
		supportedShapes.add(Triangle.class);
		supportedShapes.add(Elipse.class);
		
	}

	@Override
	public void refresh(Graphics canvas) {
		// TODO Auto-generated method stub
		for (int i = 0; i < drawnShapes.size(); i++) {
			drawnShapes.get(i).draw(canvas);
		}
	}

	@Override
	public void addShape(Shape shape) {
		// TODO Auto-generated method stub
		this.drawnShapes.add(shape);
		final Operation operation = new Operation("Add", shape, null, this.drawnShapes.size() - 1);
		if (undo.size() >= 20) {
			undo.removeLast();
		}
		this.redo.clear();
		this.undo.push(operation);
	}

	@Override
	public void removeShape(Shape shape) {
		if (!this.drawnShapes.isEmpty()) {
			final int index = this.drawnShapes.indexOf(shape);
			this.drawnShapes.remove(shape);
			final Operation operation = new Operation("Delete", shape, null, index);
			if (undo.size() >= 20) {
				undo.removeLast();
			}
			this.redo.clear();
			this.undo.push(operation);
		}
	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		// TODO Auto-generated method stub
		final int indexOfOldShape = this.drawnShapes.indexOf(oldShape);
		this.drawnShapes.remove(indexOfOldShape);
		this.drawnShapes.add(indexOfOldShape, newShape);
		final Operation operation = new Operation("Update", oldShape, newShape, -1);
		if (undo.size() >= 20) {
			undo.removeLast();
		}
		this.redo.clear();
		this.undo.push(operation);
	}

	@Override
	public Shape[] getShapes() {
		// TODO Auto-generated method stub
		final Shape[] allDrawnShapes = new Shape[this.drawnShapes.size()];
		return this.drawnShapes.toArray(allDrawnShapes);
	}

	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		// TODO Auto-generated method stub
		
		return supportedShapes;
	}

	@Override
	public void undo() {
		if (undo.isEmpty()) {
			return;
		}

		final Operation lastOperation = this.undo.pop();
		if (lastOperation.getOperation() == "Add") {
			final int index = lastOperation.getIndex();
			this.drawnShapes.remove(lastOperation.getOldShape());
			redo.push(new Operation("Delete", lastOperation.getOldShape(), null, index));
		} else if (lastOperation.getOperation() == "Delete") {
			this.drawnShapes.add(lastOperation.getIndex(), lastOperation.getOldShape());
			redo.push(new Operation("Add", lastOperation.getOldShape(), null, lastOperation.getIndex()));
		} else if (lastOperation.getOperation() == "Update") {
			final int indexOfOldShape = this.drawnShapes.indexOf(lastOperation.getNewShape());
			this.drawnShapes.remove(lastOperation.getNewShape());
			this.drawnShapes.add(indexOfOldShape, lastOperation.getOldShape());
			redo.push(new Operation("Update", lastOperation.getNewShape(), lastOperation.getOldShape(), -1));
		}

	}

	@Override
	public void redo() {
		if (redo.isEmpty()) {
			return;
		}
		final Operation lastOperation = this.redo.pop();
		if (lastOperation.getOperation() == "Add") {
			final int index = lastOperation.getIndex();
			this.drawnShapes.remove(lastOperation.getOldShape());
			undo.push(new Operation("Delete", lastOperation.getOldShape(), null, index));
		} else if (lastOperation.getOperation() == "Delete") {
			this.drawnShapes.add(lastOperation.getIndex(), lastOperation.getOldShape());
			undo.push(new Operation("Add", lastOperation.getOldShape(), null, lastOperation.getIndex()));
		} else if (lastOperation.getOperation() == "Update") {
			final int indexOfOldShape = this.drawnShapes.indexOf(lastOperation.getNewShape());
			this.drawnShapes.remove(lastOperation.getNewShape());
			this.drawnShapes.add(indexOfOldShape, lastOperation.getOldShape());
			undo.push(new Operation("Update", lastOperation.getNewShape(), lastOperation.getOldShape(), -1));
		}

	}

	@Override
	public void save(String path) {
		final int dotIndex = path.lastIndexOf('.');
		final String extension = path.substring(dotIndex + 1);
		if (extension.equals("JsOn")) {
			final Shape[] shapes = this.getShapes();

			final JSONObject allShapes = new JSONObject();
			final JSONArray shapesArray = new JSONArray();

			for (int i = 0; i < shapes.length; i++) {
				final JSONObject singleShape = new JSONObject();
				String typeS = "Null";
				try {
					typeS = ((ShapeImpl)shapes[i]).getType();
				} catch (final Exception e) {
					typeS = "Null";
				}
				
				singleShape.put("Type", typeS);

				final JSONArray properties = new JSONArray();
				if (!typeS.equals("Null")) {
					try {
						properties.add(String.valueOf(shapes[i].getPosition().getX()));
					} catch (final Exception e) {

					}
					try {
						properties.add(String.valueOf(shapes[i].getPosition().getY()));
					} catch (final Exception e) {

					}
					try {
						properties.add(String.valueOf(shapes[i].getColor().getRGB()));
					} catch (final Exception e) {

					}
					try {
						properties.add(String.valueOf(shapes[i].getFillColor().getRGB()));
					} catch (final Exception e) {

					}
					try {
						for (final Map.Entry<String, Double> s : shapes[i].getProperties().entrySet()) {
							properties.add(s.getValue().toString());
						}
					} catch (final Exception e) {

					}

				}
				singleShape.put("Properties", properties);
				shapesArray.add(singleShape);
			}
			allShapes.put("ShapesArray", shapesArray);

			// try-with-resources statement based on post comment below :)
			try (FileWriter file = new FileWriter(path)) {
				file.write(allShapes.toJSONString());
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			try {
				final String name = null;
				builder = dbf.newDocumentBuilder();
		} catch (final ParserConfigurationException pce) {
			//System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		}
				final Document dom = builder.newDocument();
				// create the root element
				final Element rootEle = dom.createElement("shapes");
				// create the root element
				for (int i = 0; i < drawnShapes.size(); i++) {
					Element e = dom.createElement("name");
					final Shape temp = drawnShapes.get(i);
					// create data elements and place them under root
					String typeS = "Null";
					try {
						typeS = ((ShapeImpl)temp).getType();
					} catch (final Exception e1) {
						typeS = "Null";
					}

					e.appendChild(dom.createTextNode(typeS));
					rootEle.appendChild(e);
					String properites = "";
					if (temp != null && !typeS.equals("Null")) {
					
						properites = properites + String.valueOf(temp.getPosition().getX()) + ",";

						properites = properites + String.valueOf(temp.getPosition().getY()) + ",";
					
						properites = properites + temp.getColor().getRed() + ",";
					
						properites = properites + temp.getColor().getGreen() + ",";
					
						properites = properites + temp.getColor().getBlue() + ",";
					
						properites = properites + temp.getFillColor().getRed() + ",";
					
						properites = properites + temp.getFillColor().getGreen() + ",";
					
						properites = properites + temp.getFillColor().getBlue() + ",";
					
						for (final Map.Entry<String, Double> s : temp.getProperties().entrySet()) {
							try {
								properites = properites + String.valueOf(s.getValue()) + ",";
							} catch (final Exception e1) {

							}
						}
						System.out.println(properites);
					
					}

					e = dom.createElement("properties");
					e.appendChild(dom.createTextNode(properites));
					rootEle.appendChild(e);
				}

				dom.appendChild(rootEle);

				try {
					final Transformer tr = TransformerFactory.newInstance().newTransformer();
					tr.setOutputProperty(OutputKeys.METHOD,"xml");
					tr.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
					tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
					tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(path)));

				} catch (final TransformerException te) {
					System.out.println(te.getMessage());
				} catch (final IOException ioe) {
					System.out.println(ioe.getMessage());
				}
			
		}
	}

	@Override
	public void load(String path) {



		final int dotIndex = path.lastIndexOf('.');
		final String extension = path.substring(dotIndex + 1);
		if (extension.equals("XmL")) {


			File file = new File(path);
			InputStream inputStream;
			Reader reader = null;
			try {
				inputStream = new FileInputStream(file);
				try {
					reader = new InputStreamReader(inputStream, "ISO-8859-1");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			InputSource is = new InputSource(reader);
			is.setEncoding("ISO-8859-1");
			// Make an instance of the DocumentBuilderFactory
			try {
				// use the factory to take an instance of the document builder
				final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				final DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document dom = db.parse(is);

				// parse using the builder to get the DOM mapping of the
				// XML file

				
				//System.out.println(drawnShapes.size());
				
				
				this.drawnShapes.clear();
				//System.out.println(drawnShapes.size());

	

				final Element root = dom.getDocumentElement();
				final NodeList shapesList = root.getChildNodes();

				for (int i = 0; i < shapesList.getLength(); i++) {
					final org.w3c.dom.Node temp = shapesList.item(i);
					if (temp.getNodeName() == "name") {
						for (int j = 0; j < this.supportedShapes.size(); j++) {
							//System.out.println(this.supportedShapes.get(j).getSimpleName());
							if (this.supportedShapes.get(j).getSimpleName().equals(temp.getTextContent())) {	
					            // Create a new instance from the loaded class
					            Constructor constructor = null;
					            Shape shapeObject = null;
								try {
									constructor = this.getSupportedShapes().get(j).getConstructor();
									shapeObject = (Shape)constructor.newInstance();
									
					
									final org.w3c.dom.Node temp2 = temp.getNextSibling();
									final String properites = temp2.getTextContent();
									final String[] properitesArray = properites.split(",");
									final Point point = new Point();
									point.x = (int) Double.parseDouble(properitesArray[0]);
									point.y = (int) Double.parseDouble(properitesArray[1]);
									
									Method methodSetPosition = this.getSupportedShapes().get(j).getMethod("setPosition", Point.class);
									System.out.println("Invoked method name: " + methodSetPosition.getName());
									methodSetPosition.invoke(shapeObject, point);
									
									final Color color = new Color(Float.parseFloat(properitesArray[2]),
											Float.parseFloat(properitesArray[3]), Float.parseFloat(properitesArray[4]));
									final Color fillColor = new Color(Float.parseFloat(properitesArray[5]),
											Float.parseFloat(properitesArray[6]), Float.parseFloat(properitesArray[7]));
									
									Method methodSetColor = this.getSupportedShapes().get(j).getMethod("setColor", Color.class);
									System.out.println("Invoked method name: " + methodSetColor.getName());
									methodSetColor.invoke(shapeObject, color);
	
									Method methodSetFillColor = this.getSupportedShapes().get(j).getMethod("setFillColor", Color.class);
									System.out.println("Invoked method name: " + methodSetFillColor.getName());
									methodSetFillColor.invoke(shapeObject, fillColor);
	
									Method methodGetProperties = this.getSupportedShapes().get(j).getMethod("getProperties");
									System.out.println("Invoked method name: " + methodGetProperties.getName());
									final Map<String, Double> map = (Map<String, Double>) methodGetProperties.invoke(shapeObject);
												
									int counter = 8;
									for (String key : map.keySet()) {
										map.put(key, Double.parseDouble(properitesArray[counter++]));
									}
									
									
									Method methodSetProperties = this.getSupportedShapes().get(j).getMethod("setProperties", Map.class);
									System.out.println("Invoked method name: " + methodSetProperties.getName());
									methodSetProperties.invoke(shapeObject, map);
						
									drawnShapes.add(shapeObject);
									i = i + 1;
									break;
									
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else if (temp.getTextContent().equals("Null")) {
								drawnShapes.add(null);
								i = i + 1;
								break;
							}
						}

					}
				}

			} catch (final ParserConfigurationException pce) {
				System.out.println(pce.getMessage());
			} catch (final SAXException se) {
				System.out.println(se.getMessage());
			} catch (final IOException ioe) {
				System.err.println(ioe.getMessage());
			} catch (Exception e) {
				System.out.println("WRONG LAST OF LOAD XML");
			}

		}
		else {
			this.drawnShapes.clear();

			final JSONParser parser = new JSONParser();

			try {

				final Object obj = parser.parse(new FileReader(path));
				// System.out.println(obj);

				final JSONObject jsonObject = (JSONObject) obj;

				final JSONArray shapesArray = (JSONArray) jsonObject.get("ShapesArray");

				for (int i = 0; i < shapesArray.size(); i++) {
					final JSONObject singleShape = (JSONObject) shapesArray.get(i);
					final String type = (String) singleShape.get("Type");
					// System.out.println(type);
					final Point position = new Point();
					Color color = new Color(0);
					Color fillColor = new Color(0);
					Map<String, Double> properties;
					Shape loadedShape = null;
					
					for (int j = 0; j < this.supportedShapes.size(); j++) {
						//System.out.println(this.supportedShapes.get(j).getSimpleName());
						if (this.supportedShapes.get(j).getSimpleName().equals(type)) {
				            // Create a new instance from the loaded class
				            Constructor constructor = null;
							try {
								constructor = this.getSupportedShapes().get(j).getConstructor();
								loadedShape = (Shape)constructor.newInstance();
								
								final JSONArray propertiesArray = (JSONArray) singleShape.get("Properties");
								String val = (String) propertiesArray.get(0);
								position.x = (int) Double.parseDouble(val);
								val = (String) propertiesArray.get(1);
								position.y = (int) Double.parseDouble(val);

								val = (String) propertiesArray.get(2);
								color = Color.decode(val);
								val = (String) propertiesArray.get(3);
								fillColor = Color.decode(val);
								
								Method methodGetProperties = this.getSupportedShapes().get(j).getMethod("getProperties");
								System.out.println("Invoked method name: " + methodGetProperties.getName());
								properties = (Map<String, Double>) methodGetProperties.invoke(loadedShape);
											
								int counter = 4;
								for (String key : properties.keySet()) {
									properties.put(key, Double.parseDouble((String) propertiesArray.get(counter++)));
								}
								
								if (!type.equals("Null")) {
									Method methodSetPosition = this.getSupportedShapes().get(j).getMethod("setPosition", Point.class);
									System.out.println("Invoked method name: " + methodSetPosition.getName());
									methodSetPosition.invoke(loadedShape, position);
									
									Method methodSetColor = this.getSupportedShapes().get(j).getMethod("setColor", Color.class);
									System.out.println("Invoked method name: " + methodSetColor.getName());
									methodSetColor.invoke(loadedShape, color);

									Method methodSetFillColor = this.getSupportedShapes().get(j).getMethod("setFillColor", Color.class);
									System.out.println("Invoked method name: " + methodSetFillColor.getName());
									methodSetFillColor.invoke(loadedShape, fillColor);
									
									Method methodSetProperties = this.getSupportedShapes().get(j).getMethod("setProperties", Map.class);
									System.out.println("Invoked method name: " + methodSetProperties.getName());
									methodSetProperties.invoke(loadedShape, properties);
									
									loadedShape.setPosition(position);
									loadedShape.setFillColor(fillColor);
									loadedShape.setColor(color);
									loadedShape.setProperties(properties);
								}

								this.addShape(loadedShape);
								break;
								
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (type.equals("Null")) {
							this.addShape(loadedShape);
							break;
						}
					}
					// System.out.println("in");
					
				}

			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		this.undo.clear();
		this.redo.clear();
	}
	
	public void reflect(String path) {
		final String cN = path.substring(path.lastIndexOf('\\') + 1, path.length());
		path = path.substring(0, path.lastIndexOf('\\'));
    	File operatorFile = new File(path);

	    ClassLoader operatorsLoader = null;
		try {
			operatorsLoader = new URLClassLoader(new URL[] { operatorFile.toURI().toURL() });
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File[] files = operatorFile.listFiles(new FilenameFilter() {
	        @Override public boolean accept(File dir, String name) {
	        	//System.out.println(cN);
	            return name.endsWith(".class") && name.equals(cN);
	        }
	    });
		//System.out.println(files.length);
		if (files.length == 0) {
			throw new RuntimeException("File is not found in the destination or not supported format.");
		}
	    ArrayList<Class> operators = new ArrayList<>();
	    for (File file : files) {
	        String className = file.getName().substring(0, file.getName().length() - 6);
	        System.out.println(className);
	        try {
				Class<? extends Shape> clazz = (Class<? extends Shape>) operatorsLoader.loadClass(className);
				this.supportedShapes.add(clazz);
		        System.out.println(clazz.getName());
        
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }

	}
	
	public void reflectJarFile(String path) {
		ArrayList<Class> classes = new ArrayList();
		String jarName = new String(path);
		try {
			JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
			File myJar = new File(jarName);
			URL url = myJar.toURI().toURL();
		
			Class[] parameters = new Class[]{URL.class};
			URLClassLoader sysLoader =(URLClassLoader)ClassLoader.getSystemClassLoader();
			Class sysClass = URLClassLoader.class;
			Method method = sysClass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[]{url});
			
			JarEntry jarEntry;
			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if (jarEntry.getName().endsWith(".class")) {
					System.out.println(jarEntry.getName().replaceAll("/", "\\."));
					String name = jarEntry.getName().replaceAll("/", "\\.").replace(".class", "");
					Constructor cs = ClassLoader.getSystemClassLoader().loadClass(name).getConstructor();
					Object instance = cs.newInstance();
					Method test = ClassLoader.getSystemClassLoader().loadClass(name).getMethod("test");
					this.supportedShapes.add((Class<? extends Shape>) ClassLoader.getSystemClassLoader()
							.loadClass(name));
					test.invoke(instance);
				}
		}
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}
}