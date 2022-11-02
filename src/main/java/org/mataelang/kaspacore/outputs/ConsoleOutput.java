package org.mataelang.kaspacore.outputs;

public class ConsoleOutput extends StreamOutput {
    public ConsoleOutput() {
        configure("complete", false);
    }

    public ConsoleOutput(String outputMode) {
        configure(outputMode, false);
    }

    public ConsoleOutput(String outputMode, Boolean truncate) {
        configure(outputMode, truncate);
    }

    private void configure(String outputMode, Boolean truncate) {
        setOutputMode(outputMode);
        setOption("truncate", String.valueOf(truncate));
        setFormat("console");
    }
}
