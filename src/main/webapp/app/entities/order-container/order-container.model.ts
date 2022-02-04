export interface IOrderContainer {
  id?: number;
  supplierRFIDTag?: string | null;
}

export class OrderContainer implements IOrderContainer {
  constructor(public id?: number, public supplierRFIDTag?: string | null) {}
}

export function getOrderContainerIdentifier(orderContainer: IOrderContainer): number | undefined {
  return orderContainer.id;
}
