import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrgUserRoleDetailComponent } from './org-user-role-detail.component';

describe('OrgUserRole Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrgUserRoleDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OrgUserRoleDetailComponent,
              resolve: { orgUserRole: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OrgUserRoleDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load orgUserRole on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrgUserRoleDetailComponent);

      // THEN
      expect(instance.orgUserRole).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
