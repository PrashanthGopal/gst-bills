import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOrgUserRole } from '../org-user-role.model';

@Component({
  standalone: true,
  selector: 'jhi-org-user-role-detail',
  templateUrl: './org-user-role-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrgUserRoleDetailComponent {
  @Input() orgUserRole: IOrgUserRole | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
