import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        data: { pageTitle: 'pbPointsConsoleApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'event',
        data: { pageTitle: 'pbPointsConsoleApp.event.home.title' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'game',
        data: { pageTitle: 'pbPointsConsoleApp.game.home.title' },
        loadChildren: () => import('./game/game.module').then(m => m.GameModule),
      },
      {
        path: 'team',
        data: { pageTitle: 'pbPointsConsoleApp.team.home.title' },
        loadChildren: () => import('./team/team.module').then(m => m.TeamModule),
      },
      {
        path: 'tournament',
        data: { pageTitle: 'pbPointsConsoleApp.tournament.home.title' },
        loadChildren: () => import('./tournament/tournament.module').then(m => m.TournamentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
