package integration;

import com.t_systems.webstore.config.WebConfig;
import com.t_systems.webstore.exception.UserExistsException;
import com.t_systems.webstore.model.entity.*;
import com.t_systems.webstore.model.enums.DeliveryMethod;
import com.t_systems.webstore.model.enums.OrderStatus;
import com.t_systems.webstore.model.enums.PaymentMethod;
import com.t_systems.webstore.model.enums.UserRole;
import com.t_systems.webstore.service.OrderService;
import com.t_systems.webstore.service.ProductService;
import com.t_systems.webstore.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class DbTests {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @Test
    public void usersTest() throws UserExistsException {
        Address address = new Address();
        address.setCity("SPB");
        address.setCountry("Russia");

        User user = new User();
        user.setEmail("niko@gmail.com");
        user.setUsername("niko");
        user.setAddress(address);
        user.setDateOfBirth(new Date());
        user.setPassword("123");
        user.setRole(UserRole.USER);
        userService.addUser(user);

        user = new User();
        user.setEmail("emily@gmail.com");
        user.setUsername("emily");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        userService.addUser(user);

        user = new User();
        user.setEmail("alice@gmail.com");
        user.setUsername("alice");
        user.setPassword("123");
        user.setRole(UserRole.USER);
        userService.addUser(user);
        try {
            userService.addUser(user);
        } catch (UserExistsException e) {
        }
        Assert.assertEquals(4, userService.getAllUsers().size());

        Assert.assertNotEquals(null, userService.findUser("niko"));
        Assert.assertNotEquals(null, userService.findUser("emily"));
        Assert.assertNotEquals(null, userService.findUser("alice"));
    }

    @Test
    public void productsAndOrdersTest() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Cheese");
        ingredient.setPrice(100);
        productService.addIngredient(ingredient);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Meat");
        ingredient2.setPrice(120);
        productService.addIngredient(ingredient2);

        Tag tag = new Tag();
        tag.setName("cheese");
        productService.addTag(tag);

        CatalogProduct product = new CatalogProduct();
        product.setCategory(productService.getCategory("Pizza"));
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        ingredients.add(ingredient2);
        product.setIngredients(ingredients);
        product.setName("Pizza");
        product.setPrice(999);
        product.setSpicy(0);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        product.setTags(tags);
        productService.addCustomProduct(product);

        Assert.assertEquals(1, productService.getAllTags().size());
        Assert.assertEquals(11, productService.getProductsByCategory("Pizza").size());

        _Order order = new _Order();
        order.setPaymentMethod(PaymentMethod.CARD);
        order.setClient(userService.findUser("niko"));
        order.setDeliveryMethod(DeliveryMethod.PICKUP);
        order.setDate(new Date());
        order.setStatus(OrderStatus.PAID);
        order.setItems(productService.getProductsByCategory("Pizza").stream()
        .map(p-> ((AbstractProduct) p)).collect(Collectors.toList()));
        orderService.addOrder(order);

        Assert.assertEquals(3, orderService.getAllOrders().size());
        Assert.assertEquals(3, orderService.getRecentOrders().size());
        Assert.assertEquals(6, productService.getTopProductsDto().size());
    }

}
