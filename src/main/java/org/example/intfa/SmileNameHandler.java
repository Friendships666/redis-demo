package org.example.intfa;

public class SmileNameHandler extends SmileHandlerSupport {

    @Override
    public void init() {
        registerSmileHandler("smileScan", new SmileScanParse());
        registerSmileHandler("smileContext", new SmileContextParse());
    }

}
