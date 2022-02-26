import { IOrder } from 'app/entities/order/order.model';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { IOrderContainer } from 'app/entities/order-container/order-container.model';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

export interface IOrderItem {
  id?: number;
  quantity?: number;
  status?: OrderItemStatus;
  containersCount?: number | null;
  productsPerContainerCount?: number | null;
  order?: IOrder | null;
  companyProduct?: ICompanyProduct | null;
  orderContainers?: IOrderContainer[] | null;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public quantity?: number,
    public status?: OrderItemStatus,
    public containersCount?: number | null,
    public productsPerContainerCount?: number | null,
    public order?: IOrder | null,
    public companyProduct?: ICompanyProduct | null,
    public orderContainers?: IOrderContainer[] | null
  ) {}
}

export function getOrderItemIdentifier(orderItem: IOrderItem): number | undefined {
  return orderItem.id;
}
