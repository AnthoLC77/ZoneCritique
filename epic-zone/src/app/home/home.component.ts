import { Component, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { HeaderComponent } from '../core/header/header.component';
import { SliderComponent } from '../shared/slider/slider.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-home',
  imports: [CommonModule, HeaderComponent, SliderComponent, FontAwesomeModule ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HomeComponent {
  faArrowUp = faArrowUp;
  showScrollButton : boolean = false;

  @HostListener('window:scroll', [])
  onWindowScroll() {
    console.log('Scroll position:', window.scrollY);
    this.showScrollButton = window.scrollY > 500;
  }

  scrollToTop() {
    window.scrollTo({
      top : 0,
      behavior: 'smooth'
    })
  }
}
