package com.ncst.util;

import java.util.Set;

/**
 * @author Lsy
 * @date 2022/1/14
 */
public class ReflectUtil {

    private ReflectUtil() {}

    /**
     * 提供的堆栈跟踪信息的印刷 printStackTrace()编程访问。
     * 返回一个堆栈跟踪元素的数组，每个数组代表一个堆栈帧
     * 数组的最后一个元素（假设数组的长度为非0）表示堆栈的底部，这是序列中的第一个方法调用。
     * main方法调用栈 方法的调用本质来说就是栈帧的入栈和出栈的操作，而main方法一定是在栈底的
     * @return 获取main所在的类
     */
    public static String getMainClass() {
       StackTraceElement[] stack = new Throwable().getStackTrace();
       return stack[stack.length - 1].getClassName();
    }

    /**
     * 根据包名获取类的set集合
     * @param packageName 包名
     * @return 所有类的set集合
     */
    public static Set<Class<?>> getClasses(String packageName) {
        return null;
    }

    /**
     * 以文件的方式扫描整个包下的文件 并添加到集合中
     */
    private static void findClassInPackageByFile(String packageName, String packagePath,
                                                 final boolean recursion, Set<Class<?>> classes) {

    }
}
