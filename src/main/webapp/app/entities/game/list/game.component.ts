import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGame } from '../game.model';
import { GameService } from '../service/game.service';
import { GameDeleteDialogComponent } from '../delete/game-delete-dialog.component';

@Component({
  selector: 'jhi-game',
  templateUrl: './game.component.html',
})
export class GameComponent implements OnInit {
  games?: IGame[];
  isLoading = false;

  constructor(protected gameService: GameService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.gameService.query().subscribe(
      (res: HttpResponse<IGame[]>) => {
        this.isLoading = false;
        this.games = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGame): number {
    return item.id!;
  }

  delete(game: IGame): void {
    const modalRef = this.modalService.open(GameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.game = game;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
