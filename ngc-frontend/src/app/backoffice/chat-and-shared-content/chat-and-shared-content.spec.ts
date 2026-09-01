import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatAndSharedContent } from './chat-and-shared-content';

describe('ChatAndSharedContent', () => {
  let component: ChatAndSharedContent;
  let fixture: ComponentFixture<ChatAndSharedContent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatAndSharedContent],
    }).compileComponents();

    fixture = TestBed.createComponent(ChatAndSharedContent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
