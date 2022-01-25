package org.aery.study.spring.mongo.service.vo;

public class AggregateResult1 {

    private Object _id;

    private Long count;

    private Long input;

    private Long output;

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getInput() {
        return input;
    }

    public void setInput(Long input) {
        this.input = input;
    }

    public Long getOutput() {
        return output;
    }

    public void setOutput(Long output) {
        this.output = output;
    }
}
