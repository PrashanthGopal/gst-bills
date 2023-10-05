import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'orders',
        data: { pageTitle: 'gstBillsApp.orders.home.title' },
        loadChildren: () => import('./orders/orders.routes'),
      },
      {
        path: 'state-code',
        data: { pageTitle: 'gstBillsApp.stateCode.home.title' },
        loadChildren: () => import('./state-code/state-code.routes'),
      },
      {
        path: 'invoice-items',
        data: { pageTitle: 'gstBillsApp.invoiceItems.home.title' },
        loadChildren: () => import('./invoice-items/invoice-items.routes'),
      },
      {
        path: 'org-users',
        data: { pageTitle: 'gstBillsApp.orgUsers.home.title' },
        loadChildren: () => import('./org-users/org-users.routes'),
      },
      {
        path: 'product-unit',
        data: { pageTitle: 'gstBillsApp.productUnit.home.title' },
        loadChildren: () => import('./product-unit/product-unit.routes'),
      },
      {
        path: 'address',
        data: { pageTitle: 'gstBillsApp.address.home.title' },
        loadChildren: () => import('./address/address.routes'),
      },
      {
        path: 'orders-items',
        data: { pageTitle: 'gstBillsApp.ordersItems.home.title' },
        loadChildren: () => import('./orders-items/orders-items.routes'),
      },
      {
        path: 'clients',
        data: { pageTitle: 'gstBillsApp.clients.home.title' },
        loadChildren: () => import('./clients/clients.routes'),
      },
      {
        path: 'organization',
        data: { pageTitle: 'gstBillsApp.organization.home.title' },
        loadChildren: () => import('./organization/organization.routes'),
      },
      {
        path: 'products',
        data: { pageTitle: 'gstBillsApp.products.home.title' },
        loadChildren: () => import('./products/products.routes'),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'gstBillsApp.invoice.home.title' },
        loadChildren: () => import('./invoice/invoice.routes'),
      },
      {
        path: 'org-user-role',
        data: { pageTitle: 'gstBillsApp.orgUserRole.home.title' },
        loadChildren: () => import('./org-user-role/org-user-role.routes'),
      },
      {
        path: 'transporter',
        data: { pageTitle: 'gstBillsApp.transporter.home.title' },
        loadChildren: () => import('./transporter/transporter.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
