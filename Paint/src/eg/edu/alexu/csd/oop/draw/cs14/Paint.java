package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Paint {

	protected static JFrame frame;
	private Shape currentShape = null;
	private Color fillColor;
	private Color color;
	final Canvas drawingCanvas = SingeltonCanvas.getCanvas();
	DrawEngineImpl drawerImpl = SingeltonDrawingEngine.getDrawingEnginInstance();
	List<Class<? extends Shape>> supportedShapes = drawerImpl.getSupportedShapes();

	static JComboBox<String> comboBox;
	private Class<? extends Shape> classOfCurrentShape;
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
	 * Paint shapes buttons according to supported classes.
	 */
	private void initializeShapesButtons() {
		for (int j = 0; j < supportedShapes.size(); j++) {
			final int k = j;
			final int buttonWidthPlusMargin = 60;
			final int firstButtonXPosition = 31;
			final JButton shapeButton = new JButton();
			try {
				shapeButton.setIcon(new ImageIcon(supportedShapes.get(k).getSimpleName() + ".png"));
			} catch (final Exception e) {
				// TODO: handle exception
			}
			shapeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					classOfCurrentShape = supportedShapes.get(k);
					ShapeFactory.supportedClasses = supportedShapes;
					currentShape = ShapeFactory.getShape(supportedShapes.get(k).getSimpleName());
					InputNode.clear();
					InputNode.initializeInputNodes(currentShape.getProperties());
				}
			});
			shapeButton.setBounds(firstButtonXPosition + buttonWidthPlusMargin * j, 13, 54, 52);
			frame.getContentPane().add(shapeButton);
		}
	}

	protected ImageIcon createImageIcon(String path) {
		final java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Create the application.
	 */
	public Paint() {
		initialize();
	}

	/**
	 * Prepare the shapes to be selected from combobox.
	 * @param drawnShapes **All drawn shapes**.
	 */
	protected void prepareComboBox(Shape[] drawnShapes) {
		comboBox.removeAllItems();
		comboBox.addItem("Nothing selected");
		for (int i = 0; i < drawnShapes.length; i++) {
			comboBox.addItem(drawnShapes[i].getClass().getSimpleName() + " " + (i+1));
		}
	}

	private void cloneSelectedShape() {
		final Shape copiedShape = drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1];
		classOfCurrentShape = copiedShape.getClass();
		ShapeFactory.supportedClasses = supportedShapes;
		currentShape = copiedShape;
		InputNode.clear();
		final Map<String , Double> properties = currentShape.getProperties();
		InputNode.initializeInputNodes(properties);
		int counter = 0;
		for (final Double val: properties.values()){
			System.out.println(InputNode.inputArray.get(counter).getText());
			InputNode.inputArray.get(counter++).setText(val + "");;
		}
		fillColor = currentShape.getFillColor();
		color = currentShape.getColor();
	}

	private void refreshCanvasAndComboBox() {
		drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
		drawerImpl.refresh(drawingCanvas.getGraphics());
		prepareComboBox(drawerImpl.getShapes());
	}

	private void highlightSelectedShape(int selectedItemIndex) {
		drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
		drawerImpl.refresh(drawingCanvas.getGraphics());
		final Shape selectedItem = drawerImpl.getShapes()[selectedItemIndex - 1];
		Shape highlightedShaped = null;
		try {
			highlightedShaped = (Shape) selectedItem.clone();
		} catch (final CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		highlightedShaped.setFillColor(Color.LIGHT_GRAY);
		highlightedShaped.draw(drawingCanvas.getGraphics());
		System.out.println("Done" + ": comboBox");
	}

	private void drawCurrentShape(MouseEvent arg0) {
		final String className = classOfCurrentShape.getSimpleName();
		currentShape = ShapeFactory.getShape(className);
		currentShape.setPosition(arg0.getPoint());
		System.out.println(currentShape.getClass().getSimpleName());
		final Map<String , Double> properties = currentShape.getProperties();
		int counter = 0;
		for (final String key: properties.keySet()){
			System.out.println(InputNode.inputArray.get(counter).getText());
			properties.put(key, Double.parseDouble(InputNode.inputArray.get(counter++).getText()));
		}
		currentShape.setProperties(properties);
		if (fillColor != null) {
			currentShape.setFillColor(fillColor);
		}
		if (color != null) {
			currentShape.setColor(color);
		}

		drawerImpl.addShape(currentShape);
		refreshCanvasAndComboBox();
		System.out.println(drawerImpl.getShapes().length);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1200,700);
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final JLabel drawingShape = new JLabel("");
		drawingShape.setBounds(577, 80, 177, 52);
		frame.getContentPane().add(drawingShape);

		final JLabel drawingShapeIcon = new JLabel("");
		drawingShapeIcon.setBounds(577, 12, 166, 55);
		frame.getContentPane().add(drawingShapeIcon);

		drawingCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(currentShape != null){
					drawCurrentShape(arg0);
				}

			}
		});


		drawingCanvas.setBackground(Color.WHITE);
		drawingCanvas.setBounds(10, 156, 1029, 495);
		frame.getContentPane().add(drawingCanvas);

		initializeShapesButtons();


		final JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(648, 508, 106, 22);
		frame.getContentPane().add(editorPane);

		final JButton colorBtn = new JButton();
		colorBtn.setIcon(new ImageIcon("color.png"));
		colorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = JColorChooser.showDialog(null, "Choose the border Color",
						Color.BLACK);
			}
		});
		colorBtn.setFont(new Font("Tahoma", Font.ITALIC, 20));
		colorBtn.setBounds(1031, 12, 141, 55);
		frame.getContentPane().add(colorBtn);

		final JButton fillColorBtn = new JButton();
		fillColorBtn.setIcon(new ImageIcon("fillcolor.png"));
		fillColorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fillColor = JColorChooser.showDialog(null, "Choose the Fill Color",
						Color.BLACK);
			}
		});
		fillColorBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		fillColorBtn.setBounds(1031, 80, 141, 52);
		frame.getContentPane().add(fillColorBtn);

		final JButton undoBtn = new JButton();
		undoBtn.setIcon(new ImageIcon("undo.png"));
		undoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				drawerImpl.undo();
				refreshCanvasAndComboBox();
			}
		});
		undoBtn.setBounds(1045, 243, 67, 52);
		frame.getContentPane().add(undoBtn);

		final JButton redoBtn = new JButton();
		redoBtn.setIcon(new ImageIcon("redo.png"));
		redoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawerImpl.redo();
				refreshCanvasAndComboBox();
			}
		});
		redoBtn.setBounds(1111, 243, 67, 52);
		frame.getContentPane().add(redoBtn);

		final JButton saveBtn = new JButton();
		saveBtn.setIcon(new ImageIcon("save.png"));
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser chooser = new JFileChooser();
				final FileNameExtensionFilter filter = new FileNameExtensionFilter("XmL", "JsOn");
				chooser.setFileFilter(filter);
				final int returnVal = chooser.showOpenDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					final String path = chooser.getSelectedFile().getAbsolutePath();
					drawerImpl.save(path);
				}
			}
		});
		saveBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		saveBtn.setBounds(1045, 508, 133, 52);
		frame.getContentPane().add(saveBtn);

		final JButton loadBtn = new JButton();
		loadBtn.setIcon(new ImageIcon("load.png"));
		loadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser chooser = new JFileChooser();
				final FileNameExtensionFilter filter = new FileNameExtensionFilter("XmL", "JsOn");
				chooser.setFileFilter(filter);
				final int returnVal = chooser.showOpenDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					final String path = chooser.getSelectedFile().getAbsolutePath();
					drawerImpl.load(path);
					refreshCanvasAndComboBox();
				}
			}
		});
		loadBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		loadBtn.setBounds(1045, 583, 133, 52);
		frame.getContentPane().add(loadBtn);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(850, 12, 169, 52);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("hna " + comboBox.getSelectedIndex());
				final int selectedItemIndex = comboBox.getSelectedIndex();
				if (selectedItemIndex > 0) {
					highlightSelectedShape(selectedItemIndex);
				} else if (selectedItemIndex == 0) {
					drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
					drawerImpl.refresh(drawingCanvas.getGraphics());
				}
			}
		});
		frame.getContentPane().add(comboBox);

		final JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon("delete.png"));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (comboBox.getSelectedIndex() - 1 >= 0) {
					drawerImpl.removeShape(drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1]);
					refreshCanvasAndComboBox();
				}
			}
		});
		btnNewButton.setBounds(1045, 165, 127, 69);
		frame.getContentPane().add(btnNewButton);

		final JButton importBtn = new JButton();
		importBtn.setIcon(new ImageIcon("import.png"));
		importBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser chooser = new JFileChooser();
				final FileNameExtensionFilter filter = new FileNameExtensionFilter("jar", "class");
				chooser.setFileFilter(filter);
				final int returnVal = chooser.showOpenDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					final String path = chooser.getSelectedFile().getAbsolutePath();
					drawerImpl.reflect(path);
					initializeShapesButtons();
					frame.repaint();
				}
			}
		});
		importBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		importBtn.setBounds(1045, 440, 133, 52);
		frame.getContentPane().add(importBtn);

		final JButton editBtn = new JButton();
		editBtn.setIcon(new ImageIcon("edit.png"));
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					UpdateShape.initialieTheShapeToBeEdited(drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1], (Shape)drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1].clone());
					UpdateShape.main(null);
				} catch (final Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		editBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		editBtn.setBounds(1045, 371, 133, 52);
		frame.getContentPane().add(editBtn);

		final JButton copyBtn = new JButton();
		copyBtn.setIcon(new ImageIcon("copy.png"));
		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedIndex() - 1 >= 0) {
					cloneSelectedShape();
				}
			}
		});
		copyBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		copyBtn.setBounds(1045, 308, 133, 52);
		frame.getContentPane().add(copyBtn);


	}
}
