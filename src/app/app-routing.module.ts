import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {ForgetComponent} from './forget/forget.component';
import {LoginComponent} from './login/login.component';
import {RegisterPasswordComponent} from './register-password/register-password.component';
import {RegisterComponent} from './register/register.component';
import {ResetPasswordComponent} from './reset-password/reset-password.component';
import {PlatformComponent} from './platform/platform.component';
import {ApiManageComponent} from './platform/api-manage/api-manage.component';
import {CaseManageComponent} from './platform/case-manage/case-manage.component';
import {EnvironmentManageComponent} from './platform/environment-manage/environment-manage.component';
import {ReportManageComponent} from './platform/report-manage/report-manage.component';
import {LogManageComponent} from './platform/log-manage/log-manage.component';
import {TaskManageComponent} from './platform/task-manage/task-manage.component';
import {IndexComponent} from './platform/index/index.component';

const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'forget', component: ForgetComponent},
  {path: 'reset-password', component: ResetPasswordComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register-password', component: RegisterPasswordComponent},
  {
    path: 'platform',
    component: PlatformComponent,
    children: [
      {path: '', redirectTo: 'index', pathMatch: 'full'},
      {path: 'index', component: IndexComponent},
      {path: 'api', component: ApiManageComponent},
      {path: 'environment', component: EnvironmentManageComponent},
      {path: 'task', component: TaskManageComponent},
      {path: 'case', component: CaseManageComponent},
      {path: 'log', component: LogManageComponent},
      {path: 'report', component: ReportManageComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
