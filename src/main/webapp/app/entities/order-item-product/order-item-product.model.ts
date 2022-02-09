import { IOrderContainer } from 'app/entities/order-container/order-container.model';

export interface IOrderItemProduct {
  id?: number;
  rfidTAG?: string;
  orderItem?: IOrderContainer | null;
}

export class OrderItemProduct implements IOrderItemProduct {
  constructor(public id?: number, public rfidTAG?: string, public orderItem?: IOrderContainer | null) {}
}

export function getOrderItemProductIdentifier(orderItemProduct: IOrderItemProduct): number | undefined {
  return orderItemProduct.id;
}
