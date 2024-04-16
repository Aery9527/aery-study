package org.aery.study.vf.tool.http;

import java.util.Objects;

public class ParagraphMergerBuilder {

    private ParagraphMergerMatchFilter matchFilter = (layerMatchCount, allMatchCount) -> true;

    private String name;

    private String startAnchor;

    private String endAnchor;

    private ParagraphMergerAction mergeAction;

    public ParagraphMerger build() {
        Objects.requireNonNull(this.matchFilter, "matchFilter");
        Objects.requireNonNull(this.name, "name");
        Objects.requireNonNull(this.startAnchor, "startAnchor");
        Objects.requireNonNull(this.endAnchor, "startAnchor");
        Objects.requireNonNull(this.mergeAction, "mergeAction");
        return new ParagraphMerger(this);
    }

    public ParagraphMergerBuilder matchFilter(ParagraphMergerMatchFilter matchFilter) {
        this.matchFilter = matchFilter;
        return this;
    }

    public ParagraphMergerBuilder namePrefix(String name) {
        this.name = name + "-paragraph-merger";
        return this;
    }

    public ParagraphMergerBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ParagraphMergerBuilder startAnchor(String startAnchor) {
        this.startAnchor = startAnchor;
        return this;
    }

    public ParagraphMergerBuilder endAnchor(String endAnchor) {
        this.endAnchor = endAnchor;
        return this;
    }

    public ParagraphMergerBuilder mergeAction(ParagraphMergerAction mergeAction) {
        this.mergeAction = mergeAction;
        return this;
    }

    public String getEndAnchor() {
        return endAnchor;
    }

    public void setEndAnchor(String endAnchor) {
        this.endAnchor = endAnchor;
    }

    public ParagraphMergerMatchFilter getMatchFilter() {
        return matchFilter;
    }

    public void setMatchFilter(ParagraphMergerMatchFilter matchFilter) {
        this.matchFilter = matchFilter;
    }

    public ParagraphMergerAction getMergeAction() {
        return mergeAction;
    }

    public void setMergeAction(ParagraphMergerAction mergeAction) {
        this.mergeAction = mergeAction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartAnchor() {
        return startAnchor;
    }

    public void setStartAnchor(String startAnchor) {
        this.startAnchor = startAnchor;
    }
}
