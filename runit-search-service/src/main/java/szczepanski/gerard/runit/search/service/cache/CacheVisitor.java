package szczepanski.gerard.runit.search.service.cache;

@FunctionalInterface
public interface CacheVisitor {
	
	void visit(Cache cache);
	
}
