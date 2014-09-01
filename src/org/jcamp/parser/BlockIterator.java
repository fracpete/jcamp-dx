package org.jcamp.parser;

/**
 * iterator over JCAMP blocks.
 * @author Thomas Weber
 */
public class BlockIterator implements IStringIterator {
    private int start = 0;
    private int end = 0;
    private int depth = 0;
    private String jcamp;
    private LabelIterator labelIter = null;
    public BlockIterator(String jcamp) {
        this.jcamp = jcamp;
        this.labelIter = new LabelIterator(jcamp);
        nextBlock();
    }
    public int getOffset() {
        if (jcamp != null && start < jcamp.length())
            return start;
        return -1;
    }
    public boolean hasNext() {
        if (jcamp == null)
            return false;
        return start < jcamp.length() && start <= end;
    }
    public String next() {
        if (jcamp != null && start < jcamp.length() && start <= end) {
            String block = jcamp.substring(start, end);
            nextBlock();
            return block;
        } else
            return null;
    }
    private void nextBlock() {
        while (true) {
            if (!labelIter.hasNext()) {
                start = end = jcamp.length();
                return;
            }
            int offset = labelIter.getOffset();
            String label = labelIter.next();
            label = Utils.normalizeLabel(label);
            if (label.equals("##TITLE=")) {
                if (depth == 1) {
                    start = offset;
                }
                depth++;
            } else if (label.equals("##END=")) {
                depth--;
                if (depth < 1) {
                    start = end = jcamp.length();
                    return;
                }
                if (depth == 1) {
                    end = labelIter.getOffset() - 1;
                    return;
                }
            }
        }
    }
}
