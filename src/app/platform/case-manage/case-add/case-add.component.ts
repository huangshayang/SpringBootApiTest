import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {Router} from '@angular/router';

@Component({
  selector: 'app-case-add',
  templateUrl: './case-add.component.html',
  styleUrls: ['./case-add.component.css']
})
export class CaseAddComponent implements OnInit {

  caseForm: FormGroup;
  status: number;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService,
    private modalRef: NzModalRef
  ) { }

  ngOnInit() {
    this.caseForm = this.fb.group({
      jsonData: '',
      paramsData: '',
      name: '',
      apiId: '',
      available: false,
      expectResult: ''
    });
  }

  add() {
    const formModel = this.caseForm.value;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.post('/case/add', formModel, httpOptions)
      .subscribe(
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.createSuccessMessage(res['message']);
            this.closeModal();
          } else if (this.status === 10008) {
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
