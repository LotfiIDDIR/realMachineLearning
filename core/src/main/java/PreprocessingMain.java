package fr.ensma.lias.bimedia2018machinelearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author  Lotfi IDDIR
 */

public class PreprocessingMain {
    
    public static void main( String[] args ) 
    {
    	Map<String,Double>map = new TreeMap<String,Double>();
    	map.put("aaaa", 1.2);
    	map.put("bbbb", 1.4);
    	map.put("cccc", 1.3);
    	List<Entry<String, Double>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<String, Double> result = new HashMap<String, Double>();
        for (Entry<String, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

    	for ( Entry<String,Double> entry :result.entrySet())
    		System.out.println(entry);
    }
}