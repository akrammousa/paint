package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Color;
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

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs14.json.JSONArray;
import eg.edu.alexu.csd.oop.draw.cs14.json.JSONObject;
import eg.edu.alexu.csd.oop.draw.cs14.json.parser.JSONParser;

public class Model {
//	public DrawEngineImpl drawEngine = new DrawEngineImpl();
	public List<Shape> drawnShapes;
	public final Deque<Operation> undo = new LinkedList<Operation>();
	public final Deque<Operation> redo = new LinkedList<Operation>();
	public LinkedList<Class<? extends Shape>> supportedShapes
	= new LinkedList<Class<? extends Shape>>();
	
	public void saveXmL(String path) {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (final ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		final Document dom = builder.newDocument();
		// create the root element
		final Element rootEle = dom.createElement("shapes");
		// create the root element
		for (int i = 0; i < this.drawnShapes.size(); i++) {
			Element e = dom.createElement("name");
			final Shape temp = this.drawnShapes.get(i);
			// create data elements and place them under root
			String typeS = "Null";
			try {
				typeS = ((ShapeImpl)temp).getClass().getSimpleName();
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
						e1.printStackTrace();
					}
				}			
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
	
	public void saveJsOn(String path) {
		Shape[] shapes = new Shape[drawnShapes.size()];
		shapes = drawnShapes.toArray(shapes);
		final JSONObject allShapes = new JSONObject();
		final JSONArray shapesArray = new JSONArray();
		for (int i = 0; i < shapes.length; i++) {
			final JSONObject singleShape = new JSONObject();
			String typeS = "Null";
			try {
				typeS = ((ShapeImpl)shapes[i]).getClass().getSimpleName();
			} catch (final Exception e) {
				e.printStackTrace();
			}
			singleShape.put("Type", typeS);
			final JSONArray properties = new JSONArray();
			if (!typeS.equals("Null")) {
				try {
					properties.add(String.valueOf(shapes[i].getPosition().getX()));
					properties.add(String.valueOf(shapes[i].getPosition().getY()));
					properties.add(String.valueOf(shapes[i].getColor().getRGB()));
					properties.add(String.valueOf(shapes[i].getFillColor().getRGB()));
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
		try (FileWriter file = new FileWriter(path)) {
			file.write(allShapes.toJSONString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadXmL(String path) {
		File file = new File(path);
		InputStream inputStream;
		Reader reader = null;
		try {
			inputStream = new FileInputStream(file);
			try {
				reader = new InputStreamReader(inputStream, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
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
			this.drawnShapes.clear();
			final Element root = dom.getDocumentElement();
			final NodeList shapesList = root.getChildNodes();
			for (int i = 0; i < shapesList.getLength(); i++) {
				final org.w3c.dom.Node temp = shapesList.item(i);
				if (temp.getNodeName() == "name") {
					for (int j = 0; j < this.supportedShapes.size(); j++) {
						//System.out.println(model.supportedShapes.get(j).getSimpleName());
						if (this.supportedShapes.get(j).getSimpleName().equals(temp.getTextContent())) {	
				            // Create a new instance from the loaded class
				            Constructor constructor = null;
				            Shape shapeObject = null;
							try {
								constructor = this.supportedShapes.get(j).getConstructor();
								shapeObject = (Shape)constructor.newInstance();

								final org.w3c.dom.Node temp2 = temp.getNextSibling();
								final String properites = temp2.getTextContent();
								final String[] properitesArray = properites.split(",");
								final Point point = new Point();
								point.x = (int) Double.parseDouble(properitesArray[0]);
								point.y = (int) Double.parseDouble(properitesArray[1]);
								
								Method methodSetPosition = this.supportedShapes.get(j).getMethod("setPosition", Point.class);
								System.out.println("Invoked method name: " + methodSetPosition.getName());
								methodSetPosition.invoke(shapeObject, point);
								
								final Color color = new Color((int)Float.parseFloat(properitesArray[2]),
										(int)Float.parseFloat(properitesArray[3]), (int)Float.parseFloat(properitesArray[4]));
								final Color fillColor = new Color((int)Float.parseFloat(properitesArray[5]),
										(int)Float.parseFloat(properitesArray[6]), (int)Float.parseFloat(properitesArray[7]));
								
								Method methodSetColor = this.supportedShapes.get(j).getMethod("setColor", Color.class);
								System.out.println("Invoked method name: " + methodSetColor.getName());
								methodSetColor.invoke(shapeObject, color);

								Method methodSetFillColor = this.supportedShapes.get(j).getMethod("setFillColor", Color.class);
								System.out.println("Invoked method name: " + methodSetFillColor.getName());
								methodSetFillColor.invoke(shapeObject, fillColor);

								Method methodGetProperties = this.supportedShapes.get(j).getMethod("getProperties");
								System.out.println("Invoked method name: " + methodGetProperties.getName());
								final Map<String, Double> map = (Map<String, Double>) methodGetProperties.invoke(shapeObject);
											
								int counter = 8;
								for (String key : map.keySet()) {
									map.put(key, Double.parseDouble(properitesArray[counter++]));
								}
								
								Method methodSetProperties = this.supportedShapes.get(j).getMethod("setProperties", Map.class);
								System.out.println("Invoked method name: " + methodSetProperties.getName());
								methodSetProperties.invoke(shapeObject, map);
					
								this.drawnShapes.add(shapeObject);
								i = i + 1;
								break;

							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (temp.getTextContent().equals("Null")) {
							this.drawnShapes.add(null);
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

	public void loadJsOn(String path) {
		this.drawnShapes.clear();
		final JSONParser parser = new JSONParser();
		try {
			final Object obj = parser.parse(new FileReader(path));
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
					if (this.supportedShapes.get(j).getSimpleName().equals(type)) {
			            // Create a new instance from the loaded class
			            Constructor constructor = null;
						try {
							constructor = this.supportedShapes.get(j).getConstructor();
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
							
							Method methodGetProperties = this.supportedShapes.get(j).getMethod("getProperties");
							System.out.println("Invoked method name: " + methodGetProperties.getName());
							properties = (Map<String, Double>) methodGetProperties.invoke(loadedShape);
										
							int counter = 4;
							for (String key : properties.keySet()) {
								properties.put(key, Double.parseDouble((String) propertiesArray.get(counter++)));
							}
							
							if (!type.equals("Null")) {
								Method methodSetPosition = this.supportedShapes.get(j).getMethod("setPosition", Point.class);
								System.out.println("Invoked method name: " + methodSetPosition.getName());
								methodSetPosition.invoke(loadedShape, position);
								
								Method methodSetColor = this.supportedShapes.get(j).getMethod("setColor", Color.class);
								System.out.println("Invoked method name: " + methodSetColor.getName());
								methodSetColor.invoke(loadedShape, color);

								Method methodSetFillColor = this.supportedShapes.get(j).getMethod("setFillColor", Color.class);
								System.out.println("Invoked method name: " + methodSetFillColor.getName());
								methodSetFillColor.invoke(loadedShape, fillColor);
								
								Method methodSetProperties = this.supportedShapes.get(j).getMethod("setProperties", Map.class);
								System.out.println("Invoked method name: " + methodSetProperties.getName());
								methodSetProperties.invoke(loadedShape, properties);
								
								loadedShape.setPosition(position);
								loadedShape.setFillColor(fillColor);
								loadedShape.setColor(color);
								loadedShape.setProperties(properties);
							}
							this.drawnShapes.add(loadedShape);
							final Operation operation = new Operation("Add", loadedShape, null, this.drawnShapes.size() - 1);
							if (this.undo.size() >= 20) {
								this.undo.removeLast();
							}
							this.redo.clear();
							this.undo.push(operation);
							
							break;
							
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (type.equals("Null")) {
						this.drawnShapes.add(loadedShape);
						final Operation operation = new Operation("Add", loadedShape, null, this.drawnShapes.size() - 1);
						if (this.undo.size() >= 20) {
							this.undo.removeLast();
						}
						this.redo.clear();
						this.undo.push(operation);
						break;
					}
				}

			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void reflect(String path) {
		final String cN = path.substring(path.lastIndexOf('\\') + 1, path.length());
		path = path.substring(0, path.lastIndexOf('\\'));
    	File operatorFile = new File(path);

	    ClassLoader operatorsLoader = null;
		try {
			operatorsLoader = new URLClassLoader(new URL[] { operatorFile.toURI().toURL() });
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		File[] files = operatorFile.listFiles(new FilenameFilter() {
	        @Override public boolean accept(File dir, String name) {
	            return name.endsWith(".class") && name.equals(cN);
	        }
	    });
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
