import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { HomeComponent } from './components/home/home.component';
import { WelcomeComponent } from './components/welcome/welcome.component';

const routes: Routes = [
  {path: "home", component: HomeComponent, canActivate: [AuthGuard]},
  {path: "welcome", component: WelcomeComponent},
  {path: "", redirectTo: "welcome", pathMatch: "full"},
  {path: "**", redirectTo: "welcome", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
