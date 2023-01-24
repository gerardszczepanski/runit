package szczepanski.gerard.runit.search.service.cache.impl;

import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.search.service.cache.CacheVisitor;
import szczepanski.gerard.runit.search.service.cache.impl.LexicalFrequencySearchCache.CacheContainer;
import szczepanski.gerard.runit.search.service.cache.impl.LexicalFrequencySearchCache.CachedSearchResultsBucket;

/**
 * This visitor counts number of search terms currently stored in Cache.
 *
 * @author Gerard Szczepanski
 * @since Runit 1.4.0
 */
public class CacheSearchTermsCountingVisitor implements CacheVisitor<Integer> {

    private int results = 0;

    @Override
    public void visit(Cache cache) {
        if (cache instanceof LexicalFrequencySearchCache) {
            procesLexicalFrequencySearchCache((LexicalFrequencySearchCache) cache);
        }
    }

    private void procesLexicalFrequencySearchCache(LexicalFrequencySearchCache cache) {
        int countedSearchTerms = 0;
        CacheContainer cacheContainer = cache.cacheContainer;

        for (int i = 0; i < cacheContainer.buckets.length; i++) {
            CachedSearchResultsBucket bucket = cacheContainer.buckets[i];
            if (bucket != null) {
                countedSearchTerms += bucket.searchTermTimeStamps.size();
            }
        }

        results = countedSearchTerms - 1; // Size of an empty String
    }

    @Override
    public Integer getResults() {
        return results;
    }

}
