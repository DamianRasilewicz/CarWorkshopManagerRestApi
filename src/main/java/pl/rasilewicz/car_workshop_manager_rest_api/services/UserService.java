package pl.rasilewicz.car_workshop_manager_rest_api.services;


import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService {

    void save (User user);

    User findByUserName(String userName);

    User findUserById(Integer id);

    List<User> findAllUsersWithoutLogInUser(String userName);

    void deleteById(Integer id);

    Integer findNumberOfAllUsers();

    Integer findNumberOfMonthlyRegisteredUsers(Integer month, Integer year);

    @Transactional
    User editUser(User user);

}
