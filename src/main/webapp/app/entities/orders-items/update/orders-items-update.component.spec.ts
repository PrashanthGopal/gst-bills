import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrdersItemsFormService } from './orders-items-form.service';
import { OrdersItemsService } from '../service/orders-items.service';
import { IOrdersItems } from '../orders-items.model';
import { IProducts } from 'app/entities/products/products.model';
import { ProductsService } from 'app/entities/products/service/products.service';
import { IOrders } from 'app/entities/orders/orders.model';
import { OrdersService } from 'app/entities/orders/service/orders.service';

import { OrdersItemsUpdateComponent } from './orders-items-update.component';

describe('OrdersItems Management Update Component', () => {
  let comp: OrdersItemsUpdateComponent;
  let fixture: ComponentFixture<OrdersItemsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ordersItemsFormService: OrdersItemsFormService;
  let ordersItemsService: OrdersItemsService;
  let productsService: ProductsService;
  let ordersService: OrdersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrdersItemsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrdersItemsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdersItemsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordersItemsFormService = TestBed.inject(OrdersItemsFormService);
    ordersItemsService = TestBed.inject(OrdersItemsService);
    productsService = TestBed.inject(ProductsService);
    ordersService = TestBed.inject(OrdersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call products query and add missing value', () => {
      const ordersItems: IOrdersItems = { id: 456 };
      const products: IProducts = { id: 29713 };
      ordersItems.products = products;

      const productsCollection: IProducts[] = [{ id: 26716 }];
      jest.spyOn(productsService, 'query').mockReturnValue(of(new HttpResponse({ body: productsCollection })));
      const expectedCollection: IProducts[] = [products, ...productsCollection];
      jest.spyOn(productsService, 'addProductsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ordersItems });
      comp.ngOnInit();

      expect(productsService.query).toHaveBeenCalled();
      expect(productsService.addProductsToCollectionIfMissing).toHaveBeenCalledWith(productsCollection, products);
      expect(comp.productsCollection).toEqual(expectedCollection);
    });

    it('Should call Orders query and add missing value', () => {
      const ordersItems: IOrdersItems = { id: 456 };
      const orders: IOrders = { id: 416 };
      ordersItems.orders = orders;

      const ordersCollection: IOrders[] = [{ id: 11019 }];
      jest.spyOn(ordersService, 'query').mockReturnValue(of(new HttpResponse({ body: ordersCollection })));
      const additionalOrders = [orders];
      const expectedCollection: IOrders[] = [...additionalOrders, ...ordersCollection];
      jest.spyOn(ordersService, 'addOrdersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ordersItems });
      comp.ngOnInit();

      expect(ordersService.query).toHaveBeenCalled();
      expect(ordersService.addOrdersToCollectionIfMissing).toHaveBeenCalledWith(
        ordersCollection,
        ...additionalOrders.map(expect.objectContaining)
      );
      expect(comp.ordersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ordersItems: IOrdersItems = { id: 456 };
      const products: IProducts = { id: 32229 };
      ordersItems.products = products;
      const orders: IOrders = { id: 16528 };
      ordersItems.orders = orders;

      activatedRoute.data = of({ ordersItems });
      comp.ngOnInit();

      expect(comp.productsCollection).toContain(products);
      expect(comp.ordersSharedCollection).toContain(orders);
      expect(comp.ordersItems).toEqual(ordersItems);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrdersItems>>();
      const ordersItems = { id: 123 };
      jest.spyOn(ordersItemsFormService, 'getOrdersItems').mockReturnValue(ordersItems);
      jest.spyOn(ordersItemsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordersItems });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ordersItems }));
      saveSubject.complete();

      // THEN
      expect(ordersItemsFormService.getOrdersItems).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordersItemsService.update).toHaveBeenCalledWith(expect.objectContaining(ordersItems));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrdersItems>>();
      const ordersItems = { id: 123 };
      jest.spyOn(ordersItemsFormService, 'getOrdersItems').mockReturnValue({ id: null });
      jest.spyOn(ordersItemsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordersItems: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ordersItems }));
      saveSubject.complete();

      // THEN
      expect(ordersItemsFormService.getOrdersItems).toHaveBeenCalled();
      expect(ordersItemsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrdersItems>>();
      const ordersItems = { id: 123 };
      jest.spyOn(ordersItemsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordersItems });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordersItemsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProducts', () => {
      it('Should forward to productsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productsService, 'compareProducts');
        comp.compareProducts(entity, entity2);
        expect(productsService.compareProducts).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrders', () => {
      it('Should forward to ordersService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ordersService, 'compareOrders');
        comp.compareOrders(entity, entity2);
        expect(ordersService.compareOrders).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
