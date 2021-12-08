import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITournament } from '../tournament.model';
import { TournamentService } from '../service/tournament.service';
import { TournamentDeleteDialogComponent } from '../delete/tournament-delete-dialog.component';

@Component({
  selector: 'jhi-tournament',
  templateUrl: './tournament.component.html',
})
export class TournamentComponent implements OnInit {
  tournaments?: ITournament[];
  isLoading = false;

  constructor(protected tournamentService: TournamentService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tournamentService.query().subscribe(
      (res: HttpResponse<ITournament[]>) => {
        this.isLoading = false;
        this.tournaments = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITournament): number {
    return item.id!;
  }

  delete(tournament: ITournament): void {
    const modalRef = this.modalService.open(TournamentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tournament = tournament;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
