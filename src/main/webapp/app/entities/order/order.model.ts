import dayjs from 'dayjs/esm';
import { ICompany } from 'app/entities/company/company.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { OrderType } from 'app/entities/enumerations/order-type.model';

export interface IOrder {
  id?: number;
  transactionNumber?: string;
  placedDate?: dayjs.Dayjs;
  status?: OrderStatus;
  code?: string;
  type?: OrderType;
  company?: ICompany | null;
  orderItems?: IOrderItem[] | null;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public transactionNumber?: string,
    public placedDate?: dayjs.Dayjs,
    public status?: OrderStatus,
    public code?: string,
    public type?: OrderType,
    public company?: ICompany | null,
    public orderItems?: IOrderItem[] | null
  ) {}
}

export function getOrderIdentifier(order: IOrder): number | undefined {
  return order.id;
}
