import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IProductUnit } from '../product-unit.model';
import { ProductUnitService } from '../service/product-unit.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './product-unit-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductUnitDeleteDialogComponent {
  productUnit?: IProductUnit;

  constructor(protected productUnitService: ProductUnitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productUnitService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
