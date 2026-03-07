package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import control.MazeController;
import model.AbstractTriviaQuestion;
import model.Direction;
import model.MultipleChoiceQuestion;
import model.ShortQuestion;
import model.TriviaDoor;
import model.TriviaRoom;
import model.TrueFalseQuestion;

/**
 * Main GUI window for the Trivia Maze game.
 *
 * @author huyle
 * @version 6
 */
public final class MazeView extends JFrame {

	private static final long serialVersionUID = 1L;

	/***/
	private static final Dimension WINDOW = new Dimension(1150, 780);
	/***/
	private static final int WINDOW_GAP = 12;
	/***/
	private static final double SPLIT_RATIO = 0.68;
	/***/
	private static final Dimension GRID = new Dimension(3, 15);
	/***/
	private static final int[] PADDING = { 10, 20, 30, 60 };
	/***/
	private static final String FONT = "Segoe UI";
	/***/
	private static final int[] FONT_SIZES = { 16, 18, 24 };
	/***/
	private static final Dimension[] DIMENSIONS = { new Dimension(110, 70), new Dimension(200, 40),
			new Dimension(100, 40) };
	/***/
	private static final int[] RATIOS = { 5, 4, 15 };

	// Core colors
	/***/
	private static final Color BG_WINDOW = new Color(25, 25, 35);
	/***/
	private static final Color BG_PANEL = new Color(40, 40, 60);
	/***/
	private static final Color BG_TOP = new Color(30, 30, 45);
	/***/
	private static final Color BACKGROUND = new Color(45, 45, 70);
	/***/
	private static final Color ACCENT_PRIMARY = new Color(0, 220, 255);
	/***/
	private static final Color ACCENT_BORDER = new Color(120, 120, 160);
	/***/
	private static final Color ROOM_BORDER = new Color(190, 190, 220);
	/***/
	private static final Color SUCCESS = new Color(40, 220, 90);
	/***/
	private static final Color WARNING = new Color(255, 200, 0);
	/***/
	private static final Color WRONG = new Color(220, 40, 40);
	/***/
	private static final Color EXIT_FILL = new Color(255, 215, 0, 80);
	/***/
	private static final Color EXIT_TEXT = new Color(255, 215, 0);
	/***/
	private static final Color PLAYER_COLOR = new Color(0, 210, 255);
	/***/
	private static final Color ROOM_COLOR = new Color(55, 55, 80);

	/***/
	private static final Border PANEL_BORDER = BorderFactory.createEmptyBorder(20, 120, 25, 120);

	/***/
	private static final Border TITLE_BORDER = BorderFactory.createEmptyBorder(15, 0, 10, 0);
	/***/
	private static final Border PROMPT_BORDER = BorderFactory.createEmptyBorder(20, 20, 30, 20);

	/***/
	private static final Border STATUS_BORDER = BorderFactory.createEmptyBorder(10, 0, 10, 0);

	/**
	 * 
	 */
	private final MazeController myController;

	/**
	 * 
	 */
	private final MazePanel myMazePanel;

	/**
	 * 
	 */
	private final QuestionPanel myQuestionPanel;

	/**
	 * 
	 */
	private JLabel myStatus;

	/**
	 * 
	 */
	private JButton myNorth;

	/**
	 * 
	 */
	private JButton mySouth;

	/**
	 * 
	 */
	private JButton myWest;

	/**
	 * 
	 */
	private JButton myEast;

	/**
	 * 
	 */
	private boolean myIndicator;

	/**
	 * 
	 */
	private boolean myEnding;

	/**
	 * Added to track the current room so updateButtons can check which doors exist.
	 */
	private TriviaRoom myCurrentRoom;

