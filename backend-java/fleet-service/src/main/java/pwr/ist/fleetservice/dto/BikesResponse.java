package pwr.ist.fleetservice.dto;

import pwr.ist.fleetservice.entity.Bike;

import java.util.List;

public record BikesResponse(List<Bike> bikes) {
}
