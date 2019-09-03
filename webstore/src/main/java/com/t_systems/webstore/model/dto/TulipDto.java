package com.t_systems.webstore.model.dto;

import lombok.Data;

@Data
public class TulipDto<K,V> {
    private K key;
    private V value;

    TulipDto(K key,V value) {
        this.key = key;
        this.value = value;
    }
}
