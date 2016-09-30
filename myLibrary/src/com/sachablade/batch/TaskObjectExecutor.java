package com.sachablade.batch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TaskObjectExecutor {
	private Object classTask;
	private String methodName;
	private Object[] params;

	public TaskObjectExecutor(Object classTask, String method, Object[] parameters)
			throws NoSuchMethodException, SecurityException {
		super();
		this.classTask = classTask;
		this.methodName = method;
		this.params = parameters;
	}

	public void Execute() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if (classTask == null || methodName == null) {
			return;
		}
		Class<?> cls = classTask.getClass();
		Method[] methods = cls.getMethods();
		Method toInvoke = null;
		methodLoop: for (Method method : methods) {
			if (!methodName.equals(method.getName())) {
				continue;
			}
			Class<?>[] paramTypes = method.getParameterTypes();
			if (params == null && paramTypes == null) {
				toInvoke = method;
				break;
			} else if (params == null || paramTypes == null || paramTypes.length != params.length) {
				continue;
			}

			for (int i = 0; i < params.length; ++i) {
				if (!paramTypes[i].isAssignableFrom(params[i].getClass())) {
					continue methodLoop;
				}
			}
			toInvoke = method;
		}
		if (toInvoke != null) {
			try {
				toInvoke.invoke(classTask, params);
			} catch (Exception t) {
				t.printStackTrace();
			}
		}		
	}
}
