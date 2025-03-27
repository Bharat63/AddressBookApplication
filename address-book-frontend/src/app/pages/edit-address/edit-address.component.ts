import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AddressBookService } from 'src/app/services/address-book.service';

@Component({
  selector: 'app-edit-address',
  templateUrl: './edit-address.component.html',
  styleUrls: ['./edit-address.component.css']
})
export class EditAddressComponent implements OnInit {
  editAddressForm!: FormGroup;
  isSubmitting: boolean = false; // ✅ Define isSubmitting
  addressId!: number;

  constructor(
    private fb: FormBuilder,
    private addressBookService: AddressBookService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.addressId = Number(this.route.snapshot.paramMap.get('id'));

    this.editAddressForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]]
    });

    this.loadAddressDetails();
  }

  loadAddressDetails(): void {
    this.addressBookService.getAddressById(this.addressId).subscribe({
      next: (response) => {
        this.editAddressForm.patchValue(response);
      },
      error: (error) => {
        console.error('❌ Error loading address details:', error);
        alert('Error loading address. Please try again.');
        this.router.navigate(['/addressbook/get']);
      }
    });
  }

  updateAddress(): void {
    if (this.editAddressForm.invalid) return;

    this.isSubmitting = true;
    this.addressBookService.updateAddress(this.addressId, this.editAddressForm.value).subscribe({
      next: () => {
        alert('✅ Address updated successfully!');
        this.router.navigate(['/addressbook/get']); // Redirect after update
      },
      error: (error) => {
        console.error('❌ Error updating address:', error);
        alert('Error updating address. Please try again.');
        this.isSubmitting = false;
      },
      complete: () => {
        this.isSubmitting = false;
      }
    });
  }
}
