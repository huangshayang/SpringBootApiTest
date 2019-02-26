import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';
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
  caseId: string | null;
  jsonData: string;
  paramsData: string;
  expectResult: string;
  apiId: string | null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private activeRouter: ActivatedRoute,
    private message: NzMessageService
  ) { }

  ngOnInit() {
    this.caseId = this.activeRouter.snapshot.parent.paramMap.get('id');
    this.apiId = this.activeRouter.snapshot.parent.parent.parent.paramMap.get('id');
    this.get();
    this.caseForm = this.fb.group({
      jsonData: '',
      paramsData: '',
      note: '',
      expectResult: '',
      available: ''
    });
  }

  edit() {
    const formModel = this.caseForm.value;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.put('/api/case/' + this.caseId, formModel, httpOptions)
      .subscribe(
        (res => {
          this.status = res['status'];
          if (this.status === 1) {
            this.createSuccessMessage(res['message']);
            this.router.navigate(['api-view/' + this.apiId + '/case-view']);
          } else if (this.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res['message']);
          } else {
            this.createErrorMessage(res['message']);
          }
        })
      );
  }

  get() {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.get('/api/case/' + this.caseId, httpOptions)
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

}
