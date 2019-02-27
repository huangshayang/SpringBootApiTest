import {Component, Input, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';

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
  env = [
    {
      id: 1,
      name: '测试网'
    },
    {
      id: 2,
      name: '现网'
    }
  ];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private activeRouter: ActivatedRoute,
    private message: NzMessageService,
    private modalRef: NzModalRef
  ) { }

  ngOnInit() {
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
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.closeModal();
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

  private get(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.get('/api/' + id, httpOptions)
      .subscribe(
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.envData = this.apiForm.patchValue(res['data']);
          } else if (res['status'] === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
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
