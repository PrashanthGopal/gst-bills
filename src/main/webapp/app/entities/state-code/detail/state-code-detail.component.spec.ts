import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StateCodeDetailComponent } from './state-code-detail.component';

describe('StateCode Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StateCodeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StateCodeDetailComponent,
              resolve: { stateCode: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(StateCodeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load stateCode on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StateCodeDetailComponent);

      // THEN
      expect(instance.stateCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
