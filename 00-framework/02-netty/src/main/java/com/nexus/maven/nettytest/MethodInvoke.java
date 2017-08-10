package com.nexus.maven.nettytest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by @panyao on 2017/8/9.
 */
public class MethodInvoke {

    public static void main(String[] args) {

        Test test = new Test();

        Method[] methods = new Method[4];
        for (int i = 0, j = 0; i < test.getClass().getMethods().length; i++) {
            Empty empty = test.getClass().getMethods()[i].getAnnotation(Empty.class);
            if (empty != null) {
                methods[j++] = test.getClass().getMethods()[i];
            }
        }

        try {
            System.out.println(methods[0].getParameterTypes().length);
            methods[0].invoke(test, new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface Empty {
}

class Test {

    @Empty
    public void args() {
        System.out.println("none");
    }

    @Empty
    public void args(Long l) {
        System.out.println("arg 1");
    }

    @Empty
    public void args(Long l, Integer ll) {
        System.out.println("arg 2");
    }
    @Empty
    public void args(Object [] xx) {
        System.out.println("none");
    }

}
