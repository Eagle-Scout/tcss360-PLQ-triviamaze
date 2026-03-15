package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.TrueFalseQuestion;

/**
 * Tests for TrueFalseQuestion.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class TrueFalseQuestionTest {

    /***/
    private TrueFalseQuestion myTrueQ;
    /***/
    private TrueFalseQuestion myFalseQ;

    private TrueFalseQuestionTest() {
        setUp();
    }

    @BeforeEach
    void setUp() {
        myTrueQ = new TrueFalseQuestion("The sky is blue.", 1, true);
        myFalseQ = new TrueFalseQuestion("The sky is green.", 2, false);
    }

    // checkAnswer()

    @Test
    void testCorrectAnswerTrue() {
        assertTrue(myTrueQ.checkAnswer("true"));
    }

    @Test
    void testCorrectAnswerFalse() {
        assertTrue(myFalseQ.checkAnswer("false"));
    }

    @Test
    void testWrongAnswerForTrue() {
        assertFalse(myTrueQ.checkAnswer("false"));
    }

    @Test
    void testWrongAnswerForFalse() {
        assertFalse(myFalseQ.checkAnswer("true"));
    }

    @Test
    void testCaseSensitiveUppercaseFails() {
        // checkAnswer uses .equals(), not equalsIgnoreCase
        assertFalse(myTrueQ.checkAnswer("True"));
        assertFalse(myTrueQ.checkAnswer("TRUE"));
    }

    @Test
    void testEmptyStringFails() {
        assertFalse(myTrueQ.checkAnswer(""));
    }

    @Test
    void testUnrelatedStringFails() {
        assertFalse(myTrueQ.checkAnswer("yes"));
    }

    // isCorrect()

    @Test
    void testIsCorrectTrue() {
        assertTrue(myTrueQ.isCorrect());
    }

    @Test
    void testIsCorrectFalse() {
        assertFalse(myFalseQ.isCorrect());
    }

    // getQuestion() / getID()

    @Test
    void testGetQuestion() {
        assertEquals("The sky is blue.", myTrueQ.getQuestion());
    }

    @Test
    void testGetID() {
        assertEquals(1, myTrueQ.getID());
    }

    @Test
    void testGetIDSecondQuestion() {
        assertEquals(2, myFalseQ.getID());
    }
}