import { IOrderContainer } from "app/entities/order-container/order-container.model";

export interface IOrderContainerImpl extends IOrderContainer {
    countProducts?: number;
}

export class OrderContainerImpl implements IOrderContainerImpl {
  constructor(
    public countProducts?: number
  ) {}
}
