import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITransporter } from '../transporter.model';
import { TransporterService } from '../service/transporter.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './transporter-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TransporterDeleteDialogComponent {
  transporter?: ITransporter;

  constructor(protected transporterService: TransporterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transporterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
