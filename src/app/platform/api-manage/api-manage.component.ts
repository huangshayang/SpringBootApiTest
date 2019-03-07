import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {ApiAddComponent} from './api-add/api-add.component';
import {ApiEditComponent} from './api-edit/api-edit.component';
import {Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';

@Component({
  selector: 'app-api-manage',
  templateUrl: './api-manage.component.html',
  styleUrls: ['./api-manage.component.css']
})
export class ApiManageComponent implements OnInit {
  content = [];
  status: number;
  pageIndex = 1;
  pageSize = 10;
  loading = true;
  total = 1;

  constructor(
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService,
    private modal: NzModalService
  ) { }

  ngOnInit() {
    this.searchData();
  }

  searchData(reset: boolean = false): void {
    if (reset) {
      this.pageIndex = 1;
    }
    this.loading = true;
    this.get(this.pageIndex, this.pageSize)
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

  private get(pageIndex: number = 1, pageSize: number = 10) {
    const params = new HttpParams()
      .append('page', `${pageIndex}`)
      .append('size', `${pageSize}`);
    return this.http.get('/api/all', {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      }),
      params
    });
  }

  addApi() {
    this.modal.create({
      nzTitle: '添加接口',
      nzContent: ApiAddComponent,
      nzWidth: 550,
      nzFooter: null
    });
  }

  editApi(id: number) {
    this.modal.create({
      nzTitle: '编辑接口',
      nzContent: ApiEditComponent,
      nzWidth: 550,
      nzComponentParams: {
        id
      },
      nzFooter: null
    });
  }

  delete(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.delete('/api/' + id, httpOptions)
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
