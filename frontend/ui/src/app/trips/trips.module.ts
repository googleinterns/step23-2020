import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TripListComponent } from './trip-list/trip-list.component';
import { TripsRoutingModule } from './trips-routing.module';
import { TripService } from './trip.service';

@NgModule({
  imports: [
    CommonModule,
    TripsRoutingModule,
  ],
  declarations: [TripListComponent],
  providers: [TripService]
})
export class TripsModule { }
