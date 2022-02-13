import { ICompanyContainer } from 'app/entities/company-container/company-container.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';

export interface IOrderContainer {
  id?: number;
  supplierRFIDTag?: string | null;
  companyContainer?: ICompanyContainer | null;
  orderItem?: IOrderItem | null;
}

export class OrderContainer implements IOrderContainer {
  constructor(
    public id?: number,
    public supplierRFIDTag?: string | null,
    public companyContainer?: ICompanyContainer | null,
    public orderItem?: IOrderItem | null
  ) {}
}

export function getOrderContainerIdentifier(orderContainer: IOrderContainer): number | undefined {
  return orderContainer.id;
}
