package com.vladimirov.url_shortener.controller;

import com.vladimirov.url_shortener.dto.ShortenerUrlDto;
import com.vladimirov.url_shortener.service.ShortenerUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping
public class ShortenerUrlController {

    private ShortenerUrlService shortenerUrlService;

    public ShortenerUrlController(ShortenerUrlService shortenerUrlService){
        this.shortenerUrlService = shortenerUrlService;
    }

    @GetMapping("{code}")
    public ResponseEntity<?> makeRedirect(@PathVariable String code) throws URISyntaxException{
        return new ResponseEntity<>(shortenerUrlService.getUrl(code), HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/show/{code}")
    public ResponseEntity<ShortenerUrlDto> showShortener(@PathVariable String code){
        return ResponseEntity.ok(shortenerUrlService.getShortenerUrl(code));
    }

    @GetMapping("/fast/{realUrl}/**")
    @ResponseBody
    public ResponseEntity<ShortenerUrlDto> createFast(@PathVariable String realUrl, HttpServletRequest request){
        return ResponseEntity.ok(shortenerUrlService.createFast(realUrl, request));
    }

    @PostMapping
    public ResponseEntity<ShortenerUrlDto> create(@Valid @RequestBody ShortenerUrlDto shortenerUrl){
        return ResponseEntity.ok(shortenerUrlService.create(shortenerUrl));
    }




}
