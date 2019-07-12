package MWExtraction.dope;

import java.util.HashMap;
import java.util.Map;
//TODO : comment file
public class Metrics {

    private Map<String, Double> metrics;

    public Metrics() {
        this.metrics = new HashMap<>();
    }

    public double getMetric(String name){
        return metrics.get(name);
    }

    public void addMetric(String name, double value){
        name = name.toLowerCase();
        metrics.put(name, value);
    }
}
