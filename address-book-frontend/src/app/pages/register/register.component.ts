import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    console.log('RegisterComponent Loaded');

    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }

    this.authService.register(this.registerForm.value).subscribe({
      next: (response) => {
        if (response.success) {
          alert(response.message);  // Handle the success response here
          this.registerForm.reset();
        } else {
          alert('Registration failed. Please try again.');
        }
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 403) {
          alert('Access Denied! Please check your authentication.');
        } else if (error.status === 409) {
          alert('Email already exists. Please use a different email.');
        } else {
          alert('Registration failed. Please try again.');
        }
        console.error('Error:', error);
      },
    });
  }
}
