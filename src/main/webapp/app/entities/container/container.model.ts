export interface IContainer {
  id?: number;
  name?: string;
  description?: string | null;
}

export class Container implements IContainer {
  constructor(public id?: number, public name?: string, public description?: string | null) {}
}

export function getContainerIdentifier(container: IContainer): number | undefined {
  return container.id;
}
