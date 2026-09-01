import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Accompaniment } from './accompaniment';

describe('Accompaniment', () => {
  let component: Accompaniment;
  let fixture: ComponentFixture<Accompaniment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Accompaniment],
    }).compileComponents();

    fixture = TestBed.createComponent(Accompaniment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
