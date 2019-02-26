import { Component, OnInit } from '@angular/core';
import {HttpHeaders, HttpParams, HttpClient} from '@angular/common/http';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

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
      newPassword: [null, [ Validators.required ] ]
    });
  }

  resetPassword() {
    const body = new HttpParams()
      .set('newPassword', this.validateForm.get('newPassword').value);
    const params = new HttpParams()
      .append('token', this.token);
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/x-www-form-urlencoded'
      }),
      params
    };
    this.http.put('/account/reset/password', body, httpOptions)
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

}
