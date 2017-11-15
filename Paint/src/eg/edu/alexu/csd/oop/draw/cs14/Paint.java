package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;

public class Paint {

	public static JFrame frame;
	private JTextField radiusTextField;

	DrawingEngine drawer = SingeltonDrawingEngine.getDrawingEnginInstance();
	Shape currentShape = null;
	InputNode inputNode = new InputNode();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final Paint window = new Paint();
					window.frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Paint() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1413, 812);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final JPanel drawingCanvas = new JPanel();
		drawingCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(currentShape != null){
					currentShape.setPosition(arg0.getPoint());
					final Map<String , Double> properties = currentShape.getProperties();
					int counter = 0;
					for (final String key: properties.keySet()){properties.put(key, Double.parseDouble(inputNode.inputArray.get(counter++).getText()));
					}
					currentShape.setProperties(properties);
					//currentShape.draw(drawingCanvas.getGraphics());
					drawer.refresh(drawingCanvas.getGraphics());
					inputNode.clear();
					currentShape = null;

				}

			}
		});
		drawingCanvas.setBackground(Color.WHITE);
		drawingCanvas.setBounds(10, 156, 1209, 599);
		frame.getContentPane().add(drawingCanvas);

		final JButton circleButton = new JButton("Circle");
		circleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//final String temp = circleButton;
				final Shape shape = ShapeFactory.getShape("Circle");
				currentShape=shape;
				inputNode.initializeInputNodes(currentShape.getProperties());
				drawer.addShape(shape);
			}
		});
		circleButton.setBounds(31, 13, 54, 52);
		frame.getContentPane().add(circleButton);

		final JButton ellipsButton = new JButton("Ellipse");
		ellipsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Shape shape = ShapeFactory.getShape("Elipse");
				currentShape=shape;
				inputNode.initializeInputNodes(currentShape.getProperties());
				drawer.addShape(shape);
			}
		});
		ellipsButton.setBounds(97, 13, 54, 52);
		frame.getContentPane().add(ellipsButton);

		final JButton squareButton = new JButton("Square");
		squareButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Shape shape = ShapeFactory.getShape("Square");
				currentShape=shape;
				inputNode.initializeInputNodes(currentShape.getProperties());
				drawer.addShape(shape);
			}
		}
				);
		squareButton.setBounds(163, 13, 54, 52);
		frame.getContentPane().add(squareButton);

		final JButton rectangleButton = new JButton("Rectangle");
		rectangleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final Shape shape = ShapeFactory.getShape("Rectangle");
				currentShape=shape;
				inputNode.initializeInputNodes(currentShape.getProperties());
				drawer.addShape(shape);
			}
		});
		rectangleButton.setBounds(229, 13, 54, 52);
		frame.getContentPane().add(rectangleButton);

		final JButton lineButton = new JButton("Line");
		lineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Shape shape = ShapeFactory.getShape("Line");
				currentShape=shape;
				inputNode.initializeInputNodes(currentShape.getProperties());
				drawer.addShape(shape);
			}
		});
		lineButton.setBounds(295, 13, 54, 52);
		frame.getContentPane().add(lineButton);

		final JButton triangleButton = new JButton("Triangle");
		triangleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Shape shape = ShapeFactory.getShape("Triangle");
				currentShape=shape;
				inputNode.initializeInputNodes(currentShape.getProperties());
				drawer.addShape(shape);
			}
		});
		triangleButton.setBounds(361, 13, 54, 52);
		frame.getContentPane().add(triangleButton);

		final JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(648, 508, 106, 22);
		frame.getContentPane().add(editorPane);

		final JButton btnNewButton_2 = new JButton("Color");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JColorChooser cr = new JColorChooser();
				final Color outline = cr.showDialog(null, "Enter outline color", Color.BLACK);
				currentShape.setColor(outline);
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.ITALIC, 20));
		btnNewButton_2.setBounds(613, 10, 141, 55);
		frame.getContentPane().add(btnNewButton_2);

		final JButton btnNewButton_3 = new JButton("Fill Color");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JColorChooser cr = new JColorChooser();
				final Color fillColor = cr.showDialog(null, "Enter outline color", Color.BLACK);
				currentShape.setFillColor(fillColor);
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_3.setBounds(613, 80, 141, 52);
		frame.getContentPane().add(btnNewButton_3);

		final JButton btnNewButton_4 = new JButton("Undo");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawer.undo();
				drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
				drawer.refresh(drawingCanvas.getGraphics());
			}
		});
		btnNewButton_4.setBounds(768, 13, 54, 52);
		frame.getContentPane().add(btnNewButton_4);

		final JButton btnRedo = new JButton("Redo");
		btnRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawer.redo();
				drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
				drawer.refresh(drawingCanvas.getGraphics());

			}
		});
		btnRedo.setBounds(768, 80, 54, 52);
		frame.getContentPane().add(btnRedo);

		final JButton btnNewButton_5 = new JButton("Save");
		btnNewButton_5.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_5.setBounds(1225, 622, 133, 52);
		frame.getContentPane().add(btnNewButton_5);

		final JButton btnLoad = new JButton("Load");
		btnLoad.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnLoad.setBounds(1225, 687, 133, 52);
		frame.getContentPane().add(btnLoad);

		final JComboBox comboBox = new JComboBox();
		comboBox.setBounds(427, 13, 169, 52);
		frame.getContentPane().add(comboBox);

		final JButton btnNewButton = new JButton("Delete");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawer.removeShape(currentShape);
				drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
				drawer.refresh(drawingCanvas.getGraphics());

			}
		});
		btnNewButton.setBounds(842, 37, 97, 84);
		frame.getContentPane().add(btnNewButton);

	}
}
