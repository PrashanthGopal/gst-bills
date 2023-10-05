import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IInvoiceItems } from '../invoice-items.model';
import { InvoiceItemsService } from '../service/invoice-items.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './invoice-items-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InvoiceItemsDeleteDialogComponent {
  invoiceItems?: IInvoiceItems;

  constructor(protected invoiceItemsService: InvoiceItemsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceItemsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
