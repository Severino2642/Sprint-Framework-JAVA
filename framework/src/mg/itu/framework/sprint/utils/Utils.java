package mg.itu.framework.sprint.utils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.framework.sprint.annotation.Argument;
import mg.itu.framework.sprint.annotation.Get;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    public static String getFileName(String fileName, String extension) {
        return fileName.substring(0, (fileName.length() - extension.length()) - 1);
    }

    public void executeMethod (String packageCtrl,Mapping map, HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException , Exception {
        Class<?> clazz = Class.forName(packageCtrl+"."+map.getClassName());
        Method method= this.getMethod(clazz,map.getMethodName());
        PrintWriter out = response.getWriter();
        if (method.getReturnType() == String.class || method.getReturnType() == ModelView.class){
            Object obj = clazz.newInstance();
            List<Object> MethodParameters = new ArrayList<>();
            if (method.getParameters().length > 0) {
                MethodParameters = this.prepareParameter(obj,method,request,response);
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

    public Method getMethod(Class<?> clazz,String methodName){
        Method [] methods = clazz.getDeclaredMethods();
        Method result = null;
        for (int i=0;i< methods.length;i++){
            if (methods[i].isAnnotationPresent(Get.class) && methods[i].getName().compareTo(methodName)==0) {
                result=methods[i];
            }
        }
        return result;
    }

    public List<Object> prepareParameter(Object obj, Method method, HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException, InstantiationException {
        Parameter[] argument = method.getParameters();
        List<Object> result = new ArrayList<>();
        PrintWriter out = response.getWriter();
        for (int i=0;i<argument.length;i++){
            Annotation arg_annotation = argument[i].getAnnotation(Argument.class);
            String name_annotation = "";
            if(arg_annotation != null){
                name_annotation = ((Argument) arg_annotation).name();
            }
            String realName = null;
            if (request.getParameter(name_annotation) != null){
                realName = name_annotation;
            }
            if (request.getParameter(argument[i].getName()) != null){
                realName = argument[i].getName();
            }
            if(realName != null){
                Object value = (Object) request.getParameter(realName);
                result.add(value);
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
}
