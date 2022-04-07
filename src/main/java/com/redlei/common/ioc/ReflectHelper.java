package com.redlei.common.ioc;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.redlei.common.response.RespCodeState;
import com.redlei.common.response.Result;
import com.redlei.common.utils.NameHumpHelper;
import com.redlei.common.utils.StringUtil;

public class ReflectHelper {

	private static Logger logger = LoggerFactory.getLogger(ReflectHelper.class);

	public static Map<String, Object> buildJSONToMap(String jsonString) {
		try {
			return JSONObject.parseObject(jsonString, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static String buildObjectToJSONString(Object obj) {
		return JSON.toJSONString(obj);
	}

	/**
	 * map to type
	 *
	 * @param map
	 * @param clzz
	 * @return
	 */
	public static <T> void buildMapToEntity(Object entity, Map<String, Object> map, Class<T> clzz) {

		if (entity == null || map == null || map.size() < 1) {
			return;
		}

		Field[] fields = clzz.getDeclaredFields();
		for (Field field : fields) {
			buildField(field, map, entity);
		}
	}

	private static void buildField(Field field, Map<String, Object> respMap, Object entity) {
		Class<?> clzz = field.getType();
		String vt = clzz.getName();
		try {
			String key = field.getName();
			Object value = null;
			if (vt.toLowerCase().startsWith("java.lang") || vt.toLowerCase().startsWith("long")
					|| vt.toLowerCase().startsWith("int") || vt.toLowerCase().startsWith("double")
					|| vt.toLowerCase().startsWith("float") || vt.toLowerCase().startsWith("char")
					|| vt.toLowerCase().startsWith("comparator") || vt.toLowerCase().startsWith("java.math")) {
				field.setAccessible(true);
				if (respMap.containsKey(key)) {
					value = respMap.get(key);
				} else if (respMap.containsKey(NameHumpHelper.humpToLine(key))) {
					value = respMap.get(NameHumpHelper.humpToLine(key));
				} else if (respMap.containsKey(NameHumpHelper.lineToHump(key))) {
					value = respMap.get(NameHumpHelper.lineToHump(key));
				}
				field.set(entity, TransEntityValue(field, value));
				return;

			} else if (vt.startsWith("Ljava")) {

			} else if (vt.equals("[Ljava.lang.String;")) {
				if (respMap.containsKey(key)) {
					value = respMap.get(key);
					String temp = value.toString();
					temp = temp.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
					field.setAccessible(true);
					field.set(entity, temp.split(","));
				}
			} else if (vt.startsWith("java.util.List")) {
				if (respMap.containsKey(field.getName())) {
					String val = respMap.get(field.getName()).toString();
					Type genericType = field.getGenericType();
					if (null == genericType) {
						return;// continue;
					}
					if (genericType instanceof ParameterizedType) {
						ParameterizedType pt = (ParameterizedType) genericType;
						// 得到泛型里的class类型对象
						Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
						Object model = ReflectHelper.buildJSONArrayToEntity(val, actualTypeArgument);
						field.setAccessible(true);
						field.set(entity, model);
					}

				}
			} else if (vt.startsWith("java.util.Map")) {
				String val = respMap.get(field.getName()).toString();
				Type genericType = field.getGenericType();
				Object map = transMapValue(genericType, val);
				field.setAccessible(true);
				field.set(entity, map);
			} else if (vt.startsWith("java.math")) {
				if (vt.equals("java.math.BigDecimal") && respMap.containsKey(key)) {
					value = respMap.get(key);
					BigDecimal bigDecimal = new BigDecimal(value.toString());
					field.setAccessible(true);
					field.set(entity, bigDecimal);
				}
			} else {
				Object model = clzz.newInstance();
				if (respMap.containsKey(field.getName())) {
					Object obj = respMap.get(field.getName());
					if (obj != null) {
						String val = obj.toString();
						if (val.contains("@")) {
							field.setAccessible(true);
							field.set(entity, obj);
						} else if (val.startsWith("{")) {
							model = JSONObject.parseObject(val, clzz);
							field.setAccessible(true);
							field.set(entity, model);
						} else if (val.startsWith("[")) {
							model = JSONObject.parseArray(val, clzz);
							field.setAccessible(true);
							field.set(entity, model);
						} else {
							String fieldType = model.toString();
							if (fieldType.contains("@")) {
								ReflectHelper.buildMapToEntity(model, respMap, model.getClass());
								field.setAccessible(true);
								field.set(entity, model);
							}

						}
					}
				} else {
					ReflectHelper.buildMapToEntity(model, respMap, clzz);
					field.setAccessible(true);
					field.set(entity, model);
				}
			}
		} catch (IllegalAccessException | InstantiationException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

	public static <T> void buildMapToEntity(T entity, Map<String, String> map) {

		try {
			if (entity == null) {
				return;
			}
			Class<?> clzz = entity.getClass();
			Field[] fields = clzz.getDeclaredFields();
			List<Field> sortFields = new ArrayList<>();
			Class<?> parentClzz = clzz.getSuperclass();
			if (parentClzz != null) {
				sortFields.addAll(Arrays.asList(fields));

				Field[] pFields = parentClzz.getDeclaredFields();
				if (pFields != null) {
					for (Field field : pFields) {
						boolean needAdd = true;
						for (Field child : fields) {
							if (field.getName().equals(child.getName())) {
								needAdd = false;
								break;
							}
						}
						if (needAdd) {
							sortFields.add(field);
						}
					}
				}
				fields = sortFields.toArray(new Field[0]);
			}
			for (Field field : fields) {
				String fieldName = field.getName();
				if (map.containsKey(fieldName) || map.containsKey(NameHumpHelper.humpToLine(fieldName))) {
					field.setAccessible(true);
					if (map.containsKey(fieldName)) {
						field.set(entity, TransEntityValue(field, map.get(field.getName())));
					} else {
						field.set(entity, TransEntityValue(field, map.get(NameHumpHelper.humpToLine(fieldName))));
					}

				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static <T> Object buildJSONToEntity(String jsonString, Class<T> clazz) {
		if (StringUtil.isEmpty(jsonString)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			Object entity = mapper.readValue(jsonString, clazz);
			return entity;
		} catch (IOException e) {
			// throw NestedException.wrap(e);
		}
		return null;
	}

	public static <T> Object buildJSONArrayToEntity(String jsonString, Class<T> clazz) {
		if (StringUtil.isEmpty(jsonString)) {
			return null;
		}
		JSONArray array = JSONArray.parseArray(jsonString);
		if (array == null) {
			return null;
		}
		List<Object> list = new ArrayList<>();

		for (Object arr : array) {
			Object model = null;
			try {
				model = clazz.newInstance();
				String value = arr.toString();
				if (isJson(value)) {
					Map<String, Object> map = ReflectHelper.buildJSONToMap(arr.toString());
					buildMapToEntity(model, map, clazz);
//                    model=buildJSONToEntity(arr.toString(), clazz);
				} else {
					model = TransValue(clazz, value);
				}

				list.add(model);
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return list;
	}

	private static boolean isJson(String jsonString) {
		try {
			JSON json = JSON.parseObject(jsonString);
			if (json != null) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public static <T> Map<String, Object> buildEntityToMap(T entity, Class<T> clzz) {
		Map<String, Object> result = new HashMap<>();
		Field[] fields = clzz.getDeclaredFields();
		for (Field field : fields) {
			try {
				String key = field.getName();
				field.setAccessible(true);
				Object value = field.get(entity);
				if (value != null)
					result.put(key, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

		return result;
	}

	public static <T> Map<String, Object> buildEntityToMap(T entity) {
		Map<String, Object> result = new HashMap<>();
		Class<?> clzz = entity.getClass();
		Field[] fields = clzz.getDeclaredFields();
		for (Field field : fields) {
			try {
				String key = field.getName();
				field.setAccessible(true);
				Object value = field.get(entity);
				if (value != null)
					result.put(key, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 更新实体类
	 *
	 * @param entity
	 * @param respMap
	 * @return
	 */
	public static <T> T updateEntity(T entity, Map<String, String> respMap, String[] exceptFields) {

		return entity;
	}

	public static Object TransEntityValue(Field field, Object value) {

		if (field == null || value == null) {
			String ftype = field.getType().getName();
			if (ftype.equals("int") || ftype.equals("float") || ftype.equals("double")) {
				return 0;
			} else if (ftype.equals("char")) {
				return '0';
			} else if (ftype.equals("java.lang.String")) {
				return "";
			}
			return null;
		}

		return TransValue(field.getType(), value.toString());
	}

	public static Result InvokerEntityMethod(Object object, String method, LinkedHashMap<String, Object> params,
			Class<?> invokeClzz) {
		Result result = new Result();
		if (invokeClzz == null) {
			return result;
		}

		Method[] methods = invokeClzz.getDeclaredMethods();
		List<Method> mList = new ArrayList<>();
		for (Method m : methods) {
			if (m.getName().toLowerCase().equals(method.toLowerCase())) {
				mList.add(m);
			}
		}
		if (mList.size() < 1) {
			result.setStatusCode(RespCodeState.API_ERROE_CODE_3000.getStatusCode());
			result.setMessage("方法不可用");
			return result;
		}
		if (mList.size() > 1) {
			Method m = getMethodByParams(mList, params);
			return doInvokerMethod(object, m, params);
		} else {
			return doInvokerMethod(object, mList.get(0), params);
		}
	}

	private static Method getMethodByParams(List<Method> mList, Map<String, Object> params) {
		boolean isContinue = true;
		Method m = mList.get(0);
		Object[] keys = params.keySet().toArray();
		for (Method method : mList) {
			Parameter[] parameters = method.getParameters();
			isContinue = true;
			if (parameters.length > keys.length) {
				continue;
			}
			for (int i = 0; i < parameters.length; i++) {
				Parameter parameter = parameters[i];
				Class<?> type = parameter.getType();
				Class<?> pClass = params.get(keys[i]).getClass();

				if (!(pClass.isInstance(type) || type.isInstance(pClass) || pClass.isAssignableFrom(type)
						|| type.isAssignableFrom(pClass))) {
					isContinue = false;
					break;
				}

			}
			if (parameters.length < 1) {
				m = method;
				isContinue = false;
			}
			if (isContinue) {
				return method;
			}
		}
		return m;
	}

	@SuppressWarnings("unused")
	private static Result doInvokerMethod(Object entity, Method method, LinkedHashMap<String, Object> params) {
		Result result = new Result();
		List<Object> argsList = new LinkedList<>();
		for (Entry<String, Object> parameter : params.entrySet()) {
			argsList.add(parameter.getValue());
		}
		Object[] args = argsList.toArray();
		Parameter[] parameters = method.getParameters();
		if (parameters.length > args.length) {
			result.setStatusCode(RespCodeState.API_OPERATOR_MISS_PARA.getStatusCode());
			result.setMessage(RespCodeState.API_OPERATOR_MISS_PARA.getMessage());
			return result;
		}

		for (int i = 0; i < parameters.length; i++) {
			args[i] = transParameter(parameters[i], args[i]);
		}
		try {
			Object invoke = method.invoke(entity, args);
			if (invoke != null) {
				if (invoke instanceof Result) {
					return (Result) invoke;
				} else {
					result.setSuccess(true);
					result.setData(invoke);
					return result;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.setStatusCode(RespCodeState.API_ERROE_CODE_3000.getStatusCode());
			if (StringUtil.isEmpty(e.getMessage())) {
				Throwable throwable = e.getCause();
				if (throwable != null) {
					if (throwable instanceof IllegalArgumentException) {
						result.setMessage(((IllegalArgumentException) throwable).getMessage());
					} else if (throwable instanceof RpcException) {
						result.setMessage("接口无响应");
					} else if (StringUtil.notEmpty(throwable.getMessage())) {
						String message = throwable.getMessage();
						if (StringUtil.notEmpty(message)) {
							int index = message.indexOf('\n');
							if (index > 0) {
								message = message.substring(0, index);
								if (message.contains(":")) {
									message = message.substring(message.indexOf(':') + 1);
								}
							}
							if (StringUtil.notEmpty(message.trim()))
								result.setMessage(message);
						}
					}
				}
			} else {
				result.setMessage(e.getMessage());
			}
			return result;
		}
		return result;
	}

	/**
	 * 动态生成数组
	 * 
	 * @param clzz
	 * @param value
	 * @return
	 */
	private static Object TransArrayValue(Class<?> clzz, Object value) {
		Object result = null;
		String valueType = value.getClass().getName();
		if (!valueType.equals("java.lang.String")) {
			return value;
		}
		String clzzName = clzz.getName();
		String temp = value.toString();
		temp = temp.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
		String[] array = temp.split(",");
		if (array == null || array.length == 0) {
			return null;
		}
		String realClass = clzzName.substring(2).replaceAll(";", "").trim();

		try {
			Class<?> realClzz = null;
			if (realClass.equals(Object.class.getName())) {
				realClzz = Object.class;
			} else {
				realClzz = Class.forName(realClass);
			}
			result = Array.newInstance(realClzz, array.length);

			for (int i = 0; i < array.length; i++) {
				String item = array[i];
				Object _v = TransValue(realClzz, item);
				Array.set(result, i, _v);
			}
			return result;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return result;
	}

	private static Object transParameter(Parameter parameter, Object value) {
		String valueType = value.getClass().getName();
		if (!valueType.equals("java.lang.String")) {
			return value;
		}
		Type type = parameter.getParameterizedType();

		String isClass = type.getClass().getTypeName();

		if (isClass.equals("java.lang.Class")) {
			return TransValue(parameter.getType(), value);
		} else if (isClass.contains("GenericArrayType")) {
			return transGenericArray(type, value);
		} else if (isClass.contains("ParameterizedType")) {
			if (parameter.getType().equals(List.class)) {
				return transList(type, value);
			} else if (parameter.getType().equals(Map.class)) {
				return transMapValue(type, value);
			}

		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void transJsonToMap(Map map, Type[] types, JSONObject json) {
		try {
			Class<?> keyClzz = Class.forName(types[0].getTypeName());
			Class<?> valueClzz = Class.forName(types[1].getTypeName());
			Set<String> keys = json.keySet();
			for (String c : keys) {
				Object _k = TransValue(keyClzz, c);
				Object _v = TransValue(valueClzz, json.get(c));
				map.put(_k, _v);
			}
		} catch (IllegalArgumentException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private static Object transGenericArray(Type type, Object value) {
		Class<?> clzz = null;
		Object result = null;
		GenericArrayType genericType = (GenericArrayType) type;
		ParameterizedType parameterizedType = (ParameterizedType) genericType.getGenericComponentType();
		Type[] argsType = parameterizedType.getActualTypeArguments();
		String realClassName = parameterizedType.getRawType().getTypeName();
		try {
			clzz = Class.forName(realClassName);
			if (!value.toString().startsWith("[")) {
				return null;
			}
			JSONArray jsonArray = (JSONArray) JSONArray.parse(value.toString());

			if (realClassName.equals("java.util.Map$Entry")) {
				return result;
			}
//			Set<String> keys=jsonObject.keySet();
			if (jsonArray.size() < 1) {
				return result;
			}
			if (argsType.length < 1) {
				return result;
			}
			result = Array.newInstance(clzz, jsonArray.size());
			Class<?> valueClzz = Class.forName(argsType[0].getTypeName());
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject c = (JSONObject) jsonArray.get(i);
				if (realClassName.contains("Map")) {
					Map entity = new HashMap();
					transJsonToMap(entity, argsType, c);
					Array.set(result, i, entity);
				} else {
					Object _v = valueClzz.newInstance();
					Map<String, Object> map = buildJSONToMap(c.toJSONString());
					buildMapToEntity(_v, map, valueClzz);
					Array.set(result, i, _v);
				}
			}
		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static Object transMapValue(Type type, Object value) {
		if (value.toString().startsWith("[")) {
			return null;
		}
		Object result = null;
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] argsType = parameterizedType.getActualTypeArguments();
		String realClassName = parameterizedType.getRawType().getTypeName();
		try {

			if (argsType.length < 1) {
				return result;
			}
			if (realClassName.equals("java.util.Map$Entry")) {
				return result;
			}

			JSONObject json = (JSONObject) JSONObject.parse(value.toString());
			Map entity = new HashMap();
			transJsonToMap(entity, argsType, json);
			result = entity;

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private static Object transList(Type type, Object value) {
		if (!value.toString().startsWith("[")) {
			return null;
		}
		Class<?> clzz = null;
		List result = new ArrayList();
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] argsType = parameterizedType.getActualTypeArguments();
		String realClassName = argsType[0].getTypeName();
		try {
			clzz = Class.forName(realClassName);

			JSONArray jsonArray = (JSONArray) JSONArray.parse(value.toString());
			if (jsonArray.size() < 1) {
				return result;
			}

			for (int i = 0; i < jsonArray.size(); i++) {
				Object entity = null;
				Object c = jsonArray.get(i);
				if (isBasicClass(clzz)) {
					entity = TransValue(clzz, c.toString());
				} else {
					Map<String, Object> map = buildJSONToMap(c.toString());
					entity = clzz.newInstance();
					buildMapToEntity(entity, map, argsType[0].getClass());
				}
				result.add(entity);
			}

		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static Object TransValue(Class<?> clzz, Object value) {
		Object result = null;
		try {
			String valueType = value.getClass().getName();
			if (!valueType.equals("java.lang.String")) {
				return value;
			}
			String clzzName = clzz.getName();
			if (clzzName.startsWith("[")) {
				// array
				return TransArrayValue(clzz, value);
			}
			switch (clzzName) {
			case "java.lang.Integer":
			case "int":
				result = Integer.parseInt(value.toString());
				break;
			case "java.lang.Long":
			case "long":
				result = Long.parseLong(value.toString());
				break;
			case "java.lang.String":
				result = String.valueOf(value);
				break;
			case "java.math.BigDecimal":
				result = new BigDecimal(value.toString());
				break;
			case "java.lang.Boolean":
			case "boolean":
				result = Boolean.parseBoolean(value.toString());
				break;
			case "java.lang.Float":
			case "float":
				result = Float.parseFloat(value.toString());
				break;
			case "java.lang.Double":
			case "double":
				result = Double.parseDouble(value.toString());
				break;
			case "java.sql.Timestamp":
				result = Timestamp.valueOf(value.toString());// (Date.parse(value));
				break;
			case "[Ljava.lang.String;":
				String temp = value.toString();
				temp = temp.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
				result = temp.split(",");// JSONObject.parseArray(value.toString(),
				break;
			default:
				result = value;
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	private static boolean isBasicClass(Class<?> clzz) {
		boolean flag = false;
		String clzzName = clzz.getName();
		if (clzzName.contains(Integer.class.getName()) || clzzName.contains(String.class.getName())
				|| clzzName.contains(Long.class.getName()) || clzzName.contains(BigDecimal.class.getName())
				|| clzzName.contains(Boolean.class.getName()) || clzzName.contains(Float.class.getName())
				|| clzzName.contains(Double.class.getName()) || clzzName.contains(Timestamp.class.getName())) {
			flag = true;
		} else if (clzzName.equals("int") || clzzName.equals("long") || clzzName.equals("boolean")
				|| clzzName.equals("float") || clzzName.equals("double")) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取所有方法
	 * 
	 * @param clzz       类名
	 * @param hasStatic  是否获取static方法
	 * @param hasPriavte 是否获取私有方法
	 * @return
	 */
	public static List<Method> getAllMethod(Class<?> clzz, Boolean hasStatic, Boolean hasPriavte) {
		if (clzz == null) {
			return null;
		}

		List<Method> methods = new ArrayList<Method>();
		Method[] m = clzz.getDeclaredMethods();
		for (Method item : m) {
			methods.add(item);
		}
		return methods;
	}

	public static List<Method> getAllMethod(Class<?> clzz) {
		return getAllMethod(clzz, false, false);
	}

	public static Method getMethod(Class<?> clzz, String method) {
		List<Method> methods = getAllMethod(clzz);
		Method m = methods.stream().filter(c -> c.getName().equals(method)).findFirst().get();
		return m;
	}

	public static Class<?> getInterface(Class<?> clzz) {
		if (clzz == null) {
			return null;
		}

		Class<?>[] inter = clzz.getInterfaces();
		if (inter == null || inter.length < 1) {
			return null;
		}
		return inter[0];
	}

	public static List<Field> getAllFields(Class<?> clzz) {
		if (clzz == null) {
			return null;
		}
		List<Field> fields = new ArrayList<Field>();
		Field[] fs = clzz.getDeclaredFields();
		for (Field f : fs) {
			fields.add(f);
		}
		return fields;
	}

	public static <T> Map<String, Object> buildEntityToMap(T entity, boolean needHum) {
		Map<String, Object> result = new HashMap<>();
		Object object = JSONObject.toJSON(entity);
		if (object instanceof JSONObject) {
			JSONObject jo = (JSONObject) object;
			Set<String> keys = jo.keySet();
			for (String k : keys) {
				String key = k;
				Object value = jo.get(key);

				if (value != null) {
					if (StringUtil.isEmpty(value.toString())) {
						continue;
					}
					if (!isBasicClass(value.getClass())) {
						value = buildEntityToMap(value, needHum);
					}
					if (needHum) {
						key = NameHumpHelper.humpToLine(key);
					}
					result.put(key, value);
				}
			}
		}
		return result;
	}

	public static Class<?> buildClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
