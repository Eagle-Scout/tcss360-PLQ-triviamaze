package model;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
@FunctionalInterface
public interface QuestionFactory {

    /**
     * 
     * @return
     */
    AbstractTriviaQuestion createQuestion();
}