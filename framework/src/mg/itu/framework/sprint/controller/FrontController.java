package mg.itu.framework.sprint.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import mg.itu.framework.sprint.utils.CheckController;
import mg.itu.framework.sprint.utils.Mapping;

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
            this.initValue();
            HashMap<String,Mapping> maps = new Mapping().checkMaps(this.getClassController());
            if (maps!=null){
                this.setMaps(maps);
            }
            else {
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
        Mapping map = new Mapping().searchUrl(this.getMaps(),request.getRequestURI());
        try {
            if (map != null){
                out.println("ClassName : "+map.getClassName());
                out.println("MethodName : "+map.getMethodName());
            }
            else {
                out.println("Le chemin indiquer est introuvable");   
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }


}
