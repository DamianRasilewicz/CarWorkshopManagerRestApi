package pl.rasilewicz.car_workshop_manager_rest_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Car;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.repositories.CarRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    @Override
    public void save(Car car) {
        carRepository.save(car);
    }

    @Override
    public Car findCarById(Integer id) {
       return carRepository.findCarById(id);
    }

    @Override
    public List<Car> findCarsByUserId(Integer userId) {
        return carRepository.findCarsByUserId(userId);
    }

    @Override
    public void deleteById(Integer id) {
        carRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Car editCar(Car car) {
        Car editCar = carRepository.findCarById(car.getId());
        editCar.setBrand(car.getBrand());
        editCar.setModel(car.getModel());
        editCar.setEngineCapacity(car.getEngineCapacity());
        editCar.setEnginePower(car.getEnginePower());
        editCar.setEngineType(car.getEngineType());
        editCar.setProductionYear(car.getProductionYear());

        return carRepository.save(editCar);
    }


}
