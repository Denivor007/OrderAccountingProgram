package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.Order;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderSpliterator {
    public ArrayList<Order> getSortedByTimeList(ArrayList<Order> orders){
        ArrayList<Order> sortOrders = (ArrayList<Order>) orders.stream()
                .filter(t -> t.getStart_trip() != null)
                .sorted(Comparator.comparing(t -> ((Order)t).getStart_trip().getTime()))
                .collect(Collectors.toList());
        return sortOrders;
    }

    private ChartInfo makeInfo(ArrayList<Order> value){
        int profit=0;
        int distance=0;
        int intraveltime=0;
        int offtraveltime=0;
        int paidtrip=0;
        int canceldtrip=0;

        for (Order order:value) {
            profit += order.getPrice();
            distance += order.getDistance();
            intraveltime += (order.getEnd_trip().getTime() - order.getStart_trip().getTime())/1000/60;
            paidtrip += (Objects.equals(order.getStatus(), "Сплачено"))? 1 : 0;
            canceldtrip += (Objects.equals(order.getStatus(), "Скасовано"))? 1 : 0;
        }
        offtraveltime = 24*60 - intraveltime;
        return new ChartInfo(
                profit,
                distance,
                intraveltime,
                offtraveltime,
                paidtrip,
                canceldtrip
        );
    }

    public HashMap<LocalDate,ChartInfo> splitOnYears(ArrayList<Order> orders){
        HashMap<LocalDate, ArrayList<Order>> byYears = new HashMap<>();
        HashMap<LocalDate,ChartInfo> result = new HashMap<>();
        LocalDate localDate1 = null;

        for (Order order: orders) {
            LocalDate localDate = LocalDate.from(order.getStart_trip().toLocalDateTime());
            Integer thisYear = localDate.getYear();
            localDate1 = LocalDate.of(thisYear,1,1);

            if (!byYears.containsKey(localDate1))
                byYears.put(localDate1, new ArrayList<Order>());
            byYears.get(localDate1).add(order);
        }
        for (Map.Entry<LocalDate, ArrayList<Order>> pair: byYears.entrySet()) {
           result.put(pair.getKey(), makeInfo(pair.getValue()));
        }
        return result;
    }

    public HashMap<LocalDate,ChartInfo> splitOnMonth(ArrayList<Order> orders){
        HashMap<LocalDate, ArrayList<Order>> byYearsAndMonth = new HashMap<>();
        HashMap<LocalDate,ChartInfo> result = new HashMap<>();
        LocalDate localDate1 = null;
        for (Order order: orders) {
            LocalDate localDate = LocalDate.from(order.getStart_trip().toLocalDateTime());
            Integer thisYear = localDate.getYear();
            Integer thisMonth = localDate.getMonthValue();
            localDate1 = LocalDate.of(thisYear,thisMonth,1);

            if (!byYearsAndMonth.containsKey(localDate1))
                byYearsAndMonth.put(localDate1, new ArrayList<Order>());
            byYearsAndMonth.get(localDate1).add(order);
        }
        for (Map.Entry<LocalDate, ArrayList<Order>> pair: byYearsAndMonth.entrySet()) {
            result.put(pair.getKey(), makeInfo(pair.getValue()));
        }
        return result;
    }

    public HashMap<LocalDate,ChartInfo> splitOnDays(ArrayList<Order> orders){
        HashMap<LocalDate, ArrayList<Order>> byDays = new HashMap<>();
        HashMap<LocalDate,ChartInfo> result = new HashMap<>();
        LocalDate localDate1 = null;
        for (Order order: orders) {
            LocalDate localDate = LocalDate.from(order.getStart_trip().toLocalDateTime());
            Integer thisYear = localDate.getYear();
            Integer thisMonth = localDate.getMonthValue();
            Integer thisDay = localDate.getDayOfMonth();
            localDate1 = LocalDate.of(thisYear,thisMonth,thisDay);

            if (!byDays.containsKey(localDate1))
                byDays.put(localDate1, new ArrayList<Order>());
            byDays.get(localDate1).add(order);
        }
        for (Map.Entry<LocalDate, ArrayList<Order>> pair: byDays.entrySet()) {
            result.put(pair.getKey(), makeInfo(pair.getValue()));
        }
        return result;
    }
}
