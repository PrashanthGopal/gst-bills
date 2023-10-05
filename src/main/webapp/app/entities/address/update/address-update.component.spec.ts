import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AddressFormService } from './address-form.service';
import { AddressService } from '../service/address.service';
import { IAddress } from '../address.model';
import { IStateCode } from 'app/entities/state-code/state-code.model';
import { StateCodeService } from 'app/entities/state-code/service/state-code.service';

import { AddressUpdateComponent } from './address-update.component';

describe('Address Management Update Component', () => {
  let comp: AddressUpdateComponent;
  let fixture: ComponentFixture<AddressUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let addressFormService: AddressFormService;
  let addressService: AddressService;
  let stateCodeService: StateCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AddressUpdateComponent],
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
      .overrideTemplate(AddressUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AddressUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    addressFormService = TestBed.inject(AddressFormService);
    addressService = TestBed.inject(AddressService);
    stateCodeService = TestBed.inject(StateCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call stateCode query and add missing value', () => {
      const address: IAddress = { id: 456 };
      const stateCode: IStateCode = { id: 19473 };
      address.stateCode = stateCode;

      const stateCodeCollection: IStateCode[] = [{ id: 27167 }];
      jest.spyOn(stateCodeService, 'query').mockReturnValue(of(new HttpResponse({ body: stateCodeCollection })));
      const expectedCollection: IStateCode[] = [stateCode, ...stateCodeCollection];
      jest.spyOn(stateCodeService, 'addStateCodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(stateCodeService.query).toHaveBeenCalled();
      expect(stateCodeService.addStateCodeToCollectionIfMissing).toHaveBeenCalledWith(stateCodeCollection, stateCode);
      expect(comp.stateCodesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const address: IAddress = { id: 456 };
      const stateCode: IStateCode = { id: 13480 };
      address.stateCode = stateCode;

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(comp.stateCodesCollection).toContain(stateCode);
      expect(comp.address).toEqual(address);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: 123 };
      jest.spyOn(addressFormService, 'getAddress').mockReturnValue(address);
      jest.spyOn(addressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: address }));
      saveSubject.complete();

      // THEN
      expect(addressFormService.getAddress).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(addressService.update).toHaveBeenCalledWith(expect.objectContaining(address));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: 123 };
      jest.spyOn(addressFormService, 'getAddress').mockReturnValue({ id: null });
      jest.spyOn(addressService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: address }));
      saveSubject.complete();

      // THEN
      expect(addressFormService.getAddress).toHaveBeenCalled();
      expect(addressService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: 123 };
      jest.spyOn(addressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(addressService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStateCode', () => {
      it('Should forward to stateCodeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(stateCodeService, 'compareStateCode');
        comp.compareStateCode(entity, entity2);
        expect(stateCodeService.compareStateCode).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
