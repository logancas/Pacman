import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

public class Game implements Runnable {
	public String levelFileName = "Levels.txt";

	public void run() {
		final JFrame frame = new JFrame("Pacman");
		final JLabel status = new JLabel("Running...");	
		final JLabel score = new JLabel("Score: 0");	
		final JPanel statusPanel = new JPanel();
		final JPanel lifePanel = new JPanel();
		final JPanel controlPanel = new JPanel();
		final JPanel rightPanel = new JPanel();
		final GameCourt court = new GameCourt(status, score, lifePanel, levelFileName);

		frame.setLocation(300, 300);
		frame.add(statusPanel, BorderLayout.NORTH);
		lifePanel.setBackground(Color.BLACK);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		score.setForeground(Color.WHITE);
		statusPanel.add(status);
		
		setInstructions(rightPanel, score, lifePanel);

		controlPanel.add(getResetButton(court));
		controlPanel.add(getClassicModeButton(frame, court));
		controlPanel.add(getCustomModeButton(frame, court));

		frame.add(court, BorderLayout.CENTER);
		frame.add(rightPanel, BorderLayout.EAST);
		frame.add(controlPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
	}

	private JButton getResetButton(GameCourt court) {
		final JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.reset(new File(levelFileName));
			}
		});
		return reset;
	}

	private JButton getClassicModeButton(final JFrame frame, final GameCourt court) {
		ActionListener classic = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				levelFileName = "Levels.txt";
				court.reset(new File(levelFileName));
			}
		};
		JButton classicMode = new JButton("Classic Mode");
		classicMode.addActionListener(classic);
		return classicMode;
	}

	private JButton getCustomModeButton(final JFrame frame, final GameCourt court) {
		 ActionListener custom = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endCurrentLevel(court);
				Object[] gOptions = {6, 5, 4, 3, 2, 1};
				int level = 7;
				levelFileName = "customLevel.txt";
				int numGhosts = 6 - JOptionPane.showOptionDialog(frame,
					    "How many ghosts do you want to have in your level?",
					    "Number of Ghosts", JOptionPane.DEFAULT_OPTION,
					    JOptionPane.QUESTION_MESSAGE, null, gOptions, gOptions[0]); 
				Object[] sOptions = {8, 7, 6, 5, 4, 3, 2, 1};
				int numSpecial = 8 - JOptionPane.showOptionDialog(frame,
					    "How many 'special' foods do you want to have in your level?",
					    "Number of Special", JOptionPane.DEFAULT_OPTION,
					    JOptionPane.QUESTION_MESSAGE, null, sOptions, sOptions[0]); 
				try {
					FileWriter out = new FileWriter(levelFileName);
					out.write(Integer.toString(level));
					out.write("\n");
					out.write(Integer.toString(numGhosts));
					out.write("\n");
					out.write(Integer.toString(numSpecial));
					out.close();
				} catch (IOException exception) {
				}
				court.reset(new File(levelFileName));
				
				court.requestFocusInWindow();
			}
		};
		JButton customButton = new JButton("Create Custom Level");
		customButton.addActionListener(custom);
		return customButton;
	}
	
	private void endCurrentLevel(GameCourt court) {
		court.setPlaying(false);
		court.setLost(true);
	}

	private void setInstructions(JPanel rightPanel, JLabel score, JPanel lifePanel) {
		List<JLabel> instructions = new ArrayList<>();
		JLabel title = new JLabel("PACMAN     ");
		instructions.add(title);
		instructions.add(new JLabel("INSTRUCTIONS: "));
		instructions.add(new JLabel("You are Pacman. Your goal is to eat all "));
		instructions.add(new JLabel("the food circles while avoiding the ghosts "));
		instructions.add(new JLabel(" "));
		instructions.add(new JLabel("CONTROLS: "));
		instructions.add(new JLabel("Move Pacman by using the 4 arrow keys. "));
		instructions.add(new JLabel("You cannot cross any of the blue lines. "));
		instructions.add(new JLabel(" "));
		instructions.add(new JLabel("SPECIAL FOOD:"));
		instructions.add(new JLabel("The larger food items are 'Special.' Eating"));
		instructions.add(new JLabel("them makes the ghosts temporarily edible. "));
		instructions.add(new JLabel("Hitting edible ghosts returns them to their"));
		instructions.add(new JLabel("original position, and Pacman will not lose"));
		instructions.add(new JLabel("a life. If Pacman hits a normal ghost, he "));
		instructions.add(new JLabel("loses a life. You start with 3 lives. "));
		instructions.add(new JLabel(" "));
		instructions.add(new JLabel("GAMEPLAY: "));
		instructions.add(new JLabel("Press 'Play' to start. The game defaults to "));
		instructions.add(new JLabel("Classic Mode, which has 6 unique levels. You win "));
		instructions.add(new JLabel("a level by eating all food items. You win the "));
		instructions.add(new JLabel("game by winning all 6 levels. There is also a "));
		instructions.add(new JLabel("Custom Mode option. Follow the prompts to create"));
		instructions.add(new JLabel("your custom mode. Each custom mode created "));
		instructions.add(new JLabel("will be saved. "));
		instructions.add(new JLabel(" "));

		for (JLabel instruction : instructions) {
			if (instruction.equals(title)) {
				instruction.setForeground(Color.CYAN);
				instruction.setFont(new Font("font", Font.HANGING_BASELINE, 20));
				rightPanel.setBackground(Color.BLACK);
				rightPanel.add(instruction);
				rightPanel.add(score);
				rightPanel.add(lifePanel);
			} else {
				instruction.setForeground(Color.WHITE);
				rightPanel.add(instruction);
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
