package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.MultipleChoiceQuestion;

/**
 * Tests for MultipleChoiceQuestion.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class MultipleChoiceQuestionTest {

    /***/
    private List<String> myChoices;
    /***/
    private MultipleChoiceQuestion myQuestion;

    private MultipleChoiceQuestionTest() {
        setUp();
    }

    @BeforeEach
    void setUp() {
        myChoices = Arrays.asList("Paris", "London", "Berlin", "Madrid");
        myQuestion
            = new MultipleChoiceQuestion("What is the capital of France?", 1, myChoices, 0);
    }

    // checkAnswer()

    @Test
    void testCorrectAnswerFirstChoice() {
        assertTrue(myQuestion.checkAnswer("Paris"));
    }

    @Test
    void testWrongAnswer() {
        assertFalse(myQuestion.checkAnswer("London"));
    }

    @Test
    void testWrongAnswerAnotherChoice() {
        assertFalse(myQuestion.checkAnswer("Berlin"));
    }

    @Test
    void testCaseSensitiveFails() {
        // checkAnswer uses .equals() — case matters
        assertFalse(myQuestion.checkAnswer("paris"));
        assertFalse(myQuestion.checkAnswer("PARIS"));
    }

    @Test
    void testCorrectAnswerLastIndex() {
        final MultipleChoiceQuestion q
            = new MultipleChoiceQuestion("Last choice correct", 2, myChoices, 3);
        assertTrue(q.checkAnswer("Madrid"));
    }

    @Test
    void testCorrectAnswerMiddleIndex() {
        final MultipleChoiceQuestion q
            = new MultipleChoiceQuestion("Middle choice correct", 3, myChoices, 2);
        assertTrue(q.checkAnswer("Berlin"));
    }

    // Edge cases — invalid index

    @Test
    void testInvalidIndexTooHighThrows() {
        final MultipleChoiceQuestion q
            = new MultipleChoiceQuestion("Bad index", 4, myChoices, 99);
        assertThrows(IllegalStateException.class, () -> q.checkAnswer("Paris"));
    }

    @Test
    void testNegativeIndexThrows() {
        final MultipleChoiceQuestion q
            = new MultipleChoiceQuestion("Negative index", 5, myChoices, -1);
        assertThrows(IllegalStateException.class, () -> q.checkAnswer("Paris"));
    }

    // getChoices() / getIndex() / getQuestion() / getID()

    @Test
    void testGetChoices() {
        assertEquals(myChoices, myQuestion.getChoices());
    }

    @Test
    void testGetChoicesSize() {
        assertEquals(4, myQuestion.getChoices().size());
    }

    @Test
    void testGetIndex() {
        assertEquals(0, myQuestion.getIndex());
    }

    @Test
    void testGetQuestion() {
        assertEquals("What is the capital of France?", myQuestion.getQuestion());
    }

    @Test
    void testGetID() {
        assertEquals(1, myQuestion.getID());
    }
}