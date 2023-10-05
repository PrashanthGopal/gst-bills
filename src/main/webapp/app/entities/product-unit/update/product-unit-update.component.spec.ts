import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductUnitFormService } from './product-unit-form.service';
import { ProductUnitService } from '../service/product-unit.service';
import { IProductUnit } from '../product-unit.model';

import { ProductUnitUpdateComponent } from './product-unit-update.component';

describe('ProductUnit Management Update Component', () => {
  let comp: ProductUnitUpdateComponent;
  let fixture: ComponentFixture<ProductUnitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productUnitFormService: ProductUnitFormService;
  let productUnitService: ProductUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProductUnitUpdateComponent],
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
      .overrideTemplate(ProductUnitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUnitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productUnitFormService = TestBed.inject(ProductUnitFormService);
    productUnitService = TestBed.inject(ProductUnitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const productUnit: IProductUnit = { id: 456 };

      activatedRoute.data = of({ productUnit });
      comp.ngOnInit();

      expect(comp.productUnit).toEqual(productUnit);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductUnit>>();
      const productUnit = { id: 123 };
      jest.spyOn(productUnitFormService, 'getProductUnit').mockReturnValue(productUnit);
      jest.spyOn(productUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productUnit }));
      saveSubject.complete();

      // THEN
      expect(productUnitFormService.getProductUnit).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productUnitService.update).toHaveBeenCalledWith(expect.objectContaining(productUnit));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductUnit>>();
      const productUnit = { id: 123 };
      jest.spyOn(productUnitFormService, 'getProductUnit').mockReturnValue({ id: null });
      jest.spyOn(productUnitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productUnit: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productUnit }));
      saveSubject.complete();

      // THEN
      expect(productUnitFormService.getProductUnit).toHaveBeenCalled();
      expect(productUnitService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductUnit>>();
      const productUnit = { id: 123 };
      jest.spyOn(productUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productUnitService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
