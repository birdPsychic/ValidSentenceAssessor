package main;

import java.util.regex.Pattern;
import java.util.logging.*;

public class ValidSentenceAssessor {

    private static final Logger log = Logger.getLogger(ValidSentenceAssessor.class.getName()).getParent();

    public ValidSentenceAssessor() {
        log.setLevel(Level.OFF);
    }

    public ValidSentenceAssessor(Level loggingLevel) {
        Handler handlerObj = new ConsoleHandler(); // Log to console for convienience
        handlerObj.setLevel(loggingLevel);
        log.addHandler(handlerObj);
        log.setLevel(loggingLevel);
        log.setUseParentHandlers(false);
    }

    public boolean assessString(String candidate) {

        log.fine("Assessing string: \"" + candidate + "\" - " + java.time.LocalDateTime.now().toString());

        // Assess a null string as incorrectly formatted, as it represents an error
        if (candidate == null) {
            log.fine("Failure - string is null");
            return false;
        }

        // Assess an empty string as correctly formatted, per vacuous truth
        if (candidate.isEmpty()) {
            log.finer("Success - string is empty");
            return true;
        }

        // Assess string complies with requirements
        boolean compliant = startsWithCapitalLetter(candidate) && containsEvenNumberOfQuotationMarks(candidate)
                && containsTerminationCharacterOnlyAtEnd(candidate) && numbersBelowThirteenSpelled(candidate);

        if (compliant) {
            log.finer("Success - string is compliant");
            return true;
        } else {
            // Note: failure message has been logged previously in failed method
            return false;
        }

    }

    // > Requirement: String starts with a capital letter =======================
    boolean startsWithCapitalLetter(String candidate) {

        Pattern initialCapitalPattern = Pattern.compile("^[A-Z]");

        if (initialCapitalPattern.matcher(candidate).find()) {
            log.finest("- String begins with a capital letter.");
            return true;
        } else {
            log.fine("FAILURE - String does not begin with a capital letter.");
            return false;
        }
    }

    // > Even number of quotation marks =========================================
    // Assumption: from requirements, solution does not check quotation marks are
    // arranged conventionally, ie, quotation marks appearing in the middle of words
    // or enclosing no characters are technically compilant.
    boolean containsEvenNumberOfQuotationMarks(String candidate) {

        if (countCharacterOccurances(candidate, '\"') % 2 == 0) {
            log.finest("- String contains an even number of quotation marks.");
            return true;
        } else {
            log.fine("Failure - String contains an uneven number of quotation marks.");
            return false;
        }

    }

    // > Ends with a termination character, ie, ".", "?", "!" ===================
    // > String contains no termination characters other than the last letter ===
    // Note: combining these two requirements into one pattern represents a more
    // efficient solution than seperating them; although logging will be less
    // clear. This has been kept seperate from initialCapitalPattern for that
    // reason, though the three requirments could be combined into
    // "^[A-Z][^.!?]*[.?!]$"
    boolean containsTerminationCharacterOnlyAtEnd(String candidate) {

        final Pattern terminationCharactersPattern = Pattern.compile("^[^.!?]*[.?!]$");

        if (terminationCharactersPattern.matcher(candidate).matches()) {
            log.finest("- String ends with a termination character and contains no other termination characters.");
            return true;
        } else {
            log.fine("Failure - String uses termination characters improperly.");
            return false;
        }
    }

    // Numbers 0-12 inclusive are spelled out in words ==========================
    // Assumption: Any word containing any non-numerical characters alongside
    // numerals will be exempt from this requirement, as these would represent,
    // eg, negative numbers, other mathematical expressions, usernames, urls, etc.
    // More broadly, the presence of any other character surrounding a "spelled out"
    // number would render that number meaningless in normal usage. Thus, the regex
    // only matches a word containing any number of digits
    boolean numbersBelowThirteenSpelled(String candidate) {

        // Split the string into words
        String[] candidateWords = candidate.split("\\s+");
        Pattern numericalPattern = Pattern.compile("^\\p{Punct}*\\d+\\p{Punct}*$");
        Pattern mathematicalSymbolsPattern = Pattern.compile("[-+/*=]");

        for (String word : candidateWords) {

            // Match a number, which may be surrounded by, but does not contain, punctuation
            // (to account
            // for the end of sentences, commas, quotation marks, etc), but excluding
            // mathematical symbols
            // from a match (mostly to allow negative numbers to pass - words with any
            // symbols except at the
            // start or end are already excluded).
            if (numericalPattern.matcher(word).matches() && !mathematicalSymbolsPattern.matcher(word).find()) {

                // Strip leading and trailing punctuation
                word = word.replaceAll("\\p{Punct}", "");

                // A word matching this regular expression will now parse as an int
                int intWord = Integer.parseInt(word);

                if (intWord >= 0 && intWord < 13) {
                    log.fine("Failure: String contains the number " + intWord + " expressed numerically.");
                    return false;
                }
            }

        }

        log.finest("String contains no numbers 0-12 expressed numerically.");
        return true;
    }

    // Helpers ==========

    private int countCharacterOccurances(String searchedString, char... desiredCharacters) {

        int totalCount = 0;

        for (char subject : desiredCharacters) {
            totalCount += searchedString.chars().filter(ch -> ch == subject).count();
        }

        return totalCount;
    }

}
