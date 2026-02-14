package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public interface QuestionFactory {

    /**
     * 
     * @return
     */
    AbstractTriviaQuestion createQuestion();
}