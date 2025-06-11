package mg.itu.framework.sprint.utils;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.framework.sprint.annotation.validation.Date;
import mg.itu.framework.sprint.annotation.validation.Email;
import mg.itu.framework.sprint.annotation.validation.Numerique;
import mg.itu.framework.sprint.annotation.validation.Required;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Validation {
    public String checkValidation (Field attr,String valeur) throws Exception {
        boolean result = true;
        String error = null;
        if (attr.isAnnotationPresent(Required.class)){
            if (valeur.equals("")) {
                result = false;
                error = "L'input de l'attribut "+attr.getName()+" doit etre completer ";
            }
        }
        if (attr.isAnnotationPresent(Numerique.class)) {
            if (!valeur.matches("-?\\d+(\\.\\d+)?")){
                result = false;
                error = "L'input de l'attribut "+attr.getName()+" doit etre de type numerique ";
            }
        }
        if (attr.isAnnotationPresent(Email.class)){
            if (!valeur.contains("@")){
                result = false;
                error = "L'input de l'attribut "+attr.getName()+" doit etre de type email ";
            }
        }
        if (attr.isAnnotationPresent(Date.class)){
            try {
                java.sql.Date.valueOf(valeur);
            }catch (Exception e){
                result = false;
                error = "L'input de l'attribut "+attr.getName()+" doit etre de type date ";
            }
        }

        return error;
    }

    public static String printError(HttpServletRequest request,String input_name){
        Parametre error = (Parametre) request.getAttribute(input_name);
        String result = "";
        if (error != null){
            if (!error.isStatus()){
                result =  error.getValue();
                result+=new Validation().setPagePrecedent(request);
            }
        }

        return result;
    }

    public static String printValue(HttpServletRequest request,String input_name){
        Parametre error = (Parametre) request.getAttribute(input_name);
        String result = "";
        if (error != null){
            if (error.isStatus()){
//                new Validation().setPagePrecedent(request);
                result = error.getValue();
            }
        }
        return result;
    }

    public String getPagePrecedent(HttpServletRequest request){
        String obj = request.getParameter("pagePrecedent");
        String pagePrecedent = "";
        if (obj == null){
            pagePrecedent = request.getHeader("Referer");
        }
        else {
            pagePrecedent = obj;
        }
        return pagePrecedent;
    }

    public String setPagePrecedent(HttpServletRequest request){
        String obj = request.getParameter("pagePrecedent");
        String result = "";
        if (obj != null){
            result = "<input type='hidden' name='pagePrecedent' value='"+obj+"' >";
        }
        return result;
    }

}
