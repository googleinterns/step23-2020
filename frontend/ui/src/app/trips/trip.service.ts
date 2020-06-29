import { Injectable } from '@angular/core';
import { Trip } from './trip';
import { TRIPS } from './mock-trips';

@Injectable()
export class TripService {

  constructor() { }

  listTrips(): Trip[] {
    return TRIPS;
  }

}
