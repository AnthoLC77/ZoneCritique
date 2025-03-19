import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCircleXmark } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-login-modal',
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './login-modal.component.html',
  styleUrl: './login-modal.component.css'
})
export class LoginModalComponent {
  icons = {
    faCircleXmark : faCircleXmark
  }
  email: string = '';
  password: string = '';

  @Input() currentView : string = '';
  @Input() isModalOpen: boolean = false;
  @Output() close = new EventEmitter<void>();

  login() {
    console.log('Connexion avec', this.email, this.password);
    this.close.emit(); // Fermer la modale après la connexion
  }

  signup() {
    this.close.emit();
  }

  closeModal() {
    this.close.emit();
  }

  switchModal(view: string) {
  this.currentView = view;
  }

  onBackgroundClick(event : MouseEvent) {
    const modal = event.target as HTMLElement;
    if(modal.classList.contains('bg-opacity-50')) {
      this.closeModal();
    }
  }

}
