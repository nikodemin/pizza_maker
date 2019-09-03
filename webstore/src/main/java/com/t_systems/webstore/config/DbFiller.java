package com.t_systems.webstore.config;

import com.t_systems.webstore.exception.UserExistsException;
import com.t_systems.webstore.model.entity.*;
import com.t_systems.webstore.model.enums.DeliveryMethod;
import com.t_systems.webstore.model.enums.OrderStatus;
import com.t_systems.webstore.model.enums.PaymentMethod;
import com.t_systems.webstore.model.enums.UserRole;
import com.t_systems.webstore.service.OrderService;
import com.t_systems.webstore.service.ProductService;
import com.t_systems.webstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DbFiller implements ApplicationListener {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    private Map<String,Category> categories = new HashMap<>();
    private Map<String,Tag> tags = new HashMap<>();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            User user = new User();
            user.setDateOfBirth(new Date());
            Address address = new Address();
            address.setCountry("Russia");
            address.setCity("SPB");
            address.setStreet("Veteranov");
            user.setAddress(address);
            user.setEmail("niko.demin@gmail.com");
            user.setUsername("admin");
            user.setPassword("1234");
            user.setRole(UserRole.ADMIN);
            try {
                userService.addUser(user);
            } catch (UserExistsException e) {
                e.printStackTrace();
            }

            _Order order = new _Order();
            order.setDate(new Date());
            order.setPaymentMethod(PaymentMethod.CARD);
            order.setClient(user);
            order.setDeliveryMethod(DeliveryMethod.COURIER);
            order.setStatus(OrderStatus.PAID);
            List<AbstractProduct> products = new ArrayList<>();

            Category category = new Category();
            category.setName("Pizza");
            category.setImage("/resources/img/cat/pizza.jpg");
            categories.put("Pizza",category);
            category = new Category();
            category.setName("Burgers");
            category.setImage("/resources/img/cat/burger.jpg");
            categories.put("Burgers",category);
            category = new Category();
            category.setName("Drinks");
            category.setImage("/resources/img/cat/drink.jpg");
            categories.put("Drinks",category);
            category = new Category();
            category.setName("Hot");
            category.setImage("/resources/img/cat/hot.jpg");
            categories.put("Hot",category);
            category = new Category();
            category.setName("Sets");
            category.setImage("/resources/img/cat/set.jpg");
            categories.put("Sets",category);
            category = new Category();
            category.setName("Sushi");
            category.setImage("/resources/img/cat/sushi.jpg");
            categories.put("Sushi",category);
            category = new Category();
            category.setName("Desserts");
            category.setImage("/resources/img/cat/sweet.jpg");
            categories.put("Desserts",category);
            category = new Category();
            category.setName("Wok");
            category.setImage("/resources/img/cat/wok.jpg");
            categories.put("Wok",category);
            for (Map.Entry<String,Category> e:categories.entrySet()) {
                productService.addCategory(e.getValue());
            }

            List<Ingredient> ingredients = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName("ingredient" + i);
                ingredient.setPrice(100);
                ingredient.setCategories(categories.entrySet().stream()
                        .map(e->e.getValue()).collect(Collectors.toList()));
                productService.addIngredient(ingredient);
                ingredients.add(ingredient);
            }

            List<Tag> tagList = new ArrayList<>();
            List<Category> tagsCategories = new ArrayList<>();

            Tag tag = new Tag();
            tag.setName("Spicy");
            tagsCategories.add(categories.get("Pizza"));
            tagsCategories.add(categories.get("Burgers"));
            tagsCategories.add(categories.get("Hot"));
            tagsCategories.add(categories.get("Sets"));
            tagsCategories.add(categories.get("Sushi"));
            tagsCategories.add(categories.get("Wok"));
            tag.setCategories(tagsCategories);
            tags.put("Spicy",tag);
            tagList.add(tag);

            tagsCategories = new ArrayList<>();
            tag = new Tag();
            tag.setName("With Mushrooms");
            tagsCategories.add(categories.get("Pizza"));
            tagsCategories.add(categories.get("Wok"));
            tagsCategories.add(categories.get("Hot"));
            tagsCategories.add(categories.get("Sets"));
            tag.setCategories(tagsCategories);
            tags.put("With Mushrooms",tag);

            tagList.add(tag);
            for (Map.Entry<String,Tag> e:tags.entrySet()) {
                productService.addTag(e.getValue());
            }

            for (int i = 0; i < 10; i++) {
                CatalogProduct product = new CatalogProduct();
                product.setName("pizza" + i);
                product.setImage("/resources/img/pizza.jpg");
                product.setCategory(categories.get("Pizza"));
                product.setIngredients(ingredients);
                product.setPrice(999);
                product.setSpicy(2);
                products.add(product);
                product.setTags(tagList);
                productService.addCustomProduct(product);
            }
            order.setItems(products);
            orderService.addOrder(order);

            order = new _Order();
            order.setDate(new Date());
            order.setPaymentMethod(PaymentMethod.CASH);
            order.setClient(user);
            order.setDeliveryMethod(DeliveryMethod.COURIER);
            order.setStatus(OrderStatus.PAID);
            List<AbstractProduct> products2 = productService.getProductsByCategory("Pizza").stream()
                    .map(p->(AbstractProduct)p).collect(Collectors.toList());
            products2.remove(9);
            products2.remove(8);
            order.setItems(products2);
            orderService.addOrder(order);

            CustomProduct product = new CustomProduct();
            product.setAuthor(user);
            product.setCategory(categories.get("Pizza"));
            product.setName("Pizza Big Lebovsky");
            product.setPrice(1322);
            product.setIngredients(ingredients);
            productService.addCustomProduct(product);
        }
    }
}
