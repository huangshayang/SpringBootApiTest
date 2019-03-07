import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';

@Component({
  selector: 'app-log-manage',
  templateUrl: './log-manage.component.html',
  styleUrls: ['./log-manage.component.css']
})
export class LogManageComponent implements OnInit {
  content = [];
  status: number;
  pageIndex = 1;
  pageSize = 10;
  loading = true;
  total = 1;

  constructor(
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService
  ) { }

  ngOnInit() {
    this.searchData();
  }

  searchData(reset: boolean = false): void {
    if (reset) {
      this.pageIndex = 1;
    }
    this.loading = true;
    this.getLogs(this.pageIndex, this.pageSize)
      .subscribe(
        res => {
          this.loading = false;
          this.status = res['status'];
          if (this.status === 1) {
            this.total = res['data']['total'];
            this.content = res['data']['list'];
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        });
  }

  private getLogs(pageIndex: number = 1, pageSize: number = 10): Observable<{}> {
    const params = new HttpParams()
      .append('page', `${pageIndex}`)
      .append('size', `${pageSize}`);
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      }),
      params
    };
    return this.http.get('/log/all', httpOptions);
  }

  delete(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.delete('/log/' + id, httpOptions)
      .subscribe(
        res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.searchData();
            this.createSuccessMessage(res['message']);
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
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

}
