package safe_unsafe_publication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fang on 2017/11/26.
 * final保证可见性和原子性.
 */
public class SafeStates {
    private final Map<String,String> states;

    public SafeStates(){
        states = new HashMap<String, String>();
        states.put("akasja","AK");
        states.put("alabama","AL");//and so on
    }

    public String getAbbreviation(String s){
        return states.get(s);
    }
}
