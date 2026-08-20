import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagementPerson } from './management-person';

describe('ManagementPerson', () => {
  let component: ManagementPerson;
  let fixture: ComponentFixture<ManagementPerson>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagementPerson]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagementPerson);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
