import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TripsModule } from './trips/trips.module';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const routes: Routes = [
  { path: 'trips', loadChildren: () => TripsModule },
  { path: '', redirectTo: '/trips', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
