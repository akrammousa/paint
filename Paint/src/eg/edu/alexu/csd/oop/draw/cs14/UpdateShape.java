package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import eg.edu.alexu.csd.oop.draw.Shape;

public class UpdateShape {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	DrawEngineImpl drawerImpl = SingeltonDrawingEngine.getDrawingEnginInstance();
	private static Shape shapeToBeEdited;
	private static Shape oldShape;
	private final ArrayList<JTextField> properties = new ArrayList<JTextField>();
	private final ArrayList<JLabel> labels = new ArrayList<JLabel>();
	Map<String, Double> propertiesMap = new HashMap<String, Double>();
	private final Canvas drawingCanvas = SingeltonCanvas.getCanvas();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final UpdateShape window = new UpdateShape();
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
	public UpdateShape() {
		initialize();
	}

	/**
	 * Initialize the shape to be edited.
	 */
	static void initialieTheShapeToBeEdited(Shape oldShapeS, Shape shapedToBeEdited) {
		shapeToBeEdited = shapedToBeEdited;
		oldShape = oldShapeS;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 250, 500);
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		//		try {
		//			((Shape)shapeToBeEdited.clone()).draw(canvas.getGraphics());
		//		} catch (CloneNotSupportedException e1) {
		//			// TODO Auto-generated catch block
		//			e1.printStackTrace();
		//		}

		final JButton btnColor = new JButton();
		btnColor.setIcon(new ImageIcon("color.png"));
		btnColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeToBeEdited.setColor(JColorChooser.showDialog(null, "Choose the border Color",
						shapeToBeEdited.getColor()));
			}
		});
		btnColor.setBounds(32, 23, 163, 32);
		frame.getContentPane().add(btnColor);

		final JButton btnFillColor = new JButton();
		btnFillColor.setIcon(new ImageIcon("fillcolor.png"));
		btnFillColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeToBeEdited.setFillColor(JColorChooser.showDialog(null, "Choose the border Color",
						shapeToBeEdited.getFillColor()));
			}
		});
		btnFillColor.setBounds(32, 66, 163, 32);
		frame.getContentPane().add(btnFillColor);

		final JLabel posX = new JLabel(" X position");
		posX.setBounds(32, 123, 163, 23);
		frame.getContentPane().add(posX);

		final JLabel posY = new JLabel(" Y position");
		posY.setBounds(31, 156, 164, 23);
		frame.getContentPane().add(posY);

		textField = new JTextField();
		textField.setBounds(96, 120, 99, 26);
		textField.setText(shapeToBeEdited.getPosition().getX() + "");
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(95, 155, 100, 24);
		textField_1.setText(shapeToBeEdited.getPosition().getY() + "");
		frame.getContentPane().add(textField_1);

		propertiesMap = shapeToBeEdited.getProperties();
		final JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon("right.png"));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < labels.size(); i++) {
					if (!properties.get(i).getText().equals("")) {
						propertiesMap.put(labels.get(i).getText(), Double.parseDouble(properties.get(i).getText()));
					}
				}
				if (!textField.getText().equals("") && !textField_1.getText().equals("")) {
					shapeToBeEdited.setPosition(new Point((int)Double.parseDouble(textField.getText()), (int)Double.parseDouble(textField_1.getText())));
				}
				shapeToBeEdited.setProperties(propertiesMap);
				drawerImpl.updateShape(oldShape, shapeToBeEdited);
				drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
				drawerImpl.refresh(drawingCanvas.getGraphics());
				Paint.comboBox.removeAllItems();
				Paint.comboBox.addItem("Nothing selected");
				for (int i = 0; i < drawerImpl.getShapes().length; i++) {
					Paint.comboBox.addItem(drawerImpl.getShapes()[i].getClass().getSimpleName() + " " + (i+1));

				}
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnNewButton.setBounds(69, 418, 89, 32);
		frame.getContentPane().add(btnNewButton);

		final int firstLableY = 220;
		final int firstTextFieldY = 217;
		int counter = 0;
		for (final Map.Entry<String, Double> entry : shapeToBeEdited.getProperties().entrySet()) {
			final JLabel lblNewLabel_1 = new JLabel(entry.getKey());
			lblNewLabel_1.setBounds(23, firstLableY + (20 * counter), 46, 14);
			frame.getContentPane().add(lblNewLabel_1);
			labels.add(lblNewLabel_1);

			textField_2 = new JTextField();
			textField_2.setBounds(96, firstTextFieldY + (20 * counter), 86, 20);
			frame.getContentPane().add(textField_2);
			textField_2.setColumns(10);
			textField_2.setText(entry.getValue() + "");
			properties.add(textField_2);

			counter++;
		}
	}
}
