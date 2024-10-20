package mg.itu.framework.sprint.utils;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import mg.itu.framework.sprint.annotation.Argument;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Reflect {

    public HashMap<String,String[]> getMapForParameter(Method method) throws InstantiationException, IllegalAccessException {
        Parameter [] argument = method.getParameters();
        HashMap<String,String[]> result = new HashMap<String, String[]>();
        String [] parameterName = this.getParameterName(method);
        for (int i=0;i<argument.length;i++){
            Annotation arg_annotation = argument[i].getAnnotation(Argument.class);
            String name_arg = parameterName[i];
            if (arg_annotation != null){
                name_arg = ((Argument) arg_annotation).name();
            }
            Class<?> clazz = argument[i].getType();
            if (this.isObject(clazz)){
                String [] values = this.getValuesForHasMap(clazz);
                result.put(name_arg,values);
            }
            else {
                String [] values = new String[1];
                values[0] = name_arg;
                result.put(name_arg,values);
            }
        }
        return result;
    }

    public String [] getParameterName(Method method){
        Paranamer paranamer = new AdaptiveParanamer();
        String [] parameterName = paranamer.lookupParameterNames(method);
        return  parameterName;
    }

    public String [] getValuesForHasMap (Class<?> clazz) throws InstantiationException, IllegalAccessException {
        List<String> result = new ArrayList<>();
        Object obj = clazz.newInstance();
        Field [] attributs = obj.getClass().getDeclaredFields();
        for (Field attr : attributs){
            result.add(attr.getName());
        }
        return result.toArray(new String[] {});
    }

    public boolean isObject(Class<?> clazz){
        if(clazz == String.class){
            return false;
        }
        if(clazz == Integer.class){
            return false;
        }
        if(clazz == Double.class){
            return false;
        }
        if(clazz == Date.class){
            return false;
        }
        return true;
    }


}
