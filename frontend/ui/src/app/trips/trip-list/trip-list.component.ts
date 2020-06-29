import { Component, OnInit } from '@angular/core';

import { Trip } from '../trip';
import { TripService} from '../trip.service';

@Component({
  selector: 'app-trip-list',
  templateUrl: './trip-list.component.html',
  styleUrls: ['./trip-list.component.css']
})
export class TripListComponent implements OnInit {

  trips: Trip[];

  constructor(private tripService: TripService) { }

  ngOnInit(): void {
    this.fetchTrips();
  }

  fetchTrips(): void {
    this.trips = this.tripService.listTrips();
  }
}
