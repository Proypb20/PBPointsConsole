import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GameService } from '../service/game.service';

import { GameComponent } from './game.component';

describe('Component Tests', () => {
  describe('Game Management Component', () => {
    let comp: GameComponent;
    let fixture: ComponentFixture<GameComponent>;
    let service: GameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GameComponent],
      })
        .overrideTemplate(GameComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GameComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(GameService);

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
      expect(comp.games?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
