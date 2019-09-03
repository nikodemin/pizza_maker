package unit;

import com.t_systems.webstore.dao.OrderDao;
import com.t_systems.webstore.model.entity.AbstractProduct;
import com.t_systems.webstore.model.entity.User;
import com.t_systems.webstore.model.entity._Order;
import com.t_systems.webstore.service.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class OrderServiceTests {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;
    @Mock
    private ModelMapper modelMapper;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addOrderTest(){
        _Order order = new _Order();
        AbstractProduct product1 = new AbstractProduct();
        product1.setPrice(1);
        AbstractProduct product2 = new AbstractProduct();
        product2.setPrice(4);
        AbstractProduct product3 = new AbstractProduct();
        product3.setPrice(6);
        AbstractProduct product4 = new AbstractProduct();
        product4.setPrice(13);

        List<AbstractProduct> listItems =
                Arrays.asList(product1,product2,product3,product4);
        order.setItems(listItems);

        orderService.addOrder(order);
        ArgumentCaptor<_Order> orderCaptor = ArgumentCaptor.forClass(_Order.class);
        verify(orderDao).addOrder(orderCaptor.capture());
        Assert.assertEquals(Integer.valueOf(24), orderCaptor.getValue().getTotal());
    }

    @Test
    public void getTotalGainDtoTest(){
        _Order order1 = new _Order();
        order1.setTotal(1);
        LocalDate localDate = LocalDate.now().with(DayOfWeek.MONDAY).minusDays(1);
        order1.setDate(asDate(localDate));

        _Order order2 = new _Order();
        order2.setTotal(3);
        localDate = LocalDate.now().with(DayOfWeek.MONDAY);
        order2.setDate(asDate(localDate));

        _Order order3 = new _Order();
        order3.setTotal(5);
        localDate = LocalDate.now().withDayOfMonth(1).minusDays(1);
        order3.setDate(asDate(localDate));

        _Order order4 = new _Order();
        order4.setTotal(7);
        localDate = LocalDate.now().withDayOfMonth(1);
        order4.setDate(asDate(localDate));

        List<_Order> orders = Arrays.asList(order1,order2,order3,order4);
        when(orderService.getRecentOrders()).thenReturn(orders);
        Assert.assertEquals(Integer.valueOf(3),orderService.getTotalGainDto().getWeek());
        Assert.assertEquals(Integer.valueOf(11),orderService.getTotalGainDto().getMonth());
    }

    @Test
    public void getTopClientsTest(){
        List<_Order> orders = new ArrayList<>();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            _Order order = new _Order();
            User user = new User();
            user.setUsername(String.valueOf(i));
            order.setClient(user);
            _Order order2 = new _Order();
            order2.setClient(user);
            orders.add(order);
            orders.add(order2);
            users.add(user);
        }

        _Order uncountedOrder = new _Order();
        User uncountedUser = new User();
        uncountedUser.setUsername("uncounted");
        uncountedOrder.setClient(uncountedUser);
        orders.add(uncountedOrder);

        Assert.assertEquals(orders.size(),21);

        when(orderService.getRecentOrders()).thenReturn(orders);
        orderService.getTopClients();
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        //todo
        //verify(mappingService,times(10)).toUserDto(userArgumentCaptor.capture());
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(true,userArgumentCaptor.getAllValues().contains(users.get(i)));
            Assert.assertEquals(false,userArgumentCaptor.getAllValues().contains(uncountedUser));
        }
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
