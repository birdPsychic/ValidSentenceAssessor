package main;

import java.util.ArrayList;
import java.util.logging.*;

import static org.junit.Assert.*;

import org.junit.jupiter.api.*;

public class ValidSentenceAssessorTest {

    protected ValidSentenceAssessor assessor;
    ArrayList<String> incorrectlyFailed;
    ArrayList<String> incorrectlyPassed;

    // Note: Please forgive the messy string construction, could improve this with ArrayUtils

    String[] validFullAssessmentStrings = { "The quick brown fox said \"hello Mr lazy dog\".",
            "The quick brown fox said hello Mr lazy dog.", "One lazy dog is too few, 13 is too many.",
            "One lazy dog is too few, thirteen is too many.", "How many \"lazy dogs\" are there?", "" };
    String[] invalidFullAssessmentStrings = { "The quick brown fox said \"hello Mr. lazy dog\".",
            "the quick brown fox said hello Mr lazy dog.", "\"The quick brown fox said \"hello Mr. lazy dog.\"",
            "One lazy dog is too few, 12 is too many.", "Are there 11, 12, or 13 lazy dogs?",
            "There is no punctuation in this sentence", null };

    String[] validCapitalStrings = { "Aaaaaaaaaaaaaaaa", "This is a complete sentence", "I",
            "K23482rhf9bf 9bfsdb fc!! sds!?.", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
            "The quick brown fox said \"hello Mr lazy dog\".", "The quick brown fox said hello Mr lazy dog.",
            "One lazy dog is too few, 13 is too many.", "One lazy dog is too few, thirteen is too many.",
            "How many \"lazy dogs\" are there?" };
    String[] invalidCapitalStrings = { "aaaaaaaaaaaaaaaa", "this is a complete sentence", "i",
            "k23482rhf9bf 9bfsdb fc!! sds!?.", "112DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD", ".", "!sucabA" };

    String[] validQuotationMarksStrings = { "Aaaaaaaaaaaaaaaa", "This i\"s a complete\" sentence", "I\"\"",
            "K23482rh\"\"f9bf 9bfsdb fc!! sds!?.", "\"DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD\"", "\"\"",
            "The quick brown fox said \"hello Mr lazy dog\".", "The quick brown fox said hello Mr lazy dog.",
            "One lazy dog is too few, 13 is too many.", "One lazy dog is too few, thirteen is too many.",
            "How many \"lazy dogs\" are there?" };
    String[] invalidQuotationMarkStrings = { "aaaaa\"aaaaaaaaaaa", "\"112DDDDDDDDDDD\"DDDDDDDDDDDDDDDDDDD\"", ".\"",
            "!\"s\"u\"cabA", "\"\"\"\"\"\"\"\"\"\"\"" };

    String[] validTerminationCharacterStrings = { "Aaaaaaaaaaaaaaaa.", "This is a complete sentence!", ".", "qwerty?",
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD!", "The quick brown fox said \"hello Mr lazy dog\".",
            "The quick brown fox said hello Mr lazy dog.", "One lazy dog is too few, 13 is too many.",
            "One lazy dog is too few, thirteen is too many.", "How many \"lazy dogs\" are there?" };
    String[] invalidTerminationCharacterStrings = { "aaaaaaaaaaaaaaaa", "This is a complete sentence??", "\"i.\"",
            "k23482rhf9bf 9bfsdb fc!! sds!?.", "!112DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD!", "...", "!suc?abA." };

