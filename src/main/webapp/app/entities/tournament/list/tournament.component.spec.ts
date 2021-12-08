import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TournamentService } from '../service/tournament.service';

import { TournamentComponent } from './tournament.component';

describe('Component Tests', () => {
  describe('Tournament Management Component', () => {
    let comp: TournamentComponent;
    let fixture: ComponentFixture<TournamentComponent>;
    let service: TournamentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TournamentComponent],
      })
        .overrideTemplate(TournamentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TournamentComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TournamentService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.tournaments?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
