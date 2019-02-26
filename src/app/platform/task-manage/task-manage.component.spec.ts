import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskManageComponent } from './task-manage.component';

describe('TaskManageComponent', () => {
  let component: TaskManageComponent;
  let fixture: ComponentFixture<TaskManageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskManageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
