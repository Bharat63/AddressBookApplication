import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AddressBookService {
  private apiUrl = 'http://localhost:8080/addressbook';

  constructor(private http: HttpClient) {}

  private getHttpOptions() {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('JWT token is missing!');
      return { 
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      };
    }

    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      })
    };
  }

  addAddress(addressData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, addressData, this.getHttpOptions());
  }

  getAllAddresses(): Observable<any> {
    return this.http.get(`${this.apiUrl}/get`, this.getHttpOptions());
  }

  getAddressById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/get/${id}`, this.getHttpOptions());
  }

  updateAddress(id: number, data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/Update/${id}`, data, this.getHttpOptions());
  }

  deleteAddress(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, this.getHttpOptions());
  }
}
