import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EnvironmentAddComponent } from './environment-add.component';

describe('EnvironmentAddComponent', () => {
  let component: EnvironmentAddComponent;
  let fixture: ComponentFixture<EnvironmentAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnvironmentAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnvironmentAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
