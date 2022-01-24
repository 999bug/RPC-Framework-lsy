package com.ncst.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例工厂
 *
 * @author Lsy
 * @date 2022/1/24
 */
public class SingletonFactory {

    private static final Map<Class<?>, Object> objMap = new HashMap<>();

    private SingletonFactory() {}

    public static <T> T getInstance(Class<T> clazz) {
        Object instance = objMap.get(clazz);
        synchronized (clazz) {
            if (instance == null) {
                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                    objMap.put(clazz, instance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return clazz.cast(instance);
    }
}
