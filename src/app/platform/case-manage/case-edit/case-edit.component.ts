import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Component({
  selector: 'app-case-edit',
  templateUrl: './case-edit.component.html',
  styleUrls: ['./case-edit.component.css']
})
export class CaseEditComponent implements OnInit {
  @Input() id;

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
    this.get(this.id);
    this.caseForm = this.fb.group({
      jsonData: '',
      paramsData: '',
      name: '',
      apiId: '',
      available: '',
      expectResult: ''
    });
  }

  edit() {
    const formModel = this.caseForm.value;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.put('/case/' + this.id, formModel, httpOptions)
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

  private get(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.get('/case/' + id, httpOptions)
      .subscribe(
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.caseForm.patchValue(res['data']);
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
