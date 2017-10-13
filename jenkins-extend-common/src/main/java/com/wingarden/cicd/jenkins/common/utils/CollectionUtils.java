package com.wingarden.cicd.jenkins.common.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;


public class CollectionUtils {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> sort(List<T> list) {
		if (list != null && list.size() > 0) {
			Collections.sort((List)list);
		}
		return list;
	}
	
	private static final Comparator<String> SIMPLE_NAME_COMPARATOR = new Comparator<String>() {
		public int compare(String s1, String s2) {
			if (s1 == null && s2 == null) {
				return 0;
			}
			if (s1 == null) {
				return -1;
			}
			if (s2 == null) {
				return 1;
			}
			int i1 = s1.lastIndexOf('.');
			if (i1 >= 0) {
				s1 = s1.substring(i1 + 1);
			}
			int i2 = s2.lastIndexOf('.');
			if (i2 >= 0) {
				s2 = s2.substring(i2 + 1);
			}
			return s1.compareToIgnoreCase(s2);
		}
	};
	
	public static List<String> sortSimpleName(List<String> list) {
		if (list != null && list.size() > 0) {
			Collections.sort(list, SIMPLE_NAME_COMPARATOR);
		}
		return list;
	}

	public static Map<String, Map<String, String>> splitAll(Map<String, List<String>> list, String separator) {
		if (list == null) {
			return null;
		}
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		for (Map.Entry<String, List<String>> entry : list.entrySet()) {
			result.put(entry.getKey(), split(entry.getValue(), separator));
		}
		return result;
	}
	
	public static Map<String, List<String>> joinAll(Map<String, Map<String, String>> map, String separator) {
		if (map == null) {
			return null;
		}
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
			result.put(entry.getKey(), join(entry.getValue(), separator));
		}
		return result;
	}

	public static Map<String, String> split(List<String> list, String separator) {
		Map<String, String> map = new HashMap<String, String>();
		if (list == null || list.size() == 0) {
			return map;
		}
		for (String item : list) {
			int index = item.indexOf(separator);
			if (index == -1) {
				map.put(item, "");
			} else {
				map.put(item.substring(0, index), item.substring(index + 1));
			}
		}
		return map;
	}

	public static List<String> join(Map<String, String> map, String separator) {
		List<String> list = new ArrayList<String>();
		if (map == null || map.size() == 0) {
			return list;
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value == null || value.length() == 0) {
				list.add(key);
			} else {
				list.add(key + separator + value);
			}
		}
		return list;
	}

	public static String join(List<Object> list, String separator) {
	    StringBuilder sb = new StringBuilder();
        for(Object ele : list) {
            if(sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(StringToolkit.getObjectString(ele));
        }
        return sb.toString();
	}
	
	public static boolean mapEquals(Map<?, ?> map1, Map<?, ?> map2) {
		if (map1 == null && map2 == null) {
			return true;
		}
		if (map1 == null || map2 == null) {
			return false;
		}
		if (map1.size() != map2.size()) {
			return false;
		}
		for (Map.Entry<?, ?> entry : map1.entrySet()) {
			Object key = entry.getKey();
			Object value1 = entry.getValue();
			Object value2 = map2.get(key);
			if (! objectEquals(value1, value2)) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean objectEquals(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		if (obj1 == null || obj2 == null) {
			return false;
		}
		return obj1.equals(obj2);
	}
	
	public static Map<String, String> toStringMap(String... pairs) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (pairs.length > 0) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("pairs must be even.");
            }
            for (int i = 0; i < pairs.length; i = i + 2) {
                parameters.put(pairs[i], pairs[i + 1]);
            }
        }
        return parameters;
    }

	@SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(Object ... pairs) {
	    Map<K, V> ret = new HashMap<K, V>();
	    if (pairs == null || pairs.length == 0) return ret;
	
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Map pairs can not be odd number.");
        }        
        int len = pairs.length / 2;
        for (int i = 0; i < len; i ++) {
            ret.put((K) pairs[2 * i], (V) pairs[2 * i + 1]);
        }
	    return ret;
	}
	
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }
    
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
	}
    
    public static boolean isNotEmpty(Object[] arr) {
        return null != arr && arr.length > 0;
	}
    
    public static boolean isEmpty(Object[] arr) {
        return !isNotEmpty(arr);
	}
    

    public static <T>T getOneIfNoEmpty(Collection<T> list){
    	if (isNotEmpty(list)){
    		return list.iterator().next();
    	}
    	
    	return null;
    }
    
    public static String[] collectionProperty2Array(Collection<?> collection,String property){
		return collectionProperty(collection,property).toArray(new String[0]);
    }
    
    public static Set<String> collectionProperty(Collection<?> collection,String property){
    	Map<String, Object> tmp = new HashMap<String, Object>();
		for (Object obj : collection) {
			if (StringToolkit.isNotEmpty(property)){
				try {
					tmp.put(StringToolkit.getObjectString(PropertyUtils.getNestedProperty(obj, property)), 0);
				} catch (Exception e) {
					//do nothing
				} 
			}else {
				tmp.put(StringToolkit.getObjectString(obj), 0);
			}
		}
		return tmp.keySet();
    }
    
	private CollectionUtils() {
	}
	
	
	public static Collection<String> asList(String[] ids) {
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, ids);
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static List arrayToList(Object source) {
		return Arrays.asList(toObjectArray(source));
	}

	public static Object[] toObjectArray(Object source) {
		if (source instanceof Object[]) {
			return (Object[]) source;
		}
		if (source == null) {
			return new Object[0];
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return new Object[0];
		}
		Class<?> wrapperType = Array.get(source, 0).getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return newArray;
	}
	
	@SuppressWarnings({ "unchecked" })
	public static <T>T[] toObjectArray(Object source,Class<T> clazz) {
		if (source == null) {
			return (T[])new Object[0];
		}
		if (clazz.isAssignableFrom(source.getClass())) {
			return (T[]) source;
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return(T[]) new Object[0];
		}
		Class<?> wrapperType = Array.get(source, 0).getClass();
		T[] newArray = (T[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = (T)Array.get(source, i);
		}
		return newArray;
	}
}