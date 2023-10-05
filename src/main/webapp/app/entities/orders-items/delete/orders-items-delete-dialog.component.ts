import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IOrdersItems } from '../orders-items.model';
import { OrdersItemsService } from '../service/orders-items.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './orders-items-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrdersItemsDeleteDialogComponent {
  ordersItems?: IOrdersItems;

  constructor(protected ordersItemsService: OrdersItemsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordersItemsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
