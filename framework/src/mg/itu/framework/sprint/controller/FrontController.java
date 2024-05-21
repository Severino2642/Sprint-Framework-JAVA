package mg.itu.framework.sprint.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import mg.itu.framework.sprint.utils.CheckController;
import mg.itu.framework.sprint.utils.HashMap;

public class FrontController extends HttpServlet{

    private ArrayList<Class<?>> classController;
    private ArrayList<HashMap> maps;

    public ArrayList<HashMap> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<HashMap> maps) {
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
        this.initValue();
        this.setMaps(new HashMap().checkMaps(this.getClassController()));
        if (new HashMap().verifRepetition(this.getMaps())){
            try {
                throw new Exception("Il y a une repetition lors de l'annotation de vos methods");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
        String url = new HashMap().makeUrl(request.getRequestURI());
        HashMap map = new HashMap().findByUrl(this.getMaps(),url);
        if (map == null){
            try {
                out.println("Le chemin indiquer est introuvable");
                throw new Exception("Le chemin indiquer est introuvable !");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (map != null){
            out.println("HashMap URL :"+ map.getUrl());
            out.println("ClassName : "+map.getMapping().getClassName());
            out.println("MethodName : "+map.getMapping().getMethodName());
        }

//        for (HashMap map:maps) {
//            out.println( map.getUrl() +" : "+map.getMapping().getClassName()+" && "+map.getMapping().getMethodName());
//        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }


}
