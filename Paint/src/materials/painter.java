package materials;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import eg.edu.alexu.csd.oop.draw.cs14.Circle;
import eg.edu.alexu.csd.oop.draw.cs14.DrawEngineImpl;
import eg.edu.alexu.csd.oop.draw.cs14.Elipse;
import eg.edu.alexu.csd.oop.draw.cs14.Line;
import eg.edu.alexu.csd.oop.draw.cs14.Rectangle;
import eg.edu.alexu.csd.oop.draw.cs14.ShapeImpl;
import eg.edu.alexu.csd.oop.draw.cs14.Square;
import eg.edu.alexu.csd.oop.draw.cs14.Triangle;

class painter extends Canvas {

	int index = 0;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final painter window = new painter();
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
	public painter() {
		initialize();
	}

	@Override
	public void paint(Graphics g) {

	}

	int counterE = 1;
	int counterS = 1;
	int counterC = 1;
	int counterR = 1;
	int counterT = 1;
	int counterL = 1;

	Double r;
	int x1 = 0;
	int y1 = 0;

	private void prepareComboBox(JComboBox comboBox,
			DrawEngineImpl drawingEngine) {
		// TODO Auto-generated method stub
		comboBox.removeAllItems();
		for (int i = 0; i < drawingEngine.drawnShapes.size(); i++) {
			comboBox.addItem(((ShapeImpl) drawingEngine.drawnShapes.get(i)).getType()
					+ "1");
		}
	}

	private void prepareLabel(JLabel label, JLabel shakl) {
		switch (shakl.getText()) {
		case "circle":
			label.setIcon(new ImageIcon("circleIcon.png"));
			break;
		case "line":
			label.setIcon(new ImageIcon("lineIcon.png"));
			break;
		case "rectangle":
			label.setIcon(new ImageIcon("rectangleIcon.png"));
			break;
		case "square":
			label.setIcon(new ImageIcon("squareIcon.png"));
			break;
		case "elipse":
			label.setIcon(new ImageIcon("elipseIcon.png"));
			break;
		case "triangle":
			label.setIcon(new ImageIcon("triangleIcon.png"));
			break;
		default:
			label.setIcon(null);

		}

	}

	private void setProperties(ShapeImpl obj) {
		// TODO Auto-generated method stub
		// settingColor
		obj.setColor(color);
		// setting Fill color
		obj.setFillColor(fillC);
		// setting position
		final Point position = new Point(x, y);
		obj.setPosition(position);

	}

