package external.api.nbp.gui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import external.api.nbp.client.FrontendClient;
import external.api.nbp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Route
public class RateTableDisplayer extends VerticalLayout {

    private TextField tableField;
    private TextField textFieldFrom;
    private TextField textFieldTo;
    private TextField currencyField;
    private Grid<RateTable> tableGrid;
    private Grid<RateDto> rateGrid;
    private Grid<RateCurrencyDto> currencyGrid;

    @Autowired
    private FrontendClient frontendClient;
    @Autowired
    public RateTableDisplayer()
    {
        tableField = new TextField("Podaj tabelę.");
        Button buttonGetRates = new Button("Pobierz kursy walut.");
        textFieldFrom = new TextField("Podaj datę od:");
        textFieldTo = new TextField("Podaj datę do:");
        Button buttonGetRatesInDateRangeFromTo = new Button("Pobierz kursy walut z podanego zakresu");
        tableGrid = new Grid<>(RateTable.class);
        rateGrid = new Grid<>(RateDto.class);
        currencyField = new TextField("Podaj walutę");
        Button buttonCurrency = new Button("Pobierz podaną walutę");
        currencyGrid = new Grid<>(RateCurrencyDto.class);

        buttonGetRates.addClickListener(buttonClickEvent -> addRatesToGrid());
        buttonGetRatesInDateRangeFromTo.addClickListener(buttonClickEvent -> addRatesInDateRangeFromTo());
        buttonCurrency.addClickListener(buttonClickEvent-> fetchApArticularCurrency());

        add(
                tableField,
                buttonGetRates,
                textFieldFrom,
                textFieldTo,
                buttonGetRatesInDateRangeFromTo,
                tableGrid,
                rateGrid,
                currencyField,
                buttonCurrency,
                currencyGrid
        );

    }

    public void addRatesToGrid() {

        RateTable rateTable = new RateTable();
        rateTable.setTable(tableField.getValue());
        List<RateTableDto> rateTableDtos = frontendClient.getRates(tableField.getValue());
        rateTable.setNo(rateTableDtos.get(0).getNo());
        rateTable.setEffectiveDate(rateTableDtos.get(0).getEffectiveDate());
        List<RateDto> rateDtoList = rateTableDtos.get(0).getRates();

        tableGrid.setItems(rateTable);
        rateGrid.setItems(rateDtoList);
    }
    public void addRatesInDateRangeFromTo() {

        RateTable rateTable = new RateTable();
        rateTable.setTable(tableField.getValue());

        List<RateTableDto> rateTableDtos = frontendClient.getRatesInDateRangeFromTo(tableField.getValue(), textFieldFrom.getValue(), textFieldTo.getValue());
        for(int i=0; i<rateTableDtos.size(); i++) {
            rateTable.setNo(rateTableDtos.get(i).getNo());
            rateTable.setEffectiveDate(rateTableDtos.get(i).getEffectiveDate());
            List<RateDto> rateDtoList = rateTableDtos.get(i).getRates();

            tableGrid.setItems(rateTable);
            rateGrid.setItems(rateDtoList);

            //TODO add data filter in grid
        }

    }
    public void fetchApArticularCurrency(){

        RateCurrencyDto rateCurrencyDto = new RateCurrencyDto();
        rateCurrencyDto.setCode(currencyField.getValue());
        rateCurrencyDto.setTable(tableField.getValue());
        RateCurrencyDto currencyDto = frontendClient.getRateAPArticularCurrency(tableField.getValue(), currencyField.getValue());
        rateCurrencyDto.setCurrency(currencyDto.getCurrency());
        rateCurrencyDto.setRates(currencyDto.getRates());

        List<RatesCurrency> ratesCurrencies = currencyDto.getRates();

        RatesCurrency ratesCurrency = new RatesCurrency();
        ratesCurrency.setEffectiveDate(ratesCurrencies.get(0).getEffectiveDate());
        ratesCurrency.setMid(ratesCurrencies.get(0).getMid());
        ratesCurrency.setNo(ratesCurrencies.get(0).getNo());

        currencyGrid.setItems(rateCurrencyDto);

    }
}
