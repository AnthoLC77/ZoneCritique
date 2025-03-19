import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MediaDetailPageComponent } from './media-detail-page.component';

describe('MediaDetailPageComponent', () => {
  let component: MediaDetailPageComponent;
  let fixture: ComponentFixture<MediaDetailPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MediaDetailPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MediaDetailPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