	private void clear(Canvas canvas) {
		final Graphics g = canvas.getGraphics();
		g.clearRect(0, 0, 1006, 557);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	int flag = 0;
	int x = 0;
	int y = 0;
	int length = 0;
	double dragLength = 0;
	double dragWidth = 0;
	Color color = Color.BLACK, fillC = null;
	private JTextField radiusTextBox;
	private JTextField lengthTextBox;
	private JTextField widthTextBox;
	ArrayList<Integer> trianglePoints = new ArrayList<Integer>();
	ArrayList<Integer> linePoints = new ArrayList<Integer>();

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.controlHighlight);
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.setBounds(100, 100, 1113, 718);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);


		final DrawEngineImpl DrawingEngine = new DrawEngineImpl();
		final Canvas canvas = new Canvas();
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(91, 62, 1006, 557);
		frame.getContentPane().add(canvas);

		final JButton btnNewButton_6 = new JButton("Fill Color");
		btnNewButton_6.setBackground(null);
		btnNewButton_6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JColorChooser jcc = new JColorChooser();
				fillC = jcc.showDialog(null, "Please Choose the Fill Color",
						null);
				DrawingEngine.refresh(canvas.getGraphics());
				if (fillC != null) {
					btnNewButton_6.setBackground(fillC);
				}
			}
		});
		btnNewButton_6.setBounds(784, 33, 315, 23);
		frame.getContentPane().add(btnNewButton_6);

		final JButton btnNewButton_1 = new JButton("Outline Color");
		btnNewButton_1.setForeground(Color.WHITE);
		btnNewButton_1.setBackground(Color.BLACK);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JColorChooser jcc = new JColorChooser();
				color = jcc.showDialog(null, "Please Choose the Outline Color",
						Color.black);
				DrawingEngine.refresh(canvas.getGraphics());
				if (color != null) {
					btnNewButton_1.setBackground(color);
				}
			}
		});
		btnNewButton_1.setBounds(784, 5, 315, 23);
		frame.getContentPane().add(btnNewButton_1);

		final JLabel lblB = new JLabel("      b");
		lblB.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblB.setBounds(10, 511, 49, 21);
		frame.getContentPane().add(lblB);
		lblB.setVisible(false);

		final JLabel lblA = new JLabel("      a");
		lblA.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblA.setBounds(10, 448, 49, 21);
		frame.getContentPane().add(lblA);
		lblA.setVisible(false);


		radiusTextBox = new JTextField();

		radiusTextBox.setForeground(Color.GRAY);
		radiusTextBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		radiusTextBox.setBounds(10, 409, 73, 28);
		frame.getContentPane().add(radiusTextBox);
		radiusTextBox.setColumns(10);
		radiusTextBox.setVisible(false);

		lengthTextBox = new JTextField();
		lengthTextBox.setForeground(Color.GRAY);
		lengthTextBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lengthTextBox.setBounds(10, 472, 73, 28);
		frame.getContentPane().add(lengthTextBox);
		lengthTextBox.setColumns(10);
		lengthTextBox.setVisible(false);

		widthTextBox = new JTextField();
		widthTextBox.setForeground(Color.GRAY);
		widthTextBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		widthTextBox.setBounds(10, 532, 73, 28);
		frame.getContentPane().add(widthTextBox);
		widthTextBox.setColumns(10);
		widthTextBox.setVisible(false);

		final JLabel lblRadius = new JLabel("Diameter");
		lblRadius.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRadius.setBounds(20, 382, 59, 28);
		lblRadius.setVisible(false);

		frame.getContentPane().add(lblRadius);

		final JLabel lblLength = new JLabel("Length");
		lblLength.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblLength.setVisible(false);
		lblLength.setBounds(20, 445, 59, 28);
		frame.getContentPane().add(lblLength);

		final JLabel lblWidth = new JLabel("Width");
		lblWidth.setVisible(false);
		lblWidth.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblWidth.setBounds(20, 508, 59, 28);
		frame.getContentPane().add(lblWidth);

		final JLabel shakl = new JLabel("None");
		shakl.setFont(new Font("Calisto MT", Font.PLAIN, 16));
		shakl.setBounds(717, 643, 109, 23);
		frame.getContentPane().add(shakl);

		final JLabel picLabel = new JLabel("");
		picLabel.setBounds(820, 625, 59, 41);
		frame.getContentPane().add(picLabel);

		final JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (comboBox.getSelectedIndex() == -1) {
					;
				} else {
					final Graphics g = canvas.getGraphics();
					clear(canvas);
					DrawingEngine.refresh(g);
					final int index = comboBox.getSelectedIndex();
					final Square selecting = new Square();
					selecting.setColor(new Color(128, 128, 128, 40));
					selecting.setFillColor(new Color(128, 128, 128, 40));
					double lengthh = 0;
					if (((ShapeImpl) DrawingEngine.drawnShapes
							.get(comboBox.getSelectedIndex())).getType()
							.equals("Circle ")) {
						lengthh = DrawingEngine.drawnShapes.get(index)
								.getProperties().get("radius");
						final double posX = DrawingEngine.drawnShapes.get(index)
								.getPosition().getX() - lengthh / 2;
						final double posY = DrawingEngine.drawnShapes.get(index)
								.getPosition().getY() - lengthh / 2;
						selecting
						.setPosition(new Point((int) posX, (int) posY));
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("length", lengthh);

						selecting.setProperties(properties);
						selecting.draw(g);
					} else if (((ShapeImpl) DrawingEngine.drawnShapes
							.get(comboBox.getSelectedIndex())).getType()
							.equals("Square ")) {
						lengthh = DrawingEngine.drawnShapes.get(index)
								.getProperties().get("length") + 40;
						final double posX = DrawingEngine.drawnShapes.get(index)
								.getPosition().getX();
						final double posY = DrawingEngine.drawnShapes.get(index)
								.getPosition().getY();
						selecting.setPosition(
								new Point((int) posX - 20, (int) posY - 20));
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("length", lengthh);

						selecting.setProperties(properties);
						selecting.draw(g);
					} else if (((ShapeImpl) DrawingEngine.drawnShapes
							.get(comboBox.getSelectedIndex())).getType()
							.equals("Rectangle ")) {
						final Rectangle rec = new Rectangle();
						rec.setColor(new Color(128, 128, 128, 40));
						rec.setFillColor(new Color(128, 128, 128, 40));
						double lnt = 0;
						double wdt = 0;
						lnt = DrawingEngine.drawnShapes.get(index).getProperties()
								.get("length") + 40;
						wdt = DrawingEngine.drawnShapes.get(index).getProperties()
								.get("width") + 40;
						final double posX = DrawingEngine.drawnShapes.get(index)
								.getPosition().getX();
						final double posY = DrawingEngine.drawnShapes.get(index)
								.getPosition().getY();
						rec.setPosition(
								new Point((int) posX - 20, (int) posY - 20));
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("length", lnt);
						properties.put("width", wdt);
						rec.setProperties(properties);
						rec.draw(g);
					}
				}
			}
		});

		comboBox.setBounds(428, 5, 128, 34);
		frame.getContentPane().add(comboBox);

		final JButton circle = new JButton("");
		circle.setIcon(new ImageIcon("circleIcon.png"));
		circle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lblRadius.setVisible(true);
				radiusTextBox.setVisible(true);
				shakl.setText("circle");
				prepareLabel(picLabel, shakl);
				lblWidth.setVisible(false);
				widthTextBox.setVisible(false);
				lblLength.setVisible(false);
				lengthTextBox.setVisible(false);

			}
		});
		circle.setBounds(10, 62, 59, 42);
		frame.getContentPane().add(circle);

		final JButton triangle = new JButton("");
		triangle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shakl.setText("triangle");
				prepareLabel(picLabel, shakl);
			}
		});
		triangle.setIcon(new ImageIcon("triangleIcon.png"));
		triangle.setBounds(10, 223, 59, 42);
		frame.getContentPane().add(triangle);

		final JButton line = new JButton("");
		line.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shakl.setText("line");
				prepareLabel(picLabel, shakl);
			}
		});
		line.setIcon(new ImageIcon("lineIcon.png"));
		line.setBounds(10, 117, 59, 42);
		frame.getContentPane().add(line);

		final JButton rectangle = new JButton("");
		rectangle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblLength.setVisible(true);
				lblWidth.setVisible(true);
				lengthTextBox.setVisible(true);
				widthTextBox.setVisible(true);
				shakl.setText("rectangle");
				prepareLabel(picLabel, shakl);
				radiusTextBox.setVisible(false);
				lblRadius.setVisible(false);
			}
		});
		rectangle.setIcon(new ImageIcon("rectangleIcon.png"));
		rectangle.setBounds(10, 276, 59, 42);
		frame.getContentPane().add(rectangle);

		final JButton elipse = new JButton("");
		elipse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lblA.setVisible(true);
				lblB.setVisible(true);
				lengthTextBox.setVisible(true);
				widthTextBox.setVisible(true);
				shakl.setText("elipse");
				prepareLabel(picLabel, shakl);
				radiusTextBox.setVisible(false);
				lblRadius.setVisible(false);
			}
		});
		elipse.setIcon(new ImageIcon("elipseIcon.png"));
		elipse.setBounds(10, 170, 59, 42);
		frame.getContentPane().add(elipse);

		final JButton square = new JButton("");
		square.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shakl.setText("square");
				lblLength.setVisible(true);
				lengthTextBox.setVisible(true);
				prepareLabel(picLabel, shakl);
				lblWidth.setVisible(false);
				widthTextBox.setVisible(false);
				lblRadius.setVisible(false);
				radiusTextBox.setVisible(false);

			}
		});
		square.setIcon(new ImageIcon("squareIcon.png"));
		square.setBounds(10, 329, 59, 42);
		frame.getContentPane().add(square);

		final JLabel counterr = new JLabel("");
		counterr.setFont(new Font("Tahoma", Font.PLAIN, 36));
		counterr.setBounds(269, 635, 73, 44);
		frame.getContentPane().add(counterr);

		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (shakl.getText().equals("circle")) {
					x = e.getX();
					y = e.getY();
					if (radiusTextBox.getText().equals("")) {
						;
					} else {
						final Circle c = new Circle();
						final Graphics g = canvas.getGraphics();
						setProperties(c);
						// setting properties
						final String radiusText = radiusTextBox.getText();
						final double radius = Double.parseDouble(radiusText);
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("radius", radius);
						c.setProperties(properties);
						// c.draw(g);
						//c.counterC = counterC++;
						DrawingEngine.addShape(c);
						prepareComboBox(comboBox, DrawingEngine);
						clear(canvas);
						DrawingEngine.refresh(g);
						radiusTextBox.setText("");
						lblRadius.setVisible(false);
						radiusTextBox.setVisible(false);
						shakl.setText("None");
					}
				} else if (shakl.getText().equals("triangle")) {
					x = e.getX();
					y = e.getY();
					trianglePoints.add(x);
					trianglePoints.add(y);
					final Circle c = new Circle();
					final Map<String, java.lang.Double> propertiess = new HashMap<String, java.lang.Double>();
					propertiess.put("radius", 8.0);
					c.setProperties(propertiess);
					c.setColor(Color.BLACK);
					c.setFillColor(null);
					final Point position1 = new Point(x, y);
					c.setPosition(position1);
					final Graphics g = canvas.getGraphics();
					c.draw(g);
					if (trianglePoints.size() == 6) {
						final Triangle t = new Triangle();
						t.setColor(color);
						// setting Fill color
						t.setFillColor(fillC);
						// setting position
						final Point position = new Point(trianglePoints.get(0),
								trianglePoints.get(1));
						t.setPosition(position);
						// setting properties
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("x2", (double) trianglePoints.get(2));
						properties.put("y2", (double) trianglePoints.get(3));
						properties.put("x3", (double) trianglePoints.get(4));
						properties.put("y3", (double) trianglePoints.get(5));
						t.setProperties(properties);
						// c.draw(g);
						//t.counterT = counterT++;
						DrawingEngine.addShape(t);
						prepareComboBox(comboBox, DrawingEngine);
						counterr.setText(String
								.valueOf(DrawingEngine.getShapes().length));
						clear(canvas);
						DrawingEngine.refresh(g);
						shakl.setText("None");
						trianglePoints.clear();
					}
				} else if (shakl.getText().equals("square")) {
					x = e.getX();
					y = e.getY();
					if (lengthTextBox.getText().equals("")) {
						;
					} else {
						final Square sq = new Square();
						final Graphics g = canvas.getGraphics();
						setProperties(sq);
						// setting properties
						final String lengthText = lengthTextBox.getText();
						final double length = Double.parseDouble(lengthText);
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("length", length);
						sq.setProperties(properties);
						// sq.draw(g);
						//sq.counterS = counterS++;
						DrawingEngine.addShape(sq);
						prepareComboBox(comboBox, DrawingEngine);
						clear(canvas);
						DrawingEngine.refresh(g);
						lengthTextBox.setText("");
						lblLength.setVisible(false);
						lengthTextBox.setVisible(false);
						shakl.setText("None");
					}

				} else if (shakl.getText().equals("elipse")) {
					x = e.getX();
					y = e.getY();
					if (lengthTextBox.getText().equals("")
							|| widthTextBox.getText().equals("")) {
						;
					} else {
						final Elipse elps = new Elipse();
						final Graphics g = canvas.getGraphics();
						setProperties(elps);
						// setting properties
						final String lengthText = lengthTextBox.getText();
						final String widthText = widthTextBox.getText();
						final double length = Double.parseDouble(lengthText);
						final double width = Double.parseDouble(widthText);
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("a", length);
						properties.put("b", width);
						elps.setProperties(properties);
						// elps.draw(g);
						//elps.counterE = counterE++;
						DrawingEngine.addShape(elps);
						prepareComboBox(comboBox, DrawingEngine);
						clear(canvas);
						DrawingEngine.refresh(g);
						lengthTextBox.setText("");
						widthTextBox.setText("");
						lblA.setVisible(false);
						lblB.setVisible(false);
						lengthTextBox.setVisible(false);
						widthTextBox.setVisible(false);
						shakl.setText("None");
					}

				} else if (shakl.getText().equals("line")) {
					x = e.getX();
					y = e.getY();
					linePoints.add(x);
					linePoints.add(y);
					final Circle c = new Circle();
					final Map<String, java.lang.Double> propertiess = new HashMap<String, java.lang.Double>();
					propertiess.put("radius", 8.0);
					c.setProperties(propertiess);
					c.setColor(Color.BLACK);
					c.setFillColor(null);
					final Point position1 = new Point(x, y);
					c.setPosition(position1);
					final Graphics g = canvas.getGraphics();
					c.draw(g);
					if (linePoints.size() == 4) {
						final Line l = new Line();
						l.setColor(color);
						// setting Fill color
						l.setFillColor(fillC);
						// setting position
						final Point position = new Point(linePoints.get(0),
								linePoints.get(1));
						l.setPosition(position);
						// setting properties
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("x2", (double) linePoints.get(2));
						properties.put("y2", (double) linePoints.get(3));
						l.setProperties(properties);
						// c.draw(g);
						//l.counterL = counterL++;
						DrawingEngine.addShape(l);
						prepareComboBox(comboBox, DrawingEngine);
						clear(canvas);
						DrawingEngine.refresh(g);
						shakl.setText("None");
						linePoints.clear();
					}
				} else if (shakl.getText().equals("rectangle")) {
					x = e.getX();
					y = e.getY();
					if (lengthTextBox.getText().equals("")
							|| widthTextBox.equals("")) {
						;
					} else {
						final Rectangle rec = new Rectangle();
						final Graphics g = canvas.getGraphics();
						setProperties(rec);
						// setting properties
						final String lengthText = lengthTextBox.getText();
						final String widthText = widthTextBox.getText();
						final double length = Double.parseDouble(lengthText);
						final double width = Double.parseDouble(widthText);
						final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
						properties.put("length", length);
						properties.put("width", width);
						rec.setProperties(properties);
						// rec.draw(g);
						//rec.counterR = counterR++;
						DrawingEngine.addShape(rec);
						prepareComboBox(comboBox, DrawingEngine);
						clear(canvas);
						DrawingEngine.refresh(g);
						lengthTextBox.setText("");
						widthTextBox.setText("");
						lblLength.setVisible(false);
						lblWidth.setVisible(false);
						lengthTextBox.setVisible(false);
						widthTextBox.setVisible(false);
						shakl.setText("None");
					}

				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (shakl.getText().equals("circle") && flag == 1) {
					flag = 0;
					final Circle c = new Circle();
					c.setColor(color);
					c.setFillColor(fillC);
					c.setPosition(new Point(x1, y1));
					final Map<String, java.lang.Double> prop = new HashMap<String, java.lang.Double>();
					prop.put("radius", r);
					c.setProperties(prop);
					DrawingEngine.addShape(c);
					//c.counterC = counterC++;
					prepareComboBox(comboBox, DrawingEngine);
					clear(canvas);
					final Graphics g = canvas.getGraphics();
					DrawingEngine.refresh(g);
					radiusTextBox.setText("");
					lblRadius.setVisible(false);
					radiusTextBox.setVisible(false);
				}
				if (shakl.getText().equals("square") && flag == 1) {
					flag = 0;
					final Square sq = new Square();
					sq.setColor(color);
					sq.setFillColor(fillC);
					sq.setPosition(new Point(x1, y1));
					final Map<String, java.lang.Double> prop = new HashMap<String, java.lang.Double>();
					prop.put("length", dragLength);
					sq.setProperties(prop);
					DrawingEngine.addShape(sq);
					//sq.counterS = counterS++;
					prepareComboBox(comboBox, DrawingEngine);
					clear(canvas);
					final Graphics g = canvas.getGraphics();
					DrawingEngine.refresh(g);
					lengthTextBox.setText("");
					lblLength.setVisible(false);
					lengthTextBox.setVisible(false);
				}
				if (shakl.getText().equals("rectangle") && flag == 1) {
					flag = 0;
					final Rectangle rec = new Rectangle();
					rec.setColor(color);
					rec.setFillColor(fillC);
					rec.setPosition(new Point(x1, y1));
					final Map<String, java.lang.Double> prop = new HashMap<String, java.lang.Double>();
					prop.put("length", dragLength);
					prop.put("width", dragWidth);
					rec.setProperties(prop);
					DrawingEngine.addShape(rec);
					//rec.counterR = counterR++;
					prepareComboBox(comboBox, DrawingEngine);
					clear(canvas);
					final Graphics g = canvas.getGraphics();
					DrawingEngine.refresh(g);
					lengthTextBox.setText("");
					lblLength.setVisible(false);
					lengthTextBox.setVisible(false);
				}
			}
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (flag == 0) {
					x1 = arg0.getX();
					y1 = arg0.getY();
					flag = 1;
				}
				final int x2 = arg0.getX();
				final int y2 = arg0.getY();
				if (shakl.getText().equals("circle")) {
					clear(canvas);
					final Graphics g = canvas.getGraphics();
					DrawingEngine.refresh(g);
					final Circle c = new Circle();
					c.setPosition(new Point(x1, y1));
					c.setColor(color);
					c.setFillColor(fillC);
					r = Math.hypot(x2 - x1, y2 - y1);
					final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
					properties.put("radius", r);
					c.setProperties(properties);
					c.draw(g);

				}
				if (shakl.getText().equals("square")) {
					final Graphics g = canvas.getGraphics();
					clear(canvas);
					DrawingEngine.refresh(g);
					final Square sq = new Square();
					sq.setPosition(new Point(x1, y1));
					sq.setColor(color);
					sq.setFillColor(fillC);
					dragLength = Math.hypot(x2 - x1, y2 - y1);
					dragLength = dragLength / 1.41421356237309;
					final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
					properties.put("length", dragLength);
					sq.setProperties(properties);
					sq.draw(g);

				}
				if (shakl.getText().equals("rectangle")) {
					final Graphics g = canvas.getGraphics();
					clear(canvas);
					DrawingEngine.refresh(g);
					final Rectangle rec = new Rectangle();
					rec.setPosition(new Point(x1, y1));
					rec.setColor(color);
					rec.setFillColor(fillC);
					dragLength = (x2 - x1);
					dragWidth = (y2 - y1);
					final Map<String, java.lang.Double> properties = new HashMap<String, java.lang.Double>();
					properties.put("length", dragLength);
					properties.put("width", dragWidth);
					rec.setProperties(properties);
					rec.draw(g);
				}
			}

		});

		final ButtonGroup group = new ButtonGroup();

		final JLabel lblCurrentShape = new JLabel("Current Shape:");
		lblCurrentShape.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCurrentShape.setBounds(706, 625, 109, 23);
		frame.getContentPane().add(lblCurrentShape);

		final JButton btnNewButton_2 = new JButton("Load");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DrawingEngine.load("Save");
				final Graphics cnv = canvas.getGraphics();
				clear(canvas);
				DrawingEngine.refresh(cnv);
				prepareComboBox(comboBox, DrawingEngine);
			}
		});
		btnNewButton_2.setIcon(new ImageIcon("loadIcon.png"));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton_2.setBounds(10, 4, 89, 34);
		frame.getContentPane().add(btnNewButton_2);

		final ImageIcon icon = new ImageIcon("F:\\Downloads\\save.png");
		final JButton btnNewButton_3 = new JButton("Save",
				new ImageIcon("saveIcon.png"));
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DrawingEngine.save("Save");
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton_3.setBounds(109, 4, 89, 34);
		frame.getContentPane().add(btnNewButton_3);

		final JButton btnDelete = new JButton("");
		btnDelete.setIcon(new ImageIcon("deleteIcon.png"));
		btnDelete.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_DELETE) {
					DrawingEngine.removeShape(DrawingEngine.drawnShapes
							.get(comboBox.getSelectedIndex()));
					final Graphics g = canvas.getGraphics();

					clear(canvas);
					DrawingEngine.refresh(g);
					counterr.setText(
							String.valueOf(DrawingEngine.getShapes().length));
					prepareComboBox(comboBox, DrawingEngine);
				}
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (comboBox.getSelectedItem() == null) {
					;
				} else {
					DrawingEngine.removeShape(DrawingEngine.drawnShapes
							.get(comboBox.getSelectedIndex()));
					final Graphics g = canvas.getGraphics();
					clear(canvas);
					DrawingEngine.refresh(g);
					counterr.setText(
							String.valueOf(DrawingEngine.getShapes().length));
					prepareComboBox(comboBox, DrawingEngine);
				}
			}
		});
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setBackground(SystemColor.controlHighlight);
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDelete.setBounds(565, 5, 49, 34);
		frame.getContentPane().add(btnDelete);

		final JButton btnNewButton = new JButton("Undo");
		btnNewButton.setIcon(new ImageIcon("undoIcon.png"));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DrawingEngine.undo();
				final Graphics g = canvas.getGraphics();
				counterr.setText(
						String.valueOf(DrawingEngine.getShapes().length));
				clear(canvas);
				DrawingEngine.refresh(g);
				prepareComboBox(comboBox, DrawingEngine);
			}

		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton.setBounds(208, 4, 89, 34);
		frame.getContentPane().add(btnNewButton);

		final JButton btnNewButton_4 = new JButton("Redo");
		btnNewButton_4.setIcon(new ImageIcon("redoIcon.png"));
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DrawingEngine.redo();
				final Graphics g = canvas.getGraphics();
				counterr.setText(
						String.valueOf(DrawingEngine.getShapes().length));
				clear(canvas);
				DrawingEngine.refresh(g);
				prepareComboBox(comboBox, DrawingEngine);
			}
		});
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton_4.setBounds(307, 4, 95, 34);
		frame.getContentPane().add(btnNewButton_4);

		final JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.setIcon(new ImageIcon("editIcon.png"));
		btnNewButton_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_5.setBounds(624, 5, 49, 34);
		frame.getContentPane().add(btnNewButton_5);

		final JButton btnNewButton_7 = new JButton("");
		btnNewButton_7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Graphics cnv = canvas.getGraphics();
				clear(canvas);
				DrawingEngine.refresh(cnv);
				counterr.setText(
						String.valueOf(DrawingEngine.getShapes().length));
				prepareComboBox(comboBox, DrawingEngine);

			}
		});
		btnNewButton_7.setBounds(683, 5, 49, 34);
		frame.getContentPane().add(btnNewButton_7);
		btnNewButton_7.setIcon(new ImageIcon("refreshIcon.png"));

		final JButton btnNewButton_8 = new JButton("");
		btnNewButton_8.setIcon(new ImageIcon("closeIcon.png"));

		btnNewButton_8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shakl.setText("None");
				picLabel.setIcon(null);
			}
		});
		btnNewButton_8.setBounds(899, 625, 49, 41);
		frame.getContentPane().add(btnNewButton_8);

	}
}
