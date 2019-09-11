package com.t_systems.webstore.controller.restController;

import com.t_systems.webstore.model.dto.*;
import com.t_systems.webstore.model.entity.Address;
import com.t_systems.webstore.model.entity.Card;
import com.t_systems.webstore.model.enums.OrderStatus;
import com.t_systems.webstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    //todo remove state from REST
    private Address address = null;
    private Card card = null;

    /**
     * get cart products
     * @param session session
     * @return list of product and its quantity
     */
    @GetMapping("/getCartProducts")
    public List<TulipDto<ProductDto,Integer>> getCartProducts(HttpSession session){
        return ((OrderDto)session.getAttribute("order")).getUniqueProducts();
    }

    /**
     * set address
     * @param addressDto address
     * @param result binding result
     * @return boolean: successful or not
     */
    @PostMapping("/setAddress")
    public Boolean setAddress(@ModelAttribute("address") @Valid AddressDto addressDto, BindingResult result){
        if (result.hasErrors()) {
            return true;
        }
        address = modelMapper.map(addressDto,Address.class);
        return false;
    }

    /**
     * set card
     * @param cardDto card
     * @param result binding result
     * @return boolean: successful or not
     */
    @PostMapping("/setCard")
    public Boolean setCard(@ModelAttribute("card") @Valid CardDto cardDto, BindingResult result){
        if (result.hasErrors()) {
            return true;
        }
        card = modelMapper.map(cardDto,Card.class);
        return false;
    }

    /**
     * is cart empty
     * @param session session
     * @return boolean
     */
    @GetMapping("/isCartEmpty")
    public Boolean isCartEmpty(HttpSession session){
        return ((OrderDto)session.getAttribute("order")).getItems().isEmpty();
    }

    /**
     * submit order
     * @param session session
     * @param principal principal
     * @return response entity
     * @throws Exception ex
     */
    @PostMapping("/submitOrder")
    public ResponseEntity<?> submitOrder(HttpSession session, Principal principal) throws Exception{
        orderService.addOrder((OrderDto)session.getAttribute("order"), principal.getName(),address,card);

        OrderDto newOrder = new OrderDto();
        newOrder.setStatus(OrderStatus.UNPAID.toString());
        newOrder.setItems(new ArrayList<>());
        session.setAttribute("order", newOrder);
        return new ResponseEntity<>("Order submitted!", HttpStatus.OK);
    }

    /**
     * get recent orders
     * @return list of orders
     */
    @GetMapping("admin/getOrders")
    public List<OrderDto> getOrders(){
        return orderService.getRecentOrderDtos();
    }

    /**
     * change order status
     * @param id id of order
     * @param newStatus new order status
     * @return response entity
     */
    @PutMapping("admin/changeOrderStatus/{id}/{newStatus}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable("id") Long id,
                                               @PathVariable("newStatus") String newStatus){
        orderService.changeStatus(id,newStatus);
        return new ResponseEntity<>("Status changed!", HttpStatus.OK);
    }

    /**
     * get total gain
     * @return total gain
     */
    @GetMapping("admin/getTotalGain")
    public TotalGainDto getTotalGain(){
        return orderService.getTotalGainDto();
    }

    /**
     * get top clients
     * @return list of users
     */
    @GetMapping("admin/getTopClientsDtos")
    List<UserDto> getTopClients(){
        return orderService.getTopClientsDtos();
    }
}
