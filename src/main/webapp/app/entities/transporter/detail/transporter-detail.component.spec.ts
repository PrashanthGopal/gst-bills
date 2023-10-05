import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TransporterDetailComponent } from './transporter-detail.component';

describe('Transporter Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransporterDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TransporterDetailComponent,
              resolve: { transporter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(TransporterDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load transporter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TransporterDetailComponent);

      // THEN
      expect(instance.transporter).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
