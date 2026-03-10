package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import control.MazeController;
import model.AbstractTriviaQuestion;
import model.Direction;
import model.MultipleChoiceQuestion;
import model.ShortQuestion;
import model.TrueFalseQuestion;

/**
 * Panel that displays trivia questions and collects answers.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class QuestionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Padding values. */
    private static final int[] PADDING = {10, 20, 30, 60};

    /** Layout ratios. */
    private static final int[] RATIOS = {5, 4, 15};

    /** Font name. */
    private static final String FONT = "Segoe UI";

    /** Font sizes. */
    private static final int[] FONT_SIZES = {16, 18, 24};

    /** Component dimensions. */
    private static final Dimension[] DIMENSIONS
        = {new Dimension(110, 70), new Dimension(200, 40), new Dimension(100, 40)};

    /** Closing tags shared by all HTML dialog strings. */
    private static final String HTML_CLOSE = "</div></html>";

    /** Panel background color. */
    private static final Color BG_PANEL = new Color(40, 40, 60);

    /** Border accent color. */
    private static final Color ACCENT_BORDER = new Color(120, 120, 160);

    /** Prompt border insets. */
    private static final Border PROMPT_BORDER
        = BorderFactory.createEmptyBorder(20, 20, 30, 20);

    /***/
    private static final int HALF = 2;

    /** Label showing the question text. */
    private JLabel myPrompt;

    /** Container for answer widgets. */
    private final JPanel myAnswerArea;

    /** The direction the player is trying to move. */
    private Direction myDirection;

    /***/
    private final transient MazeController myController;

    /**
     * Constructs and lays out the question panel.
     * 
     * @param theController
     */
    QuestionPanel(final MazeController theController) {
        super();
        myController = theController;

        setPanel();

        myAnswerArea = new JPanel();
        myAnswerArea.setLayout(new BoxLayout(myAnswerArea, BoxLayout.Y_AXIS));
        myAnswerArea.setBackground(BG_PANEL);

        add(myPrompt, BorderLayout.NORTH);
        add(myAnswerArea, BorderLayout.CENTER);
    }

    /**
     * 
     */
    private void setPanel() {
        setLayout(new BorderLayout(0, PADDING[0]));
        setBackground(BG_PANEL);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_BORDER, RATIOS[1]), " QUESTION AREA ", 0,
                0, new Font(FONT, Font.BOLD, FONT_SIZES[1]), Color.WHITE));

        setPrompt();
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
    public void loadQuestion(final AbstractTriviaQuestion theQ, final Direction theDir) {
        myDirection = theDir;
        myPrompt.setText("<html><div style='text-align:center;"
                + "width:100%;max-width:400px'>" + theQ.getQuestion() + HTML_CLOSE);
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
                _ -> myController.submitAnswer(textField.getText().trim(), myDirection));
        submit.addActionListener(
                _ -> myController.submitAnswer(textField.getText().trim(), myDirection));

        row.add(textField);
        row.add(submit);
        myAnswerArea.add(Box.createVerticalStrut(PADDING[0]));
        myAnswerArea.add(row);
    }

    /** Adds TRUE / FALSE buttons. */
    private void createTF() {
        final JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, RATIOS[HALF]));
        row.setBackground(BG_PANEL);

        final JButton tButton = new JButton("TRUE");
        final JButton fButton = new JButton("FALSE");
        tButton.setFont(new Font(FONT, Font.BOLD, PADDING[1]));
        fButton.setFont(new Font(FONT, Font.BOLD, PADDING[1]));

        tButton.addActionListener(_ -> myController.submitAnswer("true", myDirection));
        fButton.addActionListener(_ -> myController.submitAnswer("false", myDirection));

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
            radioButton.addActionListener(_ -> myController.submitAnswer(choice, myDirection));
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