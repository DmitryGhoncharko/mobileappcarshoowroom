package model.entity;

import java.util.Objects;

public class Car {
    private final long carId;
    private final String carName;
    private final String carDescription;


    private Car(Builder builder) {
        carId = builder.carId;
        carName = builder.carName;
        carDescription = builder.carDescription;

    }

    public long getCarId() {
        return carId;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarDescription() {
        return carDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (carId != car.carId) return false;
        if (!Objects.equals(carName, car.carName)) return false;
        return Objects.equals(carDescription, car.carDescription);
    }

    @Override
    public int hashCode() {
        int result = (int) (carId ^ (carId >>> 32));
        result = 31 * result + (carName != null ? carName.hashCode() : 0);
        result = 31 * result + (carDescription != null ? carDescription.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", carName='" + carName + '\'' +
                ", carDescription='" + carDescription + '\'' +
                '}';
    }

    public static class Builder {
        private long carId;
        private String carName;
        private String carDescription;

        public Builder withCarId(long carId) {
            this.carId = carId;
            return this;
        }

        public Builder withCarName(String carName) {
            this.carName = carName;
            return this;
        }

        public Builder withCarDescription(String carDescription) {
            this.carDescription = carDescription;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
