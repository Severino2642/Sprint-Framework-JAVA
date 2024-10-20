package mg.itu.framework.sprint.utils;

import jakarta.servlet.annotation.MultipartConfig;
import mg.itu.framework.sprint.annotation.Get;
import mg.itu.framework.sprint.annotation.Post;
import mg.itu.framework.sprint.annotation.Url;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Mapping {
    String className;
    List<VerbAction> verb_action = new ArrayList<>();

    public Mapping() {
    }

    public Mapping(String className, List<VerbAction> verb_action) {
        this.className = className;
        this.verb_action = verb_action;
    }

    public Mapping(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<VerbAction> getVerb_action() {
        return verb_action;
    }

    public void setVerb_action(List<VerbAction> verb_action) {
        this.verb_action = verb_action;
    }

    public void addVerbAction(VerbAction verb_action) {
        this.verb_action.add(verb_action);
    }

    public boolean isExist (VerbAction verb_action) {
        for (VerbAction vA : this.verb_action){
            if (vA.getVerb().equals(verb_action.getVerb())){
                return true;
            }
        }
        return false;
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
                        Mapping map = result.get(url);
                        if (map == null) {
                            Mapping mapping = new Mapping(classe.getSimpleName());
                            mapping.addVerbAction(new VerbAction(methods[i].getName(),this.getVerbAnnotation(methods[i])));
                            result.put(url,mapping);
                        }
                        else {
                            VerbAction verbAction = new VerbAction(methods[i].getName(),this.getVerbAnnotation(methods[i]));
                            if (!map.isExist(verbAction)) {
                                map.addVerbAction(verbAction);
                            }
                            else {
                                throw new Exception("Il y a une repetition lors de l'annotation de vos methods");
                            }
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

    public VerbAction verifMethodURL (String verb) {
        VerbAction result = null;
        for (VerbAction vA : this.verb_action){
            if (vA.getVerb().compareToIgnoreCase(verb) == 0){
                result = vA;
            }
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
