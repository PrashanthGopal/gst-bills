import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StateCodeService } from '../service/state-code.service';

import { StateCodeComponent } from './state-code.component';

describe('StateCode Management Component', () => {
  let comp: StateCodeComponent;
  let fixture: ComponentFixture<StateCodeComponent>;
  let service: StateCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'state-code', component: StateCodeComponent }]),
        HttpClientTestingModule,
        StateCodeComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(StateCodeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StateCodeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StateCodeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
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
    expect(comp.stateCodes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to stateCodeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getStateCodeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getStateCodeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
