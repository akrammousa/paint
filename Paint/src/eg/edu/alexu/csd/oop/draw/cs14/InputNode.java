package eg.edu.alexu.csd.oop.draw.cs14;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InputNode {
	static List<JTextField> inputArray;
	static List<JLabel> labelArray;
	JFrame frame;
	void initializeInputNodes(Map<String, Double> properties) {
		frame = Paint.frame;
		this.clear();
		inputArray = new ArrayList<JTextField>();
		labelArray = new ArrayList<JLabel>();
		int xCounterOfTextField = 31;
		int xCounterOfLable = 53;
		for (final String key: properties.keySet()) {
			final JTextField inputTextField = new JTextField();
			inputTextField.setBounds(xCounterOfTextField, 109, 116, 36);
			frame
			.getContentPane()
			.add(inputTextField);
			inputTextField.setColumns(10);
			inputArray.add(inputTextField);
			xCounterOfTextField += 126;

			final JLabel lbl = new JLabel(key);
			lbl.setFont(new Font("Tahoma", Font.BOLD, 21));
			lbl.setBounds(xCounterOfLable, 80, 77, 16);
			frame.getContentPane().add(lbl);
			labelArray.add(lbl);

			xCounterOfLable += 126;
		}
		frame.repaint();
	}

	void clear (){
		if(inputArray!=null){
			for (int i = 0; i < inputArray.size(); i++) {
				frame.remove(inputArray.get(i));
				frame.remove(labelArray.get(i));
				frame.repaint();

			}
		}
	}

}
