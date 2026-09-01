import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovementsHistory } from './movements-history';

describe('MovementsHistory', () => {
  let component: MovementsHistory;
  let fixture: ComponentFixture<MovementsHistory>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovementsHistory],
    }).compileComponents();

    fixture = TestBed.createComponent(MovementsHistory);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
