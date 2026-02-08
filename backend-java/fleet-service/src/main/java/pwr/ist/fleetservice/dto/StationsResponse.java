package pwr.ist.fleetservice.dto;

import pwr.ist.fleetservice.entity.Station;

import java.util.List;

public record StationsResponse(List<Station> stations) {
}
