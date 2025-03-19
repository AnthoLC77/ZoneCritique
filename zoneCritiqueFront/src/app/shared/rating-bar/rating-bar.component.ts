import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-rating-bar',
  imports: [CommonModule, FormsModule],
  templateUrl: './rating-bar.component.html',
  styleUrl: './rating-bar.component.css'
})
export class RatingBarComponent {
  stars: number[] = Array(10).fill(0);
  rating: number = 0;
  hoveredRating!: number ;


  hoverRating(value: number): void {
    this.hoveredRating = value; // Définit la note temporaire au survol
  }

  setRating(value: number): void {
    this.rating = value; // Définit la note après un clic
    this.hoveredRating = 0; // Réinitialise le survol après un clic
  }
}
