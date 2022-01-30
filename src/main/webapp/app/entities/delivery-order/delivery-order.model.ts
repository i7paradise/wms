import dayjs from 'dayjs/esm';
import { IDeliveryOrderItem } from 'app/entities/delivery-order-item/delivery-order-item.model';
import { DeliveryOrderStatus } from 'app/entities/enumerations/delivery-order-status.model';

export interface IDeliveryOrder {
  id?: number;
  doNumber?: string;
  placedDate?: dayjs.Dayjs;
  status?: DeliveryOrderStatus;
  code?: string;
  deliveryOrderItems?: IDeliveryOrderItem[] | null;
}

export class DeliveryOrder implements IDeliveryOrder {
  constructor(
    public id?: number,
    public doNumber?: string,
    public placedDate?: dayjs.Dayjs,
    public status?: DeliveryOrderStatus,
    public code?: string,
    public deliveryOrderItems?: IDeliveryOrderItem[] | null
  ) {}
}

export function getDeliveryOrderIdentifier(deliveryOrder: IDeliveryOrder): number | undefined {
  return deliveryOrder.id;
}
