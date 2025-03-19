import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { MediaDetailPageComponent } from './core/media-detail-page/media-detail-page.component';
import { LoginModalComponent } from './shared/login-modal/login-modal.component';


export const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'details/:id', component: MediaDetailPageComponent},
    {path: 'login', component: LoginModalComponent}
];
