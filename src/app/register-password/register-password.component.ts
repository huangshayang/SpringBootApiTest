import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';

@Component({
  selector: 'app-register-password',
  templateUrl: './register-password.component.html',
  styleUrls: ['./register-password.component.css']
})
export class RegisterPasswordComponent implements OnInit {

  status: number;
  validateForm: FormGroup;
  token: string;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService,
    private route: ActivatedRoute
  ) {
    this.token = this.route.snapshot.queryParamMap.get('token');
  }

  ngOnInit() {
    this.validateForm = this.fb.group({
      password: [null, [ Validators.required ] ]
    });
  }

  registerAccount() {
    const body = new HttpParams()
      .set('password', this.validateForm.get('password').value);
    const params = new HttpParams()
      .append('token', this.token);
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/x-www-form-urlencoded'
      }),
      params
    };
    this.http.put('/account/register', body, httpOptions)
      .subscribe(
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.router.navigate(['login']);
            this.createSuccessMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        })
      );
  }

  private createSuccessMessage(success: string): void {
    this.message.success(success, { nzDuration: 3000 });
  }

  private createErrorMessage(error: string): void {
    this.message.error(error, { nzDuration: 3000 });
  }

  register_forward() {
    this.router.navigate(['register']);
  }

}
