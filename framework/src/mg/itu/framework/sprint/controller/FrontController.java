package mg.itu.framework.sprint.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mg.itu.framework.sprint.utils.CheckController;
import mg.itu.framework.sprint.utils.Mapping;
import mg.itu.framework.sprint.utils.ModelView;

public class FrontController extends HttpServlet{

    private ArrayList<Class<?>> classController;
    private HashMap<String, Mapping> maps;

    public HashMap<String, Mapping> getMaps() {
        return maps;
    }

    public void setMaps(HashMap<String, Mapping> maps) {
        this.maps = maps;
    }

    public void initValue() throws ServletException{
        try {
            String packageCtrl = this.getInitParameter("packageName");
            this.setClassController(CheckController.getControllerClasses(packageCtrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws ServletException {
        try {
            String packageCtrl = this.getInitParameter("packageName");
            this.initValue();
            HashMap<String,Mapping> maps = new Mapping().checkMaps(this.getClassController());
            if (maps!=null){
                this.setMaps(maps);
            }
            if (this.getClassController().size() == 0){
                throw new Exception("le package " +packageCtrl+ " est vide ou n'existe pas");
            }
            if (maps==null){
                throw new Exception("Il y a une repetition lors de l'annotation de vos methods");   
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Class<?>> getClassController() {
        return classController;
    }
    
    public void setClassController(ArrayList<Class<?>> classController) {
        this.classController = classController;
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException , ServletException{
        PrintWriter out = response.getWriter();
        out.println("URL : " + request.getRequestURI());
        out.println("Path : " + request.getContextPath());
        Mapping map = new Mapping().searchUrl(this.getMaps(),request.getRequestURI());
        try {
            if (map != null){
                out.println("ClassName : "+map.getClassName());
                out.println("MethodName : "+map.getMethodName());
                this.executeMethod(map,request,response);
            }
            else {
                throw new Exception("404 not found");
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }

    public void executeMethod (Mapping map,HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException {
        String packageCtrl = this.getInitParameter("packageName");
        Class<?> clazz = Class.forName(packageCtrl+"."+map.getClassName());
        Method method= clazz.getDeclaredMethod(map.getMethodName());
        PrintWriter out = response.getWriter();
        try {
            if (method.getReturnType() == String.class || method.getReturnType() == ModelView.class){
                Object obj = clazz.newInstance();
                if (method.getReturnType() == String.class){
                    out.println("Execute methods : "+ method.invoke(obj).toString());
                }
                if (method.getReturnType() == ModelView.class){
                    this.sendModelView((ModelView) method.invoke(obj),request,response);
                }
            }
            else {
                throw new Exception("Le type de retour du fonction "+method.getName()+" dans "+clazz.getName()+".java est invalide");
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
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
