import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IOrganization } from '../organization.model';
import { OrganizationService } from '../service/organization.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './organization-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrganizationDeleteDialogComponent {
  organization?: IOrganization;

  constructor(protected organizationService: OrganizationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
