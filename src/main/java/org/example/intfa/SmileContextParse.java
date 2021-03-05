package org.example.intfa;

public class SmileContextParse implements SmileHandlerParse {
    @Override
    public void parse(String name) {
        System.out.println("=========name=" + name + ",class=" + this.getClass().getName());
    }
}
