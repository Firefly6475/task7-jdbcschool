package ua.com.foxminded.jdbcschool.domain;

public class GroupCountRange {
    private final int minimalGroupCount;
    private final int maximumGroupCount;
    
    public GroupCountRange(int minimalGroupCount, int maximumGroupCount) {
        this.minimalGroupCount = minimalGroupCount;
        this.maximumGroupCount = maximumGroupCount;
    }

    public int getMinimalGroupCount() {
        return minimalGroupCount;
    }

    public int getMaximumGroupCount() {
        return maximumGroupCount;
    }
}
