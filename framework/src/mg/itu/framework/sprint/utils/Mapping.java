package mg.itu.framework.sprint.utils;

import mg.itu.framework.sprint.annotation.Get;
import mg.itu.framework.sprint.annotation.Post;
import mg.itu.framework.sprint.annotation.Url;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Mapping {
    String className;
    String methodName;
    String verb;

    public Mapping() {
    }

    public Mapping(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public Mapping(String className, String methodName, String verb) {
        this.className = className;
        this.methodName = methodName;
        this.verb = verb;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public HashMap<String,Mapping> checkMaps (ArrayList<Class<?>> controlers) throws Exception {
        HashMap<String,Mapping> result = new HashMap<String,Mapping>();
        if (controlers != null) {
            for (Class<?> classe : controlers) {
                Method[] methods = classe.getDeclaredMethods();
                for (int i=0;i< methods.length;i++){
                    if (methods[i].isAnnotationPresent(Url.class)) {
                        Annotation annotation = methods[i].getAnnotation(Url.class);
                        String url = ((Url) annotation).value();
                        if (result.get(url)==null){
                            Mapping mapping = new Mapping(classe.getSimpleName(),methods[i].getName(),this.getVerbAnnotation(methods[i]));
                            result.put(url,mapping);
                        }
                        else {
                            throw new Exception("Il y a une repetition lors de l'annotation de vos methods");
                        }
                    }
                }
            }
        }
        return result;
    }

    public Mapping searchUrl(HashMap<String,Mapping> maps, String url) throws Exception {
        Mapping result = null;
        String [] chemin = url.split("/");
        String new_url = "";
        for (int i=chemin.length-1;i>=0 ;i--){
            if (i<chemin.length-1){
                new_url = "/"+new_url;
            }
            new_url = chemin[i]+new_url;
            Mapping map = maps.get(new_url);
            if (map!=null){
                result = map;
            }
        }
        if (result == null){
            throw new Exception("404 not found");
        }
        return result;
    }

    public boolean verifMethodURL (String methodName) {
        boolean result = false;
        if (this.getVerb().compareToIgnoreCase(methodName) == 0){
            result = true;
        }
        return result;
    }

    public String getVerbAnnotation (Method m) {
        String result = "GET";
        if (m.isAnnotationPresent(Get.class)) {
            result = "GET";
        }
        if (m.isAnnotationPresent(Post.class)) {
            result = "POST";
        }
        return result;
    }
}
