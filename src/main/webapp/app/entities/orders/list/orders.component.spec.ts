import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrdersService } from '../service/orders.service';

import { OrdersComponent } from './orders.component';

describe('Orders Management Component', () => {
  let comp: OrdersComponent;
  let fixture: ComponentFixture<OrdersComponent>;
  let service: OrdersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'orders', component: OrdersComponent }]), HttpClientTestingModule, OrdersComponent],
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
      .overrideTemplate(OrdersComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdersComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrdersService);

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
    expect(comp.orders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ordersService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOrdersIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOrdersIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
