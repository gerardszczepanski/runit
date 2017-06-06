package szczepanski.gerard.runit.search.service.cache.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.search.service.cache.CacheVisitor;
import szczepanski.gerard.runit.search.service.cache.impl.LexicalFrequencySearchCache.CacheContainer;
import szczepanski.gerard.runit.search.service.cache.impl.LexicalFrequencySearchCache.CachedSearchResultsBucket;
import szczepanski.gerard.runit.search.service.cache.impl.LexicalFrequencySearchCache.LexicalSearchTerm;
import szczepanski.gerard.runit.search.service.cache.report.CacheReport;
import szczepanski.gerard.runit.search.service.cache.report.CachedSearchTermInfo;

/**
 * This class instance generates {@code CacheReport} instance with informations
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
		List<CachedSearchTermInfo> cachedSearchTermInfoList = new ArrayList<>();

		CacheContainer cacheContainer = cache.cacheContainer;

		for (int i = 0; i < cacheContainer.buckets.length; i++) {
			CachedSearchResultsBucket bucket = cacheContainer.buckets[i];
			if (bucket != null) {
				countedSearchTerms += bucket.searchTermTimeStamps.size();

				Set<Entry<String, LexicalSearchTerm>> searchTimeStampsEntrySet = bucket.searchTermTimeStamps.entrySet();

				for (Entry<String, LexicalSearchTerm> entry : searchTimeStampsEntrySet) {
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
