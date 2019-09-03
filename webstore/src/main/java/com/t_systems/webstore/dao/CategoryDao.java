package com.t_systems.webstore.dao;

import com.t_systems.webstore.model.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @Autowired
    private ProductDao productDao;
    @PersistenceContext
    private EntityManager em;

    public void addCategory(Category category) {
        em.persist(category);
    }

    public Category getCategory(String name) {
        return em.createQuery("FROM Category c WHERE c.name=:name", Category.class)
                .setParameter("name", name).getSingleResult();
    }

    public List<Category> getAllCategories() {
        return em.createQuery("FROM Category c ORDER BY c.id", Category.class)
                .getResultList();
    }

    public void removeCategory(String name) {
        productDao.getProductsByCat(name).forEach(p -> productDao.detachProduct(p));
        em.remove(getCategory(name));
    }
}
