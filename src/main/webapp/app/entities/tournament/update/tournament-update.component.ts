import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITournament, Tournament } from '../tournament.model';
import { TournamentService } from '../service/tournament.service';

@Component({
  selector: 'jhi-tournament-update',
  templateUrl: './tournament-update.component.html',
})
export class TournamentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected tournamentService: TournamentService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tournament }) => {
      this.updateForm(tournament);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tournament = this.createFromForm();
    if (tournament.id !== undefined) {
      this.subscribeToSaveResponse(this.tournamentService.update(tournament));
    } else {
      this.subscribeToSaveResponse(this.tournamentService.create(tournament));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITournament>>): void {
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

  protected updateForm(tournament: ITournament): void {
    this.editForm.patchValue({
      id: tournament.id,
      name: tournament.name,
    });
  }

  protected createFromForm(): ITournament {
    return {
      ...new Tournament(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
