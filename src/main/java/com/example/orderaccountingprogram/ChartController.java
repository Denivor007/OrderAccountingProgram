package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.Database;
import com.example.orderaccountingprogram.DB_API.DatabaseCommands;
import com.example.orderaccountingprogram.DB_API.Order;
import com.example.orderaccountingprogram.Interfaces.DataReceiver;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.CheckBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChartController implements DataReceiver {


    @FXML
    private LineChart<?,?> lineChart;
    private ArrayList<Order> data;


    @FXML
    private CheckBox canceledCheck;

    @FXML
    private CheckBox distanceCheck;

    @FXML
    private CheckBox intravelCheck;

    @FXML
    private CheckBox offtravelCheck;

    @FXML
    private CheckBox paidCheck;

    @FXML
    private CheckBox profitCheck;

    @FXML
    private void initialize() {
        data = new ArrayList<>();

    }

    private ArrayList<XYChart.Series> createSeries(){
        ArrayList<XYChart.Series> dataSeries = new ArrayList<>();
        XYChart.Series dataSeriesProfit = new XYChart.Series();
        dataSeriesProfit.setName("profit");
        XYChart.Series dataSeriesDistance = new XYChart.Series();
        dataSeriesDistance.setName("distance");
        XYChart.Series dataSeriesPaid = new XYChart.Series();
        dataSeriesPaid.setName("paid trip");
        XYChart.Series dataSeriesCanceled = new XYChart.Series();
        dataSeriesCanceled.setName("canceled trip");
        XYChart.Series dataSeriesProfitIn = new XYChart.Series();
        dataSeriesProfitIn.setName("in travel time (minutes)");
        XYChart.Series dataSeriesProfitOff = new XYChart.Series();
        dataSeriesProfitOff.setName("off travel time (minutes)");

        OrderSpliterator spliterator = new OrderSpliterator();
        ArrayList<Order> sortedOrders = spliterator.getSortedByTimeList(data);

        if(sortedOrders.isEmpty()) return null;

        BigInteger secondDelta = BigInteger.valueOf(sortedOrders.get(sortedOrders.size()-1).getStart_trip().getTime() - sortedOrders.get(0).getStart_trip().getTime());
        BigInteger twoYears = BigInteger.valueOf(63000000).multiply(BigInteger.valueOf(1000));
        BigInteger twoMonth = BigInteger.valueOf( 2700000).multiply(BigInteger.valueOf(1000));
        BigInteger twoDays = BigInteger.valueOf(86400000*3);

       // assert lineChart.getXAxis() instanceof ValueAxis;
        assert lineChart.getXAxis() instanceof NumberAxis;
        long lowerBound = sortedOrders.get(0).getStart_trip().getTime();
        long upperBound = sortedOrders.get(sortedOrders.size() - 1).getStart_trip().getTime();
        long gap = upperBound - lowerBound;
        lowerBound -= gap / 4;
       // upperBound += gap / 15;
        System.out.println("lowerBound " + lowerBound);
        System.out.println("upperBound " + upperBound);

        ((NumberAxis) lineChart.getXAxis()).setTickUnit(gap/20);

        ((ValueAxis) lineChart.getXAxis()).setTickLabelFormatter(new TimetoStringConverter());
        ((ValueAxis) lineChart.getXAxis()).setLowerBound(lowerBound);
        ((ValueAxis) lineChart.getXAxis()).setUpperBound(upperBound);

        ((ValueAxis) lineChart.getXAxis()).setAutoRanging(false);

        if(secondDelta.compareTo(twoYears) >= 0){
            lineChart.setTitle("YEARS");
            HashMap<LocalDate, ChartInfo> mapOrder = spliterator.splitOnYears(sortedOrders);
            for (Map.Entry<LocalDate, ChartInfo> pair: mapOrder.entrySet()) {
                Timestamp timestamp = Timestamp.valueOf(pair.getKey().atStartOfDay());
                dataSeriesProfit.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getProfit()));
                dataSeriesCanceled.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getCanceldtrip()));
                dataSeriesDistance.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getDistance()));
                dataSeriesPaid.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getPaidtrip()));
                dataSeriesProfitIn.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getIntraveltime()));
                dataSeriesProfitOff.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getOfftraveltime()));
            }

        }

        else if(secondDelta.compareTo(twoMonth) >= 0){
            lineChart.setTitle("MONTH");
            HashMap<LocalDate, ChartInfo> mapOrder = spliterator.splitOnMonth(sortedOrders);
            for (Map.Entry<LocalDate, ChartInfo> pair: mapOrder.entrySet()) {
             //   System.out.println(pair.getKey());
                Timestamp timestamp = Timestamp.valueOf(pair.getKey().atStartOfDay());
                dataSeriesProfit.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getProfit()));
                dataSeriesCanceled.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getCanceldtrip()));
                dataSeriesDistance.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getDistance()));
                dataSeriesPaid.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getPaidtrip()));
                dataSeriesProfitIn.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getIntraveltime()));
                dataSeriesProfitOff.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getOfftraveltime()));
            }

        }

        else if(secondDelta.compareTo(twoDays) >= 0){
            lineChart.setTitle("DAYS");
            HashMap<LocalDate, ChartInfo> mapOrder = spliterator.splitOnDays(sortedOrders);
            for (Map.Entry<LocalDate, ChartInfo> pair: mapOrder.entrySet()) {
                //   System.out.println(pair.getKey());
                Timestamp timestamp = Timestamp.valueOf(pair.getKey().atStartOfDay());
                dataSeriesProfit.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getProfit()));
                dataSeriesCanceled.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getCanceldtrip()));
                dataSeriesDistance.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getDistance()));
                dataSeriesPaid.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getPaidtrip()));
                dataSeriesProfitIn.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getIntraveltime()));
                dataSeriesProfitOff.getData().add(new XYChart.Data(timestamp.getTime(),pair.getValue().getOfftraveltime()));
            }

        }
        else {
            lineChart.setTitle("ORDERS");
            System.out.println("lowerBound " + ((ValueAxis) lineChart.getXAxis()).getLowerBound());
            System.out.println("upperBound " + ((ValueAxis) lineChart.getXAxis()).getUpperBound());
            for (Order order : sortedOrders) {
                dataSeriesProfit.getData().add(new XYChart.Data(order.getStart_trip().getTime(), order.getPrice()));
                dataSeriesDistance.getData().add(new XYChart.Data(order.getStart_trip().getTime(), order.getDistance()));
            }
        }
        if (canceledCheck.isSelected()){
            dataSeries.add(dataSeriesCanceled);
        }
        if (distanceCheck.isSelected()){
            dataSeries.add(dataSeriesDistance);
        }
        if (intravelCheck.isSelected()){
            dataSeries.add(dataSeriesProfitIn);
        }
        if (offtravelCheck.isSelected()){
            dataSeries.add(dataSeriesProfitOff);
        }
        if (paidCheck.isSelected()){
            dataSeries.add(dataSeriesPaid);
        }
        if (profitCheck.isSelected()){
            dataSeries.add(dataSeriesProfit);
        }

        return dataSeries;
    }
    void initLineChart(){
        lineChart.getData().clear();
        for (XYChart.Series series :createSeries()) {
            if (series == null) continue;
            lineChart.getData().addAll(series);
        }

    }

    public void chartDataPrint(){
        System.out.println("lineChart.getData() - " + lineChart.getData().get(0).getData());
        System.out.println("lowerBound "+ ((ValueAxis) lineChart.getXAxis()).getLowerBound());
        System.out.println("upperBound "+ ((ValueAxis) lineChart.getXAxis()).getUpperBound());
        System.out.println(((ValueAxis) lineChart.getXAxis()).getTickLabelFormatter().getClass());
    }
    @Override
    public <T> void receiveData(ArrayList<T> smt) {
        ArrayList<Order> arr = (ArrayList<Order>) smt;
        data.clear();
        data.addAll(arr);
        initLineChart();
    }
}

class TimetoStringConverter extends StringConverter<Double> {

    @Override
    public String toString(Double integer) {
        LocalDate date = LocalDate.from(new Timestamp((integer.longValue())).toLocalDateTime());
        return date.toString();
    }

    @Override
    public Double fromString(String s) {
        return null;
    }
}