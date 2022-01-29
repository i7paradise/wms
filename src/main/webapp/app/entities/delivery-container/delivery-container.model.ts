import { IDeliveryOrderItem } from 'app/entities/delivery-order-item/delivery-order-item.model';
import { ICompanyContainer } from 'app/entities/company-container/company-container.model';

export interface IDeliveryContainer {
  id?: number;
  supplierRFIDTag?: string | null;
  deliveryOrderItem?: IDeliveryOrderItem | null;
  companyContainer?: ICompanyContainer | null;
}

export class DeliveryContainer implements IDeliveryContainer {
  constructor(
    public id?: number,
    public supplierRFIDTag?: string | null,
    public deliveryOrderItem?: IDeliveryOrderItem | null,
    public companyContainer?: ICompanyContainer | null
  ) {}
}

export function getDeliveryContainerIdentifier(deliveryContainer: IDeliveryContainer): number | undefined {
  return deliveryContainer.id;
}
