package com.t_systems.webstore.model.entity;

import com.t_systems.webstore.model.enums.DeliveryMethod;
import com.t_systems.webstore.model.enums.OrderStatus;
import com.t_systems.webstore.model.enums.PaymentMethod;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class _Order extends AbstractEntity{
    @ManyToOne(fetch = FetchType.EAGER)
    private User client;

    private PaymentMethod paymentMethod;

    private DeliveryMethod deliveryMethod;

    private Date date;

    @Embedded
    private Card card;

    @Embedded
    private Address address;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<AbstractProduct> items;

    private OrderStatus status;

    private Integer total;
}
