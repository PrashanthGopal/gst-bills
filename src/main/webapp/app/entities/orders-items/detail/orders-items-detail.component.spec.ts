import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrdersItemsDetailComponent } from './orders-items-detail.component';

describe('OrdersItems Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrdersItemsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OrdersItemsDetailComponent,
              resolve: { ordersItems: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OrdersItemsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ordersItems on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrdersItemsDetailComponent);

      // THEN
      expect(instance.ordersItems).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
