package view;

import business.Measurement;
import business.MeasurementDAO;
import business.State;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import java.io.Serializable;
import java.util.*;

@Named("homeController")
@ViewScoped
public class HomeController implements Serializable {

    @Inject
    private MeasurementDAO measurementDAO;
    private LineChartModel lineModel;

    @PostConstruct
    public void init() {
        createLineModel();
    }

    private void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        HashMap<Integer, Double> chartData = createChartData();
        List<Integer> sortedKeys = new ArrayList(chartData.keySet());
        Collections.sort(sortedKeys);

        for (Integer d : sortedKeys) {
            values.add(chartData.get(d));
            labels.add(d.toString());
        }

        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Emissionen in Tonnen pro Einwohner");
        dataSet.setBorderColor("rgb(33, 150, 243)");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);

        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(false);
        title.setText("Weltweite Emissionen (jährlich)");
        options.setTitle(title);

        lineModel.setOptions(options);
        data.setLabels(labels);
        lineModel.setData(data);

    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    private HashMap<Integer, Double> createChartData() {
        HashMap<Integer, Double> chartDataMap = new HashMap<>();

        List<Measurement> allMeasurements = measurementDAO.findAll();
        Calendar calendar = new GregorianCalendar();

        for (Measurement m : allMeasurements) {
            if (m.getState() == State.APPROVED) {
                calendar.setTime(m.getYear());
                int year = calendar.get(Calendar.YEAR);
                if (chartDataMap.containsKey(year)) {
                    double oldValue = chartDataMap.get(year);
                    chartDataMap.put(year, oldValue + m.getValue());
                } else {
                    chartDataMap.put(year, m.getValue());
                }
            }
        }
        return chartDataMap;
    }
}