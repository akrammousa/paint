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
	Model model = new Model();

	public DrawEngineImpl() {
		model.drawnShapes = new ArrayList<Shape>();
		model.supportedShapes.add(Circle.class);
		model.supportedShapes.add(Line.class);
		model.supportedShapes.add(Rectangle.class);
		model.supportedShapes.add(Square.class);
		model.supportedShapes.add(Triangle.class);
		model.supportedShapes.add(Elipse.class);
		
	}

	@Override
	public void refresh(Graphics canvas) {
		for (int i = 0; i < model.drawnShapes.size(); i++) {
			model.drawnShapes.get(i).draw(canvas);
		}
	}

	@Override
	public void addShape(Shape shape) {
		model.drawnShapes.add(shape);
		final Operation operation = new Operation("Add", shape, null, model.drawnShapes.size() - 1);
		if (model.undo.size() >= 20) {
			model.undo.removeLast();
		}
		model.redo.clear();
		model.undo.push(operation);
	}

	@Override
	public void removeShape(Shape shape) {
		if (!model.drawnShapes.isEmpty()) {
			final int index = model.drawnShapes.indexOf(shape);
			model.drawnShapes.remove(shape);
			final Operation operation = new Operation("Delete", shape, null, index);
			if (model.undo.size() >= 20) {
				model.undo.removeLast();
			}
			model.redo.clear();
			model.undo.push(operation);
		}
	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		final int indexOfOldShape = model.drawnShapes.indexOf(oldShape);
		model.drawnShapes.remove(indexOfOldShape);
		model.drawnShapes.add(indexOfOldShape, newShape);
		final Operation operation = new Operation("Update", oldShape, newShape, -1);
		if (model.undo.size() >= 20) {
			model.undo.removeLast();
		}
		model.redo.clear();
		model.undo.push(operation);
	}

	@Override
	public Shape[] getShapes() {
		final Shape[] allDrawnShapes = new Shape[model.drawnShapes.size()];
		return model.drawnShapes.toArray(allDrawnShapes);
	}

	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		return model.supportedShapes;
	}

	@Override
	public void undo() {
		if (model.undo.isEmpty()) {
			return;
		}
		popOverUndoAndRedoStacks(model.redo, model.undo.pop());
	}

	@Override
	public void redo() {
		if (model.redo.isEmpty()) {
			return;
		}
		popOverUndoAndRedoStacks(model.undo, model.redo.pop());
	}
	
	private void popOverUndoAndRedoStacks(Deque<Operation> stackToMoveTo, Operation lastOperation) {
		if (lastOperation.getOperation() == "Add") {
			final int index = lastOperation.getIndex();
			model.drawnShapes.remove(lastOperation.getOldShape());
			stackToMoveTo.push(new Operation("Delete", lastOperation.getOldShape(), null, index));
		} else if (lastOperation.getOperation() == "Delete") {
			model.drawnShapes.add(lastOperation.getIndex(), lastOperation.getOldShape());
			stackToMoveTo.push(new Operation("Add", lastOperation.getOldShape(), null, lastOperation.getIndex()));
		} else if (lastOperation.getOperation() == "Update") {
			final int indexOfOldShape = model.drawnShapes.indexOf(lastOperation.getNewShape());
			model.drawnShapes.remove(lastOperation.getNewShape());
			model.drawnShapes.add(indexOfOldShape, lastOperation.getOldShape());
			stackToMoveTo.push(new Operation("Update", lastOperation.getNewShape(), lastOperation.getOldShape(), -1));
		}
	}

	@Override
	public void save(String path) {
		final int dotIndex = path.lastIndexOf('.');
		final String extension = path.substring(dotIndex + 1);
		if (extension.equals("JsOn")) {
			model.saveJsOn(path.replace("\\", "\\\\"));
		} else if (extension.equals("XmL")){
			model.saveXmL(path.replace("\\", "\\\\"));
		}
	}

	@Override
	public void load(String path) {
		final int dotIndex = path.lastIndexOf('.');
		final String extension = path.substring(dotIndex + 1);
		if (extension.equals("XmL")) {
			model.loadXmL(path.replace("\\", "\\\\"));
		}
		else if (extension.equals("JsOn")){
			model.loadJsOn(path.replace("\\", "\\\\"));
		}
		model.undo.clear();
		model.redo.clear();
	}

	public void reflect(String path) {
		if (path.charAt(path.length() - 1) == 's') {
		   model.reflect(path.replace("\\", "\\\\"));
		} else if (path.charAt(path.length() - 1) == 'r') {
		   model.reflectJarFile(path.replace("\\", "\\\\"));
		}
		//supportedShapes = drawerImpl.getSupportedShapes();
	}
}