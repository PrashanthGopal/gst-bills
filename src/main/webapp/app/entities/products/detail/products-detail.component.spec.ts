import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProductsDetailComponent } from './products-detail.component';

describe('Products Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProductsDetailComponent,
              resolve: { products: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ProductsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load products on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductsDetailComponent);

      // THEN
      expect(instance.products).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
