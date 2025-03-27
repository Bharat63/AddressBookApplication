import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { AddAddressComponent } from './pages/add-address/add-address.component';
import { AddressDetailsComponent } from './pages/get-address/get-address.component';
import { EditAddressComponent } from './pages/edit-address/edit-address.component';

const routes: Routes = [
  { path: 'auth/register', component: RegisterComponent },
  { path: 'auth/login', component: LoginComponent },
  { path: '', redirectTo: '/auth/login', pathMatch: 'full' },  

  // Address Book Pages
  { path: 'addressbook/get', component: HomeComponent },
  { path: 'addressbook/add', component: AddAddressComponent },
  { path: 'addressbook/get/:id', component: AddressDetailsComponent },  
  { path: 'addressbook/Update/:id', component: EditAddressComponent },  

  { path: '**', redirectTo: '/addressbook/get' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
