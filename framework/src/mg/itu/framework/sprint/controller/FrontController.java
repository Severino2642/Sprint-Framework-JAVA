package mg.itu.framework.sprint.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import mg.itu.framework.sprint.utils.CheckController;

public class FrontController extends HttpServlet{

    private ArrayList<Class<?>> classController;
    
    public void init() throws ServletException{
        try {
            String packageCtrl = this.getInitParameter("packageName");
            this.setClassController(CheckController.getControllerClasses(packageCtrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Class<?>> getClassController() {
        return classController;
    }
    
    public void setClassController(ArrayList<Class<?>> classController) {
        this.classController = classController;
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        out.println("URL : " + request.getRequestURI());

        ArrayList<Class<?>> classes = new ArrayList<>();
        if (classes != null) {
            classes = this.getClassController();

            out.println("Controllers : ");
            for (Class<?> classe : classes) {
                out.println("- " + classe.getSimpleName());
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }

}
