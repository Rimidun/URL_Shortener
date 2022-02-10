package com.vladimirov.url_shortener.entity;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean deleted;

    @CreationTimestamp
    private Timestamp created;

    @UpdateTimestamp
    private Timestamp modified;

}
