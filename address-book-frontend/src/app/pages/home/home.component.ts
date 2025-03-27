import { Component, OnInit } from '@angular/core';
import { AddressBookService } from '../../services/address-book.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  addresses: any[] = [];  

  constructor(private addressBookService: AddressBookService) {}

  ngOnInit(): void {
    this.fetchAddresses();
  }

  fetchAddresses(): void {
    this.addressBookService.getAllAddresses().subscribe({
      next: (response) => {
        console.log("✅ Addresses fetched:", response);
        this.addresses = response;
      },
      error: (err) => {
        console.error('❌ Error fetching addresses:', err);
      }
    });
  }

 deleteAddress(id: number): void {
  const confirmDelete = confirm("⚠️ Are you sure you want to delete this address?");
  if (!confirmDelete) return;

  console.log(`🔄 Sending delete request for ID: ${id}`);

  this.addressBookService.deleteAddress(id).subscribe({
    next: (response) => {
      console.log("✅ Response from API:", response);

      if (response && response.status === 204) { 
        // API successfully processed request but returned no content (standard for DELETE)
        alert("✅ Address deleted successfully!");
        this.fetchAddresses();
      } else {
        // Even if request succeeds, backend might return an unexpected response
        console.warn("⚠️ Unexpected response format:", response);
        alert("✅ Address deleted successfully! (But received unexpected response)");
        this.fetchAddresses();
      }
    },
    error: (error) => {
      console.error("❌ Error deleting address:", error);

      // More specific error messages based on HTTP response
      if (error.status === 404) {
        alert("❌ Address not found. It might have already been deleted.");
      } else if (error.status === 500) {
        alert("❌ Server error! Please try again later.");
      } else {
        alert("❌ Error deleting address. Please try again.");
      }
    }
  });
}


  
}
