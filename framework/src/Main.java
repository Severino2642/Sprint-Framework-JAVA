import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import mg.itu.framework.sprint.utils.ModelView;
import mg.itu.framework.sprint.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Class<?> clazz = Vector.class;
        Object o = clazz.newInstance();

        Field [] attr = o.getClass().getDeclaredFields();
        System.out.println("nbAttr : "+ attr.length);
        for (Field f : attr){
            System.out.println(f.getName());
        }
    }
}