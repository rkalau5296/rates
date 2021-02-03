package external.api.nbp.gui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
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

    private final ComboBox<String> comboBox;
    private final TextField currencyField;
    private final Grid<RateTable> tableGrid;
    private final Grid<RateDto> rateGrid;
    private final Grid<RateCurrencyDto> currencyGrid;

    private final FrontendClient frontendClient;

    @Autowired
    public RateTableDisplayer(FrontendClient frontendClient) {

        this.frontendClient = frontendClient;
        comboBox = new ComboBox<>();
        comboBox.setItems("A", "B", "C");
        comboBox.setAutoOpen(false);
        comboBox.setLabel("Podaj tabelę");
        comboBox.addValueChangeListener(comboBoxStringComponentValueChangeEvent -> setComboBox());

        tableGrid = new Grid<>(RateTable.class);
        tableGrid.setMaxWidth("800px");
        tableGrid.setWidth("100%");
        tableGrid.setHeight("100px");

        rateGrid = new Grid<>(RateDto.class);
        rateGrid.setMaxWidth("800px");
        rateGrid.setWidth("100%");

        currencyField = new TextField("Podaj walutę");
        Button buttonCurrency = new Button("Pobierz podaną walutę");
        buttonCurrency.addClickListener(clickEvent -> buttonCurrency());

        currencyGrid = new Grid<>(RateCurrencyDto.class);
        currencyGrid.setMaxWidth("800px");
        currencyGrid.setWidth("100%");
        currencyGrid.setHeight("100px");

        add(comboBox, tableGrid, rateGrid, currencyField, buttonCurrency, currencyGrid);
    }

    public void setComboBox(){
        RateTable rateTable = new RateTable();
        rateTable.setTable(comboBox.getValue());
        List<RateTableDto> rateTableDtos = frontendClient.getRates(comboBox.getValue());
        rateTable.setNo(rateTableDtos.get(0).getNo());
        rateTable.setEffectiveDate(rateTableDtos.get(0).getEffectiveDate());
        List<RateDto> rateDtoList = rateTableDtos.get(0).getRates();
        tableGrid.setItems(rateTable);
        rateGrid.setItems(rateDtoList);
    }

    public void buttonCurrency(){
        try {
            RateCurrencyDto rateCurrencyDto = new RateCurrencyDto();
            rateCurrencyDto.setCode(currencyField.getValue());
            rateCurrencyDto.setTable(comboBox.getValue());
            RateCurrencyDto currencyDto = frontendClient.getRateAPArticularCurrency(comboBox.getValue(), currencyField.getValue());
            rateCurrencyDto.setCurrency(currencyDto.getCurrency());
            rateCurrencyDto.setRates(currencyDto.getRates());

            List<RatesCurrency> ratesCurrencies = currencyDto.getRates();

            RatesCurrency ratesCurrency = new RatesCurrency();
            ratesCurrency.setEffectiveDate(ratesCurrencies.get(0).getEffectiveDate());
            ratesCurrency.setMid(ratesCurrencies.get(0).getMid());
            ratesCurrency.setNo(ratesCurrencies.get(0).getNo());

            currencyGrid.setItems(rateCurrencyDto);
        }
        catch (Exception e){
            Notification notification = Notification.show(
                    "Nie ma takiego kodu waluty");
            add(notification);
        }
    }
}
