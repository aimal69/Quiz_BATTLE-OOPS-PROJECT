public class Question {
    public String question, optionA, optionB, optionC, optionD;
    public char correctoption;

    public Question(String question, String a, String b, String c, String d, char correct) {
        this.question = question;
        this.optionA = a;
        this.optionB = b;
        this.optionC = c;
        this.optionD = d;
        this.correctoption = correct;
    }
}
