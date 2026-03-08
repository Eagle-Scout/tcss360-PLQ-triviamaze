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
import model.DoorState;
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

    /** Window dimensions. */
    private static final Dimension WINDOW = new Dimension(1150, 780);

    /** Gap between window components. */
    private static final int WINDOW_GAP = 12;

    /** Split pane ratio. */
    private static final double SPLIT_RATIO = 0.68;

    /** Grid layout dimensions (rows x gap). */
    private static final Dimension GRID = new Dimension(3, 15);

    /** Padding values. */
    private static final int[] PADDING = {10, 20, 30, 60};

    /** Font name. */
    private static final String FONT = "Segoe UI";

    /** Font sizes. */
    private static final int[] FONT_SIZES = {16, 18, 24};

    /** Component dimensions. */
    private static final Dimension[] DIMENSIONS = {
        new Dimension(110, 70),
        new Dimension(200, 40),
        new Dimension(100, 40)
    };

    /** Layout ratios. */
    private static final int[] RATIOS = {5, 4, 15};

    // CORE COLORS.
    /** Window background color. */
    private static final Color BG_WINDOW = new Color(25, 25, 35);

    /** Panel background color. */
    private static final Color BG_PANEL = new Color(40, 40, 60);

    /** Top bar background color. */
    private static final Color BG_TOP = new Color(30, 30, 45);

    /** General background color. */
    private static final Color BACKGROUND = new Color(45, 45, 70);

    /** Primary accent color. */
    private static final Color ACCENT_PRIMARY = new Color(0, 220, 255);

    /** Border accent color. */
    private static final Color ACCENT_BORDER = new Color(120, 120, 160);

    /** Room border color. */
    private static final Color ROOM_BORDER = new Color(190, 190, 220);

    /** Success/open door color. */
    private static final Color SUCCESS = new Color(40, 220, 90);

    /** Warning/closed door color. */
    private static final Color WARNING = new Color(255, 200, 0);

    /** Wrong/locked door color. */
    private static final Color WRONG = new Color(220, 40, 40);

    /** Exit fill color. */
    private static final Color EXIT_FILL = new Color(255, 215, 0, 80);

    /** Exit text color. */
    private static final Color EXIT_TEXT = new Color(255, 215, 0);

    /** Player dot color. */
    private static final Color PLAYER_COLOR = new Color(0, 210, 255);

    /** Room fill color. */
    private static final Color ROOM_COLOR = new Color(55, 55, 80);

    /** Panel border insets. */
    private static final Border PANEL_BORDER =
        BorderFactory.createEmptyBorder(20, 120, 25, 120);

    /** Title border insets. */
    private static final Border TITLE_BORDER =
        BorderFactory.createEmptyBorder(15, 0, 10, 0);

    /** Prompt border insets. */
    private static final Border PROMPT_BORDER =
        BorderFactory.createEmptyBorder(20, 20, 30, 20);

    /** Status bar border insets. */
    private static final Border STATUS_BORDER =
        BorderFactory.createEmptyBorder(10, 0, 10, 0);

    /** Index of the largest padding value in the PADDING array. */
    private static final int PADDING_LARGE = 3;

    /** Closing tags shared by all HTML dialog strings. */
    private static final String HTML_CLOSE = "</div></html>";

    /** Magic number: half divider for centering. */
    private static final int HALF = 2;

    /** The game controller. */
    private final MazeController myController;

    /** The maze drawing panel. */
    private final MazePanel myMazePanel = new MazePanel();

    /** The question display panel. */
    private final QuestionPanel myQuestionPanel = new QuestionPanel();

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
     * Stores the player's current room. Updated each time the player moves so
     * updateButtons() can enable or disable the correct directional buttons.
     */
    private TriviaRoom myCurrentRoom;

    /**
     * Constructs the main view and attaches it to the given controller.
     *
     * @param theController the game controller
     */
    public MazeView(final MazeController theController) {
        super();
        this.myController = theController;
        setGui();
        buildNorthPanel();
        buildCenterPanel();
        add(createDirectionPanel(), BorderLayout.SOUTH);
        setJMenuBar(createMenuBar());
        addKeyListener(new ArrowKeyListener());
        setFocusable(true);
        setVisible(true);
    }

    /** Builds and adds the title + status bar panel to the north slot. */
    private void buildNorthPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        setTitle(panel);
        setStatus();
        panel.add(myStatus, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH);
    }

    /** Builds the split pane holding the maze and question panels. */
    private void buildCenterPanel() {
        final JSplitPane splitPane =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setResizeWeight(SPLIT_RATIO);
        splitPane.setLeftComponent(myMazePanel);
        splitPane.setRightComponent(myQuestionPanel);
        add(splitPane, BorderLayout.CENTER);
    }

    /** Configures basic window properties. */
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
     * Adds the title label to the given panel.
     *
     * @param thePanel the panel to add the title to
     */
    private void setTitle(final JPanel thePanel) {
        final JPanel northPanel = thePanel;
        northPanel.setBackground(BG_TOP);
        final JLabel title = new JLabel(
            "TRIVIA MAZE - Escape by Answering Correctly!", JLabel.CENTER);
        title.setFont(new Font(FONT, Font.BOLD, FONT_SIZES[HALF]));
        title.setForeground(ACCENT_PRIMARY);
        title.setBorder(TITLE_BORDER);
        northPanel.add(title, BorderLayout.NORTH);
    }

    /** Initializes the status bar label. */
    private void setStatus() {
        myStatus = new JLabel(
            "Click an arrow to move \u2022 Answer questions on the right",
            JLabel.CENTER);
        myStatus.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
        myStatus.setForeground(Color.WHITE);
        myStatus.setBackground(BACKGROUND);
        myStatus.setOpaque(true);
        myStatus.setBorder(STATUS_BORDER);
    }

    // MENU.
    /**
     * Builds and returns the application menu bar.
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

        save.addActionListener(e -> myController.saveGame());
        load.addActionListener(e -> myController.loadGame());
        restart.addActionListener(e -> myController.restartGame());
        exit.addActionListener(e -> System.exit(0));

        gameMenu.add(save);
        gameMenu.add(load);
        gameMenu.add(restart);
        gameMenu.addSeparator();
        gameMenu.add(exit);

        final JMenu helpMenu = new JMenu("Help");

        final JMenuItem instructions =
            new JMenuItem("\uD83D\uDCD6 Game Play Instructions");
        final JMenuItem about = new JMenuItem("\u2139\uFE0F About");

        instructions.addActionListener(e -> showInstructions());
        about.addActionListener(e -> showAbout());

        helpMenu.add(instructions);
        helpMenu.add(about);

        bar.add(gameMenu);
        bar.add(helpMenu);
        return bar;
    }

    /** Displays the instructions dialog. */
    private void showInstructions() {
        final String msg = "<html><div style='width:350px'>"
            + "<h2>How to Play Trivia Maze</h2>"
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
            + "progress and File \u2192 Load Game to resume later."
            + HTML_CLOSE;
        JOptionPane.showMessageDialog(
            this, msg, "Game Play Instructions", JOptionPane.PLAIN_MESSAGE);
    }

    /** Displays the About dialog. */
    private void showAbout() {
        final String msg = "<html><div style='width:300px; text-align:center'>"
            + "<h2>\uD83C\uDFAE Trivia Maze</h2>"
            + "<b>Version 1.0</b><br><br>"
            + "Authors:<br>"
            + "<b>Huy Le&nbsp;&nbsp;\u2022&nbsp;&nbsp;"
            + "Peyton Laudanski&nbsp;&nbsp;\u2022&nbsp;&nbsp;"
            + "Quinn Weinzoff</b><br><br>"
            + "Built with Java Swing + SQLite<br>"
            + "Uses MVC design pattern &amp; Memento save/load<br><br>"
            + "<i>Answer trivia questions to unlock doors<br>"
            + "and find your way to the exit!</i>"
            + HTML_CLOSE;
        JOptionPane.showMessageDialog(
            this, msg, "About Trivia Maze", JOptionPane.PLAIN_MESSAGE);
    }
    
    // MOVEMENT.
    /**
     * Creates the directional button panel shown at the bottom of the window.
     *
     * @return the direction JPanel
     */
    private JPanel createDirectionPanel() {
        final JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_TOP);

        final JPanel panel = new JPanel(
            new GridLayout(GRID.width, GRID.width, GRID.height, GRID.height));
        panel.setBackground(BG_TOP);
        panel.setBorder(PANEL_BORDER);

        myNorth = createDirBtn("\u2191 NORTH", Direction.NORTH);
        mySouth = createDirBtn("\u2193 SOUTH", Direction.SOUTH);
        myEast  = createDirBtn("EAST \u2192",  Direction.EAST);
        myWest  = createDirBtn("\u2190 WEST",  Direction.WEST);

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
        final JButton button = new JButton(
            "<html>" + theTxt.replace(" ", "<br>") + "</html>");
        button.setFont(new Font(FONT, Font.BOLD, FONT_SIZES[0]));
        button.setPreferredSize(DIMENSIONS[0]);
        button.setFocusable(false);
        button.addActionListener(e -> tryMove(theDir));
        return button;
    }

    /**
     * Attempts to move the player in the given direction.
     *
     * @param theDir the requested direction
     */
    private void tryMove(final Direction theDir) {
        if (!myIndicator && !myEnding) {
            myController.requestMove(theDir);
        }
    }

    // CONTROLLER CALLBACKS.
    /**
     * Called on initial load, save/load, and restart. Controller passes the
     * actual player position — never hardcoded to 0,0.
     *
     * @param theRooms    the full room grid
     * @param thePlayerX  player column index
     * @param thePlayerY  player row index
     */
    public void displayMaze(
            final TriviaRoom[][] theRooms,
            final int thePlayerX,
            final int thePlayerY) {
        myEnding = false;
        myIndicator = false;
        myCurrentRoom = theRooms[thePlayerY][thePlayerX];
        myMazePanel.setRooms(theRooms, thePlayerX, thePlayerY);
        myQuestionPanel.clear();
        updateButtons();
        myMazePanel.repaint();
    }

    /**
     * Called by controller after every move.
     *
     * @param theRooms    the full room grid
     * @param thePlayerX  player column index
     * @param thePlayerY  player row index
     */
    public void refreshView(
            final TriviaRoom[][] theRooms,
            final int thePlayerX,
            final int thePlayerY) {
        myCurrentRoom = theRooms[thePlayerY][thePlayerX];
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
    public void displayQuestion(
            final AbstractTriviaQuestion theQ, final Direction theDir) {
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
     * Updates the status bar with a plain white message.
     *
     * @param theMsg the message to display
     */
    public void showMessage(final String theMsg) {
        myStatus.setText(theMsg);
        myStatus.setForeground(Color.WHITE);
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
        JOptionPane.showMessageDialog(
            this,
            "<html><h1 style='color:#00ff88'>"
                + "\uD83C\uDF89 CONGRATULATIONS! \uD83C\uDF89</h1><br>"
                + "You escaped the Trivia Maze!</html>",
            "VICTORY!",
            JOptionPane.PLAIN_MESSAGE);
    }

    /** Shows the game-over dialog and locks movement. */
    public void showLose() {
        myEnding = true;
        updateButtons();
        JOptionPane.showMessageDialog(
            this,
            "<html><h2 style='color:#ff5555'>You are trapped!</h2><br>"
                + "All accessible doors are locked.</html>",
            "GAME OVER",
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Enables each direction button only when no question is active, the game
     * is not over, and the current room has a non-locked door in that direction.
     */
    private void updateButtons() {
        final boolean free = !myIndicator && !myEnding;
        myNorth.setEnabled(canMove(free, Direction.NORTH));
        mySouth.setEnabled(canMove(free, Direction.SOUTH));
        myEast.setEnabled(canMove(free, Direction.EAST));
        myWest.setEnabled(canMove(free, Direction.WEST));
    }

    /**
     * Returns true when the player is free to move and the current room has
     * a non-locked door in the given direction.
     *
     * @param theFree true when no question is active and the game is not over
     * @param theDir  the direction to check
     * @return true if movement in that direction is allowed
     */
    private boolean canMove(final boolean theFree, final Direction theDir) {
        return theFree
            && myCurrentRoom != null
            && myCurrentRoom.hasDoor(theDir)
            && myCurrentRoom.getDoor(theDir).getState() != DoorState.LOCKED;
    }

    // MAZEPANEL.
    /** Custom panel that draws the maze grid. */
    private final class MazePanel extends JPanel {

        private static final long serialVersionUID = 1L;

        /** The current room grid. */
        private TriviaRoom[][] myRooms;

        /** Player column index. */
        private int myPlayerX;

        /** Player row index. */
        private int myPlayerY;

        /** Constructs an empty MazePanel. */
        private MazePanel() {
            super();
        }

        /**
         * Sets the room grid and player position, then repaints.
         *
         * @param theRooms    the room grid
         * @param thePlayerX  player column
         * @param thePlayerY  player row
         */
        public void setRooms(
                final TriviaRoom[][] theRooms,
                final int thePlayerX,
                final int thePlayerY) {
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

            final Graphics2D g2 = (Graphics2D) theGraphics;
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            final int rows = myRooms.length;
            final int cols = myRooms[0].length;

            final int cell = Math.min(
                (getWidth()  - PADDING[PADDING_LARGE]) / cols,
                (getHeight() - PADDING[PADDING_LARGE]) / rows);
            final int offsetX = (getWidth()  - cols * cell) / HALF;
            final int offsetY = (getHeight() - rows * cell) / HALF;

            // EXIT highlight (bottom-right)
            final int exitCol = cols - 1;
            final int exitRow = rows - 1;
            g2.setColor(EXIT_FILL);
            g2.fillRect(
                offsetX + exitCol * cell,
                offsetY + exitRow * cell, cell, cell);
            g2.setColor(EXIT_TEXT);
            g2.setFont(new Font(FONT, Font.BOLD, cell / RATIOS[1]));
            g2.drawString("EXIT",
                offsetX + exitCol * cell + cell / RATIOS[1],
                offsetY + exitRow * cell + cell / HALF);

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    drawRoom(g2, myRooms[row][col],
                        offsetX + col * cell,
                        offsetY + row * cell, cell);
                }
            }

            // PLAYER MARKER.
            final int px = offsetX + myPlayerX * cell + cell / HALF;
            final int py = offsetY + myPlayerY * cell + cell / HALF;
            final int radius = cell / RATIOS[1];
            g2.setColor(PLAYER_COLOR);
            g2.fillOval(px - radius, py - radius, radius * HALF, radius * HALF);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font(FONT, Font.BOLD, radius));
            g2.drawString("\u2605", px - radius / HALF, py + radius / HALF);
        }

        private void drawRoom(
                final Graphics2D theG,
                final TriviaRoom theRoom,
                final int theX,
                final int theY,
                final int theSize) {
            theG.setColor(ROOM_COLOR);
            theG.fillRect(theX, theY, theSize, theSize);
            theG.setColor(ROOM_BORDER);
            theG.setStroke(new BasicStroke(GRID.width));
            theG.drawRect(theX, theY, theSize, theSize);

            final int margin = theSize / RATIOS[0];
            final int thick  = Math.max(6, theSize / RATIOS[HALF]);
            theG.setStroke(new BasicStroke(
                thick, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            if (theRoom != null) {
                drawAllDoors(theG, theRoom, theX, theY, theSize, margin);
            }
        }

        private void drawAllDoors(
                final Graphics2D theG,
                final TriviaRoom theRoom,
                final int theX, final int theY,
                final int theSize, final int theMargin) {
            if (theRoom.hasDoor(Direction.NORTH)) {
                drawDoor(theG,
                    theX + theMargin, theY,
                    theX + theSize - theMargin, theY,
                    theRoom.getDoor(Direction.NORTH));
            }
            if (theRoom.hasDoor(Direction.SOUTH)) {
                drawDoor(theG,
                    theX + theMargin, theY + theSize,
                    theX + theSize - theMargin, theY + theSize,
                    theRoom.getDoor(Direction.SOUTH));
            }
            if (theRoom.hasDoor(Direction.EAST)) {
                drawDoor(theG,
                    theX + theSize, theY + theMargin,
                    theX + theSize, theY + theSize - theMargin,
                    theRoom.getDoor(Direction.EAST));
            }
            if (theRoom.hasDoor(Direction.WEST)) {
                drawDoor(theG,
                    theX, theY + theMargin,
                    theX, theY + theSize - theMargin,
                    theRoom.getDoor(Direction.WEST));
            }
        }

        private void drawDoor(
                final Graphics2D theG,
                final int theX1, final int theY1,
                final int theX2, final int theY2,
                final TriviaDoor theDoor) {
            if (theDoor == null) {
                return;
            }
            final Color color = switch (theDoor.getState()) {
                case OPEN   -> SUCCESS;
                case CLOSED -> WARNING;
                case LOCKED -> WRONG;
            };
            theG.setColor(color);
            theG.drawLine(theX1, theY1, theX2, theY2);
        }
    }

    // QUESTIONPANEL.
    /** Panel that displays trivia questions and collects answers. */
    private final class QuestionPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        /** Label showing the question text. */
        private JLabel myPrompt;

        /** Container for answer widgets. */
        private final JPanel myAnswerArea;

        /** The direction the player is trying to move. */
        private Direction myDirection;

        /** Constructs and lays out the question panel. */
        private QuestionPanel() {
            super();
            setLayout(new BorderLayout(0, PADDING[0]));
            setBackground(BG_PANEL);
            setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_BORDER, RATIOS[1]),
                " QUESTION AREA ", 0, 0,
                new Font(FONT, Font.BOLD, FONT_SIZES[1]),
                Color.WHITE));

            setPrompt();

            myAnswerArea = new JPanel();
            myAnswerArea.setLayout(
                new BoxLayout(myAnswerArea, BoxLayout.Y_AXIS));
            myAnswerArea.setBackground(BG_PANEL);

            add(myPrompt, BorderLayout.NORTH);
            add(myAnswerArea, BorderLayout.CENTER);
        }

        /** Initializes the prompt label. */
        private void setPrompt() {
            myPrompt = new JLabel("", JLabel.CENTER);
            myPrompt.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
            myPrompt.setForeground(Color.WHITE);
            myPrompt.setBorder(PROMPT_BORDER);
            myPrompt.setHorizontalAlignment(SwingConstants.CENTER);
            myPrompt.setVerticalAlignment(SwingConstants.TOP);
        }

        /**
         * Loads a question into the panel.
         *
         * @param theQ   the question
         * @param theDir the direction the player is attempting
         */
        public void loadQuestion(
                final AbstractTriviaQuestion theQ, final Direction theDir) {
            myDirection = theDir;
            myPrompt.setText(
                "<html><div style='text-align:center;"
                + "width:100%;max-width:400px'>"
                + theQ.getQuestion() + HTML_CLOSE);
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

        /** Adds a short-answer text field and submit button. */
        private void createShort() {
            final JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
            row.setBackground(BG_PANEL);

            final JTextField textField = new JTextField(15);
            textField.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
            textField.setPreferredSize(DIMENSIONS[1]);

            final JButton submit = new JButton("Submit");
            submit.setPreferredSize(DIMENSIONS[HALF]);
            submit.setFont(new Font(FONT, Font.BOLD, FONT_SIZES[0]));

            textField.addActionListener(
                e -> myController.submitAnswer(
                    textField.getText().trim(), myDirection));
            submit.addActionListener(
                e -> myController.submitAnswer(
                    textField.getText().trim(), myDirection));

            row.add(textField);
            row.add(submit);
            myAnswerArea.add(Box.createVerticalStrut(PADDING[0]));
            myAnswerArea.add(row);
        }

        /** Adds TRUE / FALSE buttons. */
        private void createTF() {
            final JPanel row = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 50, RATIOS[HALF]));
            row.setBackground(BG_PANEL);

            final JButton tButton = new JButton("TRUE");
            final JButton fButton = new JButton("FALSE");
            tButton.setFont(new Font(FONT, Font.BOLD, PADDING[1]));
            fButton.setFont(new Font(FONT, Font.BOLD, PADDING[1]));

            tButton.addActionListener(
                e -> myController.submitAnswer("true", myDirection));
            fButton.addActionListener(
                e -> myController.submitAnswer("false", myDirection));

            row.add(tButton);
            row.add(fButton);
            myAnswerArea.add(row);
        }

        /**
         * Adds radio buttons for a multiple-choice question.
         *
         * @param theQ the multiple-choice question
         */
        private void createMC(final MultipleChoiceQuestion theQ) {
            final List<String> opts = theQ.getChoices();
            final JPanel row = new JPanel(
                new GridLayout(opts.size(), 1, RATIOS[0], 12));
            row.setBackground(BG_PANEL);

            final ButtonGroup buttonGroup = new ButtonGroup();
            for (final String choice : opts) {
                final JRadioButton rb = new JRadioButton(choice);
                rb.setFont(new Font(FONT, Font.PLAIN, FONT_SIZES[1]));
                rb.setForeground(Color.WHITE);
                rb.setBackground(BG_PANEL);
                buttonGroup.add(rb);
                row.add(rb);
                rb.addActionListener(
                    e -> myController.submitAnswer(choice, myDirection));
            }
            myAnswerArea.add(row);
        }

        /** Clears the question panel. */
        public void clear() {
            myPrompt.setText("");
            myAnswerArea.removeAll();
            myAnswerArea.revalidate();
            myAnswerArea.repaint();
        }
    }

    // ArrowKeyListener.
    /** Keyboard listener that maps arrow/WASD keys to movement. */
    private final class ArrowKeyListener extends KeyAdapter {
        /** Constructs the listener. */
        private ArrowKeyListener() {
            super();
        }
        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (!myIndicator && !myEnding) {
                switch (theEvent.getKeyCode()) {
                    case KeyEvent.VK_UP,    KeyEvent.VK_W ->
                        tryMove(Direction.NORTH);
                    case KeyEvent.VK_DOWN,  KeyEvent.VK_S ->
                        tryMove(Direction.SOUTH);
                    case KeyEvent.VK_LEFT,  KeyEvent.VK_A ->
                        tryMove(Direction.WEST);
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D ->
                        tryMove(Direction.EAST);
                    default -> { }
                }
            }
        }
    }
}