import { IProducts } from 'app/entities/products/products.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface IInvoiceItems {
  id: number;
  orgId?: string | null;
  invoiceItemsId?: string | null;
  quantity?: number | null;
  cgst?: string | null;
  sgst?: string | null;
  gst?: string | null;
  totalAmount?: number | null;
  products?: Pick<IProducts, 'id'> | null;
  invoice?: Pick<IInvoice, 'id'> | null;
}

export type NewInvoiceItems = Omit<IInvoiceItems, 'id'> & { id: null };
