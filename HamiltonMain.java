import knowledge.F1KnowledgeBase;

public class HamiltonMain {
    public static void main(String[] args) {
        F1KnowledgeBase knowledgeBase = new F1KnowledgeBase("userLogin");
        String answer = knowledgeBase.getAnswer("What is the capital of France?");
        System.out.println(answer);
    }
}