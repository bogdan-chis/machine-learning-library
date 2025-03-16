package core;

import java.util.List;

public class Instance<F, L> {
    private List<F> input;
    private L output;

    public Instance(List<F> input, L output) {
        this.input = input;
        this.output = output;
    }

    public List<F> getInput() {
        return this.input;
    }

    public void setInput(List<F> input) {
        this.input = input;
    }

    public L getOutput() {
        return this.output;
    }

    public void setOutput(L output) {
        this.output = output;
    }

    public String toString() {
        String var10000 = String.valueOf(this.input);
        return "Instance{input=" + var10000 + ", output=" + String.valueOf(this.output) + "}";
    }
}
