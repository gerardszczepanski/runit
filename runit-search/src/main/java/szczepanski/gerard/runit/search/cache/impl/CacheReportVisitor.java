package szczepanski.gerard.runit.search.cache.impl;

import szczepanski.gerard.runit.collection.AdvancedCollectionFactory;
import szczepanski.gerard.runit.search.cache.Cache;
import szczepanski.gerard.runit.search.cache.CacheVisitor;
import szczepanski.gerard.runit.search.cache.report.CacheReport;
import szczepanski.gerard.runit.search.cache.report.CachedSearchTermInfo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class instance generates {@code CacheReport} instance with information
 * of the Cache.
 *
 * @author Gerard Szczepanski
 * @since Runit 1.4.0
 */
public class CacheReportVisitor implements CacheVisitor<CacheReport> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final int MAX_PERCENT = 100;

    private CacheReport cacheReport;

    @Override
    public void visit(Cache cache) {
        if (cache instanceof LexicalFrequencySearchCache) {
            procesLexicalFrequencySearchCache((LexicalFrequencySearchCache) cache);
        }
    }

    private void procesLexicalFrequencySearchCache(LexicalFrequencySearchCache cache) {
        int countedSearchTerms = 0;
        LexicalFrequencySearchCache.CacheContainer cacheContainer = cache.cacheContainer;
        int cacheLength = cacheContainer.buckets.length;
        List<CachedSearchTermInfo> cachedSearchTermInfoList = AdvancedCollectionFactory.list(cacheLength);

        for (int i = 0; i < cacheLength; i++) {
            LexicalFrequencySearchCache.CachedSearchResultsBucket bucket = cacheContainer.buckets[i];
            if (bucket != null) {
                countedSearchTerms += bucket.searchTermTimeStamps.size();

                Set<Entry<String, LexicalFrequencySearchCache.LexicalSearchTerm>> searchTimeStampsEntrySet = bucket.searchTermTimeStamps.entrySet();

                for (Entry<String, LexicalFrequencySearchCache.LexicalSearchTerm> entry : searchTimeStampsEntrySet) {
                    String searchTerm = entry.getKey();
                    LocalDateTime dateTime = Instant.ofEpochMilli(entry.getValue().timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    int numberOfResults = bucket.searchResults.size();

                    CachedSearchTermInfo cachedSearchTermInfo = CachedSearchTermInfo.builder()
                            .searchTerm(searchTerm)
                            .cachedDate(dateTime.format(DATE_TIME_FORMATTER))
                            .results(numberOfResults)
                            .build();

                    cachedSearchTermInfoList.add(cachedSearchTermInfo);
                }
            }
        }

        int cacheOccupancy = (cacheContainer.firstFreeSlot * MAX_PERCENT) / cacheContainer.size;

        this.cacheReport = CacheReport.builder()
                .cachedSearchTerms(cachedSearchTermInfoList)
                .cacheOccupancyInPercent(cacheOccupancy)
                .cachedSearchTermsNumber(countedSearchTerms)
                .build();
    }

    @Override
    public CacheReport getResults() {
        return cacheReport;
    }

}
