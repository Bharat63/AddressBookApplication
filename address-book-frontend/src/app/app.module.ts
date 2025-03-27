import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './pages/register/register.component';
import { AuthService } from './services/auth.service';
import { LoginComponent } from './pages/login/login.component';
import { AddAddressComponent } from './pages/add-address/add-address.component';
import { HomeComponent } from './pages/home/home.component';
import { AddressDetailsComponent } from './pages/get-address/get-address.component';
import { CommonModule } from '@angular/common';
import { EditAddressComponent } from './pages/edit-address/edit-address.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    HomeComponent,
    AddAddressComponent,
    AddressDetailsComponent,
    EditAddressComponent,
    
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    CommonModule
    
  ],
  providers: [AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
