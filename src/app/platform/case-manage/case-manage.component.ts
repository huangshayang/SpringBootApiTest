import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {CaseAddComponent} from './case-add/case-add.component';
import {CaseEditComponent} from './case-edit/case-edit.component';

@Component({
  selector: 'app-case-manage',
  templateUrl: './case-manage.component.html',
  styleUrls: ['./case-manage.component.css']
})
export class CaseManageComponent implements OnInit {
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

  deleteAllByApiId() {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.delete('/api/', httpOptions)
      .subscribe(
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.get();
            this.createSuccessMessage(res['message']);
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        })
      );
  }

  deleteOne(caseId: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.delete('/case/' + caseId, httpOptions)
      .subscribe(
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.get();
            this.createSuccessMessage(res['message']);
          } else if (res['status'] === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        })
      );
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
            this.total = res['data']['totalElements'];
            this.content = res['data']['content'];
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        });
  }

  private get(pageIndex: number = 0, pageSize: number = 10) {
    const params = new HttpParams()
      .append('page', `${pageIndex - 1}`)
      .append('size', `${pageSize}`);
    return this.http.get('/case/all', {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      }),
      params
    });
  }

  private createSuccessMessage(success: string): void {
    this.message.success(success, { nzDuration: 3000 });
  }

  private createErrorMessage(error: string): void {
    this.message.error(error, { nzDuration: 3000 });
  }

  addCase() {
    this.modal.create({
      nzTitle: '添加用例',
      nzContent: CaseAddComponent,
      nzWidth: 650,
      nzFooter: null
    });
  }

  editCase(id: number) {
    this.modal.create({
      nzTitle: '编辑用例',
      nzContent: CaseEditComponent,
      nzWidth: 650,
      nzComponentParams: {
        id
      },
      nzFooter: null
    });
  }
}
