import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClientsDetailComponent } from './clients-detail.component';

describe('Clients Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ClientsDetailComponent,
              resolve: { clients: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ClientsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load clients on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClientsDetailComponent);

      // THEN
      expect(instance.clients).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
