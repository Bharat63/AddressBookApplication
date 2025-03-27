import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddressBookService } from '../../services/address-book.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-address',
  templateUrl: './add-address.component.html',
  styleUrls: ['./add-address.component.css']
})
export class AddAddressComponent {
  addressForm: FormGroup;

  constructor(
    private fb: FormBuilder, 
    private addressService: AddressBookService,
    private router: Router
  ) { 
    this.addressForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]]
    });
  }

  onSubmit(): void {
    if (this.addressForm.valid) {
      this.addressService.addAddress(this.addressForm.value).subscribe({
        next: () => {
          alert('Address saved successfully!');
          this.addressForm.reset();
          this.router.navigate(['/addressbook/get']); 
        },
        error: (error) => console.error("Error saving address:", error)
      });
    }
  }
}
