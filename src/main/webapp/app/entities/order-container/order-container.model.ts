import { ICompanyContainer } from 'app/entities/company-container/company-container.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IOrderItemProduct } from 'app/entities/order-item-product/order-item-product.model';

export interface IOrderContainer {
  id?: number;
  supplierRFIDTag?: string | null;
  companyContainer?: ICompanyContainer | null;
  orderItem?: IOrderItem | null;
  orderItemProducts?: IOrderItemProduct[] | null;
}

export class OrderContainer implements IOrderContainer {
  constructor(
    public id?: number,
    public supplierRFIDTag?: string | null,
    public companyContainer?: ICompanyContainer | null,
    public orderItem?: IOrderItem | null,
    public orderItemProducts?: IOrderItemProduct[] | null
  ) {}
}

export function getOrderContainerIdentifier(orderContainer: IOrderContainer): number | undefined {
  return orderContainer.id;
}
