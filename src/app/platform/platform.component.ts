import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';

@Component({
  selector: 'app-platform',
  templateUrl: './platform.component.html',
  styleUrls: ['./platform.component.css']
})
export class PlatformComponent implements OnInit {
  status: number;
  userInfo = sessionStorage.getItem('userInfo');

  constructor(
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService
  ) { }

  ngOnInit() {
  }

  logout() {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.delete('/account/logout', httpOptions)
      .subscribe(
        res => {
          this.status = res['status'];
          if (this.status === 1) {
            sessionStorage.clear();
            this.router.navigate(['login']);
            this.createSuccessMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        });
  }

  private createSuccessMessage(success: string): void {
    this.message.success(success, { nzDuration: 3000 });
  }

  private createErrorMessage(error: string): void {
    this.message.error(error, { nzDuration: 3000 });
  }

  changePassword() {

  }

}
