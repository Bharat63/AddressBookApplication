import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AddressBookService } from '../../services/address-book.service';

@Component({
  selector: 'app-get-address',
  templateUrl: './get-address.component.html',
  styleUrls: ['./get-address.component.css']
})
export class AddressDetailsComponent implements OnInit {
  addressId: number | null = null;
  addressDetails: any = null;

  constructor(
    private route: ActivatedRoute,
    private addressBookService: AddressBookService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.addressId = Number(params.get('id'));
      console.log("🆔 Address ID:", this.addressId);

      if (this.addressId) {
        this.fetchAddressDetails();
      }
    });
  }

  fetchAddressDetails(): void {
    this.addressBookService.getAddressById(this.addressId).subscribe({
      next: (response) => {
        console.log("✅ Address details fetched:", response);
        this.addressDetails = response;
      },
      error: (error) => {
        console.error("❌ Error fetching address:", error);
        this.addressDetails = null;
      }
    });
  }
}
