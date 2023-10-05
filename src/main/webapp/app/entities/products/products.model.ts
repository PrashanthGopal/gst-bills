import { IProductUnit } from 'app/entities/product-unit/product-unit.model';
import { IOrganization } from 'app/entities/organization/organization.model';

export interface IProducts {
  id: number;
  productId?: string | null;
  name?: string | null;
  description?: string | null;
  productHsnCode?: string | null;
  productTaxRate?: string | null;
  unitCodeId?: string | null;
  costPerQty?: number | null;
  cgst?: string | null;
  sgst?: string | null;
  igst?: string | null;
  totalTaxRate?: string | null;
  productUnit?: Pick<IProductUnit, 'id'> | null;
  orgProducts?: Pick<IOrganization, 'id'> | null;
}

export type NewProducts = Omit<IProducts, 'id'> & { id: null };