    String[] validNumbersSpelledStrings = { "Aaaaaaaaaaaaaaaa.", "This is a complete sentence!", ".", "qwerty?", "one",
            "two", "three", "-1", "13", "-0", "-4", "56", "", "abacus123", "-\"2",
            "The quick brown fox said \"hello Mr lazy dog\".", "The quick brown fox said hello Mr lazy dog.",
            "One lazy dog is too few, 13 is too many.", "One lazy dog is too few, thirteen is too many.",
            "How many \"lazy dogs\" are there?" };
    String[] invalidNumbersSpelledStrings = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
            "minus 1", "one 2 three 4", "-1 1", "-3 -2 -1 0 1 2 3" };

    @BeforeEach
    protected void setUp() {
        assessor = new ValidSentenceAssessor(Level.WARNING);
        incorrectlyFailed = new ArrayList<>();
        incorrectlyPassed = new ArrayList<>();
    }

    // Each of the below test cases are essentially idential: if any string is incorrectly returned true or false by the tested method, they will be listed by the assertion
    // Could avoid this repetition with function pointers in C++

    @Test
    @DisplayName("String should start with a capital letter")
    void testStartsWithCapitalLetter() {

        // All of the strings in these arrays should return true
        for (String candidate : validCapitalStrings) {
            if (!assessor.startsWithCapitalLetter(candidate)) {
                incorrectlyFailed.add(candidate);
            }
        }

        // All of the strings in these arrays should return false
        for (String candidate : invalidCapitalStrings) {
            if (assessor.startsWithCapitalLetter(candidate)) {
                incorrectlyPassed.add(candidate);
            }
        }

        String errorMsg = createErrorMessage(incorrectlyFailed, incorrectlyPassed);
        assertTrue(errorMsg, errorMsg.isEmpty());
    }

    @Test
    @DisplayName("String has an even number of quotation marks")
    void testContainsEvenNumberOfQuotationMarks() {

        // All of the strings in these arrays should return true
        for (String candidate : validQuotationMarksStrings) {
            if (!assessor.containsEvenNumberOfQuotationMarks(candidate)) {
                incorrectlyFailed.add(candidate);
            }
        }

        // All of the strings in these arrays should return false
        for (String candidate : invalidQuotationMarkStrings) {
            if (assessor.containsEvenNumberOfQuotationMarks(candidate)) {
                incorrectlyPassed.add(candidate);
            }
        }

        String errorMsg = createErrorMessage(incorrectlyFailed, incorrectlyPassed);
        assertTrue(errorMsg, errorMsg.isEmpty());
    }

    @Test
    @DisplayName("String ends with a termination character & contains no termination characters other than the last letter")
    void testContainsTerminationCharacterOnlyAtEnd() {

        // All of the strings in these arrays should return true
        for (String candidate : validTerminationCharacterStrings) {
            if (!assessor.containsTerminationCharacterOnlyAtEnd(candidate)) {
                incorrectlyFailed.add(candidate);
            }
        }

        // All of the strings in these arrays should return false
        for (String candidate : invalidTerminationCharacterStrings) {
            if (assessor.containsTerminationCharacterOnlyAtEnd(candidate)) {
                incorrectlyPassed.add(candidate);
            }
        }

        String errorMsg = createErrorMessage(incorrectlyFailed, incorrectlyPassed);
        assertTrue(errorMsg, errorMsg.isEmpty());
    }

    @Test
    @DisplayName("Numbers below thirteen are spelled out (0-12 inclusive)")
    void testNumbersBelowThirteenSpelled() {

        // All of the strings in these arrays should return true
        for (String candidate : validNumbersSpelledStrings) {
            if (!assessor.numbersBelowThirteenSpelled(candidate)) {
                incorrectlyFailed.add(candidate);
            }
        }

        // All of the strings in these arrays should return false
        for (String candidate : invalidNumbersSpelledStrings) {
            if (assessor.numbersBelowThirteenSpelled(candidate)) {
                incorrectlyPassed.add(candidate);
            }
        }

        String errorMsg = createErrorMessage(incorrectlyFailed, incorrectlyPassed);
        assertTrue(errorMsg, errorMsg.isEmpty());
    }

    @Test
    @DisplayName("All 5 rules adhered to")
    void testAssessString() {

        // All of the strings in these arrays should return true
        for (String candidate : validFullAssessmentStrings) {
            if (!assessor.assessString(candidate)) {
                incorrectlyFailed.add(candidate);
            }
        }

        // All of the strings in these arrays should return false
        for (String candidate : invalidFullAssessmentStrings) {
            if (assessor.assessString(candidate)) {
                incorrectlyPassed.add(candidate);
            }
        }

        String errorMsg = createErrorMessage(incorrectlyFailed, incorrectlyPassed);
        assertTrue(errorMsg, errorMsg.isEmpty());
    }

    // Helpers ==========================

    private String createErrorMessage(ArrayList<String> incorrectlyFailed, ArrayList<String> incorrectlyPassed) {

        String errorMsg = "";

        if (!incorrectlyFailed.isEmpty()) {
            errorMsg = buildErrorMessage("Incorrectly returned false:", incorrectlyFailed);
        }

        if (!incorrectlyPassed.isEmpty()) {
            errorMsg = buildErrorMessage("Incorrectly returned true:", incorrectlyPassed, errorMsg);
        }

        return errorMsg;
    }

    private String buildErrorMessage(String explaination, ArrayList<String> errors, String existingString) {

        StringBuilder builder = new StringBuilder();

        if (!existingString.isEmpty()) {
            builder.append(existingString + "\n");
        }

        builder.append(explaination + " ");

        for (int i = 0; i < errors.size(); i++) {

            if (i > 0) {
                builder.append(", ");
            }

            builder.append("\"" + errors.get(i) + "\"");
        }

        return builder.toString();
    }

    private String buildErrorMessage(String explaination, ArrayList<String> errors) {
        return buildErrorMessage(explaination, errors, "");
    }
}
