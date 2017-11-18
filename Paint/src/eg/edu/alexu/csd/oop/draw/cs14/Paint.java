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
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
			final JButton shapeButton = new JButton(supportedShapes.get(k).getSimpleName());
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
		Shape copiedShape = drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1];
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
		Shape selectedItem = drawerImpl.getShapes()[selectedItemIndex - 1];
		Shape highlightedShaped = null;
		try {
			highlightedShaped = (Shape) selectedItem.clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		highlightedShaped.setFillColor(Color.LIGHT_GRAY);
		highlightedShaped.draw(drawingCanvas.getGraphics());
		System.out.println("Done" + ": comboBox");
	}

	private void drawCurrentShape(MouseEvent arg0) {
		String className = classOfCurrentShape.getSimpleName();
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

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

		final JButton colorBtn = new JButton("Color");
		colorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = JColorChooser.showDialog(null, "Choose the border Color",
						Color.BLACK);
			}
		});
		colorBtn.setFont(new Font("Tahoma", Font.ITALIC, 20));
		colorBtn.setBounds(751, 14, 141, 55);
		frame.getContentPane().add(colorBtn);

		final JButton fillColorBtn = new JButton("Fill Color");
		fillColorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fillColor = JColorChooser.showDialog(null, "Choose the Fill Color",
						Color.BLACK);
			}
		});
		fillColorBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		fillColorBtn.setBounds(751, 80, 141, 52);
		frame.getContentPane().add(fillColorBtn);

		final JButton undoBtn = new JButton("Undo");
		undoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawerImpl.undo();
				refreshCanvasAndComboBox();
			}
		});
		undoBtn.setBounds(915, 15, 54, 52);
		frame.getContentPane().add(undoBtn);

		final JButton redoBtn = new JButton("Redo");
		redoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawerImpl.redo();
				refreshCanvasAndComboBox();
			}
		});
		redoBtn.setBounds(915, 84, 54, 52);
		frame.getContentPane().add(redoBtn);

		final JButton saveBtn = new JButton("Save");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XmL", "JsOn");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(frame);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
					   String path = chooser.getSelectedFile().getAbsolutePath();
					   drawerImpl.save(path);					   
					}
			}
		});
		saveBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		saveBtn.setBounds(1045, 508, 133, 52);
		frame.getContentPane().add(saveBtn);

		final JButton loadBtn = new JButton("Load");
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XmL", "JsOn");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(frame);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
					   String path = chooser.getSelectedFile().getAbsolutePath();
					   drawerImpl.load(path);
					   refreshCanvasAndComboBox();
					}
			}
		});
		loadBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		loadBtn.setBounds(1045, 583, 133, 52);
		frame.getContentPane().add(loadBtn);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(575, 19, 169, 52);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("hna " + comboBox.getSelectedIndex());
				int selectedItemIndex = comboBox.getSelectedIndex();
				if (selectedItemIndex > 0) {
					highlightSelectedShape(selectedItemIndex);
				} else if (selectedItemIndex == 0) {
					drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
					drawerImpl.refresh(drawingCanvas.getGraphics());
				}
				}
		});
		frame.getContentPane().add(comboBox);

		final JButton btnNewButton = new JButton("Delete");
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
		btnNewButton.setBounds(1039, 31, 97, 84);
		frame.getContentPane().add(btnNewButton);

		JButton importBtn = new JButton("Import");
		importBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("jar", "class");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(frame);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
					   String path = chooser.getSelectedFile().getAbsolutePath();
					   drawerImpl.reflect(path);
					   initializeShapesButtons();
					   frame.repaint();
					}
			}
		});
		importBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		importBtn.setBounds(1045, 440, 133, 52);
		frame.getContentPane().add(importBtn);

		JButton editBtn = new JButton("Edit");
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UpdateShape.initialieTheShapeToBeEdited((Shape)drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1], (Shape)drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1].clone());
					UpdateShape.main(null);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		editBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		editBtn.setBounds(1045, 371, 133, 52);
		frame.getContentPane().add(editBtn);

		JButton copyBtn = new JButton("Copy");
		copyBtn.addActionListener(new ActionListener() {
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
