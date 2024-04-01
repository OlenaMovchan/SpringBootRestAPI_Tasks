package com.example.tasknewspring.service;


import com.example.tasknewspring.dto.UserDto;
import com.example.tasknewspring.entity.UserEntity;
import com.example.tasknewspring.entity.UserInfoDetails;
import com.example.tasknewspring.exception.ErrorCodes;
import com.example.tasknewspring.exception.UserAlreadyExistsException;
import com.example.tasknewspring.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserInfoService implements UserDetailsService {

    private UserEntityRepository repository;
    private PasswordEncoder encoder;
    private ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userDetail = repository.findByName(username);

        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public UserDto addUser(UserDto userDto) {

        if (repository.findByName(userDto.getName()).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + userDto.getName() + " already exists",
                    ErrorCodes.USER_ALREADY_EXISTS);
        }
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        return toDto(repository.save(toModel(userDto)));
    }

    public UserEntity toModel(UserDto userDto) {
        return mapper.map(userDto, UserEntity.class);
    }

    public UserDto toDto(UserEntity userEntity) {
        return mapper.map(userEntity, UserDto.class);
    }
}
