package org.aery.study.spring.mongo.service.vo;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Map;

public class AggregateResult1 {

    @Id
    private Map<String, String> id;

    private BigDecimal count;

    private BigDecimal input;

    private BigDecimal output;

    public Map<String, String> getId() {
        return id;
    }

    public void setId(Map<String, String> id) {
        this.id = id;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getInput() {
        return input;
    }

    public void setInput(BigDecimal input) {
        this.input = input;
    }

    public BigDecimal getOutput() {
        return output;
    }

    public void setOutput(BigDecimal output) {
        this.output = output;
    }
}
