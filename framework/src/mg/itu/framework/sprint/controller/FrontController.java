package mg.itu.framework.sprint.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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

import mg.itu.framework.sprint.utils.*;

@MultipartConfig
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
//        out.println("URL : " + request.getRequestURI());
        try {
            Mapping map = new Mapping().searchUrl(this.getMaps(),request.getRequestURI());
            VerbAction verbAction = map.verifMethodURL(verb);
            if (verbAction != null) {
//                out.println("ClassName : "+map.getClassName());
//                out.println("MethodName : "+verbAction.getMethodName());
                String packageCtrl = this.getInitParameter("packageName");
                new Utils().executeMethod(packageCtrl,verbAction,map,request,response);
            }
            else {
                throw new Exception("Vous utiliser des verbes differents pour une method identique");
            }
        } catch (Exception e) {
            response.setContentType("text/html");
            String errorPage = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>SprintError</title>\n" +
                    "<style>\n" +
                    "*{ padding: 0px;\n" +
                    "    margin: 0px;\n" +
                    "    box-sizing: border-box; " +
                    "    font-family: sans-serif;" +
                    "}\n" +
                    "</style>\n"+
                    "</head>\n" +
                    "<body>\n" +
                    "    <h2 style='padding:15px;background-color: rgba(0, 0, 0, 0.355);'>\uD83D\uDE35\u200D\uD83D\uDCAB Sprint Error \uD83D\uDE35\u200D\uD83D\uDCAB</h2>\n" +
                    "    <div style='text-align:center;padding:15px;width:100%;'><p>"+e.getMessage()+"</p></div>\n" +
                    "</body>\n" +
                    "</html>";
            out.println(errorPage);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response,"GET");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response,"POST");
    }
}
