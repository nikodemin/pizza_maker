package com.t_systems.webstore.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class OrderDto implements Serializable {
    private Long id;
    private String username;
    private String paymentMethod;
    private String deliveryMethod;
    private String date;
    private List<ProductDto> items;
    private String status;
    private Integer price;

    public List<TulipDto<ProductDto,Integer>> getUniqueProducts(){
        Map<ProductDto, Integer> map = new HashMap<>();
        for (ProductDto product:items) {
           if (map.containsKey(product))
               map.put(product,map.get(product)+1);
           else
               map.put(product,1);
        }
        return map.entrySet().stream().map(e->new TulipDto<ProductDto,Integer>(e.getKey(),e.getValue()))
                .collect(Collectors.toList());
    }
}
