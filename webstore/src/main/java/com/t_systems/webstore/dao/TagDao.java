package com.t_systems.webstore.dao;

import com.t_systems.webstore.model.entity.CatalogProduct;
import com.t_systems.webstore.model.entity.Category;
import com.t_systems.webstore.model.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagDao {

    @PersistenceContext
    private EntityManager em;
    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    public void addTag(Tag tag) {
        em.persist(tag);
    }

    public List<Tag> getAllTags() {
        return em.createQuery("FROM Tag t ORDER BY t.id", Tag.class).getResultList();
    }

    public Tag getTag(String name) {
        return em.createQuery("FROM Tag t WHERE t.name=:name", Tag.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public void removeTag(String name) {
        Tag tag = getTag(name);
        em.createQuery("FROM CatalogProduct p WHERE :tag IN elements(p.tags)",
                CatalogProduct.class).setParameter("tag", tag)
                .getResultList()
                .forEach(p -> productDao.removeTagFromProduct(p, tag));
        em.remove(tag);
    }

    public List<Tag> getTagsByCategory(String category) {
        return em.createQuery("FROM Tag t WHERE :category IN elements(t.categories)", Tag.class)
                .setParameter("category",categoryDao.getCategory(category)).getResultList();
    }

    public void removeCategory(Category category) {
        List<Tag> tags = getTagsByCategory(category.getName());
        for (int i = 0; i < tags.size(); i++) {
            tags.get(i).getCategories().remove(category);
            em.persist(tags.get(i));
        }
        em.flush();
    }
}
