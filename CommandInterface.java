import knowledge.F1KnowledgeBase;

public class CommandInterface {
    private F1KnowledgeBase knowledgeBase;

    public CommandInterface() {
        this.knowledgeBase = new F1KnowledgeBase("userLogin");
    }

    public void askQuestion(String question) {
        String answer = knowledgeBase.getAnswer(question);
        System.out.println(answer);
    }
}