import dayjs from 'dayjs/esm';
import { DeliveryOrderStatus } from 'app/entities/enumerations/delivery-order-status.model';

export interface IDeliveryOrder {
  id?: number;
  doNumber?: string;
  placedDate?: dayjs.Dayjs;
  status?: DeliveryOrderStatus;
  code?: string;
}

export class DeliveryOrder implements IDeliveryOrder {
  constructor(
    public id?: number,
    public doNumber?: string,
    public placedDate?: dayjs.Dayjs,
    public status?: DeliveryOrderStatus,
    public code?: string
  ) {}
}

export function getDeliveryOrderIdentifier(deliveryOrder: IDeliveryOrder): number | undefined {
  return deliveryOrder.id;
}
