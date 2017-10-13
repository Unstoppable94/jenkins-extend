package com.wingarden.cicd.jenkins.common.utils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class JsonUtil {

	public final static JSONObject EMPTY_OBJ = new JSONObject();

	public final static JSONArray EMPTY_ARRAY = new JSONArray();

	public static String toJsonExcludeAndDate(Object object, String excludes, String dateFormat) {
		SimplePropertyPreFilter filter = makeFilter(null, excludes, null);
		return toJSon(object, dateFormat, filter);
	}

	public static String toJsonExclude(Object object, String excludes) {
		SimplePropertyPreFilter filter = makeFilter(null, excludes, null);
		return toJSon(object, filter);
	}

	public static String toJsonExclude(Object object, Class<?> filterType, String excludes) {
		SimplePropertyPreFilter filter = makeFilter(filterType, excludes, null);
		return toJSon(object, filter);
	}

	public static String toJsonIncludeAndDate(Object object, String includes, String dateFormat) {
		SimplePropertyPreFilter filter = makeFilter(null, null, includes);
		return toJSon(object, dateFormat, filter);
	}

	public static String toJsonInclude(Object object, String includes) {
		SimplePropertyPreFilter filter = makeFilter(null, null, includes);
		return toJSon(object, filter);
	}

	public static String toJsonInclude(Object object, Class<?> filterType, String includes) {
		SimplePropertyPreFilter filter = makeFilter(filterType, null, includes);
		return toJSon(object, filter);
	}

	public static String toJson(Object object, String excludes, String includes) {
		SimplePropertyPreFilter filter = makeFilter(null, excludes, includes);
		return toJSon(object, filter);
	}

	public static String toJson(Object object, Class<?> filterType, String excludes, String includes) {
		SimplePropertyPreFilter filter = makeFilter(filterType, excludes, includes);
		return toJSon(object, filter);
	}

	public static String toJSon(Object object, SerializeFilter filter) {
		return toJSon(object, null, filter);
	}

	public static String toJSon(Object object, SerializeFilter[] filters) {
		return toJSon(object, null, filters);
	}

	/**
	 * 同时支持日期处理与字段过滤
	 * 
	 * @param object
	 *            源对象
	 * @param dateFormt
	 *            日期格式
	 * @param filter
	 *            序列化过滤器
	 * @return 格式化后的json字符串
	 */
	public static String toJSon(Object object, String dateFormt, SerializeFilter filter) {
		return toJSon(object, dateFormt, new SerializeFilter[] { filter });
	}

	public static String toJSon(Object object, String dateFormat, SerializeFilter[] filters) {
		SerializeConfig config = new SerializeConfig();
		if (StringToolkit.isNotEmpty(dateFormat)) {
			config.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
		}
		return JSON.toJSONString(object, config, filters, SerializerFeature.WriteMapNullValue);
	}

	// 自定义日期格式
	public static String toJSonWithDate(Object json, String dateFormat) {
		return JSON.toJSONStringWithDateFormat(json, dateFormat, SerializerFeature.WriteMapNullValue);
	}

	// 日期格式：yyyy-MM-dd HH:mm:ss
	public static String toJSonWithDate(Object json) {
		return JSON.toJSONStringWithDateFormat(json, DateToolkit.DEFAULT_DATEFORMAT,
				SerializerFeature.WriteMapNullValue);
	}

	// 日期格式：yyyy-MM-dd HH:mm:ss
	public static final <T> T parseObject(String input, TypeReference<T> type) {
		return parseObject(input, type.getType(), DateToolkit.DEFAULT_DATEFORMAT);
	}

	// 自定义日期格式
	public static final <T> T parseObjectWithDate(String input, Class clazz) {
		ParserConfig config = new ParserConfig();
		config.putDeserializer(Date.class, new CustomDateFormatDeserializer(DateToolkit.DEFAULT_DATEFORMAT));
		return JSON.parseObject(input, clazz, config, null, JSON.DEFAULT_PARSER_FEATURE);
	}

	// 日期格式：yyyy-MM-dd HH:mm:ss
	public static final <T> T parseObject(String input, Type clazz) {
		return parseObject(input, clazz, DateToolkit.DEFAULT_DATEFORMAT);
	}

	// 自定义日期格式
	public static final <T> T parseObject(String input, TypeReference<T> type, String dateFormat) {
		return parseObject(input, type.getType(), dateFormat);
	}

	// 自定义日期格式
	public static final <T> T parseObject(String input, Type clazz, String dateFormat) {

		ParserConfig config = new ParserConfig();
		config.putDeserializer(Date.class, new CustomDateFormatDeserializer(dateFormat));
		return JSON.parseObject(input, clazz, config, null, JSON.DEFAULT_PARSER_FEATURE);
	}

	public static final <T> T parseObjectWithNormalDate(String input, TypeReference<T> type) {
		for (String dateFormat : DateToolkit.NORMAL_DATEFORMATS) {
			try {
				return parseObject(input, type.getType(), dateFormat);
			} catch (Exception e) {
				// do nothing
			}
		}

		return null;
	}
	
	public static final <T> T parseObjectWithNormalDate(String input, Type clazz) {
		for (String dateFormat : DateToolkit.NORMAL_DATEFORMATS) {
			try {
				return parseObject(input, clazz, dateFormat);
			} catch (Exception e) {
				// do nothing
			}
		}

		return null;
	}

	public static ValueUsingPropertyFilter makeFilter(Class<?> filterType, String excludes, String includes) {
		ValueUsingPropertyFilter filter = new ValueUsingPropertyFilter(filterType);
		if (StringToolkit.isNotEmpty(excludes)) {
			filter.getExcludes().addAll(CollectionUtils.arrayToList(excludes.split(",")));
		}
		if (StringToolkit.isNotEmpty(includes)) {
			filter.getIncludes().addAll(CollectionUtils.arrayToList(includes.split(",")));
		}

		return filter;
	}

	public static JSONObject parseJSONObject(String text) {
		try {
			return JSONObject.parseObject(text);
		} catch (Exception e) {
			// don't have to hanlde
		}
		return EMPTY_OBJ;
	}

	public static JSONArray parseJSONArray(String text) {
		try {
			return JSONArray.parseArray(text);
		} catch (Exception e) {
			// don't have to hanlde
		}
		return EMPTY_ARRAY;
	}

	public static JSONArray parseArray(String str) {
		JSONArray codes = new JSONArray();
		if (StringToolkit.isNotEmpty(str)) {
			for (String groupcode : str.split(",")) {
				codes.add(groupcode);
			}
		}

		return codes;
	}

	public static boolean isJsonObject(Object value) {
		if (null == value) {
			return false;
		}
		if ("".equals(value.toString())) {
			return false;
		}
		try {
			if (JSONObject.class.isAssignableFrom(value.getClass())) {
				return true;
			}

			JSONObject.parseObject(value.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isJSONArray(Object value) {
		if (StringToolkit.isEmpty(value)) {
			return false;
		}

		try {
			if (JSONArray.class.isAssignableFrom(value.getClass())) {
				return true;
			}

			JSONArray.parseArray(value.toString());
			return true;
		} catch (Exception e) {
			return false;// 发生异常，说明非JSONArray格式
		}
	}

	public static <T> T getPropertyOfJson(String jsonStr, String propPath, Class<T> clazz) {
		if (StringToolkit.isEmpty(jsonStr) || StringToolkit.isEmpty(propPath)) {
			return null;
		}

		T ret = null;

		if (isJsonObject(jsonStr)) {
			Object object = BeanUtils.getProperty(JSONObject.parseObject(jsonStr), propPath);
			if (object instanceof JSONObject) {
				ret = JSONObject.toJavaObject((JSONObject) object, clazz);
			} else if (object instanceof JSONArray) {
				ret = JSONArray.toJavaObject((JSONArray) object, clazz);
			} else {
				ret = (T) object;
			}
		} else if (isJSONArray(jsonStr)) {
			Object object = BeanUtils.getProperty(JSONArray.parseArray(jsonStr), propPath);
			if (object instanceof JSONObject) {
				ret = JSONObject.toJavaObject((JSONObject) object, clazz);
			} else if (object instanceof JSONArray) {
				ret = JSONArray.toJavaObject((JSONArray) object, clazz);
			} else {
				ret = (T) object;
			}
		}

		return ret;
	}

	public static <T> T getPropertyOfJson(JSON json, String propPath, Class<T> clazz) {
		if (null == json) {
			return null;
		}
		T ret = null;
		Object object = BeanUtils.getProperty(json, propPath);
		if (object instanceof JSONObject) {
			ret = JSONObject.toJavaObject((JSONObject) object, clazz);
		} else if (object instanceof JSONArray) {
			ret = JSONArray.toJavaObject((JSONArray) object, clazz);
		} else {
			ret = (T) object;
		}
		return ret;
	}

	public static JSONObject setPropertyOfJson(String jsonStr, String propPath, Object val) {
		if (StringToolkit.isNotEmpty(jsonStr) && StringToolkit.isNotEmpty(propPath)) {
			JSONObject json = JSONObject.parseObject(jsonStr);
			setPropertyOfJson(json, propPath, val);
			return json;
		}
		return null;
	}

	public static void setPropertyOfJson(JSON json, String propPath, Object val) {
		if (null != json && StringToolkit.isNotEmpty(propPath)) {

			List<String> nameList = new ArrayList<String>();
			List<Integer> idxList = new ArrayList<Integer>();
			List<String> keyList = new ArrayList<String>();
			try {
				ParserUtil.analyzeName(propPath, nameList, idxList, keyList);
				String tName = "";
				Object bean = json;
				int idxNum = 0;
				int keyNum = 0;
				for (int i = 0; i < nameList.size(); i++) {
					tName = nameList.get(i);
					if ((i + 1) == nameList.size()) {
						if (ParserUtil.IDX_MODEL.equals(tName)) {
							int index = idxList.get(idxNum++);
							JSONArray arr = (JSONArray) bean;
							// arr.set(index, val);
							arr.add(index, val);
						} else {
							BeanUtils.setProperty((Serializable) bean, tName, val);
						}
						break;
					}

					if (ParserUtil.IDX_MODEL.equals(tName)) {
						int index = idxList.get(idxNum++);
						bean = BeanUtils.getIdxProperty(bean, index);
					} else if (ParserUtil.MAP_MODEL.equals(tName)) {
						String key = keyList.get(keyNum++);
						bean = BeanUtils.getMapProperty(bean, key);
					} else if (ParserUtil.NESTED_MODEL.equals(tName)) {
						continue;
					} else {
						if (BeanUtils.containsProperty(bean, tName)) {
							Object tmpBean = BeanUtils.getProperty(bean, tName);
							if (ParserUtil.IDX_MODEL.equals(nameList.get(i + 1))) {
								if (!(tmpBean instanceof JSONArray)) {
									tmpBean = new JSONArray();
									BeanUtils.setProperty((Serializable) bean, tName, tmpBean);
								}
							} else if (ParserUtil.NESTED_MODEL.equals(nameList.get(i + 1))) {
								if (!(tmpBean instanceof JSONObject)) {
									tmpBean = new JSONObject();
									BeanUtils.setProperty((Serializable) bean, tName, tmpBean);
								}
							}
							bean = tmpBean;
						} else {
							Object tmpBean = null;
							if (ParserUtil.IDX_MODEL.equals(nameList.get(i + 1))) {
								tmpBean = new JSONArray();
							} else if (ParserUtil.NESTED_MODEL.equals(nameList.get(i + 1))) {
								tmpBean = new JSONObject();

							}
							BeanUtils.setProperty((Serializable) bean, tName, tmpBean);
							bean = tmpBean;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
