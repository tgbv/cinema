package com.example.application.views.calendar;

import com.example.application.entities.BroadcastPeriod;
import com.example.application.entities.Film;
import com.example.application.entities.Ticket;
import com.example.application.services.BroadcastPeriodsService;
import com.example.application.services.FilmsService;
import com.example.application.services.Shared;
import com.example.application.views.shared.AreYouSureDialog;
import com.example.application.views.shared.MainTab;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

@PageTitle("Calendar - Broadcast times")
@Route("calendar")
public class CalendarView extends VerticalLayout {

    Binder<BroadcastPeriod> bpBinder = new Binder<>(BroadcastPeriod.class);
    private BroadcastPeriod currentBp;

    private BroadcastPeriodsService bps;

    private FilmsService fs;

    private Grid<BroadcastPeriod> calendarGrid;
    private Grid<Ticket> ticketsGrid;

    private Text initTimeText() {
        // Current time
        Text timeText = new Text(
            "Current time: "+
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );


        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                getUI().ifPresent(ui -> ui.access(() -> {
                    new Page(ui).fetchCurrentURL(e -> {
                        if(e.getPath().contains("calendar")) {
                            timeText.setText(
                                "Current time: "+
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                            );
                        } else {
                            t.cancel();
                        }
                    });

                }));
            }
        },0,1000);

        return timeText;
    }

    private Grid initTicketsGrid() {
        ticketsGrid = new Grid<>(Ticket.class);
        ticketsGrid.setColumns("id", "qr", "seat");
        ticketsGrid.addColumn(cp -> cp.getPeriod().getFilm().toString()).setHeader("Film");

        return ticketsGrid;
    }

    private Grid initGridlist() {
        calendarGrid = new Grid<>(BroadcastPeriod.class);
        calendarGrid.setColumns("startTime", "endTime", "film", "nrOfSeats");
        calendarGrid.setItems(bps.findAll());
        calendarGrid.addComponentColumn(cp -> {
            Button edit = new Button("Edit");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> {
                bpBinder.setBean(cp.clone());
            });

            Button delete = new Button("Delete");
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            delete.addClickListener(e -> {
                Dialog d = new AreYouSureDialog(
                    ev -> {
                        bps.delete(cp);
                        Shared.alert("Entry deleted with success!");
                        calendarGrid.setItems(bps.findAll());
                        ev.close();
                    },
                    ev -> ev.close()
                ).getDialog();

                add(d);

                d.open();
            });

            return new HorizontalLayout(edit, delete);
        });
        calendarGrid.addSelectionListener(ev -> {
            ev.getFirstSelectedItem().ifPresent(bp -> {
                ticketsGrid.setItems(bp.getTickets());
            });
        });


        return calendarGrid;
    }

    private VerticalLayout initForm() {
        int DEFAULT_NR_OF_SEATS = 100;
        double DEFAULT_PRICE_PER_SEAT = 25;

        ComboBox<Film> filmsCombo = new ComboBox<>("Select film");
        filmsCombo.setItems(fs.findAll());
        filmsCombo.setItemLabelGenerator(f -> f.getTitle());
        bpBinder.bind(filmsCombo, BroadcastPeriod::getFilm, BroadcastPeriod::setFilm);

        DateTimePicker startTime = new DateTimePicker("Start time");
        bpBinder.bind(startTime, BroadcastPeriod::getStartTimeLDT, BroadcastPeriod::setStartTimeLDT);

        DateTimePicker endTime = new DateTimePicker("End time");
        bpBinder.bind(endTime, BroadcastPeriod::getEndTimeLDT, BroadcastPeriod::setEndTimeLDT);

        IntegerField nrOfSeats = new IntegerField("Nr. of seats", DEFAULT_NR_OF_SEATS, e -> {});
        bpBinder.bind(nrOfSeats, BroadcastPeriod::getNrOfSeats, BroadcastPeriod::setNrOfSeats);

        NumberField pricePerSeat = new NumberField("Price per seat (RON)", DEFAULT_PRICE_PER_SEAT, e -> {});
        bpBinder.bind(pricePerSeat, BroadcastPeriod::getPricePerSeat, BroadcastPeriod::setPricePerSeat);


        Button saveBtn = new Button("Save");
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.addClickListener(e -> {
            try {
                if(bpBinder.getBean() == null) {
                    bps.save(
                        startTime.getValue(),
                        endTime.getValue(),
                        nrOfSeats.getValue(),
                        filmsCombo.getValue(),
                        pricePerSeat.getValue()
                    );

                    filmsCombo.setValue(null);
                    startTime.setValue(null);
                    endTime.setValue(null);
                    nrOfSeats.setValue(DEFAULT_NR_OF_SEATS);
                } else {
                    bps.update(bpBinder.getBean());
                    bpBinder.setBean(null);
                }

                calendarGrid.setItems(bps.findAll());

                Shared.alert("Entry saved with success!");
            } catch(Exception exception) {
                exception.printStackTrace();
                Shared.alert("Could not alter data: "+exception.toString());
            }
        });
        Button abandonBtn = new Button("Abandon", e -> bpBinder.setBean(null));


        VerticalLayout vl = new VerticalLayout(
            new Text("Edit entry"),
            filmsCombo,
            startTime,
            endTime,
            pricePerSeat,
            new HorizontalLayout(nrOfSeats),
            new HorizontalLayout(saveBtn, abandonBtn)
        );
        vl.setWidth("35%");

        return vl;
    }

    public CalendarView(BroadcastPeriodsService bps, FilmsService fs) {
        this.bps = bps;
        this.fs = fs;

        MainTab mt = new MainTab("calendar");
        mt.getStyle().set("width", "100%");
        add(mt);


        HorizontalLayout hl = new HorizontalLayout(
            new VerticalLayout(initGridlist(), initTicketsGrid()),
            initForm()
        );
        hl.setWidth("100%");

        add(hl);
    }
}
