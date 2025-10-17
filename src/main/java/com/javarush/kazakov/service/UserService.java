package com.javarush.kazakov.service;

import com.javarush.kazakov.dto.user.UserTo;
import com.javarush.kazakov.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static com.javarush.kazakov.dto.Dto.DTO;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public int getQuestQuantity(UserTo user){
        return userRepository.countAuthorship(user.id());
    }

    public List<UserTo> getAll() {
        return userRepository.getAll().stream().map(DTO::from).toList();
    }

    public Optional<UserTo> get(long id){
        return userRepository.get(id).map(DTO::from);
    }

    public Optional<UserTo> get(String login, String pass){
        return userRepository.get(login, pass).map(DTO::from);
    }

    public void create(UserTo user){
        userRepository.save(DTO.from(user));
    }

    public void update(UserTo user){
        userRepository.update(DTO.from(user));
    }

    public void delete(UserTo user){
        userRepository.delete(DTO.from(user));
    }

    public void delete(long id){
        userRepository.delete(id);
    }
}
