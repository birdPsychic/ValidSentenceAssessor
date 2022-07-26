package main;
import java.util.logging.Level;

public class App {
    public static void main(String[] args) {

        // NOTE: Please ignore this! Used only for early testing

        ValidSentenceAssessor assessor = new ValidSentenceAssessor(Level.FINE);

        boolean success = assessor.assessString("Abacus 0.");

        String report = "> Sentence valid!";
        if(!success) { report = "> Sentence invalid..."; }

        System.out.println(report);

    }
}
