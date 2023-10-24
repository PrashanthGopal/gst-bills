import dayjs from 'dayjs/esm';
import { ITransporter } from 'app/entities/transporter/transporter.model';
import { IAddress } from 'app/entities/address/address.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { IClients } from 'app/entities/clients/clients.model';
import { InvoiceStatus } from 'app/entities/enumerations/invoice-status.model';

export interface IInvoice {
  id: number;
  invoiceId?: string | null;
  status?: keyof typeof InvoiceStatus | null;
  createDateTime?: dayjs.Dayjs | null;
  updateDateTime?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  totalIgstRate?: string | null;
  totalCgstRate?: string | null;
  totalSgstRate?: string | null;
  totalTaxRate?: string | null;
  totalAssAmount?: string | null;
  totalNoItems?: string | null;
  grandTotal?: number | null;
  distance?: number | null;
  modeOfTransaction?: string | null;
  transType?: string | null;
  vehicleNo?: string | null;
  transporter?: Pick<ITransporter, 'id'> | null;
  address?: Pick<IAddress, 'id'> | null;
  orgInvoices?: Pick<IOrganization, 'id'> | null;
  clients?: Pick<IClients, 'id'> | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
