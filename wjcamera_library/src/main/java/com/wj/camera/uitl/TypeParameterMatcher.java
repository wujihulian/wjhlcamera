

package com.wj.camera.uitl;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public  class TypeParameterMatcher {

    private static final String TAG = "TypeParameterMatcher";
    public static Type find0(Class<?> parametrizedSuperclass) {
        Type type = parametrizedSuperclass.getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) type;
        Type[] actualTypeArguments = p.getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[0];
        //  Class actualTypeArgument = (Class) actualTypeArguments[0];
        return actualTypeArgument;
    }
}
