import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';  
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {  
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.authService.login(this.loginForm.value).subscribe({
      next: (response: any) => {
        console.log('Received response:', response); 
        const { message, token } = response;

        if (token) {
          console.log('Extracted token:', token); 
          
          
          localStorage.setItem('token', token);         
          console.log('Token stored in localStorage:', localStorage.getItem('token'));

          this.loginForm.reset();
          this.router.navigate(['/addressbook/get']); 
        } else {
          console.error('No token found in the response');
        }
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error:', error);
        if (error.status === 403) {
          alert('Your account is not verified. Please check your email.');
        } else if (error.status === 401) {
          alert('Invalid email or password.');
        } else {
          alert('Login failed. Please try again.');
        }
      },
    });
  }
}
