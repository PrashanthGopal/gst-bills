import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrdersDetailComponent } from './orders-detail.component';

describe('Orders Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrdersDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OrdersDetailComponent,
              resolve: { orders: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OrdersDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load orders on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrdersDetailComponent);

      // THEN
      expect(instance.orders).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
