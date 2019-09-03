package com.t_systems.webstore.dao;

import com.t_systems.webstore.model.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public List<User> getAllUsers() {
        return em.createQuery("FROM User u ORDER BY u.id", User.class).getResultList();
    }

    public Boolean existUserByNameOrByEmail(String email, String username){
        return em.createQuery("FROM User u WHERE u.username=:username OR u.email=:email")
                .setParameter("username",username)
                .setParameter("email",email).getResultList().size() > 0;
    }

    public void addUser(User user) {
        em.persist(user);
    }

    public User getUserByEmail(String email) {
        try {
            return em.createQuery("FROM User u WHERE u.email=:email", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUser(String username) {
        try {
            return em.createQuery("FROM User u WHERE u.username=:username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void updateUser(User user) {
        em.merge(user);
    }
}
