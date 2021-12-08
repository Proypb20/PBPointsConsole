jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TournamentService } from '../service/tournament.service';
import { ITournament, Tournament } from '../tournament.model';

import { TournamentUpdateComponent } from './tournament-update.component';

describe('Component Tests', () => {
  describe('Tournament Management Update Component', () => {
    let comp: TournamentUpdateComponent;
    let fixture: ComponentFixture<TournamentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tournamentService: TournamentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TournamentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TournamentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TournamentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tournamentService = TestBed.inject(TournamentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tournament: ITournament = { id: 456 };

        activatedRoute.data = of({ tournament });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tournament));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tournament = { id: 123 };
        spyOn(tournamentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tournament });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tournament }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tournamentService.update).toHaveBeenCalledWith(tournament);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tournament = new Tournament();
        spyOn(tournamentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tournament });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tournament }));
        saveSubject.complete();

        // THEN
        expect(tournamentService.create).toHaveBeenCalledWith(tournament);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tournament = { id: 123 };
        spyOn(tournamentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tournament });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tournamentService.update).toHaveBeenCalledWith(tournament);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
