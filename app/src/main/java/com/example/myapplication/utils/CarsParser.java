package com.example.myapplication.utils;


import com.example.myapplication.entity.Car;

import java.util.ArrayList;
import java.util.List;

public class CarsParser {
    private CarsParser(){

    }
    public static List<Car> parse(String jsonAsString) {
        List<Car> cars = new ArrayList<>();
        String stringWithoutWastedSymbols = jsonAsString.replace("[", "").replace("{", "").replace("}", "").
                replace("]", "").replace("\"", "").trim();
        String[] dataParts = stringWithoutWastedSymbols.split(",");
        for (int i = 0; i < dataParts.length; i += 3) {
            String carId = dataParts[i].replace("carId:", "");
            String carName = dataParts[i + 1].replace("carName:", "");
            String carDescription = dataParts[i + 2].replace("carDescription:", "");
            Car car = new Car.Builder().
                    withCarId(Long.parseLong(carId)).
                    withCarName(carName).
                    withCarDescription(carDescription).
                    build();
            cars.add(car);
        }
        return cars;
    }
}