	/**
	 * 
	 * @param theController
	 */
	public MazeView(final MazeController theController) {
		super();
		this.myController = theController;
		setGui();
		final JPanel panel = new JPanel(new BorderLayout());
		setTitle(panel);
		setStatus();

		panel.add(myStatus, BorderLayout.SOUTH);
		add(panel, BorderLayout.NORTH);

		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		splitPane.setResizeWeight(SPLIT_RATIO);

		myMazePanel = new MazePanel();
		myQuestionPanel = new QuestionPanel();

		splitPane.setLeftComponent(myMazePanel);
		splitPane.setRightComponent(myQuestionPanel);

		add(splitPane, BorderLayout.CENTER);
		add(createDirectionPanel(), BorderLayout.SOUTH);
		setJMenuBar(createMenuBar());

		addKeyListener(new ArrowKeyListener());
		setFocusable(true);

		setVisible(true);
	}

	/**
	 * 
	 */
	private void setGui() {

		setTitle("Trivia Maze");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WINDOW);

		setLocationRelativeTo(null);
		setResizable(false);
		setBackground(BG_WINDOW);
		setLayout(new BorderLayout(WINDOW_GAP, WINDOW_GAP));

	}

	/**
	 * 
	 * @param thePanel
	 */
	private void setTitle(final JPanel thePanel) {

		final JPanel northPanel = thePanel;
		northPanel.setBackground(BG_TOP);

		final JLabel title = new JLabel("TRIVIA MAZE - Escape by Answering Correctly!", JLabel.CENTER);
		title.setFont(new Font(FONT, Font.BOLD, FONT_SIZES[2]));
		title.setForeground(ACCENT_PRIMARY);

		title.setBorder(TITLE_BORDER);

		northPanel.add(title, BorderLayout.NORTH);

	}

	/**
	 * 
	 */
	private void setStatus() {
		myStatus = new JLabel("Click an arrow to move • Answer questions on the right", JLabel.CENTER);
		myStatus.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
		myStatus.setForeground(Color.WHITE);
		myStatus.setBackground(BACKGROUND);
		myStatus.setOpaque(true);
		myStatus.setBorder(STATUS_BORDER);
	}

	// Menu.

	/**
	 * 
	 * @return
	 */
	private JMenuBar createMenuBar() {
		final JMenuBar bar = new JMenuBar();

		// --- File menu ---
		final JMenu gameMenu = new JMenu("File");

		final JMenuItem save = new JMenuItem("💾 Save Game");
		final JMenuItem load = new JMenuItem("📂 Load Game");
		final JMenuItem restart = new JMenuItem("🔄 Restart");
		final JMenuItem exit = new JMenuItem("Exit");

		save.addActionListener(e -> myController.saveGame());
		load.addActionListener(e -> myController.loadGame());
		restart.addActionListener(e -> myController.restartGame());
		exit.addActionListener(e -> System.exit(0));

		gameMenu.add(save);
		gameMenu.add(load);
		gameMenu.add(restart);
		gameMenu.addSeparator();
		gameMenu.add(exit);

		// --- Help menu ---
		final JMenu helpMenu = new JMenu("Help");

		final JMenuItem instructions = new JMenuItem("📖 Game Play Instructions");
		final JMenuItem about = new JMenuItem("ℹ️ About");

		instructions.addActionListener(e -> JOptionPane.showMessageDialog(this, "<html><div style='width:350px'>"
				+ "<h2>How to Play Trivia Maze</h2>" + "<b>Goal:</b> Navigate from the top-left entrance to the "
				+ "bottom-right exit (★).<br><br>" + "<b>Moving:</b> Use the North / South / East / West buttons "
				+ "or the arrow keys (WASD also works). Only buttons for directions "
				+ "that have a door will be active.<br><br>" + "<b>Doors:</b><br>"
				+ "&nbsp;&nbsp;🟡 <b>Yellow</b> — closed, answer a question to open<br>"
				+ "&nbsp;&nbsp;🟢 <b>Green</b> — open, walk through freely<br>"
				+ "&nbsp;&nbsp;🔴 <b>Red</b> — locked forever (wrong answer)<br><br>"
				+ "<b>Questions:</b> Answer in the panel on the right. "
				+ "Multiple choice, True/False, and Short Answer questions appear.<br><br>"
				+ "<b>Losing:</b> If all paths to the exit are blocked by locked " + "doors, the game is over.<br><br>"
				+ "<b>Save/Load:</b> Use File → Save Game to save your progress "
				+ "and File → Load Game to resume later." + "</div></html>", "Game Play Instructions",
				JOptionPane.PLAIN_MESSAGE));

		about.addActionListener(e -> JOptionPane.showMessageDialog(this,
				"<html><div style='width:300px; text-align:center'>" + "<h2>🎮 Trivia Maze</h2>"
						+ "<b>Version 1.0</b><br><br>" + "Created by:<br>"
						+ "<b>Peyton Laudanski &amp; Huy Le</b><br><br>" + "Built with Java Swing + SQLite<br>"
						+ "Uses MVC design pattern &amp; Memento save/load<br><br>"
						+ "<i>Answer trivia questions to unlock doors<br>" + "and find your way to the exit!</i>"
						+ "</div></html>",
				"About Trivia Maze", JOptionPane.PLAIN_MESSAGE));

		helpMenu.add(instructions);
		helpMenu.add(about); // The about menu added to the menu bar.
		bar.add(gameMenu);
		bar.add(helpMenu); // The help menu added to the menu bar.
		return bar;
	}

	// Movement.

	/**
	 * 
	 * @return
	 */
	private JPanel createDirectionPanel() {
		final JPanel outer = new JPanel(new BorderLayout());
		outer.setBackground(BG_TOP);

		final JPanel panel = new JPanel(new GridLayout(GRID.width, GRID.width, GRID.height, GRID.height));
		panel.setBackground(BG_TOP);
		panel.setBorder(PANEL_BORDER);

		myNorth = createDirBtn("↑ NORTH", Direction.NORTH);
		mySouth = createDirBtn("↓ SOUTH", Direction.SOUTH);
		myEast = createDirBtn("EAST →", Direction.EAST);
		myWest = createDirBtn("← WEST", Direction.WEST);

		panel.add(new JLabel());
		panel.add(myNorth);
		panel.add(new JLabel());
		panel.add(myWest);
		panel.add(new JLabel("YOU", SwingConstants.CENTER));
		panel.add(myEast);
		panel.add(new JLabel());
		panel.add(mySouth);
		panel.add(new JLabel());

		outer.add(panel, BorderLayout.CENTER);
		return outer;
	}

	/**
	 * 
	 * @param theTxt
	 * @param theDir
	 * @return
	 */
	private JButton createDirBtn(final String theTxt, final Direction theDir) {
		final JButton button = new JButton("<html>" + theTxt.replace(" ", "<br>") + "</html>");
		button.setFont(new Font(FONT, Font.BOLD, FONT_SIZES[0]));
		button.setPreferredSize(DIMENSIONS[0]);
		button.setFocusable(false);
		button.addActionListener(e -> tryMove(theDir));
		return button;
	}

	/**
	 * 
	 * @param theDir
	 */
	private void tryMove(final Direction theDir) {
		if (!myIndicator && !myEnding) {
			myController.requestMove(theDir);
		}
	}

	// Controller callback.

	/**
	 * Called on initial load, save/load, and restart. Controller passes the actual
	 * player position — never hardcoded to 0,0.
	 */
	public void displayMaze(final TriviaRoom[][] theRooms, final int thePlayerX, final int thePlayerY) {
		myEnding = false;
		myIndicator = false;
		myCurrentRoom = theRooms[thePlayerY][thePlayerX]; // Save the current room for door checks in updateButtons().
		myMazePanel.setRooms(theRooms, thePlayerX, thePlayerY);
		myQuestionPanel.clear();
		updateButtons();
		myMazePanel.repaint();
	}

	/**
	 * Called by controller after every move. Receives rooms and player coordinates
	 * — view never calls getPlayer().
	 */
	public void refreshView(final TriviaRoom[][] theRooms, final int thePlayerX, final int thePlayerY) {
		myCurrentRoom = theRooms[thePlayerY][thePlayerX]; // Update current room reference after every move.
		myMazePanel.setRooms(theRooms, thePlayerX, thePlayerY);
		myMazePanel.repaint();
		updateButtons();
	}

	/**
	 * 
	 * @param theQ
	 * @param theDir
	 */
	public void displayQuestion(final AbstractTriviaQuestion theQ, final Direction theDir) {
		myIndicator = true;
		myQuestionPanel.loadQuestion(theQ, theDir);
		updateButtons();
	}

	/**
	 * 
	 */
	public void clearQuestion() {
		myIndicator = false;
		myQuestionPanel.clear();
		updateButtons();
	}

	/***/
	public void showMessage(final String theMsg) {
		myStatus.setText(theMsg);
		myStatus.setForeground(Color.WHITE);
	}

	/***/
	public void showMessage(final String theMsg, final Color theColor) {
		myStatus.setText(theMsg);
		myStatus.setForeground(theColor);
	}

	/***/
	public void showWin() {
		myEnding = true;
		updateButtons();
		JOptionPane.showMessageDialog(this, "<html><h1 style='color:#00ff88'>🎉 CONGRATULATIONS! 🎉</h1><br>"
				+ "You escaped the Trivia Maze!</html>", "VICTORY!", JOptionPane.PLAIN_MESSAGE);
	}

	/***/
	public void showLose() {
		myEnding = true;
		updateButtons();
		JOptionPane.showMessageDialog(this,
				"<html><h2 style='color:#ff5555'>You are trapped!</h2><br>" + "All accessible doors are locked.</html>",
				"GAME OVER", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * New update: Now each button turns on or off based on if there is a door in
	 * that direction in the room. Before all four buttons would turn on or off
	 * together. This meant you could click on buttons for directions that didn't
	 * have a door. Now a button is only active when: 1. No question is currently
	 * being answered (myIndicator is false). 2. The game is not over (myEnding is
	 * false). 3. The current room actually has a door in that direction.
	 */
	private void updateButtons() {
		final boolean free = !myIndicator && !myEnding;
		// Updated buttons so now each direction checked independently against the
		// current room's doors.
		myNorth.setEnabled(free && myCurrentRoom != null && myCurrentRoom.hasDoor(Direction.NORTH));
		mySouth.setEnabled(free && myCurrentRoom != null && myCurrentRoom.hasDoor(Direction.SOUTH));
		myEast.setEnabled(free && myCurrentRoom != null && myCurrentRoom.hasDoor(Direction.EAST));
		myWest.setEnabled(free && myCurrentRoom != null && myCurrentRoom.hasDoor(Direction.WEST));
	}

	// Maze panel.

	/***/
	private final class MazePanel extends JPanel {

		private static final long serialVersionUID = 1L;

		/***/
		private TriviaRoom[][] myRooms;
		/***/
		private int myPlayerX;
		/***/
		private int myPlayerY;

		/***/
		private MazePanel() {
			super();
		}

		/***/
		public void setRooms(final TriviaRoom[][] theRooms, final int thePlayerX, final int thePlayerY) {
			myRooms = Arrays.copyOf(theRooms, theRooms.length);
			myPlayerX = thePlayerX;
			myPlayerY = thePlayerY;
		}

		@Override
		protected void paintComponent(final Graphics theGraphics) {
			super.paintComponent(theGraphics);
			if (myRooms == null) {
				return;
			}

			final Graphics2D graphics2D = (Graphics2D) theGraphics;
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			final int rows = myRooms.length;
			final int cols = myRooms[0].length;

			final int cell = Math.min((getWidth() - PADDING[3]) / cols, (getHeight() - PADDING[3]) / rows);
			final int someX = (getWidth() - cols * cell) / 2;
			final int someY = (getHeight() - rows * cell) / 2;

			// EXIT highlight (bottom-right).
			final int exitX = cols - 1;
			final int exitY = rows - 1;
			graphics2D.setColor(EXIT_FILL);
			graphics2D.fillRect(someX + exitX * cell, someY + exitY * cell, cell, cell);
			graphics2D.setColor(EXIT_TEXT);
			graphics2D.setFont(new Font(FONT, Font.BOLD, cell / RATIOS[1]));

			graphics2D.drawString("EXIT", someX + exitX * cell + cell / RATIOS[1], someY + exitY * cell + cell / 2);

			for (int y = 0; y < rows; y++) {
				for (int x = 0; x < cols; x++) {
					drawRoom(graphics2D, myRooms[y][x], someX + x * cell, someY + y * cell, cell);
				}
			}

			// Player drawn from coordinates pushed by controller.
			final int playerX = someX + myPlayerX * cell + cell / 2;
			final int playerY = someY + myPlayerY * cell + cell / 2;
			final int ratio = cell / RATIOS[1];
			graphics2D.setColor(PLAYER_COLOR);
			graphics2D.fillOval(playerX - ratio, playerY - ratio, ratio * 2, ratio * 2);
			graphics2D.setColor(Color.BLACK);
			graphics2D.setFont(new Font(FONT, Font.BOLD, ratio));
			graphics2D.drawString("★", playerX - ratio / 2, playerY + ratio / 2);
		}

		private void drawRoom(final Graphics2D theGraphics, final TriviaRoom theRoom, final int theX, final int theY,
				final int theS) {
			theGraphics.setColor(ROOM_COLOR);
			theGraphics.fillRect(theX, theY, theS, theS);
			theGraphics.setColor(ROOM_BORDER);
			theGraphics.setStroke(new BasicStroke(GRID.width));
			theGraphics.drawRect(theX, theY, theS, theS);

			final int margin = theS / RATIOS[0];
			final int thick = Math.max(6, theS / RATIOS[2]);
			theGraphics.setStroke(new BasicStroke(thick, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

			if (theRoom != null) {
				if (theRoom.hasDoor(Direction.NORTH)) {
					drawDoor(theGraphics, theX + margin, theY, theX + theS - margin, theY,
							theRoom.getDoor(Direction.NORTH));
				}
				if (theRoom.hasDoor(Direction.SOUTH)) {
					drawDoor(theGraphics, theX + margin, theY + theS, theX + theS - margin, theY + theS,
							theRoom.getDoor(Direction.SOUTH));
				}
				if (theRoom.hasDoor(Direction.EAST)) {
					drawDoor(theGraphics, theX + theS, theY + margin, theX + theS, theY + theS - margin,
							theRoom.getDoor(Direction.EAST));
				}
				if (theRoom.hasDoor(Direction.WEST)) {
					drawDoor(theGraphics, theX, theY + margin, theX, theY + theS - margin,
							theRoom.getDoor(Direction.WEST));
				}
			}
		}

		private void drawDoor(final Graphics2D theGraphics, final int theFirstX, final int theFirstY,
				final int theSecondX, final int theSecondY, final TriviaDoor theDoor) {
			if (theDoor == null) {
				return;
			}
			final Color color = switch (theDoor.getState()) {
			case OPEN -> SUCCESS;
			case CLOSED -> WARNING;
			case LOCKED -> WRONG;
			};
			theGraphics.setColor(color);
			theGraphics.drawLine(theFirstX, theFirstY, theSecondX, theSecondY);
		}
	}

	// Question Panel.

	/**
	 * 
	 */
	private final class QuestionPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		private JLabel myPrompt;

		/**
		 * 
		 */
		private final JPanel myAnswerArea;

		/**
		 * 
		 */
		private Direction myDirection;

		/**
		 * 
		 */
		private QuestionPanel() {
			super();
			setLayout(new BorderLayout(0, PADDING[0]));
			setBackground(BG_PANEL);
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT_BORDER, RATIOS[1]),
					" QUESTION AREA ", 0, 0, new Font(FONT, Font.BOLD, FONT_SIZES[1]), Color.WHITE));

			setPrompt();

			myAnswerArea = new JPanel();
			myAnswerArea.setLayout(new BoxLayout(myAnswerArea, BoxLayout.Y_AXIS));
			myAnswerArea.setBackground(BG_PANEL);

			add(myPrompt, BorderLayout.NORTH);
			add(myAnswerArea, BorderLayout.CENTER);
		}

		private void setPrompt() {
			myPrompt = new JLabel("", JLabel.CENTER);
			myPrompt.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
			myPrompt.setForeground(Color.WHITE);
			myPrompt.setBorder(PROMPT_BORDER);
			myPrompt.setHorizontalAlignment(SwingConstants.CENTER);
			myPrompt.setVerticalAlignment(SwingConstants.TOP);
		}

		/**
		 * 
		 * @param theQ
		 * @param theDir
		 */
		public void loadQuestion(final AbstractTriviaQuestion theQ, final Direction theDir) {
			myDirection = theDir;
			myPrompt.setText("<html><div style='text-align:center;width:100%;max-width:400px'>" + theQ.getQuestion()
					+ "</div></html>");
			myAnswerArea.removeAll();

			if (theQ instanceof ShortQuestion) {
				createShort();
			} else if (theQ instanceof TrueFalseQuestion) {
				createTF();
			} else if (theQ instanceof MultipleChoiceQuestion) {
				createMC((MultipleChoiceQuestion) theQ);
			}

			myAnswerArea.revalidate();
			myAnswerArea.repaint();
		}

		/**
		 * 
		 */
		private void createShort() {
			final JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
			row.setBackground(BG_PANEL);

			final JTextField textField = new JTextField(15);
			textField.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
			textField.setPreferredSize(DIMENSIONS[1]);

			final JButton submit = new JButton("Submit");
			submit.setPreferredSize(DIMENSIONS[2]);
			submit.setFont(new Font(FONT, Font.BOLD, FONT_SIZES[0]));

			textField.addActionListener(e -> myController.submitAnswer(textField.getText().trim(), myDirection));
			submit.addActionListener(e -> myController.submitAnswer(textField.getText().trim(), myDirection));

			row.add(textField);
			row.add(submit);
			myAnswerArea.add(Box.createVerticalStrut(PADDING[0]));
			myAnswerArea.add(row);
		}

		/**
		 * 
		 */
		private void createTF() {
			final JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, RATIOS[2]));
			row.setBackground(BG_PANEL);

			final JButton tButton = new JButton("TRUE");
			final JButton fButton = new JButton("FALSE");
			tButton.setFont(new Font(FONT, Font.BOLD, PADDING[1]));
			fButton.setFont(new Font(FONT, Font.BOLD, PADDING[1]));

			tButton.addActionListener(e -> myController.submitAnswer("true", myDirection));
			fButton.addActionListener(e -> myController.submitAnswer("false", myDirection));

			row.add(tButton);
			row.add(fButton);
			myAnswerArea.add(row);
		}

		/**
		 * 
		 * @param theQ
		 */
		private void createMC(final MultipleChoiceQuestion theQ) {
			final List<String> opts = theQ.getChoices();
			final JPanel row = new JPanel(new GridLayout(opts.size(), 1, RATIOS[0], 12));
			row.setBackground(BG_PANEL);

			final ButtonGroup buttonGroup = new ButtonGroup();
			for (final String choice : opts) {
				final JRadioButton radioButton = new JRadioButton(choice);
				radioButton.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
				radioButton.setForeground(Color.WHITE);
				radioButton.setBackground(BG_PANEL);
				buttonGroup.add(radioButton);
				row.add(radioButton);
				radioButton.addActionListener(e -> myController.submitAnswer(choice, myDirection));
			}
			myAnswerArea.add(row);
		}

		/**
		 * 
		 */
		public void clear() {
			myPrompt.setText("");
			myAnswerArea.removeAll();
			myAnswerArea.revalidate();
			myAnswerArea.repaint();
		}
	}

	// Keyboard.

	/***/
	private final class ArrowKeyListener extends KeyAdapter {

		/***/
		private ArrowKeyListener() {
			super();
		}

		@Override
		public void keyPressed(final KeyEvent theEvent) {
			if (!myIndicator && !myEnding) {
				switch (theEvent.getKeyCode()) {
				case KeyEvent.VK_UP, KeyEvent.VK_W -> tryMove(Direction.NORTH);
				case KeyEvent.VK_DOWN, KeyEvent.VK_S -> tryMove(Direction.SOUTH);
				case KeyEvent.VK_LEFT, KeyEvent.VK_A -> tryMove(Direction.WEST);
				case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> tryMove(Direction.EAST);
				}
			}
		}
	}
}