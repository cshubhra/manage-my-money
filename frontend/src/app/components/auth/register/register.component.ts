import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  submitted = false;
  error: string = '';
  
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {
    // Redirect to home if already logged in
    if (this.authService.isAuthenticated) {
      this.router.navigate(['/']);
    }
    
    // Initialize form
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, {
      validators: this.passwordMatchValidator
    });
  }
  
  ngOnInit(): void {}
  
  /**
   * Convenience getter for easy access to form fields
   */
  get f() { return this.registerForm.controls; }
  
  /**
   * Custom validator to check if password and confirm password match
   */
  passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    
    return password === confirmPassword ? null : { passwordMismatch: true };
  }
  
  /**
   * Submit the registration form
   */
  onSubmit(): void {
    this.submitted = true;
    
    // stop here if form is invalid
    if (this.registerForm.invalid) {
      return;
    }
    
    this.loading = true;
    this.authService.register(
      this.f.email.value,
      this.f.username.value,
      this.f.password.value
    ).subscribe(
      () => {
        this.snackBar.open('Registration successful', 'Close', {
          duration: 3000
        });
        this.router.navigate(['/']);
      },
      error => {
        this.error = error.error?.message || 'Registration failed';
        this.snackBar.open(this.error, 'Close', {
          duration: 5000
        });
        this.loading = false;
      }
    );
  }
}