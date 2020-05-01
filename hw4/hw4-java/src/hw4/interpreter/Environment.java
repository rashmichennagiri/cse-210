package hw2.interpreter;

import java.util.HashMap;
import java.util.Map;

/** 
 * storage area for variables
 * 
 * @author rashmichennagiri
 *
 */
public class Environment {
	
	private final Map<String, Object> variableStore = new HashMap<>();
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void defineVariable(String name, Object value) {
			variableStore.put(name, value);
	}
	
	public Object getVariableValue(String name) {
		if(variableStore.containsKey(name))
			return variableStore.get(name);
		else 
			return 0;
	}
}
