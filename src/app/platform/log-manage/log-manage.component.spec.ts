import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LogManageComponent } from './log-manage.component';

describe('LogManageComponent', () => {
  let component: LogManageComponent;
  let fixture: ComponentFixture<LogManageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LogManageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LogManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
