import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InvoiceItemsDetailComponent } from './invoice-items-detail.component';

describe('InvoiceItems Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvoiceItemsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InvoiceItemsDetailComponent,
              resolve: { invoiceItems: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(InvoiceItemsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load invoiceItems on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InvoiceItemsDetailComponent);

      // THEN
      expect(instance.invoiceItems).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
