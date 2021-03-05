package org.example.intfa;


import java.util.HashMap;
import java.util.Map;

public abstract class SmileHandlerSupport implements SmileHandler {

    private final Map<String, SmileHandlerParse> smileParsers = new HashMap<>();

    @Override
    public void parse(String name) {
        SmileHandlerParse parse = this.smileParsers.get(name);
        parse.parse(name);
    }

    public final void registerSmileHandler(String name, SmileHandlerParse parse){
        this.smileParsers.put(name, parse);
    }

    public Map<String, SmileHandlerParse> getSmileParsers(){
        return this.smileParsers;
    }
}
