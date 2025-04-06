package net.uncc.app.session;


import java.util.HashMap;

public class SessionData {

    private HashMap<String,Object> userSessionData = new HashMap<>();

    public SessionData() {
    }

    public SessionData(String key, String value) {
        setValue(key,value);
    }

    //GETTERS & SETTERS
    public Object getValue(String key) {
        if (!userSessionData.containsKey(key)) {
            return null;
        }
        // else
        return userSessionData.get(key);
    }

    public void setValue(String key, Object value) {
        if (userSessionData.containsKey(key)) {
            userSessionData.replace(key,value);
        } else {
            userSessionData.put(key,value);
        }
    }

}
