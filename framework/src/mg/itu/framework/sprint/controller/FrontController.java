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
import mg.itu.framework.sprint.utils.Utils;

public class FrontController extends HttpServlet{

    private ArrayList<Class<?>> classController;
    private HashMap<String, Mapping> maps;

    public HashMap<String, Mapping> getMaps() {
        return maps;
    }

    public void setMaps(HashMap<String, Mapping> maps) {
        this.maps = maps;
    }

    public void init() throws ServletException {
        try {
            String packageCtrl = this.getInitParameter("packageName");
            this.setClassController(CheckController.getControllerClasses(packageCtrl));
            HashMap<String,Mapping> maps = new Mapping().checkMaps(this.getClassController());
            this.setMaps(maps);
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

    public void processRequest(HttpServletRequest request, HttpServletResponse response,String verb) throws IOException , ServletException{
        PrintWriter out = response.getWriter();
        out.println("URL : " + request.getRequestURI());
        try {
            Mapping map = new Mapping().searchUrl(this.getMaps(),request.getRequestURI());
            if (map.verifMethodURL(verb)){
                out.println("ClassName : "+map.getClassName());
                out.println("MethodName : "+map.getMethodName());
                String packageCtrl = this.getInitParameter("packageName");
                new Utils().executeMethod(packageCtrl,map,request,response);
            }
            else {
                throw new Exception("Vous utiliser des verbes differents pour une method identique");
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response,"GET");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response,"POST");
    }
}
