package plus.crates.Utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

	public static final String NMS_PATH = getNMSPackageName();
	public static final String CB_PATH = getCBPackageName();

	public static String getNMSPackageName() {
		return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}

	public static String getCBPackageName() {
		return "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}

	/**
	 * Class stuff
	 */

	public static Class getClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Class getNMSClass(String className) {
		return getClass(NMS_PATH + "." + className);
	}

	public static Class getCBClass(String className) {
		return getClass(CB_PATH + "." + className);
	}

	/**
	 * Field stuff
	 */

	public static Field getField(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			return field;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getField(Class<?> clazz, String fieldName, Object instance) {
		try {
			return (T) getField(clazz, fieldName).get(instance);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setField(Class<?> clazz, String fieldName, Object instance, Object value) {
		try {
			getField(clazz, fieldName).set(instance, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method stuff
	 */

	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
		try {
			Method method = clazz.getDeclaredMethod(methodName, params);

			if (!method.isAccessible()) {
				method.setAccessible(true);
			}

			return method;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T invokeMethod(Method method, Object instance, Object... args) {
		try {
			return (T) method.invoke(instance, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Constructor getConstructor(Class<?> clazz, Class<?>... params) {
		try {
			Constructor constructor = clazz.getConstructor(params);

			if (!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}

			return constructor;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T invokeConstructor(Constructor constructor, Object... args) {
		try {
			return (T) constructor.newInstance(args);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}