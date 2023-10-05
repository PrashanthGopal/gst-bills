import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IOrgUserRole } from '../org-user-role.model';
import { OrgUserRoleService } from '../service/org-user-role.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './org-user-role-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrgUserRoleDeleteDialogComponent {
  orgUserRole?: IOrgUserRole;

  constructor(protected orgUserRoleService: OrgUserRoleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orgUserRoleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
