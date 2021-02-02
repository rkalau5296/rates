package external.api.nbp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateCurrencyDto {

    String table;
    String currency;
    String code;
    List<RatesCurrency> rates;
}
