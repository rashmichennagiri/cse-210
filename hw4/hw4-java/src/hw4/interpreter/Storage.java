package hw4.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * storage area for variable states
 * 
 * @author rashmichennagiri
 *
 */
public class Storage {

	private List<Map> archivedStates = new ArrayList<>();

	private static Map<String, Integer> variableStateStore = new HashMap<>(); // current store
	private static Map<String, String> arrayStore = new HashMap<>(); // current store

	/**
	 * 
	 */
	public static void resetVariableStore() {
		variableStateStore.clear();
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public static void defineVariable(String name, Integer value) {
		variableStateStore.put(name, value);
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public static void defineArray(String name, String value) {
		arrayStore.put(name, value);
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Object getVariableValue(String name) {
		if (variableStateStore.containsKey(name))
			return variableStateStore.get(name);
		else
			return 0;
	}

	/**
	 * 
	 * @return
	 */
	public static String getCurrentState() {

		StringBuilder sb = new StringBuilder();

		char rightArrow = (char) '\u2192';
		// System.out.print(rightArrow);

		sb.append("{");

		if(!arrayStore.isEmpty()) {

			Iterator<Entry<String, String>> iter = arrayStore.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				sb.append(entry.getKey());
				sb.append(" " + rightArrow + " ");
				sb.append(entry.getValue());
				if (iter.hasNext()) {
					sb.append(',').append(' ');
				}
			}
		}
		else {
			Iterator<Entry<String, Integer>> iter = variableStateStore.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = iter.next();
				sb.append(entry.getKey());
				sb.append(" " + rightArrow + " ");
				sb.append(entry.getValue());
				if (iter.hasNext()) {
					sb.append(',').append(' ');
				}
			}
		}
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 
	 * @param oldState
	 */
	public void archiveState(Map oldState) {
		archivedStates.add(oldState);
	}

	/**
	 * 
	 */
	public void printAllStates() {

		for (Map m : archivedStates) {
			System.out.println();
			for (Object k : m.keySet()) {
				String key = k.toString();
				String value = m.get(key).toString();
				System.out.print("	" + key + " " + value + ", ");
			}
		}
	}
}
