package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.*;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents user service
 */
public interface UserService extends EntityService<User> {

    /**Registers user
     * @param email email
     * @param password1 entered password
     * @param password2 confirmed password
     * @param firstName first name
     * @param secondName last name
     * @param role user role
     * @param status user status
     * @param locale locale
     * @return optional with registered user or empty optional
     * @throws ServiceException while unable to perform a service action
     */
    Optional<User> register(String email, String password1, String password2, String firstName, String secondName,
                            UserRole role, UserStatus status, String locale) throws ServiceException;

    /**Authenticates user
     * @param login login
     * @param password password
     * @return optional with authenticated user or empty optional
     * @throws ServiceException while unable to perform a service action
     */
    Optional<User> authenticate(String login, String password) throws ServiceException;

    /**Finds user by email
     * @param email email
     * @return optional with user if found or empty optional
     * @throws ServiceException while unable to perform a service action
     */
    Optional<User> findUserByEmail(String email) throws ServiceException;

    /**Finds user by id
     * @param id id
     * @return optional with user if found or empty optional
     * @throws ServiceException while unable to perform a service action
     */
    Optional<User> findUserById(Long id) throws ServiceException;

    /**Updates user status
     * @param status user status
     * @param id id
     * @throws ServiceException while unable to perform a service action
     */
    void updateUserStatus(UserStatus status, long id) throws ServiceException;

    /**Updates user role
     * @param role user role
     * @param id id
     * @throws ServiceException while unable to perform a service action
     */
    void updateUserRole(UserRole role, long id) throws ServiceException;

    /**Checks is user with email registered
     * @param email email
     * @return true if registered
     * @throws ServiceException while unable to perform a service action
     */
    boolean isEmailRegistered(String email) throws ServiceException;

    /**Finds user by token id
     * @param tokenId token id
     * @return optional with user if found or empty optional
     * @throws ServiceException while unable to perform a service action
     */
    Optional<User> findUserByTokenId(long tokenId) throws ServiceException;

    /**Finds user token by token id
     * @param tokenId token id
     * @return optional with token if found or empty optional
     * @throws ServiceException while unable to perform a service action
     */
    Optional<Token> retrieveUserToken(long tokenId) throws ServiceException;

    /**Confirms user registration
     * @param tokenId user token id
     * @param tokenValue user token value
     * @return true if token valid
     * @throws ServiceException while unable to perform a service action
     */
    boolean confirmUser(long tokenId, String tokenValue) throws ServiceException;

    /**Finds all trainers with active status
     * @return list of found trainers
     * @throws ServiceException while unable to perform a service action
     */
    List<User> findActiveTrainers() throws ServiceException;

    /**Finds all clients with active status
     * @return list of found clients
     * @throws ServiceException while unable to perform a service action
     */
    List<User> findActiveClients() throws ServiceException;

    /**Finds user details by user id
     * @param userId user id
     * @return user details
     * @throws ServiceException if user details not found
     */
    UserDetails findUserDetails(long userId) throws ServiceException;

    /**Finds all user details
     * @return list of all user details
     * @throws ServiceException while unable to perform a service action
     */
    List<UserDetails> findAllUserDetails() throws ServiceException;

    /**Adds user details
     * @param user user
     * @return user details
     * @throws ServiceException if user details not inserted
     */
    UserDetails addUserDetails(User user) throws ServiceException;

    /**Updates user details
     * @param userDetails user details
     * @throws ServiceException while unable to perform a service action
     */
    void updateUserDetails(UserDetails userDetails) throws ServiceException;

    /**Updates user data
     * @param id user id
     * @param fieldName user data field name
     * @param value user data field value
     * @throws ServiceException while unable to perform a service action
     */
    void updateUserData(long id, String fieldName, String value) throws ServiceException;

    /**Updates user personal discount
     * @param userId user id
     * @param discount discount
     * @throws ServiceException while unable to perform a service action
     */
    void updateUserDetailsDiscount(long userId, String discount) throws ServiceException;

    /**Updates user details with new trainer id
     * @param userDetails user details
     * @param trainerId trainer id
     * @throws ServiceException while unable to perform a service action
     */
    void updateUserDetailsTrainerId(UserDetails userDetails, long trainerId) throws ServiceException;
}
