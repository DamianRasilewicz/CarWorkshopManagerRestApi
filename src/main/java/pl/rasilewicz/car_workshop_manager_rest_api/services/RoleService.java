package pl.rasilewicz.car_workshop_manager_rest_api.services;


import pl.rasilewicz.car_workshop_manager_rest_api.entities.Car;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;

import java.util.List;

public interface RoleService {

    Role findRoleById(Integer id);

    Role findRoleByName(String name);

    List<Role> findAllRoles();

}
