package com.ncst.util;

import com.ncst.common.RpcError;
import com.ncst.exception.RpcException;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;

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
     *
     * @return 获取main所在的类
     */
    public static String getMainClass() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[stack.length - 1].getClassName();
    }

    /**
     * 根据包名获取类的set集合
     *
     * @param packageName 包名
     * @return 所有类的set集合
     */
    public static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classSet = new LinkedHashSet<>();
        boolean recursive = true;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                // 协议名称
                String protocol = url.getProtocol();
                switch (protocol) {
                    case "file":
                        // 包的绝对路径
                        String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.toString());
                        findClassInPackageByFile(packageName, filePath, recursive, classSet);
                        break;
                    case "jar":
                        Enumeration<JarEntry> entries = ((JarURLConnection) url.openConnection()).getJarFile().entries();
                        findClassByJar(packageDirName, packageName, classSet, entries);
                        break;
                    default:
                        throw new RpcException(RpcError.UNKNOW_ERROR);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    private static void findClassByJar(String packageDirName, String packageName, Set<Class<?>> classSet, Enumeration<JarEntry> entries) {
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            if (name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                if (idx != -1) {
                    packageName = name.substring(0, idx).replace('/', '.');
                }

                if (name.endsWith(".class") && !jarEntry.isDirectory()) {
                    //去掉后面的 ".class" 获取类名
                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                    try {
                        classSet.add(Class.forName(packageName + '.' + className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 以文件的方式扫描整个包下的文件 并添加到集合中
     */
    private static void findClassInPackageByFile(String packageName, String packagePath,
                                                 final boolean recursion, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(file -> (recursion && file.isDirectory()) || (file.getName().endsWith(".class")));
        Objects.requireNonNull(dirFiles);
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findClassInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(), recursion, classes);
            }else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
