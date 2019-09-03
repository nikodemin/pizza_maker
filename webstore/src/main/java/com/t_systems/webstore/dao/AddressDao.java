package com.t_systems.webstore.dao;

import com.t_systems.webstore.model.entity.Address;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao{

    @PersistenceContext
    private EntityManager em;

    public void addAddress(Address address) {
        em.persist(address);
    }
}
