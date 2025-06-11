package mg.itu.framework.sprint.utils;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.framework.sprint.annotation.Authentification;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Auth {
    public boolean check_authClass(Class<?> clazz, HttpServletRequest request) {
        boolean result = true;
        Annotation class_annotation = clazz.getAnnotation(Authentification.class);
        if (class_annotation != null){
            int level = ((Authentification) class_annotation).level();
            int auth = (int) request.getSession().getAttribute("authentification");
            if (level>auth){
                result = false;
            }
        }
        return result;
    }

    public boolean check_authMethod(Method method, HttpServletRequest request) {
        boolean result = true;
        Annotation class_annotation = method.getAnnotation(Authentification.class);
        if (class_annotation != null){
            int level = ((Authentification) class_annotation).level();
            int auth = (int) request.getSession().getAttribute("authentification");
            if (level>auth){
                result = false;
            }
        }
        return result;
    }
}
