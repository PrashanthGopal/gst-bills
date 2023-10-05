import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductsFormService } from './products-form.service';
import { ProductsService } from '../service/products.service';
import { IProducts } from '../products.model';
import { IProductUnit } from 'app/entities/product-unit/product-unit.model';
import { ProductUnitService } from 'app/entities/product-unit/service/product-unit.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { ProductsUpdateComponent } from './products-update.component';

describe('Products Management Update Component', () => {
  let comp: ProductsUpdateComponent;
  let fixture: ComponentFixture<ProductsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productsFormService: ProductsFormService;
  let productsService: ProductsService;
  let productUnitService: ProductUnitService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProductsUpdateComponent],
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
      .overrideTemplate(ProductsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productsFormService = TestBed.inject(ProductsFormService);
    productsService = TestBed.inject(ProductsService);
    productUnitService = TestBed.inject(ProductUnitService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call productUnit query and add missing value', () => {
      const products: IProducts = { id: 456 };
      const productUnit: IProductUnit = { id: 30699 };
      products.productUnit = productUnit;

      const productUnitCollection: IProductUnit[] = [{ id: 32112 }];
      jest.spyOn(productUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: productUnitCollection })));
      const expectedCollection: IProductUnit[] = [productUnit, ...productUnitCollection];
      jest.spyOn(productUnitService, 'addProductUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ products });
      comp.ngOnInit();

      expect(productUnitService.query).toHaveBeenCalled();
      expect(productUnitService.addProductUnitToCollectionIfMissing).toHaveBeenCalledWith(productUnitCollection, productUnit);
      expect(comp.productUnitsCollection).toEqual(expectedCollection);
    });

    it('Should call Organization query and add missing value', () => {
      const products: IProducts = { id: 456 };
      const orgProducts: IOrganization = { id: 1911 };
      products.orgProducts = orgProducts;

      const organizationCollection: IOrganization[] = [{ id: 18924 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [orgProducts];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ products });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining)
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const products: IProducts = { id: 456 };
      const productUnit: IProductUnit = { id: 9255 };
      products.productUnit = productUnit;
      const orgProducts: IOrganization = { id: 25585 };
      products.orgProducts = orgProducts;

      activatedRoute.data = of({ products });
      comp.ngOnInit();

      expect(comp.productUnitsCollection).toContain(productUnit);
      expect(comp.organizationsSharedCollection).toContain(orgProducts);
      expect(comp.products).toEqual(products);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProducts>>();
      const products = { id: 123 };
      jest.spyOn(productsFormService, 'getProducts').mockReturnValue(products);
      jest.spyOn(productsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ products });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: products }));
      saveSubject.complete();

      // THEN
      expect(productsFormService.getProducts).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productsService.update).toHaveBeenCalledWith(expect.objectContaining(products));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProducts>>();
      const products = { id: 123 };
      jest.spyOn(productsFormService, 'getProducts').mockReturnValue({ id: null });
      jest.spyOn(productsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ products: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: products }));
      saveSubject.complete();

      // THEN
      expect(productsFormService.getProducts).toHaveBeenCalled();
      expect(productsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProducts>>();
      const products = { id: 123 };
      jest.spyOn(productsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ products });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductUnit', () => {
      it('Should forward to productUnitService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productUnitService, 'compareProductUnit');
        comp.compareProductUnit(entity, entity2);
        expect(productUnitService.compareProductUnit).toHaveBeenCalledWith(entity, entity2);
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
