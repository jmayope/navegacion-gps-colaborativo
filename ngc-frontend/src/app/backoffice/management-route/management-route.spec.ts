import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagementRoute } from './management-route';

describe('ManagementRoute', () => {
  let component: ManagementRoute;
  let fixture: ComponentFixture<ManagementRoute>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagementRoute]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagementRoute);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
