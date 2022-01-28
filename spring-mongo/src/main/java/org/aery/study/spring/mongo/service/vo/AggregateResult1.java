package org.aery.study.spring.mongo.service.vo;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class AggregateResult1 {

    @Id
    private Map<String, String> id;

    private BigDecimal count = BigDecimal.ZERO;

    private BigDecimal input = BigDecimal.ZERO;

    private BigDecimal output = BigDecimal.ZERO;

    public AggregateResult1() {
    }

    public AggregateResult1(String brand, String siteId) {
        this.id = new LinkedHashMap<>();
        this.id.put("brand", brand);
        this.id.put("siteId", siteId);
    }

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
