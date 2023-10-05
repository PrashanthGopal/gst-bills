import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrdersFormService } from './orders-form.service';
import { OrdersService } from '../service/orders.service';
import { IOrders } from '../orders.model';
import { IClients } from 'app/entities/clients/clients.model';
import { ClientsService } from 'app/entities/clients/service/clients.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { OrdersUpdateComponent } from './orders-update.component';

describe('Orders Management Update Component', () => {
  let comp: OrdersUpdateComponent;
  let fixture: ComponentFixture<OrdersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ordersFormService: OrdersFormService;
  let ordersService: OrdersService;
  let clientsService: ClientsService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrdersUpdateComponent],
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
      .overrideTemplate(OrdersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordersFormService = TestBed.inject(OrdersFormService);
    ordersService = TestBed.inject(OrdersService);
    clientsService = TestBed.inject(ClientsService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call clients query and add missing value', () => {
      const orders: IOrders = { id: 456 };
      const clients: IClients = { id: 10596 };
      orders.clients = clients;

      const clientsCollection: IClients[] = [{ id: 591 }];
      jest.spyOn(clientsService, 'query').mockReturnValue(of(new HttpResponse({ body: clientsCollection })));
      const expectedCollection: IClients[] = [clients, ...clientsCollection];
      jest.spyOn(clientsService, 'addClientsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      expect(clientsService.query).toHaveBeenCalled();
      expect(clientsService.addClientsToCollectionIfMissing).toHaveBeenCalledWith(clientsCollection, clients);
      expect(comp.clientsCollection).toEqual(expectedCollection);
    });

    it('Should call Organization query and add missing value', () => {
      const orders: IOrders = { id: 456 };
      const orgOrders: IOrganization = { id: 24545 };
      orders.orgOrders = orgOrders;

      const organizationCollection: IOrganization[] = [{ id: 17784 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [orgOrders];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining)
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orders: IOrders = { id: 456 };
      const clients: IClients = { id: 12717 };
      orders.clients = clients;
      const orgOrders: IOrganization = { id: 8831 };
      orders.orgOrders = orgOrders;

      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      expect(comp.clientsCollection).toContain(clients);
      expect(comp.organizationsSharedCollection).toContain(orgOrders);
      expect(comp.orders).toEqual(orders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrders>>();
      const orders = { id: 123 };
      jest.spyOn(ordersFormService, 'getOrders').mockReturnValue(orders);
      jest.spyOn(ordersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orders }));
      saveSubject.complete();

      // THEN
      expect(ordersFormService.getOrders).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordersService.update).toHaveBeenCalledWith(expect.objectContaining(orders));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrders>>();
      const orders = { id: 123 };
      jest.spyOn(ordersFormService, 'getOrders').mockReturnValue({ id: null });
      jest.spyOn(ordersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orders: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orders }));
      saveSubject.complete();

      // THEN
      expect(ordersFormService.getOrders).toHaveBeenCalled();
      expect(ordersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrders>>();
      const orders = { id: 123 };
      jest.spyOn(ordersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordersService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClients', () => {
      it('Should forward to clientsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientsService, 'compareClients');
        comp.compareClients(entity, entity2);
        expect(clientsService.compareClients).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrganization', () => {
      it('Should forward to organizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(organizationService, 'compareOrganization');
        comp.compareOrganization(entity, entity2);
        expect(organizationService.compareOrganization).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
