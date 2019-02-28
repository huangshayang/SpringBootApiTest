import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {EnvironmentAddComponent} from './environment-add/environment-add.component';
import {EnvironmentEditComponent} from './environment-edit/environment-edit.component';

@Component({
  selector: 'app-environment-manage',
  templateUrl: './environment-manage.component.html',
  styleUrls: ['./environment-manage.component.css']
})
export class EnvironmentManageComponent implements OnInit {
  content = [];
  status: number;
  loading = true;

  constructor(
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService,
    private modal: NzModalService
  ) { }

  ngOnInit() {
    this.searchData();
  }

  searchData(): void {
    this.loading = true;
    this.getEnvs()
      .subscribe(
        res => {
          this.loading = false;
          this.status = res['status'];
          if (this.status === 1) {
            this.content = res['data'];
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        });
  }

  private getEnvs() {
    return this.http.get('/env/all', {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    });
  }

  addEnv() {
    this.modal.create({
      nzTitle: '添加环境',
      nzContent: EnvironmentAddComponent,
      nzWidth: 550,
      nzFooter: null
    });
  }

  editEnv(id: number) {
    this.modal.create({
      nzTitle: '编辑环境',
      nzContent: EnvironmentEditComponent,
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
    this.http.delete('/env/' + id, httpOptions)
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
