import { AfterViewChecked, AfterViewInit, Component, ContentChild, CUSTOM_ELEMENTS_SCHEMA, ElementRef, Input, OnInit, signal, ViewChild } from '@angular/core';
import { CardComponent } from '../card/card.component';
import { Media } from '../../../models/media.model';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { SwiperContainer } from 'swiper/element';
import { Swiper } from 'swiper/types';
import { MediaServiceService } from '../../services/media-service.service';

@Component({
  selector: 'app-slider',
  imports: [CardComponent, CommonModule, FontAwesomeModule],
  templateUrl: './slider.component.html',
  styleUrl: './slider.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SliderComponent implements AfterViewInit, OnInit  {
  @Input() categorie: string = '';
  slidesPerView = 5;
  faArrowLeft = faArrowLeft;
  faArrowRight = faArrowRight;
  currentIndex : number = 0;
  @ViewChild('swiper') swiperRef!: ElementRef<SwiperContainer>;

  @Input() mediaList : Media[] = [];

  constructor(
    private mediaService: MediaServiceService
  ) {

  }
  ngOnInit(): void {
    this.mediaService.getMedias().subscribe(data => {
      this.mediaList = data;
    })
    console.log('Media chargé ' + this.mediaList);
    
  }


    private swiperInstance: Swiper | null = null;

    ngAfterViewInit(): void {
      setTimeout(() => {
        if (this.swiperRef.nativeElement.swiper) {
          this.swiperInstance = this.swiperRef.nativeElement.swiper;
  
          // Mettre à jour currentIndex à chaque changement de slide
          this.swiperInstance.on('slideChange', () => {
            this.currentIndex = this.swiperInstance!.realIndex;
            console.log('Slide index updated (swiper event):', this.currentIndex);
          });
  
          // Initialisation correcte de l'index au démarrage
          this.currentIndex = this.swiperInstance.realIndex;
        }
      });
    }

    getSlidesPerView(): number {
      return this.slidesPerView;
    }
  
    goToPrevious() {
      if (this.swiperInstance) {
        this.swiperInstance.slidePrev();
        
      }
    }
  
    goToNext() {
      if (this.swiperInstance) {
        this.swiperInstance.slideNext();
        
      }
    }
  
    isAtStart() : boolean {
       return this.currentIndex === 0;
    }

    isAtEnd() {
      return this.currentIndex === this.mediaList.length - 1;
    }
}
