import { CommonModule } from '@angular/common';
import { Component, CUSTOM_ELEMENTS_SCHEMA, EventEmitter, HostListener, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MediaServiceService } from '../../services/media-service.service';
import { Media } from '../../../models/media.model';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faMagnifyingGlass, faTimes, faBars, faUser } from '@fortawesome/free-solid-svg-icons';
import { LoginModalComponent } from '../../shared/login-modal/login-modal.component';

@Component({
  selector: 'app-header',
  imports: [CommonModule, RouterLink, FormsModule, FontAwesomeModule, LoginModalComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  schemas : [CUSTOM_ELEMENTS_SCHEMA]
})
export class HeaderComponent implements OnInit {
  icons = { 
    faMagnifyingGlass : faMagnifyingGlass,
    faTimes : faTimes,
    faBars : faBars,
    faUser : faUser,
  }
  isScrolled : boolean = false;
  searchQuery : string = "";
  searchResults : Media[] = [];
  isMobileMenuOpen = false;
  isLargeScreen = false;
  isSearchInputVisible = false;

  isModalOpen = false;
  currentView : string = 'login'

  @Output() searchEvent = new EventEmitter<string>();

  constructor(
    private mediaService : MediaServiceService,
    private router : Router
  ) {}

  ngOnInit(): void {
    this.isLargeScreen = window.innerWidth >= 1024;
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isScrolled = window.scrollY > 50;
  }

  onSearch() : void {
    if(this.searchQuery.trim()) {
      this.mediaService.searchMedias(this.searchQuery).subscribe(results => {
        this.searchResults = results;
      })
    } else {
      this.searchResults = [];
    }
  }
  
  goToDetailMedia(id : number){
    this.router.navigate(['/details', id]).then(() => {
      this.searchQuery = "";
      this.searchResults = [];
    }) 
  }

  openLoginModal() {
    this.isModalOpen = true
    this.currentView = 'login'
  }

  openSignupModal() {
    this.isModalOpen = true
    this.currentView = 'signup'
  }

  closeModal() {
    this.isModalOpen = false
  }

  resetInput() {
    this.searchQuery = "";
    this.onSearch();
  }

  @HostListener('document:click', ['$event'])
  onBackgroundClick(event: MouseEvent) {
    
    const searchBar = event.target as HTMLElement;
    const inputContainer = searchBar.closest('.search-bar');
    const resultsContainer = searchBar.closest('.search-bar-results'); 
    
    if (!inputContainer && !resultsContainer) {
    this.searchResults = [];
  }
}

  getNoteColor(note : number): string {
    if(note >=80) return '#4caf50';
    if(note >=50) return '#ffc107';
    return '#f44336';
  }

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  toggleInputVisible() {
    this.isSearchInputVisible = !this.isSearchInputVisible;
    this.searchQuery = "";
    this.onSearch();
  }

  // Détecter si l'écran est grand ou petit
  @HostListener('window:resize', ['$event'])
  onResize() {
    this.isLargeScreen = window.innerWidth >= 1024; 
  }
}
