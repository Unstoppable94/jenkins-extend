package com.wingarden.cicd.jenkins.common.utils;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class JsonResponseUtils {
	// 返回值（成功为正整数，失败为负整数）
	public final static Integer SUCCESS = 1;
	public final static Integer ERROR = -1;

	// response标准属性
	public final static String RETURN_CODE = "rc";
	public final static String MESSAGE = "msg";
	public final static String DATA = "data";


	private static final SerializeConfig defaultSerializeConfig;
	
	
	static {
		defaultSerializeConfig =  new SerializeConfig();
		defaultSerializeConfig.put(Date.class, new SimpleDateFormatSerializer(DateToolkit.DEFAULT_DATEFORMAT));
	}
	
	/**
	 * 返回json的通用描述 {"rc":-1,"msg":"错误信息"，"data": 数据}
	 * 
	 * 成功是1，错误是-1
	 * @param result 返回的json结果
	 * @return json应答对象
	 */
	public static String success(Object result) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		return JSON.toJSONString(json,defaultSerializeConfig,SerializerFeature.WriteMapNullValue);
	}
	

	public static String successExclude(Object result, String excludes) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		SerializeFilter filter = JsonUtil.makeFilter(null, excludes,
				null);
		return JSON.toJSONString(json, defaultSerializeConfig, filter,
				SerializerFeature.WriteMapNullValue);
	}

	public static String successInclude(Object result, String includes) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		SerializeFilter filter = JsonUtil.makeFilter(null, null,
				includes);
		return JSON.toJSONString(json, defaultSerializeConfig, filter,
				SerializerFeature.WriteMapNullValue);
	}

	public static String successExclude(Object result, String excludes,
			String dateFormat) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		SerializeConfig config = new SerializeConfig();
		if (StringToolkit.isNotEmpty(dateFormat)) {
			config.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
		}
		SerializeFilter filter = JsonUtil.makeFilter(null, excludes,
				null);
		return JSON.toJSONString(json, config, filter,
				SerializerFeature.WriteMapNullValue);
	}

	public static String successInclude(Object result, String includes,
			String dateFormat) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		SerializeConfig config = new SerializeConfig();
		if (StringToolkit.isNotEmpty(dateFormat)) {
			config.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
		}
		SerializeFilter filter = JsonUtil.makeFilter(null, null,
				includes);
		return JSON.toJSONString(json, config, filter,
				SerializerFeature.WriteMapNullValue);
	}
	
	
	public static String successExclude(Object result, String excludes,
			Class<?> type) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		
		SerializeFilter filter = JsonUtil.makeFilter(type, excludes,
				null);
		return JSON.toJSONString(json, defaultSerializeConfig, filter,
				SerializerFeature.WriteMapNullValue);
	}
	
	public static String successInclude(Object result, String includes,
			Class<?> type) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		
		SerializeFilter filter = JsonUtil.makeFilter(type, null,
				includes);
		return JSON.toJSONString(json, defaultSerializeConfig, filter,
				SerializerFeature.WriteMapNullValue);
	}
	

	public static String success(Object result, SerializeFilter filter) {
		//Object body = JSONObject.toJSON(result);
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		return JSON.toJSONString(json, defaultSerializeConfig, filter,
				SerializerFeature.WriteMapNullValue);
	}

	public static String success(Object result, SerializeFilter[] filters) {
		return success(result, DateToolkit.DEFAULT_DATEFORMAT, filters);
	}
	
	public static String success(Object result, String dateFormat) {
		//Object body = JSONObject.toJSON(result);
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		SerializeConfig config = new SerializeConfig();
		if (StringToolkit.isNotEmpty(dateFormat)) {
			config.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
		}

		return JSON.toJSONString(json, config,
				SerializerFeature.WriteMapNullValue);
	}

	public static String success(Object result, String dateFormat,
			SerializeFilter filter) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		SerializeConfig config = new SerializeConfig();
		if (StringToolkit.isNotEmpty(dateFormat)) {
			config.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
		}

		return JSON.toJSONString(json, config, filter,
				SerializerFeature.WriteMapNullValue);
	}
	
	public static String success(Object result, String dateFormat,
			SerializeFilter[] filters) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		SerializeConfig config = new SerializeConfig();
		if (StringToolkit.isNotEmpty(dateFormat)) {
			config.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
		}

		return JSON.toJSONString(json, config, filters,
				SerializerFeature.WriteMapNullValue);
	}
	

	public static String success(Object result, SerializeConfig config,
			SerializeFilter filter) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, SUCCESS);
		json.put(MESSAGE, "success");
		json.put(DATA, result);

		return JSON.toJSONString(json, config, filter,
				SerializerFeature.WriteMapNullValue);
	}

	public static String error(String message) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, ERROR);
		json.put(MESSAGE, message);

		return json.toJSONString();
	}

	public static String error(JSONObject data) {
		JSONObject json = new JSONObject();
		json.put(RETURN_CODE, ERROR);
		json.put(MESSAGE, "");
		json.put(DATA, data);

		return json.toJSONString();
	}

	public static String errorUndefined() {
		return error("发生未知异常！");
	}
	

	public static <T> T getData(String rsp,Class<T> cls){
		Object data = JSONObject.parseObject(rsp).get(DATA);
		if (data != null) {
			if (data instanceof JSONObject){
				return JSON.toJavaObject((JSONObject)data, cls);
			}else {
				if (data.getClass().isAssignableFrom(cls)){
					return cls.cast(data);
				}
				return JSON.toJavaObject(JSONObject.parseObject(data.toString()), cls);
			}
		}
		return null;
	}
	
	public static JSONObject getData(String rsp){
		return JSONObject.parseObject(rsp).getJSONObject(DATA);
	}

}
