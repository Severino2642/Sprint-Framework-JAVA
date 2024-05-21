package mg.itu.framework.sprint.utils;

import mg.itu.framework.sprint.annotation.Get;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class HashMap {
    String url;
    Mapping mapping;

    public HashMap() {
    }

    public HashMap(String url, Mapping mapping) {
        this.url = url;
        this.mapping = mapping;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public ArrayList<HashMap> checkMaps (ArrayList<Class<?>> controlers){
        ArrayList<HashMap> result = new ArrayList<>();
        if (controlers != null) {
            for (Class<?> classe : controlers) {
                Method [] methods = classe.getDeclaredMethods();
                for (int i=0;i< methods.length;i++){
                    if (methods[i].isAnnotationPresent(Get.class)) {
                        Annotation annotation = methods[i].getAnnotation(Get.class);
                        String url = classe.getSimpleName()+"/"+((Get) annotation).value();

                        Mapping mapping = new Mapping(classe.getSimpleName(),methods[i].getName());
                        result.add(new HashMap(url,mapping));
                    }
                }
            }
        }
        return result;
    }

    public String makeUrl (String url){
        String [] chemin = url.split("/");
        String result = "";
        for (int i=2;i<chemin.length;i++){
            if (i>2){
                result += "/";
            }
            result += chemin[i];
        }
        return result;
    }

    public boolean checkUrl (String url){
        if (this.getUrl().compareToIgnoreCase(url)==0){
            return true;
        }
        return false;
    }

    public HashMap findByUrl (ArrayList<HashMap> maps,String url){
        HashMap result = null;
        for (HashMap map:maps) {
            if (map.checkUrl(url)){
                result = map;
            }
        }
        return result;
    }

    public boolean verifRepetition (ArrayList<HashMap> maps){
        for (HashMap map1:maps) {
            int count = 0;
            for (HashMap map2:maps) {
                if (map2.checkUrl(map1.getUrl())){
                    count ++;
                }
            }
            if (count > 1){
                return true;
            }
        }
        return false;
    }
}
