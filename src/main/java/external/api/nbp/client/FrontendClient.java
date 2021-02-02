package external.api.nbp.client;

import external.api.nbp.config.RateConfig;
import external.api.nbp.model.RateCurrencyDto;
import external.api.nbp.model.RateTableDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class FrontendClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendClient.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RateConfig rateConfig;

    public List<RateTableDto> getRates(String table){
        URI uri = UriComponentsBuilder.fromHttpUrl(rateConfig.getRateEndPoint() + "exchangerates/tables/" + table)
                .build().encode().toUri();
        try{
            RateTableDto[] rateResponse = restTemplate.getForObject(uri, RateTableDto[].class);
            return Arrays.asList(Optional.ofNullable(rateResponse).orElse(new RateTableDto[0]));
        }catch(RestClientException e){
            LOGGER.error(e.getMessage(), e);
            return  new ArrayList<>();
        }
    }
    public List<RateTableDto> getRatesInDateRangeFromTo(String table, String startDate, String endDate){
        URI uri = UriComponentsBuilder.fromHttpUrl(rateConfig.getRateEndPoint() + "exchangerates/tables/" + table + "/" + startDate + "/" + endDate)
                .build().encode().toUri();
        try{
            RateTableDto[] rateResponse = restTemplate.getForObject(uri, RateTableDto[].class);
            return Arrays.asList(Optional.ofNullable(rateResponse).orElse(new RateTableDto[0]));
        }catch(RestClientException e){
            LOGGER.error(e.getMessage(), e);
            return  new ArrayList<>();
        }
    }
    public RateCurrencyDto getRateAPArticularCurrency(String table, String code){
        URI uri = UriComponentsBuilder.fromHttpUrl(rateConfig.getRateEndPoint() + "exchangerates/rates/" + table + "/" + code)
                .build().encode().toUri();
        try{
            return restTemplate.getForObject(uri, RateCurrencyDto.class);
        }catch(RestClientException e){
            LOGGER.error(e.getMessage(), e);
            return new RateCurrencyDto();
        }
    }



}
