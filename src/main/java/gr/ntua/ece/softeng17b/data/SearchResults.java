package gr.ntua.ece.softeng17b.data;

import java.util.List;

public class SearchResults {

    public final long total;
    public final long start;
    public final long count;
    public final List<String> ids;

    public SearchResults(long total, long start, long count, List<String> ids) {
        this.total = total;
        this.start = start;
        this.count = count;
        this.ids   = ids;
    }
}
