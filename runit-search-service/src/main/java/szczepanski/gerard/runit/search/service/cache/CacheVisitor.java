package szczepanski.gerard.runit.search.service.cache;

public interface CacheVisitor<T> {
	
	void visit(Cache cache);
	
	T getResults();
	
}
