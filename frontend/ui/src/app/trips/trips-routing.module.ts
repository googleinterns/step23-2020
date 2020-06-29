import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TripListComponent } from './trip-list/trip-list.component';

const routes: Routes = [
  { path: '', component: TripListComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TripsRoutingModule { }
