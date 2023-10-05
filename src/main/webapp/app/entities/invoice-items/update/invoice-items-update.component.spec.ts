import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvoiceItemsFormService } from './invoice-items-form.service';
import { InvoiceItemsService } from '../service/invoice-items.service';
import { IInvoiceItems } from '../invoice-items.model';
import { IProducts } from 'app/entities/products/products.model';
import { ProductsService } from 'app/entities/products/service/products.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';

import { InvoiceItemsUpdateComponent } from './invoice-items-update.component';

describe('InvoiceItems Management Update Component', () => {
  let comp: InvoiceItemsUpdateComponent;
  let fixture: ComponentFixture<InvoiceItemsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceItemsFormService: InvoiceItemsFormService;
  let invoiceItemsService: InvoiceItemsService;
  let productsService: ProductsService;
  let invoiceService: InvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InvoiceItemsUpdateComponent],
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
      .overrideTemplate(InvoiceItemsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceItemsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceItemsFormService = TestBed.inject(InvoiceItemsFormService);
    invoiceItemsService = TestBed.inject(InvoiceItemsService);
    productsService = TestBed.inject(ProductsService);
    invoiceService = TestBed.inject(InvoiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call products query and add missing value', () => {
      const invoiceItems: IInvoiceItems = { id: 456 };
      const products: IProducts = { id: 31399 };
      invoiceItems.products = products;

      const productsCollection: IProducts[] = [{ id: 14316 }];
      jest.spyOn(productsService, 'query').mockReturnValue(of(new HttpResponse({ body: productsCollection })));
      const expectedCollection: IProducts[] = [products, ...productsCollection];
      jest.spyOn(productsService, 'addProductsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItems });
      comp.ngOnInit();

      expect(productsService.query).toHaveBeenCalled();
      expect(productsService.addProductsToCollectionIfMissing).toHaveBeenCalledWith(productsCollection, products);
      expect(comp.productsCollection).toEqual(expectedCollection);
    });

    it('Should call Invoice query and add missing value', () => {
      const invoiceItems: IInvoiceItems = { id: 456 };
      const invoice: IInvoice = { id: 5692 };
      invoiceItems.invoice = invoice;

      const invoiceCollection: IInvoice[] = [{ id: 1897 }];
      jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItems });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(expect.objectContaining)
      );
      expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoiceItems: IInvoiceItems = { id: 456 };
      const products: IProducts = { id: 10425 };
      invoiceItems.products = products;
      const invoice: IInvoice = { id: 12125 };
      invoiceItems.invoice = invoice;

      activatedRoute.data = of({ invoiceItems });
      comp.ngOnInit();

      expect(comp.productsCollection).toContain(products);
      expect(comp.invoicesSharedCollection).toContain(invoice);
      expect(comp.invoiceItems).toEqual(invoiceItems);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceItems>>();
      const invoiceItems = { id: 123 };
      jest.spyOn(invoiceItemsFormService, 'getInvoiceItems').mockReturnValue(invoiceItems);
      jest.spyOn(invoiceItemsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItems });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceItems }));
      saveSubject.complete();

      // THEN
      expect(invoiceItemsFormService.getInvoiceItems).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceItemsService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceItems));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceItems>>();
      const invoiceItems = { id: 123 };
      jest.spyOn(invoiceItemsFormService, 'getInvoiceItems').mockReturnValue({ id: null });
      jest.spyOn(invoiceItemsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItems: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceItems }));
      saveSubject.complete();

      // THEN
      expect(invoiceItemsFormService.getInvoiceItems).toHaveBeenCalled();
      expect(invoiceItemsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceItems>>();
      const invoiceItems = { id: 123 };
      jest.spyOn(invoiceItemsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItems });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceItemsService.update).toHaveBeenCalled();
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

    describe('compareInvoice', () => {
      it('Should forward to invoiceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
