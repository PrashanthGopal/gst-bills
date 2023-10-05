import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrdersItemsService } from '../service/orders-items.service';

import { OrdersItemsComponent } from './orders-items.component';

describe('OrdersItems Management Component', () => {
  let comp: OrdersItemsComponent;
  let fixture: ComponentFixture<OrdersItemsComponent>;
  let service: OrdersItemsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'orders-items', component: OrdersItemsComponent }]),
        HttpClientTestingModule,
        OrdersItemsComponent,
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
      .overrideTemplate(OrdersItemsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdersItemsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrdersItemsService);

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
    expect(comp.ordersItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ordersItemsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOrdersItemsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOrdersItemsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
