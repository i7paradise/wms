export interface IContainerCategory {
  id?: number;
  name?: string;
  description?: string | null;
}

export class ContainerCategory implements IContainerCategory {
  constructor(public id?: number, public name?: string, public description?: string | null) {}
}

export function getContainerCategoryIdentifier(containerCategory: IContainerCategory): number | undefined {
  return containerCategory.id;
}
