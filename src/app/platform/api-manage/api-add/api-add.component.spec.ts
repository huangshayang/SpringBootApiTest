import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApiAddComponent } from './api-add.component';

describe('ApiAddComponent', () => {
  let component: ApiAddComponent;
  let fixture: ComponentFixture<ApiAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApiAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApiAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
