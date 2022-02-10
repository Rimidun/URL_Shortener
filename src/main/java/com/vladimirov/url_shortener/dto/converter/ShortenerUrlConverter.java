package com.vladimirov.url_shortener.dto.converter;

import com.vladimirov.url_shortener.dto.ShortenerUrlDto;
import com.vladimirov.url_shortener.entity.ShortenerUrl;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class ShortenerUrlConverter {

    public ShortenerUrlDto shortenerUrlDto(ShortenerUrl shortenerUrl){
        return ShortenerUrlDto.builder()
                .code(shortenerUrl.getCode())
                .realValue(shortenerUrl.getRealValue())
                .build();
    }

    public ShortenerUrl dtoToShortenerUrl(ShortenerUrlDto shortenerUrlDto){
        ShortenerUrl shortenerUrl = new ShortenerUrl();
        shortenerUrl.setCode(shortenerUrlDto.getCode());
        shortenerUrl.setRealValue(shortenerUrlDto.getRealValue());
        shortenerUrl.setIdAddress(((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getRemoteAddr()
                .toString());

        return shortenerUrl;
    }

}
