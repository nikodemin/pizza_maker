package com.t_systems.webstore.dao;

import com.t_systems.webstore.model.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProductDao {

    @PersistenceContext
    private EntityManager em;

    public void addProduct(AbstractProduct product) {
        if (product instanceof CatalogProduct)
            em.persist((CatalogProduct)product);
        else if (product instanceof CustomProduct)
            em.persist((CustomProduct)product);
    }

    public List<CatalogProduct> getProductsByCat(String category) {
        em.clear();
        return em.createQuery("FROM CatalogProduct p WHERE p.category=(FROM Category c WHERE c.name=:category) ORDER BY p.id",
                CatalogProduct.class)
                .setParameter("category", category).getResultList();
    }

    public void detachProduct(AbstractProduct product) {
        product.setCategory(null);
        product.setIngredients(null);
        if (product instanceof CatalogProduct)
            ((CatalogProduct)product).setTags(null);
        em.merge(product);
    }

    public void removeIngredientFromProduct(AbstractProduct product, Ingredient ingredient) {
        em.createNativeQuery("DELETE FROM product_ingredient WHERE product_id=? AND ingredient_id=?")
                .setParameter(1, product.getId()).setParameter(2, ingredient.getId())
                .executeUpdate();
    }

    public void removeTagFromProduct(CatalogProduct product, Tag tag) {
        em.createNativeQuery("DELETE FROM product_tag WHERE product_id=? AND tag_id=?")
                .setParameter(1, product.getId()).setParameter(2, tag.getId())
                .executeUpdate();
    }

    public void addTagToProduct(CatalogProduct product, Tag tag) {
        product.getTags().add(tag);
        em.merge(product);
    }

    public void addIngToProduct(CatalogProduct product, Ingredient ingredient) {
        product.getIngredients().add(ingredient);
        em.merge(product);
    }

    public void updateProduct(AbstractProduct product) {
        if (product instanceof CatalogProduct)
            em.merge((CatalogProduct)product);
        else if (product instanceof CustomProduct)
            em.merge((CustomProduct)product);
    }

    public AbstractProduct getProduct(String name, User user) {
        if (user == null)
            return em.createQuery("FROM CatalogProduct p WHERE p.name=:name",CatalogProduct.class)
                .setParameter("name",name).getSingleResult();
        else
            return em.createQuery("FROM CustomProduct p WHERE p.name=:name AND p.author=:user",
                    CustomProduct.class)
            .setParameter("name", name)
            .setParameter("user", user)
            .getSingleResult();
    }

    public List<CustomProduct> getProductsByCatAndUser(Category category, User user) {
        return em.createQuery("FROM CustomProduct p WHERE p.category=:category and p.author=:user", CustomProduct.class)
                .setParameter("category",category)
                .setParameter("user",user)
                .getResultList();
    }

    public void removeProduct(AbstractProduct product) {
        if (product instanceof CatalogProduct)
            em.remove((CatalogProduct)product);
        else if (product instanceof CustomProduct)
            em.remove((CustomProduct)product);
    }

    public boolean isCatalogProductExists(String name) {
        return !em.createQuery("FROM CatalogProduct p WHERE p.name=:name",CatalogProduct.class)
                .setParameter("name",name)
                .getResultList().isEmpty();
    }

    public boolean isCustomProductExists(String name, User user) {
        return !em.createQuery("FROM CustomProduct p WHERE p.name=:name AND p.author=:user",
                CustomProduct.class)
                .setParameter("name",name)
                .setParameter("user",user)
                .getResultList().isEmpty();
    }
}
