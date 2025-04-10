import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
    loading = false;
    hidePassword = true;

    constructor(
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private snackBar: MatSnackBar
    ) {
        this.loginForm = this.formBuilder.group({
            username: ['', [Validators.required]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    ngOnInit(): void {
        if (this.authService.isAuthenticated()) {
            this.router.navigate(['/dashboard']);
        }
    }

    onSubmit(): void {
        if (this.loginForm.valid) {
            this.loading = true;
            
            this.authService.login(this.loginForm.value)
                .subscribe({
                    next: () => {
                        this.loading = false;
                        this.router.navigate(['/dashboard']);
                    },
                    error: (error) => {
                        this.loading = false;
                        this.snackBar.open(
                            error.message || 'Login failed. Please try again.',
                            'Close',
                            { duration: 5000 }
                        );
                    }
                });
        }
    }
}