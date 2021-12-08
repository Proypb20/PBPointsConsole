import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEvent, Event } from '../event.model';
import { EventService } from '../service/event.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html',
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;

  tournamentsSharedCollection: ITournament[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    status: [],
    tournament: [null, Validators.required],
  });

  constructor(
    protected eventService: EventService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      this.updateForm(event);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.createFromForm();
    if (event.id !== undefined) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(event: IEvent): void {
    this.editForm.patchValue({
      id: event.id,
      name: event.name,
      status: event.status,
      tournament: event.tournament,
    });

    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      event.tournament
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tournamentService
      .query()
      .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
      .pipe(
        map((tournaments: ITournament[]) =>
          this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
        )
      )
      .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));
  }

  protected createFromForm(): IEvent {
    return {
      ...new Event(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      status: this.editForm.get(['status'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
    };
  }
}
