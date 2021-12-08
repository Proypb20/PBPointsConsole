import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICategory, Category } from '../category.model';
import { CategoryService } from '../service/category.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

@Component({
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html',
})
export class CategoryUpdateComponent implements OnInit {
  isSaving = false;

  eventsSharedCollection: IEvent[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    gameTimeType: [null, [Validators.required]],
    gameTime: [null, [Validators.required]],
    stopTimeType: [null, [Validators.required]],
    stopTime: [],
    stopSdTimeType: [null, [Validators.required]],
    stopSdTime: [null, [Validators.required]],
    points: [],
    difPoints: [null, [Validators.required]],
    event: [null, Validators.required],
  });

  constructor(
    protected categoryService: CategoryService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ category }) => {
      this.updateForm(category);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const category = this.createFromForm();
    if (category.id !== undefined) {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    }
  }

  trackEventById(index: number, item: IEvent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
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

  protected updateForm(category: ICategory): void {
    this.editForm.patchValue({
      id: category.id,
      name: category.name,
      gameTimeType: category.gameTimeType,
      gameTime: category.gameTime,
      stopTimeType: category.stopTimeType,
      stopTime: category.stopTime,
      stopSdTimeType: category.stopSdTimeType,
      stopSdTime: category.stopSdTime,
      points: category.points,
      difPoints: category.difPoints,
      event: category.event,
    });

    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing(this.eventsSharedCollection, category.event);
  }

  protected loadRelationshipsOptions(): void {
    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing(events, this.editForm.get('event')!.value)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }

  protected createFromForm(): ICategory {
    return {
      ...new Category(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      gameTimeType: this.editForm.get(['gameTimeType'])!.value,
      gameTime: this.editForm.get(['gameTime'])!.value,
      stopTimeType: this.editForm.get(['stopTimeType'])!.value,
      stopTime: this.editForm.get(['stopTime'])!.value,
      stopSdTimeType: this.editForm.get(['stopSdTimeType'])!.value,
      stopSdTime: this.editForm.get(['stopSdTime'])!.value,
      points: this.editForm.get(['points'])!.value,
      difPoints: this.editForm.get(['difPoints'])!.value,
      event: this.editForm.get(['event'])!.value,
    };
  }
}
