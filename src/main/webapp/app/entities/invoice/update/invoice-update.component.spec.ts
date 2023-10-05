import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvoiceFormService } from './invoice-form.service';
import { InvoiceService } from '../service/invoice.service';
import { IInvoice } from '../invoice.model';
import { ITransporter } from 'app/entities/transporter/transporter.model';
import { TransporterService } from 'app/entities/transporter/service/transporter.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { IClients } from 'app/entities/clients/clients.model';
import { ClientsService } from 'app/entities/clients/service/clients.service';

import { InvoiceUpdateComponent } from './invoice-update.component';

describe('Invoice Management Update Component', () => {
  let comp: InvoiceUpdateComponent;
  let fixture: ComponentFixture<InvoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceFormService: InvoiceFormService;
  let invoiceService: InvoiceService;
  let transporterService: TransporterService;
  let addressService: AddressService;
  let organizationService: OrganizationService;
  let clientsService: ClientsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InvoiceUpdateComponent],
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
      .overrideTemplate(InvoiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceFormService = TestBed.inject(InvoiceFormService);
    invoiceService = TestBed.inject(InvoiceService);
    transporterService = TestBed.inject(TransporterService);
    addressService = TestBed.inject(AddressService);
    organizationService = TestBed.inject(OrganizationService);
    clientsService = TestBed.inject(ClientsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call transporter query and add missing value', () => {
      const invoice: IInvoice = { id: 456 };
      const transporter: ITransporter = { id: 965 };
      invoice.transporter = transporter;

      const transporterCollection: ITransporter[] = [{ id: 1880 }];
      jest.spyOn(transporterService, 'query').mockReturnValue(of(new HttpResponse({ body: transporterCollection })));
      const expectedCollection: ITransporter[] = [transporter, ...transporterCollection];
      jest.spyOn(transporterService, 'addTransporterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(transporterService.query).toHaveBeenCalled();
      expect(transporterService.addTransporterToCollectionIfMissing).toHaveBeenCalledWith(transporterCollection, transporter);
      expect(comp.transportersCollection).toEqual(expectedCollection);
    });

    it('Should call address query and add missing value', () => {
      const invoice: IInvoice = { id: 456 };
      const address: IAddress = { id: 31240 };
      invoice.address = address;

      const addressCollection: IAddress[] = [{ id: 3029 }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: IAddress[] = [address, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should call Organization query and add missing value', () => {
      const invoice: IInvoice = { id: 456 };
      const orgInvoices: IOrganization = { id: 10604 };
      invoice.orgInvoices = orgInvoices;

      const organizationCollection: IOrganization[] = [{ id: 13282 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [orgInvoices];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining)
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Clients query and add missing value', () => {
      const invoice: IInvoice = { id: 456 };
      const clients: IClients = { id: 26868 };
      invoice.clients = clients;

      const clientsCollection: IClients[] = [{ id: 11340 }];
      jest.spyOn(clientsService, 'query').mockReturnValue(of(new HttpResponse({ body: clientsCollection })));
      const additionalClients = [clients];
      const expectedCollection: IClients[] = [...additionalClients, ...clientsCollection];
      jest.spyOn(clientsService, 'addClientsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(clientsService.query).toHaveBeenCalled();
      expect(clientsService.addClientsToCollectionIfMissing).toHaveBeenCalledWith(
        clientsCollection,
        ...additionalClients.map(expect.objectContaining)
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoice: IInvoice = { id: 456 };
      const transporter: ITransporter = { id: 6768 };
      invoice.transporter = transporter;
      const address: IAddress = { id: 7046 };
      invoice.address = address;
      const orgInvoices: IOrganization = { id: 13230 };
      invoice.orgInvoices = orgInvoices;
      const clients: IClients = { id: 4345 };
      invoice.clients = clients;

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(comp.transportersCollection).toContain(transporter);
      expect(comp.addressesCollection).toContain(address);
      expect(comp.organizationsSharedCollection).toContain(orgInvoices);
      expect(comp.clientsSharedCollection).toContain(clients);
      expect(comp.invoice).toEqual(invoice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 123 };
      jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue(invoice);
      jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoice }));
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceService.update).toHaveBeenCalledWith(expect.objectContaining(invoice));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 123 };
      jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue({ id: null });
      jest.spyOn(invoiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoice }));
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(invoiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 123 };
      jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransporter', () => {
      it('Should forward to transporterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transporterService, 'compareTransporter');
        comp.compareTransporter(entity, entity2);
        expect(transporterService.compareTransporter).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAddress', () => {
      it('Should forward to addressService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(addressService, 'compareAddress');
        comp.compareAddress(entity, entity2);
        expect(addressService.compareAddress).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareClients', () => {
      it('Should forward to clientsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientsService, 'compareClients');
        comp.compareClients(entity, entity2);
        expect(clientsService.compareClients).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
