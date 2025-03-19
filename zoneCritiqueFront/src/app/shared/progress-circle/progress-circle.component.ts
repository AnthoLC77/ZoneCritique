import { Component, Input, OnInit } from '@angular/core';
import { Media } from '../../../models/media.model';

@Component({
  selector: 'app-progress-circle',
  imports: [],
  templateUrl: './progress-circle.component.html',
  styleUrl: './progress-circle.component.css'
})
export class ProgressCircleComponent implements OnInit {
  @Input() media!: Media | undefined;
  @Input() label: string = " ";
  @Input() note!: number;
  @Input() radius : number = 12;

  circumference: number = 0;

  ngOnInit(): void {
    this.circumference = 2 * Math.PI * this.radius;
  }

  getDashOffset(): number {
    return this.circumference - (this.circumference * this.note / 100);
  }

  getNoteColor(note: number | undefined): string {
    if (note === undefined) {
      return 'grey'; 
    }
    if (note >= 75) {
      return 'green';
    }
    if (note >= 50) {
      return 'yellow';
    }
    return 'red';
  }
  
}
