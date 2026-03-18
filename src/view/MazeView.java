package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import control.TriviaGame;
import model.AbstractTriviaQuestion;
import model.Direction;
import model.DoorState;
import model.TriviaRoom;

/**
 * Main GUI window for the Trivia Maze game.
 *
 * @author huyle
 * @version 6
 */
public final class MazeView extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Grid layout dimensions (rows x gap). */
    private static final Dimension GRID = new Dimension(3, 15);

    /** Font name. */
    private static final String FONT = "Segoe UI";

    /** Font sizes. */
    private static final int[] FONT_SIZES = {16, 18, 24};

    /** Component dimensions. */
    private static final Dimension[] DIMENSIONS
        = {new Dimension(110, 70), new Dimension(200, 40), new Dimension(100, 40)};

    /** Closing tags shared by all HTML dialog strings. */
    private static final String HTML_CLOSE = "</div></html>";

    /** Window dimensions. */
    private static final Dimension WINDOW = new Dimension(1150, 780);

    /** Gap between window components. */
    private static final int WINDOW_GAP = 12;

    /** Split pane ratio. */
    private static final double SPLIT_RATIO = 0.68;

    // CORE COLORS.
    /** Window background color. */
    private static final Color BG_WINDOW = new Color(25, 25, 35);

    /** Top bar background color. */
    private static final Color BG_TOP = new Color(30, 30, 45);

    /** General background color. */
    private static final Color BACKGROUND = new Color(45, 45, 70);

    /** Primary accent color. */
    private static final Color ACCENT_PRIMARY = new Color(0, 220, 255);

    /** Panel border insets. */
    private static final Border PANEL_BORDER
        = BorderFactory.createEmptyBorder(20, 120, 25, 120);

    /** Title border insets. */
    private static final Border TITLE_BORDER = BorderFactory.createEmptyBorder(15, 0, 10, 0);

    /** Status bar border insets. */
    private static final Border STATUS_BORDER = BorderFactory.createEmptyBorder(10, 0, 10, 0);

    /** Magic number: half divider for centering. */
    private static final int HALF = 2;

    /** The maze drawing panel. */
    private final MazePanel myMazePanel;

    /** The question display panel. */
    private final QuestionPanel myQuestionPanel;

    /** The game controller. */
    private final transient TriviaGame myGame;

    /** Status bar label. */
    private JLabel myStatus;

    /** North movement button. */
    private JButton myNorth;

    /** South movement button. */
    private JButton mySouth;

    /** West movement button. */
    private JButton myWest;

    /** East movement button. */
    private JButton myEast;

    /** True when a question is currently active. */
    private boolean myIndicator;

    /** True when the game has ended (win or lose). */
    private boolean myEnding;

    /**
     * Stores the player's current room. Updated each time the player moves so updateButtons()
     * can enable or disable the correct directional buttons.
     */
    private TriviaRoom myRoom;

    /**
     * Constructs the main view and attaches it to the given controller.
     *
     * @param theController the game controller
     */
    public MazeView(final TriviaGame theController) {
        super();
        myGame = theController;
        myMazePanel = new MazePanel();
        myQuestionPanel = new QuestionPanel(myGame);

        setGui();
        buildPanels();

    }

    /** Configures basic window properties. */
    private void setGui() {

        setTitle("Trivia Maze");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW);
        setBackground(BG_WINDOW);
        setLayout(new BorderLayout(WINDOW_GAP, WINDOW_GAP));
        setFocusable(true);
        setVisible(true);
        setJMenuBar(createMenuBar());
        add(createDirectionPanel(), BorderLayout.SOUTH);
        addKeyListener(new ArrowKeyListener());
    }

    /**
     * Builds and adds the title + status bar panel to the north slot. Builds the split pane
     * holding the maze and question panels.
     */
    private void buildPanels() {
        final JPanel panel = new JPanel(new BorderLayout());
        final JLabel title
            = new JLabel("TRIVIA MAZE - Escape by Answering Correctly!", JLabel.CENTER);
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

        myStatus = new JLabel("Click an arrow to move \u2022 Answer questions on the right",
                JLabel.CENTER);
        myStatus.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
        myStatus.setForeground(Color.WHITE);
        myStatus.setBackground(BACKGROUND);
        myStatus.setOpaque(true);
        myStatus.setBorder(STATUS_BORDER);

        title.setFont(new Font(FONT, Font.BOLD, FONT_SIZES[HALF]));
        title.setForeground(ACCENT_PRIMARY);
        title.setBorder(TITLE_BORDER);

        panel.setBackground(BG_TOP);
        panel.add(title, BorderLayout.NORTH);
        panel.add(myStatus, BorderLayout.SOUTH);

        splitPane.setResizeWeight(SPLIT_RATIO);
        splitPane.setLeftComponent(myMazePanel);
        splitPane.setRightComponent(myQuestionPanel);

        add(panel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Builds and returns the application menu bar.
     * 
     * @param frame
     *
     * @return the constructed JMenuBar
     */
    private JMenuBar createMenuBar() {
        final JMenuBar bar = new JMenuBar();

        final JMenu gameMenu = new JMenu("File");

        final JMenuItem save = new JMenuItem("\uD83D\uDCBE Save Game");
        final JMenuItem load = new JMenuItem("\uD83D\uDCC2 Load Game");
        final JMenuItem restart = new JMenuItem("\uD83D\uDD04 Restart");
        final JMenuItem exit = new JMenuItem("Exit");

        save.addActionListener(_ -> myGame.saveGame());
        load.addActionListener(_ -> myGame.loadGame());
        restart.addActionListener(_ -> myGame.restartGame());
        exit.addActionListener(_ -> myGame.endGame());

        gameMenu.add(save);
        gameMenu.add(load);
        gameMenu.add(restart);
        gameMenu.addSeparator();
        gameMenu.add(exit);

        final JMenu helpMenu = new JMenu("Help");

        final JMenuItem instructions = new JMenuItem("\uD83D\uDCD6 Game Play Instructions");
        final JMenuItem about = new JMenuItem("\u2139\uFE0F About");

        instructions.addActionListener(_ -> {
            final String msg
                = "<html><div style='width:350px'>" + "<h2>How to Play Trivia Maze</h2>"
                        + "<b>Goal:</b> Navigate from the top-left entrance to the "
                        + "bottom-right exit.<br><br>"
                        + "<b>Moving:</b> Use the North / South / East / West buttons "
                        + "or the arrow keys (WASD also works). Only buttons for "
                        + "directions that have a door will be active.<br><br>"
                        + "<b>Doors:</b><br>"
                        + "&nbsp;&nbsp;\uD83D\uDFE1 <b>Yellow</b> \u2014 closed, "
                        + "answer a question to open<br>"
                        + "&nbsp;&nbsp;\uD83D\uDFE2 <b>Green</b> \u2014 open, "
                        + "walk through freely<br>"
                        + "&nbsp;&nbsp;\uD83D\uDD34 <b>Red</b> \u2014 locked forever "
                        + "(wrong answer)<br><br>"
                        + "<b>Questions:</b> Answer in the panel on the right. "
                        + "Multiple choice, True/False, and Short Answer questions "
                        + "appear.<br><br>"
                        + "<b>Losing:</b> If all paths to the exit are blocked by "
                        + "locked doors, the game is over.<br><br>"
                        + "<b>Save/Load:</b> Use File \u2192 Save Game to save your "
                        + "progress and File \u2192 Load Game to resume later." + HTML_CLOSE;

            JOptionPane.showMessageDialog(this, msg, "Game Play Instructions",
                    JOptionPane.PLAIN_MESSAGE);
        });
        about.addActionListener(_ -> {
            final String msg = "<html><div style='width:300px; text-align:center'>"
                    + "<h2>\uD83C\uDFAE Trivia Maze</h2>" + "<b>Version 1.0</b><br><br>"
                    + "Authors:<br>" + "<b>Huy Le&nbsp;&nbsp;\u2022&nbsp;&nbsp;"
                    + "Peyton Laudanski&nbsp;&nbsp;\u2022&nbsp;&nbsp;"
                    + "Quinn Weinzoff</b><br><br>" + "Built with Java Swing + SQLite<br>"
                    + "Uses MVC design pattern &amp; Memento save/load<br><br>"
                    + "<i>Answer trivia questions to unlock doors<br>"
                    + "and find your way to the exit!</i>" + HTML_CLOSE;

            JOptionPane.showMessageDialog(this, msg, "About Trivia Maze",
                    JOptionPane.PLAIN_MESSAGE);
        });

        helpMenu.add(instructions);
        helpMenu.add(about);

        bar.add(gameMenu);
        bar.add(helpMenu);
        return bar;
    }

    /**
     * Creates the directional button panel shown at the bottom of the window.
     * 
     * @param
     *
     * @return the direction JPanel
     */
    private JPanel createDirectionPanel() {
        final JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_TOP);

        final JPanel panel
            = new JPanel(new GridLayout(GRID.width, GRID.width, GRID.height, GRID.height));
        panel.setBackground(BG_TOP);
        panel.setBorder(PANEL_BORDER);

        myNorth = createDirBtn("\u2191 NORTH", Direction.NORTH);
        mySouth = createDirBtn("\u2193 SOUTH", Direction.SOUTH);
        myEast = createDirBtn("EAST \u2192", Direction.EAST);
        myWest = createDirBtn("\u2190 WEST", Direction.WEST);

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
     * Creates a single directional button.
     *
     * @param theTxt label text
     * @param theDir direction this button triggers
     * @return the configured JButton
     */
    private JButton createDirBtn(final String theTxt, final Direction theDir) {
        final JButton button = new JButton("<html>" + theTxt.replace(" ", "<br>") + "</html>");
        button.setFont(new Font(FONT, Font.BOLD, FONT_SIZES[0]));
        button.setPreferredSize(DIMENSIONS[0]);
        button.setFocusable(false);
        button.addActionListener(_ -> {
            if (!myIndicator && !myEnding) {
                myGame.requestMove(theDir);
            }
        });
        return button;
    }

    /**
     * Called on initial load, save/load, and restart. Controller passes the actual player
     * position — never hardcoded to 0,0.
     *
     * @param theRooms   the full room grid
     * @param thePlayerX player column index
     * @param thePlayerY player row index
     */
    public void displayMaze(final TriviaRoom[][] theRooms, final int thePlayerX,
            final int thePlayerY) {
        myEnding = false;
        myIndicator = false;
        myRoom = theRooms[thePlayerY][thePlayerX];
        myMazePanel.setRooms(theRooms, thePlayerX, thePlayerY);
        myQuestionPanel.clear();
        updateButtons();
        myMazePanel.repaint();
    }

    /**
     * Called by controller after every move.
     *
     * @param theRooms   the full room grid
     * @param thePlayerX player column index
     * @param thePlayerY player row index
     */
    public void refreshView(final TriviaRoom[][] theRooms, final int thePlayerX,
            final int thePlayerY) {
        myRoom = theRooms[thePlayerY][thePlayerX];
        myMazePanel.setRooms(theRooms, thePlayerX, thePlayerY);
        myMazePanel.repaint();
        updateButtons();
    }

    /**
     * Displays a trivia question in the question panel.
     *
     * @param theQ   the question to display
     * @param theDir the direction the player is trying to move
     */
    public void displayQuestion(final AbstractTriviaQuestion theQ, final Direction theDir) {
        myIndicator = true;
        myQuestionPanel.loadQuestion(theQ, theDir);
        updateButtons();
    }

    /** Clears the question panel and re-enables movement. */
    public void clearQuestion() {
        myIndicator = false;
        myQuestionPanel.clear();
        updateButtons();
    }

    /**
     * Updates the status bar with a coloured message.
     *
     * @param theMsg   the message to display
     * @param theColor the foreground colour
     */
    public void showMessage(final String theMsg, final Color theColor) {
        myStatus.setText(theMsg);
        myStatus.setForeground(theColor);
    }

    /** Shows the victory dialog and locks movement. */
    public void showWin() {
        myEnding = true;
        updateButtons();
        JOptionPane.showMessageDialog(this,
                "<html><h1 style='color:#00ff88'>"
                        + "\uD83C\uDF89 CONGRATULATIONS! \uD83C\uDF89</h1><br>"
                        + "You escaped the Trivia Maze!</html>",
                "VICTORY!", JOptionPane.PLAIN_MESSAGE);
    }

    /** Shows the game-over dialog and locks movement. */
    public void showLose() {
        myEnding = true;
        updateButtons();
        JOptionPane.showMessageDialog(this,
                "<html><h2 style='color:#ff5555'>You are trapped!</h2><br>"
                        + "All accessible doors are locked.</html>",
                "GAME OVER", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Enables each direction button only when no question is active, the game is not over, and
     * the current room has a non-locked door in that direction.
     */
    private void updateButtons() {
        final boolean free = !myIndicator && !myEnding;
        myNorth.setEnabled(canMove(free, Direction.NORTH));
        mySouth.setEnabled(canMove(free, Direction.SOUTH));
        myEast.setEnabled(canMove(free, Direction.EAST));
        myWest.setEnabled(canMove(free, Direction.WEST));
    }

    /**
     * Returns true when the player is free to move and the current room has a non-locked door
     * in the given direction.
     *
     * @param theFree true when no question is active and the game is not over
     * @param theDir  the direction to check
     * @return true if movement in that direction is allowed
     */
    private boolean canMove(final boolean theFree, final Direction theDir) {
        return theFree && myRoom != null && myRoom.hasDoor(theDir)
                && myRoom.getDoor(theDir).getState() != DoorState.LOCKED;
    }

    /** Keyboard listener that maps arrow/WASD keys to movement. */
    private final class ArrowKeyListener extends KeyAdapter {

        /***/
        private static final Map<Integer, Direction> KEY_TO_DIRECTION
            = new ConcurrentHashMap<>();

        /** Constructs the listener. */
        private ArrowKeyListener() {
            super();

            KEY_TO_DIRECTION.put(KeyEvent.VK_UP, Direction.NORTH);
            KEY_TO_DIRECTION.put(KeyEvent.VK_W, Direction.NORTH);
            KEY_TO_DIRECTION.put(KeyEvent.VK_DOWN, Direction.SOUTH);
            KEY_TO_DIRECTION.put(KeyEvent.VK_S, Direction.SOUTH);
            KEY_TO_DIRECTION.put(KeyEvent.VK_LEFT, Direction.WEST);
            KEY_TO_DIRECTION.put(KeyEvent.VK_A, Direction.WEST);
            KEY_TO_DIRECTION.put(KeyEvent.VK_RIGHT, Direction.EAST);
            KEY_TO_DIRECTION.put(KeyEvent.VK_D, Direction.EAST);
        }

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            final Direction dir = KEY_TO_DIRECTION.get(theEvent.getKeyCode());

            if (!myIndicator && !myEnding && dir != null) {
                myGame.requestMove(dir);
            }

        }
    }
}