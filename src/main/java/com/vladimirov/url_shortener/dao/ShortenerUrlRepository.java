package com.vladimirov.url_shortener.dao;

import com.vladimirov.url_shortener.entity.ShortenerUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortenerUrlRepository extends JpaRepository<ShortenerUrl, Long> {

    Optional<ShortenerUrl> findByCode(String code);

    Boolean existsByCode(String code);
}
