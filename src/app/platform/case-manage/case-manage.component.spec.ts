import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseManageComponent } from './case-manage.component';

describe('CaseManageComponent', () => {
  let component: CaseManageComponent;
  let fixture: ComponentFixture<CaseManageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaseManageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
