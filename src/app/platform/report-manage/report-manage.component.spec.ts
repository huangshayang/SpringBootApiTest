import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportManageComponent } from './report-manage.component';

describe('ReportManageComponent', () => {
  let component: ReportManageComponent;
  let fixture: ComponentFixture<ReportManageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportManageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
