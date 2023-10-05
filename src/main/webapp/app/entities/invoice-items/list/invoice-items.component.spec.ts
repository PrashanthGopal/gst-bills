import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InvoiceItemsService } from '../service/invoice-items.service';

import { InvoiceItemsComponent } from './invoice-items.component';

describe('InvoiceItems Management Component', () => {
  let comp: InvoiceItemsComponent;
  let fixture: ComponentFixture<InvoiceItemsComponent>;
  let service: InvoiceItemsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'invoice-items', component: InvoiceItemsComponent }]),
        HttpClientTestingModule,
        InvoiceItemsComponent,
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
      .overrideTemplate(InvoiceItemsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceItemsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InvoiceItemsService);

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
    expect(comp.invoiceItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to invoiceItemsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getInvoiceItemsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getInvoiceItemsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
