import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  status: number;
  validateForm: FormGroup;
  userInfo: string;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService
  ) {
  }

  ngOnInit() {
    this.validateForm = this.fb.group({
      username: [null, [Validators.required]],
      password: [null, [Validators.required]]
    });
  }

  login() {
    const body = new HttpParams()
      .set('username', this.validateForm.get('username').value)
      .set('password', this.validateForm.get('password').value);
    this.http.post('/account/login', body, {
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded'
      })
    })
      .subscribe(
        res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.profile();
            this.createSuccessMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        });
  }

  private profile() {
    this.http.get('/user/profile', {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    }).subscribe(
      res => {
        this.status = res['status'];
        if (this.status === 1) {
          this.userInfo = res['data']['username'];
          sessionStorage.setItem('userInfo', this.userInfo);
          this.router.navigate(['/platform/index']);
        } else {
          this.createErrorMessage(res['message']);
        }
      }
    );
  }

  register_forward() {
    this.router.navigate(['register']);
  }

  private createSuccessMessage(success: string): void {
    this.message.success(success, {nzDuration: 3000});
  }

  private createErrorMessage(error: string): void {
    this.message.error(error, {nzDuration: 3000});
  }

  forget_forward() {
    this.router.navigate(['forget']);
  }
}
