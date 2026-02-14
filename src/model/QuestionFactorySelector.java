package model;

import java.util.List;

/**
 * 
 * @author peytonlaudanski
 * @version 1
 */
public final class QuestionFactorySelector {

    /**
     * 
     */
    private QuestionFactorySelector() {

    }

    /**
     * 
     */
    public enum QuestionType {

        /**
         * 
         */
        MULTIPLE_CHOICE {
            @Override
            public QuestionFactory create(final String thePrompt, final int theID,
                    final Object theData) {
                final MultipleChoiceData mcData = (MultipleChoiceData) theData;
                return new MultipleChoiceQuestionFactory(thePrompt, theID, mcData.getChoices(),
                        mcData.getCorrectIndex());
            }
        },

        /**
         * 
         */
        SHORT_ANSWER {
            @Override
            public QuestionFactory create(final String thePrompt, final int theID,
                    final Object theData) {
                return new ShortQuestionFactory(thePrompt, theID, (String) theData);
            }
        },

        /**
         * 
         */
        TRUE_FALSE {
            @Override
            public QuestionFactory create(final String thePrompt, final int theID,
                    final Object theData) {
                return new TrueFalseQuestionFactory(thePrompt, theID, (Boolean) theData);
            }
        };

        /**
         * 
         * @param thePrompt
         * @param theID
         * @param theData
         * @return
         */
        public abstract QuestionFactory create(String thePrompt, int theID, Object theData);

    }

    /**
     * 
     * @param theType
     * @param thePrompt
     * @param theID
     * @param theData
     * @return
     */
    public static QuestionFactory createFactory(final QuestionType theType,
            final String thePrompt, final int theID, final Object theData) {

        return theType.create(thePrompt, theID, theData);
    }

    /**
     * 
     */
    public static final class MultipleChoiceData {

        /**
         * 
         */
        private final List<String> myChoices;

        /**
         * 
         */
        private final int myCorrectIndex;

        /**
         * 
         * @param theChoices
         * @param theCorrectIndex
         */
        public MultipleChoiceData(final List<String> theChoices, final int theCorrectIndex) {
            this.myChoices = theChoices;
            this.myCorrectIndex = theCorrectIndex;
        }

        /**
         * 
         * @return
         */
        public List<String> getChoices() {
            return myChoices;
        }

        /**
         * 
         * @return
         */
        public int getCorrectIndex() {
            return myCorrectIndex;
        }
    }
}