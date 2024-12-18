package Records;
import Enums.Specializations;


public record ResearchResult(String professorName, Specializations specialization, String result) {

    public String getSummary() {
        return professorName + "(" + specialization + ") - " + result;
    }
}
