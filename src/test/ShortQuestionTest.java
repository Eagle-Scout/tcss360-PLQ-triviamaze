package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShortQuestion;

/**
 * Tests for ShortQuestion.
 * 
 * @author peytonlaudanski
 * @version 1
 */
final class ShortQuestionTest {

    /***/
    private ShortQuestion myQuestion;

    private ShortQuestionTest() {
        setUp();
    }

    @BeforeEach
    void setUp() {
        myQuestion = new ShortQuestion("What is 2+2?", 1, "4");
    }

    // checkAnswer()

    @Test
    void testCorrectAnswer() {
        assertTrue(myQuestion.checkAnswer("4"));
    }

    @Test
    void testWrongAnswer() {
        assertFalse(myQuestion.checkAnswer("5"));
    }

    @Test
    void testCaseInsensitiveCorrect() {
        final ShortQuestion q = new ShortQuestion("What color is the sky?", 2, "Blue");
        assertTrue(q.checkAnswer("blue"));
        assertTrue(q.checkAnswer("BLUE"));
        assertTrue(q.checkAnswer("Blue"));
    }

    @Test
    void testNullAnswerReturnsFalse() {
        assertFalse(myQuestion.checkAnswer(null));
    }

    @Test
    void testEmptyAnswerReturnsFalse() {
        assertFalse(myQuestion.checkAnswer(""));
    }

    @Test
    void testUnrelatedAnswerReturnsFalse() {
        assertFalse(myQuestion.checkAnswer("banana"));
    }

    // Konami code easter egg

    @Test
    void testKonamiCodeLowercase() {
        assertTrue(myQuestion.checkAnswer("uuddlrlrba"));
    }

    @Test
    void testKonamiCodeUppercase() {
        assertTrue(myQuestion.checkAnswer("UUDDLRLRBA"));
    }

    @Test
    void testKonamiCodeMixedCase() {
        assertTrue(myQuestion.checkAnswer("UuDdLrLrBa"));
    }

    @Test
    void testPartialKonamiCodeFails() {
        assertFalse(myQuestion.checkAnswer("uudd"));
    }

    // getAnswer() / getQuestion() / getID()

    @Test
    void testGetAnswer() {
        assertEquals("4", myQuestion.getAnswer());
    }

    @Test
    void testGetQuestion() {
        assertEquals("What is 2+2?", myQuestion.getQuestion());
    }

    @Test
    void testGetID() {
        assertEquals(1, myQuestion.getID());
    }
}