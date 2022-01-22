import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { IDeliveryOrder } from 'app/entities/delivery-order/delivery-order.model';
import { DeliveryOrderItemStatus } from 'app/entities/enumerations/delivery-order-item-status.model';

export interface IDeliveryOrderItem {
  id?: number;
  unitQuantity?: number;
  containerQuantity?: number;
  status?: DeliveryOrderItemStatus;
  compganyProduct?: ICompanyProduct | null;
  deliveryOrder?: IDeliveryOrder | null;
}

export class DeliveryOrderItem implements IDeliveryOrderItem {
  constructor(
    public id?: number,
    public unitQuantity?: number,
    public containerQuantity?: number,
    public status?: DeliveryOrderItemStatus,
    public compganyProduct?: ICompanyProduct | null,
    public deliveryOrder?: IDeliveryOrder | null
  ) {}
}

export function getDeliveryOrderItemIdentifier(deliveryOrderItem: IDeliveryOrderItem): number | undefined {
  return deliveryOrderItem.id;
}
