import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-api-add',
  templateUrl: './api-add.component.html',
  styleUrls: ['./api-add.component.css']
})
export class ApiAddComponent implements OnInit {

  apiForm: FormGroup;
  status: number;
  style = {
    display   : 'block',
    height    : '30px',
    lineHeight: '30px'
  };
  envData: any;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService,
    private modalRef: NzModalRef
  ) { }

  ngOnInit() {
    this.getEnv();
    this.apiForm = this.fb.group({
      url: '',
      method: ['get'],
      cookie: false,
      name: '',
      envId: 1
    });
  }

  private getEnv() {
    return this.http.get('/env/all', {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    }).subscribe(res => {
      this.status = res['status'];
      if (this.status === 1) {
        this.envData = res['data'];
      } else if (this.status === 10008) {
        this.router.navigate(['login']);
        this.createErrorMessage(res['message']);
      } else {
        this.createErrorMessage(res['message']);
      }
    });
  }

  add() {
    const formModel = this.apiForm.value;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.post('/api/add', formModel, httpOptions)
      .subscribe(
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.createSuccessMessage(res['message']);
            this.closeModal();
          } else if (this.status === 10008) {
            this.createErrorMessage(res['message']);
            this.router.navigate(['login']);
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

  closeModal() {
    this.modalRef.close();
  }
}
