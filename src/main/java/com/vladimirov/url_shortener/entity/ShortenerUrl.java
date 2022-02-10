package com.vladimirov.url_shortener.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@ToString
@Where(clause = "deleted = false")
public class ShortenerUrl extends BaseEntity implements Serializable {

    private String code;

    private String realValue;

    private String idAddress;
}
