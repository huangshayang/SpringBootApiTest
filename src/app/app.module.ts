import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ForgetComponent } from './forget/forget.component';
import { LoginComponent } from './login/login.component';
import { RegisterPasswordComponent } from './register-password/register-password.component';
import {AppRoutingModule} from './app-routing.module';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgZorroAntdModule, NZ_I18N, zh_CN} from 'ng-zorro-antd';
import {registerLocaleData} from '@angular/common';
import { RegisterComponent } from './register/register.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { PlatformComponent } from './platform/platform.component';
import { ApiManageComponent } from './platform/api-manage/api-manage.component';
import { ApiEditComponent } from './platform/api-manage/api-edit/api-edit.component';
import { ApiAddComponent } from './platform/api-manage/api-add/api-add.component';
import { CaseManageComponent } from './platform/case-manage/case-manage.component';
import { EnvironmentManageComponent } from './platform/environment-manage/environment-manage.component';
import { TaskManageComponent } from './platform/task-manage/task-manage.component';
import { LogManageComponent } from './platform/log-manage/log-manage.component';
import { ReportManageComponent } from './platform/report-manage/report-manage.component';
import { IndexComponent } from './platform/index/index.component';
import { CaseAddComponent } from './platform/case-manage/case-add/case-add.component';
import { CaseEditComponent } from './platform/case-manage/case-edit/case-edit.component';
import { TaskAddComponent } from './platform/task-manage/task-add/task-add.component';
import { TaskEditComponent } from './platform/task-manage/task-edit/task-edit.component';
import { EnvironmentAddComponent } from './platform/environment-manage/environment-add/environment-add.component';
import { EnvironmentEditComponent } from './platform/environment-manage/environment-edit/environment-edit.component';
import zh from '@angular/common/locales/zh';

registerLocaleData(zh);

@NgModule({
  declarations: [
    AppComponent,
    ForgetComponent,
    LoginComponent,
    RegisterPasswordComponent,
    RegisterComponent,
    ResetPasswordComponent,
    PlatformComponent,
    ApiManageComponent,
    ApiEditComponent,
    ApiAddComponent,
    CaseManageComponent,
    EnvironmentManageComponent,
    TaskManageComponent,
    LogManageComponent,
    ReportManageComponent,
    IndexComponent,
    CaseAddComponent,
    CaseEditComponent,
    TaskAddComponent,
    TaskEditComponent,
    EnvironmentAddComponent,
    EnvironmentEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    FormsModule,
    NgZorroAntdModule
  ],
  entryComponents: [
    ApiAddComponent, ApiEditComponent,
    CaseAddComponent, CaseEditComponent,
    EnvironmentAddComponent,
    EnvironmentEditComponent,
    TaskAddComponent, TaskEditComponent
  ],
  providers: [{ provide: NZ_I18N, useValue: zh_CN }],
  bootstrap: [AppComponent]
})
export class AppModule { }
