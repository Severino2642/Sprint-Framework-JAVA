package mg.itu.framework.sprint.utils;

import java.util.HashMap;

public class ModelView {
    String url;
    HashMap<String,Object> data = new HashMap<String,Object>();

    public ModelView() {
    }

    public ModelView(String url, HashMap<String, Object> data) {
        this.url = url;
        this.data = data;
    }

    public ModelView(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addData(String variable_name,Object variable_value){
        this.data.put(variable_name,variable_value);
    }
}
