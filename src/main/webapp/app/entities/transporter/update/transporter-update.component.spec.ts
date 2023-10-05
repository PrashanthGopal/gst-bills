import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransporterFormService } from './transporter-form.service';
import { TransporterService } from '../service/transporter.service';
import { ITransporter } from '../transporter.model';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';

import { TransporterUpdateComponent } from './transporter-update.component';

describe('Transporter Management Update Component', () => {
  let comp: TransporterUpdateComponent;
  let fixture: ComponentFixture<TransporterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transporterFormService: TransporterFormService;
  let transporterService: TransporterService;
  let addressService: AddressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TransporterUpdateComponent],
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
      .overrideTemplate(TransporterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransporterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transporterFormService = TestBed.inject(TransporterFormService);
    transporterService = TestBed.inject(TransporterService);
    addressService = TestBed.inject(AddressService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call address query and add missing value', () => {
      const transporter: ITransporter = { id: 456 };
      const address: IAddress = { id: 8172 };
      transporter.address = address;

      const addressCollection: IAddress[] = [{ id: 23474 }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: IAddress[] = [address, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transporter });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transporter: ITransporter = { id: 456 };
      const address: IAddress = { id: 2408 };
      transporter.address = address;

      activatedRoute.data = of({ transporter });
      comp.ngOnInit();

      expect(comp.addressesCollection).toContain(address);
      expect(comp.transporter).toEqual(transporter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransporter>>();
      const transporter = { id: 123 };
      jest.spyOn(transporterFormService, 'getTransporter').mockReturnValue(transporter);
      jest.spyOn(transporterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transporter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transporter }));
      saveSubject.complete();

      // THEN
      expect(transporterFormService.getTransporter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transporterService.update).toHaveBeenCalledWith(expect.objectContaining(transporter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransporter>>();
      const transporter = { id: 123 };
      jest.spyOn(transporterFormService, 'getTransporter').mockReturnValue({ id: null });
      jest.spyOn(transporterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transporter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transporter }));
      saveSubject.complete();

      // THEN
      expect(transporterFormService.getTransporter).toHaveBeenCalled();
      expect(transporterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransporter>>();
      const transporter = { id: 123 };
      jest.spyOn(transporterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transporter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transporterService.update).toHaveBeenCalled();
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
