package external.api.nbp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatesCurrency {

    String no;
    private String effectiveDate;
    double mid;

    @Override
    public String toString() {
        return "" + mid;
    }
}
