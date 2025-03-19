import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { ActivatedRoute } from '@angular/router';
import { MediaServiceService } from '../../services/media-service.service';
import { Media } from '../../../models/media.model';
import { RatingBarComponent } from '../../shared/rating-bar/rating-bar.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faChevronRight, faChevronLeft, faChevronUp } from '@fortawesome/free-solid-svg-icons';
import { faFlag as faFlagRegular, faHeart as faSolidHeart} from '@fortawesome/free-regular-svg-icons';
import { faFlag as faFlagSolid, faHeart as faRegularHeart } from '@fortawesome/free-solid-svg-icons';
import { faReplyAll as faSolidReply } from '@fortawesome/free-solid-svg-icons';

import { ProgressCircleComponent } from '../../shared/progress-circle/progress-circle.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-media-detail-page',
  imports: [CommonModule, HeaderComponent, RatingBarComponent, FontAwesomeModule, ProgressCircleComponent],
  templateUrl: './media-detail-page.component.html',
  styleUrl: './media-detail-page.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MediaDetailPageComponent implements OnInit {
  media !: Media | undefined;
  faChevronRight = faChevronRight;
  faChevronLeft = faChevronLeft;
  faChevronUp = faChevronUp;
  faFlagSolid = faFlagSolid;
  faFlagRegular = faFlagRegular;
  faHeartRegular = faRegularHeart;
  faHeartSolid = faSolidHeart;
  faSolidReply = faSolidReply;
  isIconStates: boolean[] = [false, false]
  isActiveStates = [false, false];


  constructor(
    private route: ActivatedRoute,
    private mediaService: MediaServiceService
  ) {
  
  }
  ngOnInit(): void {
    // S'abonner aux changements de paramètres
    this.route.paramMap.subscribe((params) => {
      const mediaId = Number(params.get('id'));
      this.loadMedia(mediaId);
    });
  }

  loadMedia(id: number): void {
    this.mediaService.getMediaById(id).subscribe((media) => {
      this.media = media;
    });
  }

  toggleIcon(index: number): void {
    this.isIconStates[index] = !this.isIconStates[index];
    this.isActiveStates[index] = !this.isActiveStates[index]; // Inverse l'état actif
  }
}
