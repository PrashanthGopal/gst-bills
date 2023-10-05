import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrganizationFormService } from './organization-form.service';
import { OrganizationService } from '../service/organization.service';
import { IOrganization } from '../organization.model';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';

import { OrganizationUpdateComponent } from './organization-update.component';

describe('Organization Management Update Component', () => {
  let comp: OrganizationUpdateComponent;
  let fixture: ComponentFixture<OrganizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organizationFormService: OrganizationFormService;
  let organizationService: OrganizationService;
  let addressService: AddressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrganizationUpdateComponent],
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
      .overrideTemplate(OrganizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organizationFormService = TestBed.inject(OrganizationFormService);
    organizationService = TestBed.inject(OrganizationService);
    addressService = TestBed.inject(AddressService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call address query and add missing value', () => {
      const organization: IOrganization = { id: 456 };
      const address: IAddress = { id: 19745 };
      organization.address = address;

      const addressCollection: IAddress[] = [{ id: 11915 }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: IAddress[] = [address, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const organization: IOrganization = { id: 456 };
      const address: IAddress = { id: 12629 };
      organization.address = address;

      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      expect(comp.addressesCollection).toContain(address);
      expect(comp.organization).toEqual(organization);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganization>>();
      const organization = { id: 123 };
      jest.spyOn(organizationFormService, 'getOrganization').mockReturnValue(organization);
      jest.spyOn(organizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organization }));
      saveSubject.complete();

      // THEN
      expect(organizationFormService.getOrganization).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organizationService.update).toHaveBeenCalledWith(expect.objectContaining(organization));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganization>>();
      const organization = { id: 123 };
      jest.spyOn(organizationFormService, 'getOrganization').mockReturnValue({ id: null });
      jest.spyOn(organizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organization: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organization }));
      saveSubject.complete();

      // THEN
      expect(organizationFormService.getOrganization).toHaveBeenCalled();
      expect(organizationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganization>>();
      const organization = { id: 123 };
      jest.spyOn(organizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organizationService.update).toHaveBeenCalled();
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
  });
});
