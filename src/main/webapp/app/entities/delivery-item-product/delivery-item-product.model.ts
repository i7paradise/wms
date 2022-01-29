import { IDeliveryContainer } from 'app/entities/delivery-container/delivery-container.model';

export interface IDeliveryItemProduct {
  id?: number;
  rfidTAG?: string;
  deliveryContainer?: IDeliveryContainer | null;
}

export class DeliveryItemProduct implements IDeliveryItemProduct {
  constructor(public id?: number, public rfidTAG?: string, public deliveryContainer?: IDeliveryContainer | null) {}
}

export function getDeliveryItemProductIdentifier(deliveryItemProduct: IDeliveryItemProduct): number | undefined {
  return deliveryItemProduct.id;
}
