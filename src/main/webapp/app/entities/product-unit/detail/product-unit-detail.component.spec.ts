import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProductUnitDetailComponent } from './product-unit-detail.component';

describe('ProductUnit Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductUnitDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProductUnitDetailComponent,
              resolve: { productUnit: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ProductUnitDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load productUnit on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductUnitDetailComponent);

      // THEN
      expect(instance.productUnit).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
