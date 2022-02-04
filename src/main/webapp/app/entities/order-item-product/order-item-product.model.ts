import { IContainerCategory } from 'app/entities/container-category/container-category.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';

export interface IOrderItemProduct {
  id?: number;
  rfidTAG?: string;
  containerCategory?: IContainerCategory | null;
  orderItem?: IOrderItem | null;
}

export class OrderItemProduct implements IOrderItemProduct {
  constructor(
    public id?: number,
    public rfidTAG?: string,
    public containerCategory?: IContainerCategory | null,
    public orderItem?: IOrderItem | null
  ) {}
}

export function getOrderItemProductIdentifier(orderItemProduct: IOrderItemProduct): number | undefined {
  return orderItemProduct.id;
}
