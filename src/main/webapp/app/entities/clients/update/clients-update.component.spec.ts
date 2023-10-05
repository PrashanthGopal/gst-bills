import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClientsFormService } from './clients-form.service';
import { ClientsService } from '../service/clients.service';
import { IClients } from '../clients.model';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { ClientsUpdateComponent } from './clients-update.component';

describe('Clients Management Update Component', () => {
  let comp: ClientsUpdateComponent;
  let fixture: ComponentFixture<ClientsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clientsFormService: ClientsFormService;
  let clientsService: ClientsService;
  let addressService: AddressService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ClientsUpdateComponent],
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
      .overrideTemplate(ClientsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClientsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clientsFormService = TestBed.inject(ClientsFormService);
    clientsService = TestBed.inject(ClientsService);
    addressService = TestBed.inject(AddressService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call address query and add missing value', () => {
      const clients: IClients = { id: 456 };
      const address: IAddress = { id: 8545 };
      clients.address = address;

      const addressCollection: IAddress[] = [{ id: 1993 }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: IAddress[] = [address, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should call Organization query and add missing value', () => {
      const clients: IClients = { id: 456 };
      const orgClients: IOrganization = { id: 2709 };
      clients.orgClients = orgClients;

      const organizationCollection: IOrganization[] = [{ id: 20362 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [orgClients];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining)
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const clients: IClients = { id: 456 };
      const address: IAddress = { id: 29356 };
      clients.address = address;
      const orgClients: IOrganization = { id: 223 };
      clients.orgClients = orgClients;

      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      expect(comp.addressesCollection).toContain(address);
      expect(comp.organizationsSharedCollection).toContain(orgClients);
      expect(comp.clients).toEqual(clients);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClients>>();
      const clients = { id: 123 };
      jest.spyOn(clientsFormService, 'getClients').mockReturnValue(clients);
      jest.spyOn(clientsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clients }));
      saveSubject.complete();

      // THEN
      expect(clientsFormService.getClients).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clientsService.update).toHaveBeenCalledWith(expect.objectContaining(clients));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClients>>();
      const clients = { id: 123 };
      jest.spyOn(clientsFormService, 'getClients').mockReturnValue({ id: null });
      jest.spyOn(clientsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clients: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clients }));
      saveSubject.complete();

      // THEN
      expect(clientsFormService.getClients).toHaveBeenCalled();
      expect(clientsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClients>>();
      const clients = { id: 123 };
      jest.spyOn(clientsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clients });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clientsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
  });
});
