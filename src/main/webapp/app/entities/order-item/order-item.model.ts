import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { IOrder } from 'app/entities/order/order.model';
import { IContainerCategory } from 'app/entities/container-category/container-category.model';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

export interface IOrderItem {
  id?: number;
  quantity?: number;
  status?: OrderItemStatus;
  compganyProduct?: ICompanyProduct | null;
  order?: IOrder | null;
  containerCategories?: IContainerCategory[] | null;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public quantity?: number,
    public status?: OrderItemStatus,
    public compganyProduct?: ICompanyProduct | null,
    public order?: IOrder | null,
    public containerCategories?: IContainerCategory[] | null
  ) {}
}

export function getOrderItemIdentifier(orderItem: IOrderItem): number | undefined {
  return orderItem.id;
}
