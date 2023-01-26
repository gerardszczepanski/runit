package szczepanski.gerard.runit.search.cache;

public interface CacheVisitor<T> {

    void visit(Cache cache);

    T getResults();

}
