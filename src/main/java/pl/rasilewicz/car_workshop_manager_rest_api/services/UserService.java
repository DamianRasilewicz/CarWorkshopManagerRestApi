package pl.rasilewicz.car_workshop_manager_rest_api.services;


import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService {

    void save (User user);

    User findByUsername(String username);

    User findUserById(Integer id);

    List<User> findAllUsersWithoutLogInUser(String username);

    void deleteById(Integer id);

    Integer findNumberOfAllUsers();

    Integer findNumberOfMonthlyRegisteredUsers(Integer month, Integer year);

    User editUser(User user);

    User editProfile(User user);

}
