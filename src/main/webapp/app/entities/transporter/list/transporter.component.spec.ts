import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TransporterService } from '../service/transporter.service';

import { TransporterComponent } from './transporter.component';

describe('Transporter Management Component', () => {
  let comp: TransporterComponent;
  let fixture: ComponentFixture<TransporterComponent>;
  let service: TransporterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'transporter', component: TransporterComponent }]),
        HttpClientTestingModule,
        TransporterComponent,
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
      .overrideTemplate(TransporterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransporterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TransporterService);

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
    expect(comp.transporters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to transporterService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTransporterIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTransporterIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
