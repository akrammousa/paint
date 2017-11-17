package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;


import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;

public class Paint {

	protected static JFrame frame;
	private Shape currentShape = null;
	private Color fillColor;
	private Color color;
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
	
	void initializeButton() {
		System.out.println("initializeButton " + supportedShapes.size());
		for (int j = 0; j < supportedShapes.size(); j++) {
			System.out.println(supportedShapes.get(j).getSimpleName());
			final int k = j;
			final int buttonWidthPlusMargin = 60;
			final int firstButtonXPosition = 31;
			final JButton shapeButton = new JButton(supportedShapes.get(k).getSimpleName());
			shapeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.out.println(supportedShapes.size());
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1200,700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final Canvas drawingCanvas = SingeltonCanvas.getCanvas();
		drawingCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(currentShape != null){
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
					drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
					drawerImpl.refresh(drawingCanvas.getGraphics());
					prepareComboBox(drawerImpl.getShapes());
					System.out.println(drawerImpl.getShapes().length);
				}

			}
		});
		drawingCanvas.setBackground(Color.WHITE);
		drawingCanvas.setBounds(10, 156, 1029, 495);
		frame.getContentPane().add(drawingCanvas);

		initializeButton();
		

		final JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(648, 508, 106, 22);
		frame.getContentPane().add(editorPane);

		final JButton btnNewButton_2 = new JButton("Color");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = JColorChooser.showDialog(null, "Choose the border Color",
						Color.BLACK);
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.ITALIC, 20));
		btnNewButton_2.setBounds(751, 14, 141, 55);
		frame.getContentPane().add(btnNewButton_2);

		final JButton btnNewButton_3 = new JButton("Fill Color");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fillColor = JColorChooser.showDialog(null, "Choose the Fill Color",
						Color.BLACK);
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_3.setBounds(751, 80, 141, 52);
		frame.getContentPane().add(btnNewButton_3);

		final JButton btnNewButton_4 = new JButton("Undo");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawerImpl.undo();
				System.out.println(drawerImpl.getShapes().length);
				drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
				drawerImpl.refresh(drawingCanvas.getGraphics());
				prepareComboBox(drawerImpl.getShapes());
			}
		});
		btnNewButton_4.setBounds(915, 15, 54, 52);
		frame.getContentPane().add(btnNewButton_4);

		final JButton btnRedo = new JButton("Redo");
		btnRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawerImpl.redo();
				drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
				drawerImpl.refresh(drawingCanvas.getGraphics());
				prepareComboBox(drawerImpl.getShapes());
			}
		});
		btnRedo.setBounds(915, 84, 54, 52);
		frame.getContentPane().add(btnRedo);

		final JButton btnNewButton_5 = new JButton("Save");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XmL", "JsOn");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(frame);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
					   String path = chooser.getSelectedFile().getAbsolutePath();
					   
					   if (path.charAt(path.length() - 1) == 'L') {
						   drawerImpl.save(path.replace("\\", "\\\\"));
					   } else if (path.charAt(path.length() - 1) == 'n') {
						   drawerImpl.save(path.replace("\\", "\\\\"));
					   }
		
					}
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_5.setBounds(1045, 508, 133, 52);
		frame.getContentPane().add(btnNewButton_5);

		final JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XmL", "JsOn");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(frame);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
					   String path = chooser.getSelectedFile().getAbsolutePath();
					   
					   if (path.charAt(path.length() - 1) == 'L') {
						   drawerImpl.load(path.replace("\\", "\\\\"));
						   drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
						   drawerImpl.refresh(drawingCanvas.getGraphics());
							prepareComboBox(drawerImpl.getShapes());
					   } else if (path.charAt(path.length() - 1) == 'n') {
						   drawerImpl.load(path.replace("\\", "\\\\"));
						   drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
						   drawerImpl.refresh(drawingCanvas.getGraphics());
							prepareComboBox(drawerImpl.getShapes());
					   }
		
					}
			}
		});
		btnLoad.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnLoad.setBounds(1045, 583, 133, 52);
		frame.getContentPane().add(btnLoad);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(575, 19, 169, 52);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("hna " + comboBox.getSelectedIndex());
				int selectedItemIndex = comboBox.getSelectedIndex();
				if (selectedItemIndex != -1 && selectedItemIndex != 0) {
					drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
					drawerImpl.refresh(drawingCanvas.getGraphics());
					Shape selectedItem = drawerImpl.getShapes()[selectedItemIndex - 1];
					Shape highlightedShaped = null;
					try {
						highlightedShaped = (Shape) selectedItem.clone();
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					highlightedShaped.setFillColor(Color.LIGHT_GRAY);
					highlightedShaped.draw(drawingCanvas.getGraphics());
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
				drawerImpl.removeShape(drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1]);
				drawingCanvas.getGraphics().clearRect(0, 0, 1209, 599);
				drawerImpl.refresh(drawingCanvas.getGraphics());
				prepareComboBox(drawerImpl.getShapes());

			}
		});
		btnNewButton.setBounds(1039, 31, 97, 84);
		frame.getContentPane().add(btnNewButton);
		
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("jar", "class");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(frame);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
					   String path = chooser.getSelectedFile().getAbsolutePath();
					   System.out.println(path.replace("\\", "\\\\"));
					   System.out.println(drawerImpl.getSupportedShapes().size());
					   if (path.charAt(path.length() - 1) == 's') {
						   drawerImpl.reflect(path.replace("\\", "\\\\"));
					   } else if (path.charAt(path.length() - 1) == 'r') {
						   drawerImpl.reflectJarFile(path.replace("\\", "\\\\"));
					   }
					   
					   supportedShapes = drawerImpl.getSupportedShapes();
					   System.out.println("hna " + supportedShapes.size());
					   initializeButton();
					   frame.repaint();
					   System.out.println(drawerImpl.getSupportedShapes().size());
//					   try {
////						for (int k = 0 ; k < drawerImpl.getSupportedShapes().size(); k++) {
////							System.out.println(drawerImpl.getSupportedShapes().get(k).getSimpleName());
////						}
////						System.out.println(drawerImpl.getSupportedShapes().get(6).getSimpleName());
//						Object clazz = drawerImpl.getSupportedShapes().get(6).getConstructor().newInstance();
//						Method me = drawerImpl.getSupportedShapes().get(6).getMethod("test");
//						me.invoke(clazz);
//					   } catch (Exception e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
					   //frame.repaint();
					}
			}
		});
		btnImport.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnImport.setBounds(1045, 440, 133, 52);
		frame.getContentPane().add(btnImport);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UpdateShape.initialieTheShapeToBeEdited((Shape)drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1], (Shape)drawerImpl.getShapes()[comboBox.getSelectedIndex() - 1].clone());
					UpdateShape.main(null);
				} catch (Exception exc) {
					System.out.println(exc.getMessage());
				}
			}
		});
		btnEdit.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnEdit.setBounds(1045, 371, 133, 52);
		frame.getContentPane().add(btnEdit);
		
		JButton btnCopy = new JButton("Copy");
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedIndex() - 1 >= 0) {
					System.out.println(supportedShapes.size());
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
			}
		});
		btnCopy.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnCopy.setBounds(1045, 308, 133, 52);
		frame.getContentPane().add(btnCopy);

	}
}
