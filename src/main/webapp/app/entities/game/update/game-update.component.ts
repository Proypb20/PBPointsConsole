import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGame, Game } from '../game.model';
import { GameService } from '../service/game.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

@Component({
  selector: 'jhi-game-update',
  templateUrl: './game-update.component.html',
})
export class GameUpdateComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];
  categoriesSharedCollection: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    gameNum: [],
    clasif: [],
    splitdeckNum: [],
    pointsA: [],
    pointsB: [],
    overA: [],
    overB: [],
    pvpA: [],
    pvpB: [],
    timeLeft: [],
    status: [],
    teamA: [null, Validators.required],
    teamB: [null, Validators.required],
    category: [null, Validators.required],
  });

  constructor(
    protected gameService: GameService,
    protected teamService: TeamService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ game }) => {
      this.updateForm(game);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const game = this.createFromForm();
    if (game.id !== undefined) {
      this.subscribeToSaveResponse(this.gameService.update(game));
    } else {
      this.subscribeToSaveResponse(this.gameService.create(game));
    }
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGame>>): void {
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

  protected updateForm(game: IGame): void {
    this.editForm.patchValue({
      id: game.id,
      gameNum: game.gameNum,
      clasif: game.clasif,
      splitdeckNum: game.splitdeckNum,
      pointsA: game.pointsA,
      pointsB: game.pointsB,
      overA: game.overA,
      overB: game.overB,
      pvpA: game.pvpA,
      pvpB: game.pvpB,
      timeLeft: game.timeLeft,
      status: game.status,
      teamA: game.teamA,
      teamB: game.teamB,
      category: game.category,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, game.teamA, game.teamB);
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(this.categoriesSharedCollection, game.category);
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(
        map((teams: ITeam[]) =>
          this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('teamA')!.value, this.editForm.get('teamB')!.value)
        )
      )
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): IGame {
    return {
      ...new Game(),
      id: this.editForm.get(['id'])!.value,
      gameNum: this.editForm.get(['gameNum'])!.value,
      clasif: this.editForm.get(['clasif'])!.value,
      splitdeckNum: this.editForm.get(['splitdeckNum'])!.value,
      pointsA: this.editForm.get(['pointsA'])!.value,
      pointsB: this.editForm.get(['pointsB'])!.value,
      overA: this.editForm.get(['overA'])!.value,
      overB: this.editForm.get(['overB'])!.value,
      pvpA: this.editForm.get(['pvpA'])!.value,
      pvpB: this.editForm.get(['pvpB'])!.value,
      timeLeft: this.editForm.get(['timeLeft'])!.value,
      status: this.editForm.get(['status'])!.value,
      teamA: this.editForm.get(['teamA'])!.value,
      teamB: this.editForm.get(['teamB'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }
}
