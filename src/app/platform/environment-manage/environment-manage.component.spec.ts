import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EnvironmentManageComponent } from './environment-manage.component';

describe('EnvironmentManageComponent', () => {
  let component: EnvironmentManageComponent;
  let fixture: ComponentFixture<EnvironmentManageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnvironmentManageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnvironmentManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
