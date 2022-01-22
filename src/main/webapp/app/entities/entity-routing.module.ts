import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'company',
        data: { pageTitle: 'wmsApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'company-user',
        data: { pageTitle: 'wmsApp.companyUser.home.title' },
        loadChildren: () => import('./company-user/company-user.module').then(m => m.CompanyUserModule),
      },
      {
        path: 'container',
        data: { pageTitle: 'wmsApp.container.home.title' },
        loadChildren: () => import('./container/container.module').then(m => m.ContainerModule),
      },
      {
        path: 'company-container',
        data: { pageTitle: 'wmsApp.companyContainer.home.title' },
        loadChildren: () => import('./company-container/company-container.module').then(m => m.CompanyContainerModule),
      },
      {
        path: 'uhfrfid-reader',
        data: { pageTitle: 'wmsApp.uHFRFIDReader.home.title' },
        loadChildren: () => import('./uhfrfid-reader/uhfrfid-reader.module').then(m => m.UHFRFIDReaderModule),
      },
      {
        path: 'uhfrfid-antenna',
        data: { pageTitle: 'wmsApp.uHFRFIDAntenna.home.title' },
        loadChildren: () => import('./uhfrfid-antenna/uhfrfid-antenna.module').then(m => m.UHFRFIDAntennaModule),
      },
      {
        path: 'warehouse',
        data: { pageTitle: 'wmsApp.warehouse.home.title' },
        loadChildren: () => import('./warehouse/warehouse.module').then(m => m.WarehouseModule),
      },
      {
        path: 'area',
        data: { pageTitle: 'wmsApp.area.home.title' },
        loadChildren: () => import('./area/area.module').then(m => m.AreaModule),
      },
      {
        path: 'door',
        data: { pageTitle: 'wmsApp.door.home.title' },
        loadChildren: () => import('./door/door.module').then(m => m.DoorModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'wmsApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'wh-row',
        data: { pageTitle: 'wmsApp.wHRow.home.title' },
        loadChildren: () => import('./wh-row/wh-row.module').then(m => m.WHRowModule),
      },
      {
        path: 'bay',
        data: { pageTitle: 'wmsApp.bay.home.title' },
        loadChildren: () => import('./bay/bay.module').then(m => m.BayModule),
      },
      {
        path: 'wh-level',
        data: { pageTitle: 'wmsApp.wHLevel.home.title' },
        loadChildren: () => import('./wh-level/wh-level.module').then(m => m.WHLevelModule),
      },
      {
        path: 'position',
        data: { pageTitle: 'wmsApp.position.home.title' },
        loadChildren: () => import('./position/position.module').then(m => m.PositionModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'wmsApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'product-category',
        data: { pageTitle: 'wmsApp.productCategory.home.title' },
        loadChildren: () => import('./product-category/product-category.module').then(m => m.ProductCategoryModule),
      },
      {
        path: 'company-product',
        data: { pageTitle: 'wmsApp.companyProduct.home.title' },
        loadChildren: () => import('./company-product/company-product.module').then(m => m.CompanyProductModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'wmsApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'delivery-order',
        data: { pageTitle: 'wmsApp.deliveryOrder.home.title' },
        loadChildren: () => import('./delivery-order/delivery-order.module').then(m => m.DeliveryOrderModule),
      },
      {
        path: 'delivery-order-item',
        data: { pageTitle: 'wmsApp.deliveryOrderItem.home.title' },
        loadChildren: () => import('./delivery-order-item/delivery-order-item.module').then(m => m.DeliveryOrderItemModule),
      },
      {
        path: 'delivery-container',
        data: { pageTitle: 'wmsApp.deliveryContainer.home.title' },
        loadChildren: () => import('./delivery-container/delivery-container.module').then(m => m.DeliveryContainerModule),
      },
      {
        path: 'delivery-item-product',
        data: { pageTitle: 'wmsApp.deliveryItemProduct.home.title' },
        loadChildren: () => import('./delivery-item-product/delivery-item-product.module').then(m => m.DeliveryItemProductModule),
      },
      {
        path: 'door-antenna',
        data: { pageTitle: 'wmsApp.doorAntenna.home.title' },
        loadChildren: () => import('./door-antenna/door-antenna.module').then(m => m.DoorAntennaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
