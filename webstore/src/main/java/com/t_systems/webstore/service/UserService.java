package com.t_systems.webstore.service;

import com.t_systems.webstore.dao.UserDao;
import com.t_systems.webstore.exception.UserExistsException;
import com.t_systems.webstore.model.dto.UserDto;
import com.t_systems.webstore.model.entity.Address;
import com.t_systems.webstore.model.entity.User;
import com.t_systems.webstore.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Configuration
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final ModelMapper modelMapper;

    /**
     * find user
     * @param username username
     * @return user
     */
    @Transactional(readOnly = true)
    public User findUser(String username) {
        return userDao.getUser(username);
    }

    /**
     * find user by email
     * @param email user email
     * @return user
     */
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    /**
     * get all users
     * @return list of users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * add user
     * @param user user
     * @throws UserExistsException ex
     */
    @Transactional
    public void addUser(User user) throws UserExistsException {
        if (!userDao.existUserByNameOrByEmail(user.getEmail(), user.getUsername())) {
            user.setPassword(passwordEncoder().encode(user.getPassword()));
            userDao.addUser(user);
            return;
        }
        throw new UserExistsException(user.getUsername(),user.getEmail());
    }

    /**
     * change user
     * @param username username
     * @param userDto user dto
     * @throws ParseException ex
     */
    @Transactional
    public void changeUser(String username, UserDto userDto) throws ParseException {
        User user = userDao.getUser(username);
        Address address = modelMapper.map(userDto, Address.class);
        user.setAddress(address);
        user.setPassword(passwordEncoder().encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        user.setDateOfBirth(dateFormat.parse(userDto.getDateOfBirth()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        userDao.updateUser(user);
    }

    /**
     * get user details for spring security
     * @param username username
     * @return user details
     * @throws UsernameNotFoundException ex
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUser(username);
        if (user != null) {
            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
                    return authorities;
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }

                @Override
                public String getUsername() {
                    return user.getUsername();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        }
        throw new UsernameNotFoundException("User not found");
    }

    /**
     * add user
     * @param userDto user dto
     * @throws UserExistsException ex
     */
    @Transactional
    public void addUser(UserDto userDto) throws UserExistsException {
        Address address = modelMapper.map(userDto, Address.class);
        User user = modelMapper.map(userDto, User.class);
        user.setRole(UserRole.USER);
        user.setAddress(address);
        addUser(user);
    }

    /**
     * PasswordEncoder config
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
