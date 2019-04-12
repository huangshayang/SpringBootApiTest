import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {ResponseValues} from '../../../model/model';

@Component({
  selector: 'app-environment-edit',
  templateUrl: './environment-edit.component.html',
  styleUrls: ['./environment-edit.component.css']
})
export class EnvironmentEditComponent implements OnInit {
  @Input() id;

  envForm: FormGroup;
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
    this.envForm = this.fb.group({
      name: ['', Validators.required],
      domain: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  edit() {
    const formModel = this.envForm.value;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.put('/env/' + this.id, formModel, httpOptions)
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
    this.http.get('/env/' + id, httpOptions)
      .subscribe(
        ((res: ResponseValues) => {
          this.status = res.status;
          if (this.status === 1) {
            this.envForm.patchValue(res.data);
          } else if (res.status === 10008) {
            this.router.navigate(['login']);
            this.createErrorMessage(res.message);
          } else {
            this.createErrorMessage(res.message);
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
