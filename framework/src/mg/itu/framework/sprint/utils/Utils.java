package mg.itu.framework.sprint.utils;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.framework.sprint.annotation.Argument;
import mg.itu.framework.sprint.annotation.Get;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static String getFileName(String fileName, String extension) {
        return fileName.substring(0, (fileName.length() - extension.length()) - 1);
    }

    public void addSession(Object obj,HttpServletRequest request,HttpServletResponse response) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        Field [] attributs = obj.getClass().getDeclaredFields();
        PrintWriter out = response.getWriter();
        for (Field attr : attributs){
            if(attr.getType() == MySession.class){
                String method_name = "set"+this.maj(attr.getName());
                MySession session = new MySession(request.getSession());
                Object [] arguments = new Object[1];
                arguments[0] = session;
                out.println("1 : "+attr.getName());
                obj.getClass().getDeclaredMethod(method_name, MySession.class).invoke(obj,session);
                out.println("2 : "+attr.getName());
                break;
            }
        }
    }

    public void executeMethod (String packageCtrl,Mapping map, HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException , Exception {
        Class<?> clazz = Class.forName(packageCtrl+"."+map.getClassName());
        Object obj = clazz.newInstance();
        this.addSession(obj,request,response);
        Method method= this.getMethod(obj.getClass().getDeclaredMethods(),map.getMethodName());
        PrintWriter out = response.getWriter();
        if (method.getReturnType() == String.class || method.getReturnType() == ModelView.class){
            List<Object> MethodParameters = new ArrayList<>();
            if (method.getParameters().length > 0) {
                MethodParameters = this.prepareParameter(obj,method,request,response);
                out.println("mandehaa24");
                if (MethodParameters.size() != method.getParameters().length){
                    throw new Exception("Le nombre de parametre est insuffisant !");
                }
            }
            if (method.getReturnType() == String.class){
                out.println("Execute methods : "+ method.invoke(obj,MethodParameters.toArray(new Object[]{})).toString());
            }
            if (method.getReturnType() == ModelView.class){
                this.sendModelView((ModelView) method.invoke(obj,MethodParameters.toArray(new Object[]{})),request,response);
            }
        }

        else {
            throw new Exception("Le type de retour du fonction "+method.getName()+" dans "+clazz.getName()+".java est invalide");
        }
    }

    public Method getMethod(Method [] methods,String methodName){
        Method result = null;
        for (int i=0;i< methods.length;i++){
            if (methods[i].isAnnotationPresent(Get.class) && methods[i].getName().compareTo(methodName)==0) {
                result=methods[i];
            }
        }
        return result;
    }

    public List<Object> prepareParameter(Object obj, Method method, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Object> result = new ArrayList<>();
        PrintWriter out = response.getWriter();
        Parameter [] argument = method.getParameters();
        String [] parameterName = this.getParameterName(method);
        for (int i=0;i<argument.length;i++){
            Annotation arg_annotation = argument[i].getAnnotation(Argument.class);
            String name_arg = parameterName[i];
            if (arg_annotation != null){
                name_arg = ((Argument) arg_annotation).name();
            }
//            if (arg_annotation == null ){
//                throw new Exception("ETU002642 : L'argument "+name_arg+" n'est pas annoter ");
//            }
            Class<?> clazz = argument[i].getType();

            if(clazz == MySession.class){
                MySession session = new MySession(request.getSession());
                result.add(session);
            }
            if (this.isObject(clazz) && clazz != MySession.class){
                out.println("arg :" +name_arg);
                Object o = clazz.newInstance();
                result.add(this.prepareObject(name_arg,o,request));
            }
            if (!this.isObject(clazz)) {
                if(request.getParameter(name_arg)!=null){
                    result.add(this.castValueOfParameter(request.getParameter(name_arg),argument[i].getType()));
                }
            }
        }

        return result;
    }


    public void sendModelView (ModelView donnee,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        for (Map.Entry<String,Object> data : donnee.getData().entrySet()){
            String name = data.getKey();
            Object value = data.getValue();
            request.setAttribute(name,value);
        }
        String url = request.getContextPath()+"/"+donnee.getUrl();
        RequestDispatcher dispat = request.getServletContext().getRequestDispatcher("/"+donnee.getUrl());
        dispat.forward(request,response);
    }


    public String [] getParameterName(Method method){
        Paranamer paranamer = new AdaptiveParanamer();
        String [] parameterName = paranamer.lookupParameterNames(method);
        return  parameterName;
    }

    public Object castValueOfParameter(String value,Class<?> clazz) throws Exception {
        Object result = null;
        try {
            if(clazz == String.class){
                result = value;
            }
            if(clazz == Integer.class){
                result = Integer.valueOf(value);
            }
            if(clazz == Double.class){
                result = Double.valueOf(value);
            }
            if(clazz == Date.class){
                result = Date.valueOf(value);
            }
        }catch (Exception e){
            throw new Exception("Impossible de caster l'objet");
        }
        return result;
    }

    public Object prepareObject (String name,Object obj, HttpServletRequest request) throws Exception {
        Field[] attributs = obj.getClass().getDeclaredFields();
        for (Field attr : attributs){
            String method_name = "set"+this.maj(attr.getName());
            Method method = obj.getClass().getDeclaredMethod(method_name,attr.getType());
            String input_name = name+":"+attr.getName();
            if(request.getParameter(input_name)!=null){
                method.invoke(obj,this.castValueOfParameter(request.getParameter(input_name),attr.getType()));
            }
        }
        return obj;
    }

    public String maj(String mot){
        return mot.substring(0,1).toUpperCase() + mot.substring(1);
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
