package com.example.application.views.tickets;

import com.example.application.entities.BroadcastPeriod;
import com.example.application.entities.Film;
import com.example.application.entities.Ticket;
import com.example.application.services.FilmsService;
import com.example.application.services.Shared;
import com.example.application.services.TicketsService;
import com.example.application.views.shared.MainTab;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collection;

@PageTitle("Buy ticket for film!")
@Route("tickets/buy")
public class BuyTicketView extends VerticalLayout {

    private Binder<Ticket> tBinder = new Binder<>(Ticket.class);

    private FilmsService fs;
    private TicketsService ts;

    private void initForm() {

        TextField priceField = new TextField("Price (RON)");
        priceField.setEnabled(false);

        ComboBox<BroadcastPeriod> bpCombo = new ComboBox<>("Select period");
        bpCombo.setItemLabelGenerator(f -> f.toString(true));
        bpCombo.setWidth("400px");
        bpCombo.addValueChangeListener(ev -> {
            if(ev.getValue() != null) {
                priceField.setValue(ev.getValue().getPricePerSeat().toString());
            }
        });

        ComboBox<Film> filmsCombo = new ComboBox<>("Select film");
        filmsCombo.setItemLabelGenerator(f -> f.getTitle());
        filmsCombo.setItems(fs.findAll());
        filmsCombo.addValueChangeListener(ev -> {
            if(ev.getValue() != null) {
                bpCombo.setItems(ev.getValue().getBroadcastPeriods());
                priceField.setValue("0.0");
            }
        });


        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "I"};
        Collection<Text> seats = new ArrayList<>();
        for (int i=1; i<=10; i++) {
            for(int j=0; j<10; j++) {
                seats.add(new Text(letters[j] + i));
            }
        }
        ComboBox<Text> seatsCombo = new ComboBox<>("Seat");
        seatsCombo.setItems(seats);
        seatsCombo.setItemLabelGenerator(t -> t.getText());

        Button bookBtn = new Button("Book ticket", ev -> {
            if(bpCombo.getValue() == null) {
                Shared.alert("please fill in all fields");
                return;
            }

            try {
                Ticket t = ts.save(
                    new Ticket(
                        bpCombo.getValue(),
                        bpCombo.getValue().getPricePerSeat(),
                        null,
                        seatsCombo.getValue().getText()
                    )
                );

                Shared.alert("Ticket registered! QR code is: " +t.getQr());

            } catch(Exception e) {
                e.printStackTrace();
                Shared.alert(e.toString());
            }


        });

        add(
            new HorizontalLayout(filmsCombo, bpCombo),
            priceField ,seatsCombo, bookBtn
        );
    }

    public BuyTicketView(FilmsService fs, TicketsService ts) {
        this.fs = fs;
        this.ts = ts;

//        MainTab mt = new MainTab("tickets/buy");
//        mt.getStyle().set("width", "100%");
//        add(mt);


        add(new H2("Book ticket"));

        initForm();

    }

}
