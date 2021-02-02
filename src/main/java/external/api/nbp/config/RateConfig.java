package external.api.nbp.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RateConfig {
    @Value("${api.nbp.pl/api}")
    private String rateEndPoint;
}
