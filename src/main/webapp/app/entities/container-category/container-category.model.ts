import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IOrderItemProduct } from 'app/entities/order-item-product/order-item-product.model';

export interface IContainerCategory {
  id?: number;
  name?: string;
  description?: string | null;
  orderItem?: IOrderItem | null;
  orderItemProducts?: IOrderItemProduct[] | null;
}

export class ContainerCategory implements IContainerCategory {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public orderItem?: IOrderItem | null,
    public orderItemProducts?: IOrderItemProduct[] | null
  ) {}
}

export function getContainerCategoryIdentifier(containerCategory: IContainerCategory): number | undefined {
  return containerCategory.id;
}
