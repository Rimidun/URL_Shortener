package com.vladimirov.url_shortener.service;

import com.vladimirov.url_shortener.dao.ShortenerUrlRepository;
import com.vladimirov.url_shortener.dto.ShortenerUrlDto;
import com.vladimirov.url_shortener.dto.converter.ShortenerUrlConverter;

import com.vladimirov.url_shortener.entity.ShortenerUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

import java.security.SecureRandom;


@Service
public class ShortenerUrlService {

    private final ShortenerUrlRepository shortenerUrlRepository;
    private final ShortenerUrlConverter shortenerUrlConverter;

    @Value("4")
    private int codeLength;

    Logger logger = LoggerFactory.getLogger(ShortenerUrlService.class);

    public ShortenerUrlService(ShortenerUrlRepository shortenerUrlRepository, ShortenerUrlConverter shortenerUrlConverter){
        this.shortenerUrlRepository = shortenerUrlRepository;
        this.shortenerUrlConverter = shortenerUrlConverter;
    }

    public HttpHeaders getUrl(String code) throws URISyntaxException{
        ShortenerUrl shortenerUrl = getShortUrl(code);
        logger.info("ShortenerURL info: " + shortenerUrl.toString());
        URI uri = new URI(shortenerUrl.getRealValue());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return httpHeaders;

    }

    public ShortenerUrlDto getShortenerUrl(String code){
        return shortenerUrlConverter.shortenerUrlDto(getShortUrl(code));
    }

    public ShortenerUrlDto create(ShortenerUrlDto shortenerUrlDto){
        if(shortenerUrlRepository.existsByCode(shortenerUrlDto.getCode())){
            System.out.println("эта короткая ссылка уже существует");
        }
        if(shortenerUrlDto.getCode() == null || shortenerUrlDto.getCode().trim().length() <= 0){
            String code = generateRandomCode();
            shortenerUrlDto.setCode(code);
            logger.info(code);
        }
        shortenerUrlRepository.save(shortenerUrlConverter.dtoToShortenerUrl(shortenerUrlDto));
        return shortenerUrlDto;
    }

    public ShortenerUrlDto createFast(String realUrl, HttpServletRequest request){
        ShortenerUrlDto shortenerUrlDto = new ShortenerUrlDto();
        shortenerUrlDto.setRealValue(convertRealUrl(realUrl, request));
        shortenerUrlDto.setCode(generateRandomCode());
        shortenerUrlRepository.save(shortenerUrlConverter.dtoToShortenerUrl(shortenerUrlDto));
        return shortenerUrlDto;
    }

    protected ShortenerUrl getShortUrl(String code){
        return shortenerUrlRepository.findByCode(code).orElseThrow();
    }

    protected String convertRealUrl(String realUrlRaw, HttpServletRequest request){
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);

        String realUrl;
        if (null != arguments && !arguments.isEmpty()) {
            realUrl = realUrlRaw + '/' + arguments;
        } else {
            realUrl = realUrlRaw;
        }
        return realUrl;
    }

    protected String generateRandomCode() {

        String code = "";

        SecureRandom r = new SecureRandom();

        char chars[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P',
                'R', 'S', 'T', 'U', 'V', 'Y', 'Z', 'W', 'Q', 'X',
                '1', '2', '3', '4', '5', '6', '7', '8', '9',};

        int low = 0;
        int high = chars.length;

        do {
            code = "";
            for (int i = 0; i < codeLength; i++) {
                int index = r.nextInt(high - low) + low;
                code += chars[index];
            }
        } while (shortenerUrlRepository.existsByCode(code));


        return code;
    }

}
