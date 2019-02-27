import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {TaskEditComponent} from './task-edit/task-edit.component';
import {TaskAddComponent} from './task-add/task-add.component';

@Component({
  selector: 'app-task-manage',
  templateUrl: './task-manage.component.html',
  styleUrls: ['./task-manage.component.css']
})
export class TaskManageComponent implements OnInit {
  data = [];
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

  searchData(reset: boolean = false): void {
    this.loading = true;
    this.get()
      .subscribe(
        res => {
          this.loading = false;
          this.status = res['status'];
          if (this.status === 1) {
            this.data = res['data'];
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        });
  }

  private get() {
    return this.http.get('/task/all', {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    });
  }

  addTask() {
    this.modal.create({
      nzTitle: '添加任务',
      nzContent: TaskAddComponent,
      nzWidth: 550,
      nzFooter: null
    });
  }

  edit(id: number) {
    this.modal.create({
      nzTitle: '编辑任务',
      nzContent: TaskEditComponent,
      nzWidth: 550,
      nzComponentParams: {
        id
      },
      nzFooter: null
    });
  }

  start(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.post('/task/start/' + id, httpOptions)
      .subscribe(
        res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.createSuccessMessage(res['message']);
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        });
  }

  stop(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.post('/task/stop/' + id, httpOptions)
      .subscribe(
        res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.createSuccessMessage(res['message']);
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        });
  }

  delete(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.delete('/task/' + id, httpOptions)
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
