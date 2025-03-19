import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Media } from '../../models/media.model';

@Injectable({
  providedIn: 'root'
})
export class MediaServiceService {
  private jsonUrl = 'media.json'

  constructor(
    private http: HttpClient
  ) { }

  getMedias(): Observable<Media[]> {
    return this.http.get<Media[]>(this.jsonUrl);
  }

  getMediaById(id: number): Observable<Media | undefined> {
    return this.getMedias().pipe(
      map((medias: Media[]) => {
        const foundMedia = medias.find(media => String(media.id) === String(id));  // Convertir les deux en string
        console.log('Média trouvé :', foundMedia);  // Vérifiez que le média est bien trouvé
        return foundMedia;
      })
    );
  }

  searchMedias(query: string) : Observable<Media[]> {
    return this.getMedias().pipe(
      map((medias: Media[]) => 
      medias.filter(media => 
        media.titre.toLowerCase().includes(query.toLowerCase())
      ))
    )
  }
  
  



}
