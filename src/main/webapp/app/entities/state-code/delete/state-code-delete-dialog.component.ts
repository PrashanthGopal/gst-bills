import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IStateCode } from '../state-code.model';
import { StateCodeService } from '../service/state-code.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './state-code-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StateCodeDeleteDialogComponent {
  stateCode?: IStateCode;

  constructor(protected stateCodeService: StateCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stateCodeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
