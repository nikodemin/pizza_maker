package com.t_systems.webstore.service;

import com.t_systems.webstore.dao.OrderDao;
import com.t_systems.webstore.dao.ProductDao;
import com.t_systems.webstore.dao.UserDao;
import com.t_systems.webstore.model.dto.OrderDto;
import com.t_systems.webstore.model.dto.ProductDto;
import com.t_systems.webstore.model.dto.TotalGainDto;
import com.t_systems.webstore.model.dto.UserDto;
import com.t_systems.webstore.model.entity.*;
import com.t_systems.webstore.model.enums.DeliveryMethod;
import com.t_systems.webstore.model.enums.OrderStatus;
import com.t_systems.webstore.model.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private static final int TOP_CLIENTS_QUANTITY = 10;
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ProductDao productDao;
    private final ModelMapper modelMapper;
    private final JavaMessageService javaMessageService;

    @PostConstruct
    private void init(){
        Converter<_Order,OrderDto> toOrderDtoConverter = new AbstractConverter<_Order, OrderDto>() {
            @Override
            protected OrderDto convert(_Order order) {
                OrderDto res = new OrderDto();
                res.setDate(order.getDate().toString());
                res.setDeliveryMethod(order.getDeliveryMethod().toString());
                List<ProductDto> items = order.getItems().stream()
                        .map(p->{
                            ProductDto productDto = modelMapper.map(p,ProductDto.class);
                            if(p instanceof CustomProduct)
                                productDto.setIsCustom(true);
                            return productDto;
                        }).collect(Collectors.toList());
                res.setItems(items);
                res.setStatus(order.getStatus().toString());
                res.setUsername(order.getClient().getUsername());
                res.setPaymentMethod(order.getPaymentMethod().toString());
                res.setId(order.getId());
                res.setPrice(order.getTotal());
                return res;
            }
        };
        Converter<User,UserDto> toUserDtoConverter = new AbstractConverter<User, UserDto>() {
            @Override
            protected UserDto convert(User user) {
                UserDto userDto = new UserDto();
                userDto.setUsername(user.getUsername());
                userDto.setLastName(user.getLastName());
                userDto.setFirstName(user.getFirstName());
                userDto.setEmail(user.getEmail());
                userDto.setDateOfBirth(user.getDateOfBirth().toString());
                userDto.setCountry(user.getAddress().getCountry());
                userDto.setCity(user.getAddress().getCity());
                userDto.setStreet(user.getAddress().getStreet());
                userDto.setHouse(user.getAddress().getHouse());
                userDto.setFlat(user.getAddress().getFlat());
                userDto.setPassword(null);
                return userDto;
            }
        };
        modelMapper.addConverter(toOrderDtoConverter);
        modelMapper.addConverter(toUserDtoConverter);
    }

    /**
     * add order
     * @param order order
     */
    public void addOrder(_Order order) {
        log.trace("Adding new order");
        Integer sum = 0;
        for (AbstractProduct product:order.getItems()) {
            sum += product.getPrice();
        }
        order.setTotal(sum);
        orderDao.addOrder(order);
    }

    /**
     * get all orders
     * @return list of orders
     */
    public List<_Order> getAllOrders() {
        log.trace("Getting all orders");
        return orderDao.getAllOrders();
    }

    /**
     * get recent orders
     * @return list of orders
     */
    public List<_Order> getRecentOrders() {
        log.trace("Getting recent orders");
        return orderDao.getRecentOrders();
    }

    /**
     * change order status
     * @param id id of order
     * @param newStatus new status
     */
    public void changeStatus(Long id, String newStatus) {
        log.trace("Changing status of order");
        OrderStatus status = toOrderStatus(newStatus);
        orderDao.changeStatus(id,status);
    }

    /**
     * convert string to OrderStatus
     * @param status string
     * @return OrderStatus
     */
    public OrderStatus toOrderStatus(String status){
        OrderStatus res = null;
        switch (status)
        {
            case "Unpaid":
                res = OrderStatus.UNPAID;
                break;
            case "Paid":
                res = OrderStatus.PAID;
                break;
            case "Delivered":
                res = OrderStatus.DELIVERED;
                break;
        }
        return res;
    }

    /**
     * get orders by user
     * @param user username
     * @return list of orders
     */
    public List<OrderDto> getOrderDtosByUser(String user) {
        log.trace("Getting user order dtos");
        return orderDao.getOrdersByUser(userDao.getUser(user)).stream()
                .map(o->modelMapper.map(o,OrderDto.class)).collect(Collectors.toList());
    }

    /**
     * get recent orders
     * @return list of orders
     */
    public List<OrderDto> getRecentOrderDtos() {
        log.trace("Getting recent order dtos");
        return getRecentOrders().stream().map(o->modelMapper.map(o,OrderDto.class))
                .collect(Collectors.toList());
    }

    /**
     * get total gain
     * @return total gain
     */
    public TotalGainDto getTotalGainDto() {
        log.trace("Getting total gain dto");
        TotalGainDto res = new TotalGainDto();
        Integer forMonth = 0, forWeek = 0;

        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);

        List<_Order> orders = getRecentOrders();
        for (_Order order:orders) {
            LocalDate orderDate = order.getDate().toInstant().
                    atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (!orderDate.isBefore(monthStart))
                forMonth += order.getTotal();
            if (!orderDate.isBefore(weekStart))
                forWeek += order.getTotal();
        }

        res.setMonth(forMonth);
        res.setWeek(forWeek);
        return res;
    }

    /**
     * get top client dtos
     * @return list of users
     */
    public List<UserDto> getTopClientsDtos() {
        log.trace("Getting top client dtos");
        Map<User,Integer> map = new HashMap<>();
        for (_Order order:getRecentOrders()) {
            User user = order.getClient();
            if (map.containsKey(user))
                map.put(user,map.get(user)+1);
            else
                map.put(user,1);
        }

        return map.entrySet().stream().sorted((e1,e2)->e2.getValue().compareTo(e1.getValue()))
                .limit(TOP_CLIENTS_QUANTITY).map(e->modelMapper.map(e.getKey(),UserDto.class)).collect(Collectors.toList());
    }

    /**
     * add order
     * @param orderDto order dto
     * @param username username
     * @param address address
     * @param card card
     */
    public void addOrder(OrderDto orderDto, String username, Address address, Card card) {
        log.trace("Adding new order from order dto");
        _Order order = new _Order();
        order.setStatus(OrderStatus.UNPAID);
        User user = userDao.getUser(username);
        order.setClient(user);
        order.setDate(new Date());
        List<AbstractProduct> products = orderDto.getItems().stream()
                .map(p->{
                    return p.getIsCustom()? productDao.getProduct(p.getName(),user):
                            productDao.getProduct(p.getName(),null);
                })
                .collect(Collectors.toList());
        order.setItems(products);

        if (address == null)
            order.setDeliveryMethod(DeliveryMethod.PICKUP);
        else{
            order.setDeliveryMethod(DeliveryMethod.COURIER);
            order.setAddress(address);
        }

        if (card == null) {
            order.setPaymentMethod(PaymentMethod.CASH);
            order.setStatus(OrderStatus.UNPAID);
        }
        else{
            order.setPaymentMethod(PaymentMethod.CARD);
            order.setStatus(OrderStatus.PAID);
            order.setCard(card);
        }

        Integer sum = 0;
        for (AbstractProduct product:order.getItems()) {
            sum += product.getPrice();
        }
        order.setTotal(sum);
        orderDao.addOrder(order);
        javaMessageService.sendTopProducts();
    }
}
