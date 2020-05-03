package hw4.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * storage area for variable states
 * 
 * @author rashmichennagiri
 *
 */
public class Storage {
	
	private List<Map> archivedStates = new ArrayList<>();
	
	private final Map<String, Object> variableStateStore = new HashMap<>();	// current store
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void defineVariable(String name, Object value) {
			variableStateStore.put(name, value);
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Object getVariableValue(String name) {
		if(variableStateStore.containsKey(name))
			return variableStateStore.get(name);
		else 
			return 0;
	}
	
	/**
	 * 
	 * @param oldState
	 */
	public void archiveState(Map oldState) {
		archivedStates.add(oldState);
	}
	
	public void printAllStates() {
		
		for(Map m : archivedStates) {
			System.out.println();
			for (Object k: m.keySet()){
	            String key = k.toString();
	            String value = m.get(key).toString();  
	            System.out.print("	" + key + " " + value + ", ");  
	}
		}
	}
}
