package szczepanski.gerard.advanced.collection.facade;

import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Facade for advanced Collections (not from standard Java API).
 * 
 * I've created this Factory in case of change advanced collections lib. This class
 * helps me to centralize creational process for collections that are not from standard Java API.
 * 
 * Eventual lib change will be easy.
 * 
 * @author Gerard Szczepanski
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdvancedCollectionFactory {
	
	public static <K, V> Map<K, V> map() {
		return new Object2ObjectOpenHashMap<K, V>();
	}
	
	public static <V> List<V> list() {
		return new ObjectArrayList<V>();
	}
	
	public static <V> List<V> list(int size) {
		return new ObjectArrayList<V>(size);
	}
}
