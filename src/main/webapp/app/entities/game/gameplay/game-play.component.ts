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

@Component({
  selector: 'jhi-game-play',
  templateUrl: './game-play.component.html',
})
export class GamePlayComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];

    games?: IGame[];
    gameNum?: number;
    isLoading = false;
    gameTime?: number;
    gameStop?: number;
    pointsA?: number;
    pointsB?: number;
    teamA?: string;
    teamB?: string;

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
    category: [],
    nextTeamA: [null, Validators.required],
    nextTeamB: [null, Validators.required],
    nextTimeLeft: [],
  });

  constructor(
    protected gameService: GameService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {
  this.gameNum = 0;
  this.isLoading = true;
  this.gameTime = 0;
  this.gameStop = 0;
  this.pointsA = 0;
  this.pointsB = 0;
  }

  ngOnInit(): void {

      if (sessionStorage.getItem('GAMEID') === ''){
             this.gameNum = 0;
             sessionStorage.setItem('GAMEID', this.gameNum.toString());
            }
            else{
             this.gameNum = +sessionStorage.getItem('GAMEID')!;
            }
    if (this.isLoading){
              this.gameService.query({'status.notIn': ['CANCEL', 'DONE','SUSPENDED']}).subscribe(
                (res: HttpResponse<IGame[]>) => {
                  this.games = res.body ?? [];
                  this.updateForm(this.games[this.gameNum!],this.games[this.gameNum!+1]);
                }
              );
     }
     else
     {
        this.updateForm(this.games![this.gameNum],this.games![this.gameNum+1]);
     }
  }

  previousState(): void {
    window.history.back();
  }

 formatTime(time: any): string {
  const timem = Math.floor(time/60) ;
  const times = time%60;
  return timem.toString().padStart( 2 , "0") + ":" + times.toString().padStart( 2 , "0");
  }

  changeSide(): boolean {
    if ((this.pointsA! + this.pointsB!)%2 !== 0){
      return true;
    }
      return false;
  }

  playBuzzer(): void {
    const audio = new Audio();
    audio.src = '../../../../../resources/sounds/Buzzer.wav';
    audio.load();
    audio.play();
  }

  plus1s(): void {
      this.gameTime = this.gameTime! + 1;
  }

  min1s(): void {
     if (this.gameTime! !== 0){
        this.gameTime = this.gameTime! - 1;
     }
  }

  plus1m(): void {
        this.gameTime = this.gameTime! + 60;
  }

  min1m(): void {
     if (this.gameTime! > 60){
        this.gameTime = this.gameTime! - 60;
     }
  }

  plusSec2(): void {
          this.gameStop = this.gameStop! + 1;
  }

  minSec2(): void {
     if (this.gameStop! !== 0){
        this.gameStop = this.gameStop! - 1;
     }
  }

  plusMin2(): void {
            this.gameStop = this.gameStop! + 60;
  }

  minMin2(): void {
     if (this.gameStop! > 60){
        this.gameStop = this.gameStop! - 60;
     }
  }

  plusPointA(): void{
    this.pointsA = this.pointsA! + 1;
  }

  plusPointB(): void{
    this.pointsB = this.pointsB! + 1;
  }

  minPointA(): void{
    if (this.pointsA! !== 0){
      this.pointsA = this.pointsA! - 1;
    }
  }

  minPointB(): void{
    if (this.pointsB! !== 0){
      this.pointsB = this.pointsB! - 1;
    }
  }


  resetTime(): void {
      alert("Reiniciar Tiempo");
  }
  resetGame(): void {
        alert("Reiniciar Juego");
    }

  stopPoint(): void {
        alert("Stop Point");
  }

  stopGame(): void {
        alert("Stop Game");
  }

  overAct(): void {
        alert("OverTime Activado");
  }

  overDes(): void {
        alert("OverTime Desactivado");
  }

  last(): void{
      let last = this.gameNum! - 1;
      if (last < 0){last = 0;}
       sessionStorage.setItem('GAMEID', last.toString());
       this.isLoading = false;
       this.ngOnInit();
    }

  next(): void{
    const next = this.gameNum! + 1;
     sessionStorage.setItem('GAMEID', next.toString());
     this.isLoading = false;
     this.ngOnInit();
  }

  getGameTime(): string {
  let gtime: string;
   if (this.games![this.gameNum!]!.category!.gameTimeType! === "M"){
             gtime = this.formatTime( (this.games![this.gameNum!].category!.gameTime! * 60)-this.gameTime!);
        }else{
            gtime = this.formatTime(this.games![this.gameNum!]!.category!.gameTime! - this.gameTime!);
        }
  return gtime;
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

  protected updateForm(game: IGame, nextGame: IGame): void {

    this.pointsA = game.pointsA!;
    this.pointsB = game.pointsB!;
    this.teamA = game.teamA?.name;
    this.teamB = game.teamB?.name;

    if(game.timeLeft === null){
      if (game.category?.gameTimeType === "M"){
          this.gameTime = game.category.gameTime! * 60;
      }else{
          this.gameTime = game.category?.gameTime;
      }
    }else
    {
      this.gameTime = game.timeLeft;
    }


      if (game.category?.stopTimeType === "M"){
          this.gameStop = game.category.stopTime! * 60;
      }else{
          this.gameStop = game.category!.stopTime!;
      }



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
      nextTeamA: nextGame.teamA,
      nextTeamB: nextGame.teamB,
      nextTimeLeft: nextGame.timeLeft,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, game.teamA, game.teamB);
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
    };
  }
}
