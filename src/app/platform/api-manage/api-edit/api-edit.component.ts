import {Component, Input, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {ResponseValues} from '../../../model/model';

@Component({
  selector: 'app-api-edit',
  templateUrl: './api-edit.component.html',
  styleUrls: ['./api-edit.component.css']
})
export class ApiEditComponent implements OnInit {
  @Input() id;

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
    this.get(this.id);
    this.apiForm = this.fb.group({
      url: '',
      method: '',
      cookie: '',
      name: '',
      envId: ''
    });
  }

  edit() {
    const formModel = this.apiForm.value;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.put('/api/' + this.id, formModel, httpOptions)
      .subscribe(
        ((res: ResponseValues) => {
          this.status = res.status;
          if (this.status === 1) {
            this.createSuccessMessage(res.message);
            this.closeModal();
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res.message);
          } else {
            this.createErrorMessage(res.message);
          }
        })
      );
  }

  private get(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.get('/api/' + id, httpOptions)
      .subscribe(
        ((res: ResponseValues) => {
          this.status = res.status;
          if (this.status === 1) {
            this.apiForm.patchValue(res.data);
          } else if (res.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res.message);
          } else {
            this.createErrorMessage(res.message);
          }
        })
      );
  }

  private getEnv() {
    return this.http.get('/env/all', {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    }).subscribe((res: ResponseValues) => {
      this.status = res.status;
      if (this.status === 1) {
        this.envData = res.data;
      } else if (this.status === 10008) {
        this.router.navigate(['login']);
        this.createErrorMessage(res.message);
      } else {
        this.createErrorMessage(res.message);
      }
    });
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
