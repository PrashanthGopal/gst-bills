import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IOrgUsers } from '../org-users.model';
import { OrgUsersService } from '../service/org-users.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './org-users-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrgUsersDeleteDialogComponent {
  orgUsers?: IOrgUsers;

  constructor(protected orgUsersService: OrgUsersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orgUsersService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
