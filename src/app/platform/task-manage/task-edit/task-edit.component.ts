import {Component, Input, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ResponseValues} from '../../../model/model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-task-edit',
  templateUrl: './task-edit.component.html',
  styleUrls: ['./task-edit.component.css']
})
export class TaskEditComponent implements OnInit {
  @Input() id;

  taskForm: FormGroup;
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
    this.taskForm = this.fb.group({
      name: ['', Validators.required],
      apiIdList: ['', Validators.required],
      taskTime: ['', Validators.required]
    });
  }

  edit() {
    const formModel = this.taskForm.value;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    this.http.put('/task/' + this.id, formModel, httpOptions)
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
    this.http.get('/task/' + id, httpOptions)
      .subscribe(
        ((res: ResponseValues) => {
          this.status = res.status;
          if (this.status === 1) {
            this.taskForm.patchValue(res.data);
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
