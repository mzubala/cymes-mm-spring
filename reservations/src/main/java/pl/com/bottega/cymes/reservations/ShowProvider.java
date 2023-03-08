package pl.com.bottega.cymes.reservations;

import pl.com.bottega.cymes.showscheduler.dto.ShowDto;

interface ShowProvider {
    ShowDto getShow(Long showId);
}
