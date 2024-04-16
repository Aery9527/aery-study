package org.aery.study.vf.source.tool;

import org.aery.study.vf.tool.http.ParagraphMergerAction;

public class AnchorMerger implements ParagraphMergerAction {

    private final StringBuilder builder = new StringBuilder();

    private final String anchor;

    public AnchorMerger(String anchor) {
        this.anchor = anchor;
    }

    @Override
    public String merge(String content) {
        boolean itemFinished = content.contains(this.anchor);
        if (!itemFinished) {
            this.builder.append(content).append(" ");
            return null;
        }

        int anchorIndex = content.indexOf(this.anchor);
        int substringIndex = anchorIndex + this.anchor.length();
        String beforeAnchor = content.substring(0, substringIndex);
        String afterAnchor = content.substring(substringIndex);

        this.builder.append(beforeAnchor);
        String result = returnAndClear();
        this.builder.append(afterAnchor);

        return result;
    }

    public String returnAndClear() {
        String result = this.builder.toString();
        this.builder.setLength(0);
        return result;
    }

}
