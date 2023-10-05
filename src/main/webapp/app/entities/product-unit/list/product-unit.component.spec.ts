import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProductUnitService } from '../service/product-unit.service';

import { ProductUnitComponent } from './product-unit.component';

describe('ProductUnit Management Component', () => {
  let comp: ProductUnitComponent;
  let fixture: ComponentFixture<ProductUnitComponent>;
  let service: ProductUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'product-unit', component: ProductUnitComponent }]),
        HttpClientTestingModule,
        ProductUnitComponent,
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
      .overrideTemplate(ProductUnitComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUnitComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProductUnitService);

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
    expect(comp.productUnits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to productUnitService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getProductUnitIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getProductUnitIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
