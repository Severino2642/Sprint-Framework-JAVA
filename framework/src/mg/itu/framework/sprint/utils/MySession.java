package mg.itu.framework.sprint.utils;

import jakarta.servlet.http.HttpSession;

public class MySession {
    HttpSession session;

    public MySession() {
    }

    public MySession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Object getData (String key){
        return this.getSession().getAttribute(key);
    }

    public void addData (String key,Object value){
        this.getSession().setAttribute(key,value);
    }

    public void delete (String key){
        this.getSession().setAttribute(key,null);
    }

    public void deleteAll (){
        this.getSession().invalidate();
    }
}
