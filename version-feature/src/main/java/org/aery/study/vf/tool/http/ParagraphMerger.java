package org.aery.study.vf.tool.http;

import java.util.Optional;

public class ParagraphMerger implements LineMerger {

    private final ParagraphMergerMatchFilter matchFilter;

    private final String name;

    private final String startAnchor;

    private final String endAnchor;

    private final ParagraphMergerAction mergeAction;

    private int paragraphDepthCount = 0;

    private int layerMatchCount = 0;

    private int allMatchCount = 0;

    public ParagraphMerger(ParagraphMergerBuilder builder) {
        this.matchFilter = builder.getMatchFilter();
        this.name = builder.getName();
        this.startAnchor = builder.getStartAnchor();
        this.endAnchor = builder.getEndAnchor();
        this.mergeAction = builder.getMergeAction();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public final Optional<String> merge(String line) {
        boolean currentStart = line.contains(this.startAnchor);
        if (currentStart) {
            this.paragraphDepthCount++;

            if (this.paragraphDepthCount == 1) {
                this.layerMatchCount++;
            }
            this.allMatchCount++;
        }

        boolean currentEnd = line.contains(this.endAnchor);
        if (currentEnd) {
            this.paragraphDepthCount--;
        }

        boolean inParagraph = this.paragraphDepthCount > 0 || currentStart || currentEnd;
        if (inParagraph) {
            inParagraph = this.matchFilter.match(this.layerMatchCount, this.allMatchCount);
        }

        String content = line.trim();
        if (inParagraph) {
            if (currentStart) {
                content = trimWithStart(content);
            }
            if (currentEnd) {
                content = trimWithEnd(content);
            }
        }

        return inParagraph ? Optional.ofNullable(this.mergeAction.merge(content)) : Optional.empty();
    }

    public String trimWithStart(String content) {
        int index = content.indexOf(this.startAnchor);
        return content.substring(index + this.startAnchor.length());
    }

    public String trimWithEnd(String content) {
        int index = content.indexOf(this.endAnchor);
        return content.substring(0, index);
    }

}
